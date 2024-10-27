(ns duct.router.reitit-test
  (:require [clojure.test :refer [deftest is]]
            [integrant.core :as ig]
            [duct.router.reitit :as reitit]))

(deftest hierarchy-test
  (ig/load-hierarchy)
  (is (isa? :duct.router/reitit :duct/router)))

(deftest router-test
  (let [handler (constantly {:status 200, :body "Hello World"})
        config  {:duct.router/reitit
                 {:routes {"/" {:get {:handler handler}}}}}
        router  (:duct.router/reitit (ig/init config))]
    (is (= {:status 200, :body "Hello World"}
           (router {:request-method :get, :uri "/"})))
    (is (nil? (router {:request-method :get, :uri "/bad"})))))
