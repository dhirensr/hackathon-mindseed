(ns hackathon.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [hackathon.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[hackathon started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[hackathon has shut down successfully]=-"))
   :middleware wrap-dev})
