(ns hackathon.views
  (:require [hackathon.layout :as layout]
            [compojure.core :refer [defroutes GET]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]
            [hackathon.db.core :as db]
            ))

(defn register
  [email name country language dob mobilenumber password]
  (if (db/enter-details email name country language dob mobilenumber password)
     true
    false))

(defn login?
  [mobilenumber password]
  (if (> (count (filter true?
                        (map #(if (and (= mobilenumber (:mobilenumber %))
                                       (= password (:password %)))
                                true
                                false)
                             (db/all-users)))) 0)
    true
    false))
