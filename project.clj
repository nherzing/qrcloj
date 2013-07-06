(defproject qrcloj "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :source-paths ["src-clj"]
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/math.combinatorics "0.0.4"]]
 :plugins [[lein-cljsbuild "0.3.2"]]
 :cljsbuild {
  :crossovers [qrcloj.encode qrcloj.error-correction qrcloj.utils qrcloj.symbol 
               qrcloj.binary-field qrcloj.version clojure.math.combinatorics]
  :builds [{:source-paths ["src-cljs"]
            :compiler {:output-to "out/main.js"
                       :optimizations :whitespace
                       :pretty-print true}}]})
