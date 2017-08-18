(ns hackathon.routes.home
  (:require [hackathon.layout :as layout]
            [compojure.core :refer [defroutes GET]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]
            [hackathon.view :as view]))

(defn home-page []
  (layout/render "home.html"))

(defroutes home-routes
  (GET "/" []
       (home-page))
  (GET "/words" []
       (layout/render-json (view/get-words)))
  (GET "/animals" []
       (layout/render-json (view/get-animals))))
