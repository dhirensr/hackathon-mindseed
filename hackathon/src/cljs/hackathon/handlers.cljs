(ns hackathon.handlers
  (:require [hackathon.db :as db]
            [re-frame.core :refer [dispatch reg-event-db]]))

(reg-event-db
 :initialize-db
 (fn [_ _]
   db/default-db))

(reg-event-db
 :set-active-page
 (fn [db [_ page]]
   (assoc db :page page)))

(reg-event-db
 :set-current-user
 (fn [db [_ page]]
   (assoc db :user page)))

(reg-event-db
 :set-user-details
 (fn [db [_ page]]
   (assoc db :user-details page)))

(reg-event-db
 :set-word-of-the-day
 (fn [db [_ page]]
   (assoc db :word-of-the-day page)))

(reg-event-db
 :set-dictionary
 (fn [db [_ page]]
   (assoc db :dictionary page)))

(reg-event-db
 :set-translate-answer
 (fn [db [_ page]]
   (assoc db :translate-answer page)))

(reg-event-db
  :set-docs
  (fn [db [_ docs]]
    (assoc db :docs docs)))
