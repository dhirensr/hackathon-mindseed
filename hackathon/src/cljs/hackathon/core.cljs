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
  (if (first resp)
    (do (rf/dispatch [:set-active-page :home])
        (rf/dispatch [:set-current-user (second resp)]))
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
                             (POST (str server "putdetails")
                                    {:params {:email l
                                              :name m
                                              :country n
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

      [sa/Input {:type "text"
               :id "email" :placeholder "Enter Email" :required true} ]]
     [:div
      [sa/Input {:type "text"
               :id "name" :placeholder "Enter Name" :required true} ]]
     [:div

      [sa/Input {:type "text"
               :id "country" :placeholder "Enter Country" :required true}]]
     [:div
      [:label "MotherTongue"]
      [:select {:id "language"}
       [:option {:value "de"} "German"]
       [:option {:value "es"} "Spanish"]
       [:option {:value "fr"} "French"]]]
     [:div
      [sa/Input {:type "text"
               :id "DOB" :placeholder "1.07.2017" :required true} ]]
     [:div
      [sa/Input {:type "text"
               :id "mobilenumber" :placeholder "Enter Mobile number" :required true} ]]
     [:div
      [sa/Input {:type "password"
               :id "password" :placeholder "Enter Password" :required true}]]]
    [sa/Input {:type "submit" :value "Register"}]
    [sa/Input{:type "button" :value "Login" :on-click #(rf/dispatch [:set-active-page :login])}]]])




(defn print-userdetails
  [resp]
  (rf/dispatch [:set-user-details resp]))

(defn render-userdetails
  []
  [:div.container
   [:div [sa/Input {:value (str "Name  " (:name @(rf/subscribe [:user-details])))}]]
   [:div [sa/Input {:value (str "Email  " (:email @(rf/subscribe [:user-details])))}]]
   [:div [sa/Input {:value (str "Country  " (:country @(rf/subscribe [:user-details])))}]]
   [:div [sa/Input {:value (str "DOB  " (:dob @(rf/subscribe [:user-details])))}]]])

(defn store-to-db
  [resp]
  (rf/dispatch [:set-translate-answer resp]))


(defn translate-page
  []
  [:div.container
   [:textarea {:type "text" :rows "4" :cols "100" :placeholder "Enter the string" :id "input-str"}]
   [:div
    [:input {:type "button" :value "Translate"
             :on-click (fn [e]
                         (let   [string (.-value (get-by-id "input-str"))]
                           (GET (str server "translate")
                                {:params {:input-str string}
                                 :format :json
                                 :response-format :json
                                 :keywords? true
                                 :handler store-to-db
                                 :error-handler error-handler})))}]]
   [:div
    [:textarea {:type "text" :cols "100" :rows "4" :value @(rf/subscribe [:translate-answer])}]]

   [:div
    [:input {:type "button" :value "Go back to Home!" :on-click #(rf/dispatch [:set-active-page :home])}]]])

(defn dictionary-render
  [resp]
  (log resp)
  (rf/dispatch [:set-dictionary resp]))


(defn dictionary-page
  []
  [:div.container
   [:textarea {:type "text" :placeholder "Enter the word" :id "input-str"}]
   [:div
    [:input {:type "button" :value "Check meaning"
             :on-click (fn [e]
                         (let   [string (.-value (get-by-id "input-str"))]
                           (GET (str server "dictionary")
                                {:params {:input-str string}
                                 :format :json
                                 :response-format :json
                                 :keywords? true
                                 :handler dictionary-render
                                 :error-handler error-handler})))}]]
   [:div
    [:textarea {:type "text" :rows "4" :cols "100" :value (:meaning @(rf/subscribe [:dictionary]))}]]

   [:div
    [:input {:type "button" :value "Go back to Home!" :on-click #(rf/dispatch [:set-active-page :home])}]]])



(defn home-page []
  [:div.container
   [:h2 "Hello " @(rf/subscribe [:user])]
   [:form {:action "#" :method "get"
           :on-submit (fn [e]
                        (let   [u @(rf/subscribe [:user])]
                          (GET (str server "userdetails")
                               {:params {:mobilenumber u}
                                :format :json
                                :response-format :json
                                :keywords? true
                                :handler print-userdetails
                                :error-handler error-handler})))}
    [sa/Input {:type "submit" :value "Get Details!"}]
    [sa/Input {:type "button" :value "Translate into English"
               :on-click (fn [e]
                           (rf/dispatch [:set-active-page :translate-page]))}]
    [sa/Input {:type "button" :value "Dictionary"
               :on-click (fn [e]
                           (rf/dispatch [:set-active-page :dictionary-page]))}]
    [sa/Input {:type "button" :value "Logout!"
             :on-click (fn [e]
                         (rf/dispatch [:set-active-page :login])
                         (rf/dispatch [:set-current-user ""])
                         (rf/dispatch [:set-user-details {}]))}]]
   [render-userdetails]
   [:div
    [sa/Input {:type "button" :value "Word of the day"
               :on-click (fn [e]
                           (GET (str server "wordoftheday")
                                {
                                 :format :json
                                 :response-format :json
                                 :keywords? true
                                 :handler #(rf/dispatch [:set-word-of-the-day %])
                                 :error-handler error-handler})) }]]

   [:div
    [sa/Card
     [sa/CardContent
      [sa/CardHeader "Word of the Day!"]
      [sa/CardDescription (:word @(rf/subscribe [:word-of-the-day]))]
      [sa/CardDescription (:meaning @(rf/subscribe [:word-of-the-day]))]]]]])




(def pages
  {:home #'home-page
   :about #'about-page
   :login #'login-page
   :signup #'signup-page
   :thankyou #'thankyou-page
   :register-fail #'register-fail-page
   :login-fail #'login-fail-page
   :translate-page #'translate-page
   :dictionary-page #'dictionary-page})

(defn page []
  [:div
   [navbar]
   [(pages @(rf/subscribe [:page]))]])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (rf/dispatch [:set-active-page :login]))

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
