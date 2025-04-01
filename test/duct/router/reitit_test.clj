(ns duct.router.reitit-test
  (:require [clojure.test :refer [deftest is]]
            [integrant.core :as ig]
            [duct.router.reitit]
            [duct.handler.reitit]))

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

(deftest default-handler-test
  (let [handler (constantly {:status 200, :body "Hello World"})
        config
        {:duct.router/reitit
         {:routes {"/"    {:get {:handler handler}}
                   "/406" {:handler (constantly nil)}}
          :handlers [(ig/ref :duct.handler.reitit/default)]}
         :duct.handler.reitit/default
         {:not-found (constantly {:status 404, :body "404"})
          :method-not-allowed (constantly {:status 405, :body "405"})
          :not-acceptable (constantly {:status 406, :body "406"})}}
        router (:duct.router/reitit (ig/init config))]
    (is (= {:status 200, :body "Hello World"}
           (router {:request-method :get, :uri "/"})))
    (is (= {:status 404, :body "404"}
           (router {:request-method :get, :uri "/bad"})))
    (is (= {:status 405, :body "405"}
           (router {:request-method :post, :uri "/"})))
    (is (= {:status 406, :body "406"}
           (router {:request-method :post, :uri "/406"})))))

(defn- wrap-header [handler header value]
  (fn [request]
    (-> (handler request)
        (assoc-in [:headers header] value))))

(deftest middleware-test
  (let [handler (constantly {:status 200, :body "Hello World"})
        config  {:duct.router/reitit
                 {:routes {"/" {:get {:handler handler}}}
                  :module-middleware [[wrap-header "X-One" "1"]]
                  :middleware [[wrap-header "X-Two" "2"]]
                  :data {:module-middleware [[wrap-header "X-Three" "3"]]
                         :middleware [[wrap-header "X-Four" "4"]]}}}
        router  (:duct.router/reitit (ig/init config))]
    (is (= {"X-One" "1", "X-Two" "2", "X-Three" "3", "X-Four" "4"}
           (:headers (router {:request-method :get, :uri "/"}))))))
