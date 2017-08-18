(ns hackathon.core
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [secretary.core :as secretary]
            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType]
            [markdown.core :refer [md->html]]
            [ajax.core :refer [GET POST]]
            [hackathon.ajax :refer [load-interceptors!]]
            [hackathon.handlers]
            [hackathon.subscriptions]
            [soda-ash.core :as sa])
  (:import goog.History))

(enable-console-print!)

(defn nav-link [uri title page collapsed?]
  (let [selected-page (rf/subscribe [:page])]
    [:li.nav-item
     {:class (when (= page @selected-page) "active")}
     [:a.nav-link
      {:href uri
       :on-click #(reset! collapsed? true)} title]]))

(defn navbar []
  (r/with-let [collapsed? (r/atom true)]
    [:nav.navbar.navbar-dark.bg-primary
     [:button.navbar-toggler.hidden-sm-up
      {:on-click #(swap! collapsed? not)} "☰"]
     [:div.collapse.navbar-toggleable-xs
      (when-not @collapsed? {:class "in"})
      [:a.navbar-brand {:href "#/"} "hackathon"]
      [:ul.nav.navbar-nav
       [nav-link "#/" "Home" :home collapsed?]
       [nav-link "#/synonym-game" "synonym" :synonym collapsed?]]]]))



(defn synonym-game []
  (let [synonyms (rf/subscribe [:get-k :synonyms])
        on-button-click (fn
                          [word-map]
                          (rf/dispatch [:set-toggle])
                          (rf/dispatch [:set-synonym (assoc word-map
                                                           :clicked?
                                                           true)]))]
    (fn []
      (println (count @synonyms))
      [:div.container
       [:h1 "Synonyms Game"]
       #_(rf/dispatch [:set-first-value false])
       (doall
        (map-indexed (fn [i {:keys [word synonym clicked? disabled?] :as k}]
                       ^{:key i}
                       [sa/Button {:onClick #(on-button-click k)}
                        (println @synonyms)
                        (if disabled?
                            word
                            (if clicked?
                              word
                              "SYNONYM"))])
                     @synonyms))])))

(defn home-page []
  [:div.container
   (when-let [docs @(rf/subscribe [:docs])]
     [:div.row>div.col-sm-12
      [:div {:dangerouslySetInnerHTML
             {:__html (md->html docs)}}]])])

(def pages
  {:home #'home-page
   :synonym #'synonym-game})

(defn page []
  [:div
   [navbar]
   [(pages @(rf/subscribe [:page]))]])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (rf/dispatch [:set-active-page :home]))

(secretary/defroute "/synonym-game" []
  (rf/dispatch [:set-active-page :synonym]))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
      HistoryEventType/NAVIGATE
      (fn [event]
        (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

;; -------------------------
;; Initialize app
(defn fetch-docs! []
  (GET "/docs" {:handler #(rf/dispatch [:set-docs %])}))

(defn mount-components []
  (rf/clear-subscription-cache!)
  (r/render [#'page] (.getElementById js/document "app")))

(defn init! []
  (rf/dispatch-sync [:initialize-db])
  (load-interceptors!)
  (fetch-docs!)
  (hook-browser-navigation!)
  (mount-components))
