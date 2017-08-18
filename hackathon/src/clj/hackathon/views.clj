(ns hackathon.views
  (:require [hackathon.layout :as layout]
            [compojure.core :refer [defroutes GET]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]
            [hackathon.db.core :as db]
            ))

(defn get-multipart-param [request name]
  (get-in request [:params name]))


(defn register
  [email name country language dob mobilenumber password]
  (if (db/enter-details email name country language dob mobilenumber password)
    true
    false))

(defn login?
  [mobilenumber password]
  (if (> (count (filter true?
                        (map #(if (and (= mobilenumber (:mobilenumber %))
                                       (= password (:password %)))
                                true
                                false)
                             (db/all-users)))) 0)
    [true mobilenumber password]
    [false mobilenumber password]))


(defn get-userdetails
  [mobilenumber]
  (first (db/get-userdetail mobilenumber)))
