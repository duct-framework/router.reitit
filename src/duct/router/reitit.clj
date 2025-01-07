(ns duct.router.reitit
  (:require [integrant.core :as ig]
            [muuntaja.core :as mu]
            [reitit.ring :as ring]
            [reitit.ring.coercion :as rrc]
            [reitit.ring.middleware.muuntaja :as rmu]))

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
  [rmu/format-middleware])

(defn- convert-muuntaja [data]
  (cond-> data
    (:muuntaja data)
    (-> (update :muuntaja #(mu/create (merge mu/default-options %)))
        (update :middleware #(into muuntaja-middleware %)))))

(def ^:private coercion-middleware
  [rrc/coerce-exceptions-middleware
   rrc/coerce-request-middleware
   rrc/coerce-response-middleware])

(def ^:private coercion-engines
  '{:malli  reitit.coercion.malli/coercion
    :schema reitit.coercion.schema/coercion
    :spec   reitit.coercion.spec/coercion})

(defn- convert-coercion [data]
  (cond-> data
    (:coercion data)
    (-> (update :coercion (comp var-get requiring-resolve coercion-engines))
        (update :middleware #(into coercion-middleware %)))))

(defmethod ig/init-key :duct.router/reitit [_ options]
  (let [opts   (-> options
                   (update-data convert-coercion)
                   (update-data convert-muuntaja))
        router (ring/router (:routes opts) opts)]
    (if-some [handlers (seq (:handlers opts))]
      (ring/ring-handler router (apply ring/routes handlers) opts)
      (ring/ring-handler router opts))))
