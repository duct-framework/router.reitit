(ns duct.router.reitit
  (:require [integrant.core :as ig]
            [muuntaja.core :as mu]
            [reitit.ring :as ring]
            [reitit.ring.middleware.muuntaja :as reitit-muuntaja]))

(defn- update-routes-data [routes f]
  (if (vector? routes)
    (if (vector? (first routes))
      (mapv #(update-routes-data % f) routes)
      (let [[path data] routes]
        [path (cond
                (vector? data)  (update-routes-data data f)
                (map? data)     (f data)
                (keyword? data) (f {:name data})
                :else           data)]))
    routes))

(defn- update-data [options f]
  (-> options
      (update :routes update-routes-data f)
      (update :data f)))

(def ^:private muuntaja-middleware
  [reitit-muuntaja/format-middleware])

(defn- convert-muuntaja [data]
  (cond-> data
    (:muuntaja data)
    (-> (update :muuntaja #(mu/create (merge mu/default-options %)))
        (update :middleware #(into muuntaja-middleware %)))))

(def ^:private handler-keys
  [:middleware :inject-match? :inject-router?])

(defmethod ig/init-key :duct.router/reitit [_ options]
  (let [{:keys [routes] :as opts} (-> options
                                      (update-data convert-muuntaja))]
    (ring/ring-handler
     (ring/router routes (apply dissoc opts handler-keys))
     (select-keys opts handler-keys))))
