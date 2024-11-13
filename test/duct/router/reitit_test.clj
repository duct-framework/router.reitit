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

(deftest muuntaja-test
  (let [handler  (constantly {:status 200, :body {:count 1}})
        config   {:duct.router/reitit
                  {:routes {"/" {:get {:handler handler}}}
                   :data   {:muuntaja {}}}}
        router   (:duct.router/reitit (ig/init config))
        response (router {:request-method :get
                          :uri "/"
                          :headers {"Accept" "application/json"}})]
    (is (= {:status  200
            :headers {"Content-Type" "application/json; charset=utf-8"}
            :body    "{\"count\":1}"}
           (update response :body slurp)))))

(deftest coercion-test
  (let [handler  (fn [{:keys [parameters]}]
                   {:status 200, :body (pr-str parameters)})
        config   {:duct.router/reitit
                  {:routes {"/" {:get {:handler handler}
                                 :parameters {:query {:x :int}}}}
                   :data   {:coercion :malli}}}
        router   (:duct.router/reitit (ig/init config))]
    (is (= {:status 200, :body "{:query {:x 1}}"}
           (router {:request-method :get
                    :uri "/"
                    :query-params {"x" "1"}
                    :headers {"Accept" "application/json"}})))))
