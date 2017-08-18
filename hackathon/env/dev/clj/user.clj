(ns user
  (:require [mount.core :as mount]
            [hackathon.figwheel :refer [start-fw stop-fw cljs]]
            hackathon.core))

(defn start []
  (mount/start-without #'hackathon.core/repl-server))

(defn stop []
  (mount/stop-except #'hackathon.core/repl-server))

(defn restart []
  (stop)
  (start))


