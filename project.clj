(defproject api "1.1.0-SNAPSHOT"
  :description "The API for Tigris, the Enterprise Deploy for Jogral"

  :url "https://bitbucket.org/jogral/tigris-api"

  :dependencies [[org.postgresql/postgresql "42.1.1"]
                 [buddy "1.3.0"]
                 [environ "1.1.0"]
                 [clj-pdf "2.2.29"]
                 [clj-sendgrid "0.1.2"]
                 [clj-time "0.13.0"]
                 [com.layerware/hugsql "0.4.7"]
                 [com.microsoft.azure/azure-storage "5.3.1"]
                 [com.onelogin/java-saml "2.0.0"]
                 [compojure "1.6.0"]
                 [conman "0.6.4"]
                 [cprop "0.1.10"]
                 [danlentz/clj-uuid "0.1.7"]
                 [funcool/struct "1.0.0"]
                 [io.forward/sendgrid-clj "1.0"]
                 [luminus-immutant "0.2.3"]
                 [luminus-migrations "0.3.5"]
                 [luminus-nrepl "0.1.4"]
                 [luminus/ring-ttl-session "0.3.2"]
                 [markdown-clj "0.9.99"]
                 [metosin/compojure-api "1.1.10"]
                 [metosin/muuntaja "0.2.1"]
                 [metosin/ring-http-response "0.9.0"]
                 [migratus "0.9.5"]
                 [mount "0.1.11"]
                 [org.clojars.pntblnk/clj-ldap "0.0.12"]
                 [org.clojure/clojure "1.8.0"]
                 [org.clojure/java.jdbc "0.7.0-alpha3"]
                 [org.clojure/tools.cli "0.3.5"]
                 [org.clojure/tools.logging "0.3.1"]
                 [org.webjars.bower/tether "1.4.0"]
                 [org.webjars/bootstrap "4.0.0-alpha.5"]
                 [org.webjars/font-awesome "4.7.0"]
                 [org.webjars/jquery "3.1.1"]
                 [org.webjars/webjars-locator-jboss-vfs "0.1.0"]
                 [ring-cors "0.1.10"]
                 [ring-webjars "0.2.0"]
                 [ring/ring-core "1.6.1"]
                 [ring/ring-defaults "0.3.0"]
                 [selmer "1.10.7"]
                 [slugify "0.0.1"]]

  :min-lein-version "2.0.0"

  :jvm-opts ["-server" "-Dconf=.lein-env"]
  :source-paths ["src/clj"]
  :test-paths ["test/clj"]
  :resource-paths ["resources"]
  :target-path "target/%s/"
  :main ^:skip-aot api.core
  :migratus {:store :database 
             :db ~(get (System/getenv) "DATABASE_URL")}

  :plugins [[lein-cprop "1.0.3"]
            [migratus-lein "0.4.9"]
            [lein-immutant "2.1.0"]]


  :profiles
  {:uberjar {:omit-source true
             :aot :all
             :uberjar-name "tigris-api.jar"
             :source-paths ["env/prod/clj"]
             :resource-paths ["env/prod/resources"]}

   :dev           [:project/dev :profiles/dev]
   :test          [:project/dev :project/test :profiles/test]

   :project/dev  {:dependencies [[prone "1.1.4"]
                                 [ring/ring-mock "0.3.0"]
                                 [ring/ring-devel "1.6.1"]
                                 [pjstadig/humane-test-output "0.8.1"]]
                  :plugins      [[com.jakemccrary/lein-test-refresh "0.19.0"]]
                  
                  :source-paths ["env/dev/clj"]
                  :resource-paths ["env/dev/resources"]
                  :repl-options {:init-ns user}
                  :injections [(require 'pjstadig.humane-test-output)
                               (pjstadig.humane-test-output/activate!)]}
   :project/test {:resource-paths ["env/dev/resources" "env/test/resources"]}
   :profiles/dev {}
   :profiles/test {}})
