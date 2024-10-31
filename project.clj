(defproject org.duct-framework/router.reitit "0.1.0-SNAPSHOT"
  :description "Integrant methods for the Reitit routing library"
  :url "https://github.com/duct-framework/router.reitit"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.11.4"]
                 [integrant "0.13.0"]
                 [metosin/reitit "0.7.2"]]
  :repl-options {:init-ns duct.router.reitit})
