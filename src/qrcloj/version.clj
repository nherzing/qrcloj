(ns qrcloj.version
  (:use [clojure.contrib.seq :only [positions]]))


(def data-capacity-by-ecl {
  :L [19 34 55 80 108 136 156 194 232 274]
  :M [16 28 44 64  86 108 124 154 182 216]
  :Q [13 22 34 48  62  76  88 110 132 154]
  :H [ 9 16 26 36  46  60  66  86 100 122]
  })

(def bit-capacity-by-ecl (into {} 
  (for [[ecl versions] data-capacity-by-ecl]
    [ecl (vec (map (partial * 8) versions))])))

(defn best-fit [ecl num-bits]
  {:ecl ecl :version (inc (first (positions (fn [[f s]] (when (and (> num-bits f) (<= num-bits s)) s)) 
    (partition 2 1 (conj (seq (bit-capacity-by-ecl ecl)) 0)))))})

(defn bit-capacity [{:keys [version ecl]}]
  ((bit-capacity-by-ecl ecl) (dec version)))

(defn error-correction-layout [{:keys [version ecl]}] (({
  :L [[[19  7]] [[34 10]] [[55 15]] [[80 20]] [[108 26]]]
  :M [[[16 10]] [[28 16]] [[44 26]] [[32 36] [32 36]] [[43 48] [43 48]]]
  :Q [[[13 13]] [[22 22]] [[17 36] [17 36]] [[24 52] [24 52]] [[15 72] [15 72] [17 72] [17 72]]]
  :H [[[ 9 17]] [[16 28]] [[13 44] [13 44]] (repeat 4 [9 64]) [[11 88] [11 88] [12 88] [12 88]]]
  } ecl) (dec version)))


