(ns duct.router.reitit
  (:require [integrant.core :as ig]
            [reitit.ring :as ring]))

(def ^:private handler-keys
  [:middleware :inject-match? :inject-router?])

(defmethod ig/init-key :duct.router/reitit
  [_ {:keys [routes] :as options}]
  (ring/ring-handler
   (ring/router routes (apply dissoc options handler-keys))
   (select-keys options handler-keys)))
