(ns qrcloj.error-correction
  (:require [qrcloj.binary-field :as binary-field])
  (:use [qrcloj.utils :only [take-many]]))


(def field {:order 256 :prim-poly 2r100011101})
(def generator (binary-field/generator field))
(def poly-rem (binary-field/poly-rem field))

(defn error-correction-codewords [n data-codewords]
  (poly-rem data-codewords (generator n)))

(defn attach-error-correction-codewords [data-codewords layout]
  (let [grouped-codewords (take-many (map first layout) data-codewords)]
    (map vector 
      grouped-codewords 
      (map #(error-correction-codewords %1 %2) (map second layout) grouped-codewords))))
