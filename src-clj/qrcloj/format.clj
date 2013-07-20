(ns qrcloj.format
  (:use [qrcloj.utils :only [dec-to-bin]]
        [qrcloj.error-correction :only [poly-rem]]))

(def ecl-indicator {
  :L [0 1] 
  :M [0 0]
  :Q [1 1]
  :H [1 0]
  })

(def mask-indicator (partial dec-to-bin 3))

(def generator [1 0 1 0 0 1 1 0 1 1 1])
(def mask [1 0 1 0 1 0 0 0 0 0 1 0 0 1 0])


(defn indicator [ecl mask-idx]
  (let [data (concat (ecl-indicator ecl) (mask-indicator mask-idx))]
    (reverse (map bit-xor
      (concat data
        (poly-rem data generator))
      mask))))


(defn vert-modules [dim] (concat 
  [[8 0] [8 1] [8 2] [8 3] [8 4] [8 5] [8 7] [8 8]]
  (for [y (range (- dim 7) dim)] [8 y])))

(defn horz-modules [dim] (concat
  (for [x (range (dec dim) (- dim 9) -1)] [x 8])
  [[7 8] [5 8] [4 8] [3 8] [2 8] [1 8] [0 8]]))

(defn dark-module [dim] [8 (- dim 8)])