(ns duct.handler.reitit
  (:require [integrant.core :as ig]
            [reitit.ring :as ring]))

(defmethod ig/init-key ::default [_ opts]
  (ring/create-default-handler opts))
