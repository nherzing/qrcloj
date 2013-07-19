(ns qrcloj.interop
  (:require [clojure.math.numeric-tower :as numeric-tower]))


(defn int-to-str [n b] (Integer/toString n b))
(defn str-to-int 
  ([s] (Integer/parseInt s))
  ([s b] (Integer/parseInt s b)))

(def char-to-ascii int)
(def abs numeric-tower/abs)

