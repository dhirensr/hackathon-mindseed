(ns hackathon.core-test
  (:require [cljs.test :refer-macros [is are deftest testing use-fixtures]]
            [pjstadig.humane-test-output]
            [reagent.core :as reagent :refer [atom]]
            [hackathon.core :as rc]
            [midje.sweet :refer :all]))

#_
(deftest test-home
  (is (= true true)))

(fact
 (* 22 2) => 44)
