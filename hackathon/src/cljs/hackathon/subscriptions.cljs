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
