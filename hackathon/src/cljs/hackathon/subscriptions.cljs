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
 :user
 (fn [db _]
   (:user db)))

(reg-sub
 :user-details
 (fn [db _]
   (:user-details db)))

(reg-sub
 :translate-answer
 (fn [db _]
   (:translate-answer db)))

(reg-sub
 :word-of-the-day
 (fn [db _]
   (:word-of-the-day db)))

(reg-sub
 :dictionary
 (fn [db _]
   (:dictionary db)))






(reg-sub
 :get-k
 (fn [db [_ k]]
   #_(println db)
   (k db)))
