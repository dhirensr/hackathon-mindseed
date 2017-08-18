(ns hackathon.routes.home
  (:require [hackathon.layout :as layout]
            [compojure.core :refer [defroutes GET POST]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]
            [clojure.data.json :as json]
            [clj-http.client :as client]
            [hackathon.views :as views]))

(defn home-page []
  (layout/render "home.html"))


(defn register-fn
  [email name country language dob mobilenumber password]
  (layout/render-json
   (views/register email name country language dob mobilenumber password)))

(defn word-of-the-day []
  (layout/render-json  (-> (str "http://urban-word-of-the-day.herokuapp.com/")
                           client/get
                           :body
                           json/read-str
                           (clojure.walk/keywordize-keys)
                           )))





(defn logincheck
  [mobilenumber password]
  (layout/render-json
   (views/login? mobilenumber password)))

(defn getdetails [mobilenumber]
  (layout/render-json
   (views/get-userdetails mobilenumber)))

(defn translate-into-en
  [input-str]
  (layout/render-json
   (views/translate-into-eng input-str)))

(defroutes home-routes
  (GET "/" []
       (home-page))
  (POST "/putdetails" [email name country language dob mobilenumber password]
        (register-fn email name country language dob mobilenumber password))
  (GET "/loginuser" [mobilenumber password]
       (logincheck mobilenumber password))
  (GET "/wordoftheday" []
       (word-of-the-day))
  (GET "/userdetails" [mobilenumber]
       (getdetails mobilenumber))
  (GET "/translate" [input-str]
       (translate-into-en input-str)))


(client/get "http://urban-word-of-the-day.herokuapp.com/")


(defn news []
  (let [content (-> (str "http://urban-word-of-the-day.herokuapp.com/")
                    client/get
                    :body
                    json/read-str)]
    content))
