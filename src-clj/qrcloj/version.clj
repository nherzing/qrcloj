(ns qrcloj.version
  (:use [qrcloj.utils :only [positions]]))


(def data-capacity-by-ecl {
  :L [19 34 55 80 108 136 156 194 232 274 324 370 428 461 523 589
      647 721 795 861 932 1006 1094 1174 1276 1370 1468 1531 1631
      1735 1843 1955 2071 2191 2306 2434 2566 2702 2812 2956]
  :M [16 28 44 64  86 108 124 154 182 216 254 290 334 365 415 453
      507 563 627 669 714 782 860 914 1000 1062 1128 1193 1267
      1373 1455 1541 1631 1725 1812 1914 1992 2102 2216 2334]
  :Q [13 22 34 48  62  76  88 110 132 154 180 206 244 261 295 325
      367 397 445 485 512 568 614 664 718 754 808 871 911 985
      1033 1115 1171 1231 1286 1354 1426 1502 1582 1666]
  :H [ 9 16 26 36  46  60  66  86 100 122 140 158 180 197 223 253
      283 313 341 385 406 442 464 514 538 596 628 661 701 745
      793 845 901 961 986 1054 1096 1142 1222 1276]
  })

(def bit-capacity-by-ecl (into {} 
  (for [[ecl versions] data-capacity-by-ecl]
    [ecl (vec (map (partial * 8) versions))])))

(defn best-fit [ecl num-bits]
  {:ecl ecl :version (inc (first (positions (fn [[f s]] (when (and (> num-bits f) (<= num-bits s)) s)) 
    (partition 2 1 (conj (seq (bit-capacity-by-ecl ecl)) 0)))))})

(defn bit-capacity [{:keys [version ecl]}]
  ((bit-capacity-by-ecl ecl) (dec version)))


(defn raw-error-correction-layouts [{:keys [version ecl]}] (({
  :L [[1 [26 19]] [1 [44 34]] [1 [70 55]] [1 [100 80]] [1 [134 108]]
      [2 [86 68]] [2 [98 78]] [2 [121 97]] [2 [146 116]] [2 [86 68] 2 [87 69]]]
  :M [[1 [26 16]] [1 [44 28]] [1 [70 44]] [2 [50 32]] [2 [67 43]]
      [4 [43 27]] [4 [49 31]] [2 [60 38] 2 [61 39]] [3 [58 36] 2 [59 37]] [4 [69 43] 1 [70 44]]]
  :Q [[1 [26 13]] [1 [44 22]] [2 [35 17]] [2 [50 24]] [2 [33 15] 2 [34 16]]
      [4 [43 19]] [2 [32 14] 4 [33 15]] [4 [40 18] 2 [41 19]] [4 [36 16] 4 [37 17]] [6 [43 19] 2 [44 20]]]
  :H [[1 [26 9]] [1 [44 16]] [2 [35 13]] [4 [25 9]] [2 [33 11] 2 [34 12]]
      [4 [43 15]] [4 [39 13] 1 [40 14]] [4 [40 14] 2 [41 15]] [4 [36 12] 4 [37 13]] [6 [43 15] 2 [44 16]]]
  } ecl) (dec version)))

(defn error-correction-layout [{:keys [version ecl] :as ver}]
  (apply concat (map (fn [[ct [total data]]] (repeat ct [data (- total data)]))
    (partition 2 (raw-error-correction-layouts ver)))))


