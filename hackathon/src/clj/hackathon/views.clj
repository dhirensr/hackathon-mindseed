(ns hackathon.views
  (:require [hackathon.layout :as layout]
            [compojure.core :refer [defroutes GET]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]
            [hackathon.translate :as translateapi]
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
                             (db/all-users-details)))) 0)
    [true mobilenumber password]
    [false mobilenumber password]))


(defn get-userdetails
  [mobilenumber]
  (first (db/get-userdetail mobilenumber)))


(defn translate-into-eng
  [input-str]
  (translateapi/translate input-str {:api-key "trnsl.1.1.20170818T090410Z.daf464b1460f4219.a958a99ab4ed926190accc6d7259c57094b2600c"
                                     :lang "en"}))



(defn get-words
  []
  (map #(select-keys % [:word :synonym :clicked? :disabled?])
       (db/display-words)))

(defn get-animals
  "Returns the animals returned from the database"
  []
  (map #(select-keys % [:src :name])
       (db/display-animals)))
