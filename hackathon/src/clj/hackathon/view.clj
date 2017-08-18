(ns hackathon.view
  (:require [hackathon.db.core :as db]))

(defn get-words
  "Returns the words retrieved from the database and ignores the id"
  []
  (map #(select-keys % [:word :synonym :clicked? :disabled?])
       (db/display-words)))

(defn get-animals
  "Returns the animals returned from the database"
  []
  (map #(select-keys % [:src :name])
       (db/display-animals)))
