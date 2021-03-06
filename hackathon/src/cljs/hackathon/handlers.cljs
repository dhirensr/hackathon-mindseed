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





(defn disable-word
  [word]
  #_(println "@@@@@@@%%%%%%%%"
   (assoc word :disabled? true))
  (assoc word :disabled? true))

(defn replace-word
  [db new-word]
  (assoc db :synonyms (concat (take-while
                               #(not= (:word new-word)
                                      (:word %))
                               (:synonyms db))
                              [new-word]
                              (rest (drop-while
                                     #(not= (:word new-word)
                                            (:word %))
                                     (:synonyms db))))))



(reg-event-db
 :set-synonym
 (fn [db [_ synonym]]
   (let [clicked-maps (filter #(and (= (:disabled? %) false) (= (:clicked? %) true))
                              (:synonyms db))
         [word1 word2] (if (= 2 (count clicked-maps)) clicked-maps [] )]
     (if (= 2 (count clicked-maps))
       (if (= (:word word1)
              (:synonym word2))
         (replace-word (replace-word db (assoc word1 :disabled? true))
                       (assoc word2 :disabled? true))
         (assoc db :synonyms (map (fn [a]
                                    (assoc a :clicked? false))
                                  (:synonyms db))))
       (assoc db :synonyms (concat (take-while
                                    #(not= (:word synonym)
                                           (:word %))
                                    (:synonyms db))
                                   [synonym]
                                   (rest (drop-while
                                          #(not= (:word synonym)
                                                 (:word %))
                                          (:synonyms db)))))))))




(reg-event-db
 :set-words
 (fn [db [_ words]]
   (assoc db :synonyms words)))

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
 (fn [db [_ _]]
   (update-in db [:toggle] not)))
(reg-event-db
 :set-animals
 (fn [db [_ animals]]
   (assoc db :animals animals)))
(reg-event-db
 :set-index
 (fn [db [_ _]]
   (update-in db [:current-index] inc)))
