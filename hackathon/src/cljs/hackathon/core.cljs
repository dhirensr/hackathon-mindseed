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


(defn log [& params] (.log js/console (apply str params)))

(defn get-by-id [id] (.getElementById js/document id))

(defn error-handler
  [p]
  (log p))
(def server "http://localhost:3000/")


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
       [nav-link "#/about" "About" :about collapsed?]
       [nav-link "#/login" "Login" :login collapsed?]
       [nav-link "#/register" "Register" :signup collapsed?]]]]))

(defn about-page []
  [:div.container
   [:div.row
    [:div.col-md-12
     [:img {:src (str js/context "/img/warning_clojure.png")}]]]])

(defn login-display
  [resp]
  (if resp
    (rf/dispatch [:set-active-page :home])
    (rf/dispatch [:set-active-page :login-fail])))

(defn login-page
  []
  [:div.container
   [:h2 "Login Page!"]
   [:form {:action "#" :method "get"
           :on-submit (fn [e]
                        (let   [u (.-value (get-by-id "mobilenumber"))
                                p (.-value (get-by-id "password"))]
                               (GET (str server "loginuser")
                                    {:params {:mobilenumber u
                                              :password p}
                                     :format :json
                                     :response-format :json
                                     :keywords? true
                                     :handler login-display
                                     :error-handler error-handler})))}
    [:input {:type "text"
             :id "mobilenumber" :placeholder "9773475171"}]
    [:input {:type "password"
             :id "password" :placeholder "password"}]
    [:input {:type "submit" :value "LogIn"}]] ])



(defn thankyou-page []
  [:div.container
   [:h2 "Thank you for registering"]])
(defn login-fail-page []
  [:div.container
   [:h2 "Either mobile number or password was wrong! Please try again!"]])

(defn register-fail-page []
  [:div.container
   [:h2 "Something went wrong! Please try again!"]])

(defn register
  [resp]
  (if resp
    (rf/dispatch [:set-active-page :thankyou])
    (rf/dispatch [:set-active-page :register-fail])))

(defn signup-page
  []
  [:div.container
   [:h2 "Registration Page!"]
   [:form {:action "#" :method "get"
           :on-submit (fn [e]
                       (let [l (.-value (get-by-id "email"))
                             m (.-value (get-by-id "name"))
                             n (.-value (get-by-id "country"))
                             o (.-value (get-by-id "language"))
                             p (.-value (get-by-id "DOB"))
                             q (.-value (get-by-id "mobilenumber"))
                             r (.-value (get-by-id "password"))]
                             (GET (str server "putdetails")
                                    {:params {:email l
                                              :name m
                                              :contry n
                                              :language o
                                              :dob p
                                              :mobilenumber q
                                              :password r}
                                     :format :json
                                     :response-format :json
                                     :keywords? true
                                     :handler register
                                     :error-handler error-handler})))}
    [:div.container
     [:div

      [:input {:type "text"
               :id "email" :placeholder "Enter Email" :required true} ]]
     [:div
      [:input {:type "text"
               :id "name" :placeholder "Enter Name" :required true} ]]
     [:div

      [:input {:type "text"
               :id "country" :placeholder "Enter Country" :required true}]]
     [:div
      [:label "MotherTongue"]
      [:select {:id "language"}
       [:option {:value "de"} "German"]
       [:option {:value "es"} "Spanish"]
       [:option {:value "fr"} "French"]]]
     [:div
      [:input {:type "text"
               :id "DOB" :placeholder "1.07.2017" :required true} ]]
     [:div
      [:input {:type "text"
               :id "mobilenumber" :placeholder "Enter Mobile number" :required true} ]]
     [:div
      [:input {:type "password"
               :id "password" :placeholder "Enter Password" :required true}]]]
    [:input {:type "submit" :value "Register"}]
    [:input {:type "button" :value "Login" :on-click #(rf/dispatch [:set-active-page :login])}]]])



#_(defn login-page
  []
  [:div.container
   [:form {:class "form-horizontal"}
    [:div {:class "form-group"}
     [:label {:class "col-sm-2 control-label"} "Mobile Number"]
     [:div {:class "col-sm-10"}
      [:input {:type "text" :class "form-control" :placeholder "9773475171"} "Mobile Number"]]]
    [:div {:class "form-group"}
     [:label {:class "col-sm-2 control-label" :for "inputPassword3"} "Password"]
     [:div {:class "col-sm-10"}
      [:input {:type "password" :class "form-control" :placeholder "password" } "Mobile Number"]]]
    [:div {:class "form-group"}
     [:div {:class "col-sm-offset-2 col-sm-10"}
      [:button {:type "submit" :class "btn btn-default"} "Sign In"]
      #_[:button {:type "submit" :class "btn btn-default"} "Register"]]]]])


(defn home-page []
  [:div.container
   [:h2 "Hello World"]])

(def pages
  {:home #'home-page
   :about #'about-page
   :login #'login-page
   :signup #'signup-page
   :thankyou #'thankyou-page
   :register-fail #'register-fail-page
   :login-fail #'login-fail-page})

(defn page []
  [:div
   [navbar]
   [(pages @(rf/subscribe [:page]))]])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (rf/dispatch [:set-active-page :home]))

(secretary/defroute "/about" []
  (rf/dispatch [:set-active-page :about]))

(secretary/defroute "/login" []
  (rf/dispatch [:set-active-page :login]))

(secretary/defroute "/register" []
  (rf/dispatch [:set-active-page :signup]))

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


(defn mount-components []
  (rf/clear-subscription-cache!)
  (r/render [#'page] (.getElementById js/document "app")))

(defn init! []
  (rf/dispatch-sync [:initialize-db])
  (load-interceptors!)

  (hook-browser-navigation!)
  (mount-components))
