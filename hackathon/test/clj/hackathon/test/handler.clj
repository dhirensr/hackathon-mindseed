(ns hackathon.test.handler
  (:require #_[clojure.test :refer :all]
            [ring.mock.request :refer :all]
            [hackathon.handler :refer :all]
            [midje.sweet :refer :all]))

#_(deftest test-app
  (testing "main route"
    (let [response ((app) (request :get "/"))]
      (is (= 200 (:status response)))))

  (testing "not-found route"
    (let [response ((app) (request :get "/invalid"))]
      (is (= 404 (:status response))))))

(fact "First test"
      (* 2 2) => 4)
