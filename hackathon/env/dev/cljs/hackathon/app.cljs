(ns ^:figwheel-no-load hackathon.app
  (:require [hackathon.core :as core]
            [devtools.core :as devtools]))

(enable-console-print!)

(devtools/install!)

(core/init!)
