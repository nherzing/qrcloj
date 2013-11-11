

(defproject qrcloj "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :source-paths ["src-clj"]
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2030"]
                 [org.clojure/math.combinatorics "0.0.4"]
                 [org.clojure/math.numeric-tower "0.0.1"]]
 :plugins [[lein-cljsbuild "1.0.0-alpha2"]]
 :cljsbuild {
  :crossovers [qrcloj.encode qrcloj.error-correction qrcloj.utils qrcloj.symbol qrcloj.masking
               qrcloj.binary-field qrcloj.version qrcloj.format clojure.math.combinatorics]
  :builds [{:source-paths ["src-cljs"]
            :compiler {:output-to "out/main.js"
                       :optimizations :advanced
                       :pretty-print true}}]})
