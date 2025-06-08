(defproject org.duct-framework/router.reitit "0.5.0"
  :description "Integrant methods for the Reitit routing library"
  :url "https://github.com/duct-framework/router.reitit"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.12.1"]
                 [integrant "0.13.1"]
                 [metosin/reitit "0.9.1"]]
  :repl-options {:init-ns duct.router.reitit})
