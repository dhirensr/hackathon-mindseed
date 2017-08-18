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

(defn enter-details
  [email name country language dob mobilenumber password]
  (create-row {:email email
               :name name
               :country country
               :language language
               :dob dob
               :mobilenumber mobilenumber
               :password password
               :score1 0
               :score2 0}))

(defn all-users
  "all users map"
  []
  (mapv
   #(select-keys % [:mobilenumber :password])
   (mc/find-maps db "hackathon")))
