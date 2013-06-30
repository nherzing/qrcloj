(ns qrcloj.version)

(def capacity-by-ecl {
  :L (sorted-set 152 272 440 640 864)
  :M (sorted-set 128 224 352 512 688)
  :Q (sorted-set 104 176 272 384 496)
  :H (sorted-set 72  128 208 288 368)
  })

(defn best-fit [ecl num-bits]
  (some (fn [[f s]] (when (and (> num-bits f) (<= num-bits s)) s)) 
    (partition 2 1 (conj (seq (capacity-by-ecl ecl)) 0))))
