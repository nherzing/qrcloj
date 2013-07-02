(ns qrcloj.galois-field)

(def antilog (vec (take 255 (iterate (fn [prev]
  (let [next (* prev 2)]
    (if (<= next 255)
      next
      (bit-xor next 2r100011101))))
  1))))

(def log (zipmap antilog (range 0 255)))

