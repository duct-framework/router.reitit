(defproject org.duct-framework/router.reitit "0.4.0"
  :description "Integrant methods for the Reitit routing library"
  :url "https://github.com/duct-framework/router.reitit"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.12.0"]
                 [integrant "0.13.1"]
                 [metosin/reitit "0.8.0"]]
  :repl-options {:init-ns duct.router.reitit})
