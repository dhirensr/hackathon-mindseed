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
  :set-docs
  (fn [db [_ docs]]
    (assoc db :docs docs)))

(reg-event-db
 :set-synonym
 (fn [db [_ synonym]]


   (assoc db :synonyms (concat (take-while
                             #(not= (:word synonym)
                                    (:word %))
                             (:synonyms db))
                            [synonym]
                            (rest (drop-while
                                   #(not= (:word synonym)
                                          (:word %))
                                   (:synonyms db)))) ) ))

(reg-event-db
 :set-word
 (fn [db [_ word]]
   (assoc db :word word)))

(reg-event-db
 :set-first-page
 (fn [db [_ page]]
   (assoc db :first-page page)))

(reg-event-db
 :set-second-page
 (fn [db [_ page]]
   (assoc db :second-page page)))

(reg-event-db
 :set-toggle
 (fn [db [_ toggle]]
   (assoc db :toggle toggle)))
