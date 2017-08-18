(ns hackathon.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[hackathon started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[hackathon has shut down successfully]=-"))
   :middleware identity})
