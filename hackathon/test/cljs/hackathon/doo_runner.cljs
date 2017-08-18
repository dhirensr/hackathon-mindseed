(ns hackathon.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [hackathon.core-test]))

(doo-tests 'hackathon.core-test)

