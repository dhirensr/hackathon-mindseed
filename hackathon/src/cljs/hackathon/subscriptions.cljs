(ns hackathon.subscriptions
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  :page
  (fn [db _]
    (:page db)))

(reg-sub
 :docs
 (fn [db _]
   (:docs db)))

(reg-sub
 :get-k
 (fn [db [_ k]]
   #_(println db)
   (k db)))

#_(reg-sub
 :word
 (fn [db _]
   (:word db)))

#_(reg-sub
 :first-value
 (fn [db _]
   (:first-value db)))

#_(reg-sub
 :synon
 (fn [db _]
   (or(:buttons-selected db) #{} ) ))
