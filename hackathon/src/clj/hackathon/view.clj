(ns hackathon.view
  (:require [hackathon.db.core :as db]))

(defn get-words
  []
  (map #(select-keys % [:word :synonym :clicked? :disabled?])
       (db/display-words)))
