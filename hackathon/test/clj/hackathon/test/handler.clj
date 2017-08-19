(ns hackathon.test.handler
  (:require [clojure.test :refer :all]
            [hackathon.core :refer :all]
            [ring.mock.request :refer :all]
            [hackathon.handler :refer :all]
            [midje.sweet :refer :all]))
(defn test-fn
  [f]
  (-main)
  (f))

(use-fixtures :once test-fn)

(deftest test-app
  (testing "main route"
    (let [response ((app) (request :get "/"))]
      (is (= 200 (:status response)))))

  (testing "not-found route"
    (let [response ((app) (request :get "/invalid"))]
      (is (= 404 (:status response)))))
  (testing "testing animals"
    (let [response ((app) (request :get "/animals"))]
      (println response)
      (is (= 200 (:status response)))))
  (testing "tesing wordoftheday route"
    (let [response ((app) (request :get "/wordoftheday"))]
      (is (= 200 (:status response)))))
  (testing "testing words route"
    (let [response ((app) (request :get "/words"))]
      (is (= 200 (:status response)))))
  (testing "testing userdetails route"
    (let [response ((app) (body (request :get "/userdetails") {:mobilenumber "12"}) )]
      (is (= 200 (:status response)))))
  (testing "testing translate route"
    (let [response ((app) (body (request :get "/translate") {:input-str "iAFAFA"}) )]
      (is (= 200 (:status response)))))
  (testing "testing dictionary route"
    (let [response ((app) (body (request :get "/dictionary") {:input-str "hello"}) )]
      (is (= 200 (:status response)))))
  (testing "testing login route"
    (let [response ((app) (body (request :post "/loginuser") {:mobilenumber "12" :password "serai"}))]
      (is (= 200 (:status response))))))

#_(deftest test-app2


  (testing "not-found route"
    (let [response ((app) (request :get "/invalid"))]
      (is (= 404 (:status response))))))


#_(fact
 (* 2 2) => 4)
