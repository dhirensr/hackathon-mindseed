(ns hackathon.db.core
    (:require [monger.core :as mg]
              [monger.collection :as mc]
              [monger.operators :refer :all]
              [mount.core :refer [defstate]]
              [hackathon.config :refer [env]]))

(defstate db*
  :start (-> env :database-url mg/connect-via-uri)
  :stop (-> db* :conn mg/disconnect))

(defstate db
  :start (:db db*))

(defn create-user [user]
  (mc/insert db "users" user))

(defn update-user [id first-name last-name email]
  (mc/update db "users" {:_id id}
             {$set {:first_name first-name
                    :last_name last-name
                    :email email}}))

(defn get-user [id]
  (mc/find-one-as-map db "users" {:_id id}))


(defn create-row [user-details]
  (dissoc (mc/insert-and-return db "hackathon" user-details) :_id))

(defn check-duplicates
  [mobilenumber]
  (if (< 0  (count  (filter true? (map (fn [a]
                                         (if (= mobilenumber (:mobilenumber a))
                                           true
                                           false)) (into #{}
                                                         (mapv
                                                          #(select-keys % [:mobilenumber])
                                                          (mc/find-maps db "hackathon")))))))
    true
    false))

(defn enter-details
  [email name country language dob mobilenumber password]
  (if (check-duplicates mobilenumber)
    false
    (create-row {:email email
                 :name name
                 :country country
                 :language language
                 :dob dob
                 :mobilenumber mobilenumber
                 :password password
                 :score1 0
                 :score2 0})))

(defn all-users-details
  "all users map"
  []
  (mapv
   #(select-keys % [:mobilenumber :password :email :score1 :name :dob :language :score2 :country])
   (mc/find-maps db "hackathon")))





(defn get-userdetail
  [mobilenumber]
  (flatten (remove nil? (map (fn [a]
                               (if (= mobilenumber (:mobilenumber a))
                                 [a])) (all-users-details)))))
