(ns hackathon.routes.home
  (:require [hackathon.layout :as layout]
            [compojure.core :refer [defroutes GET]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]
            [hackathon.views :as views]))

(defn home-page []
  (layout/render "home.html"))
(defn register-fn
  [email name country language dob mobilenumber password]
  (layout/render-json
   (views/register email name country language dob mobilenumber password)))
(defn logincheck
  [mobilenumber password]
  (layout/render-json
   (views/login? mobilenumber password)))

(defroutes home-routes
  (GET "/" []
       (home-page))
  (GET "/putdetails" [email name country language dob mobilenumber password]
       (register-fn email name country language dob mobilenumber password)))
(GET "/loginuser" [mobilenumber password]
     (logincheck mobilenumber password))
