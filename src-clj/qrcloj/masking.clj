(ns qrcloj.masking)

(def masks (vec (map (fn [m] #(if (= 0 (m %)) :d :l))
  [(fn [[x y]] (mod (+ x y) 2))
   (fn [[x y]] (mod y 2))
   (fn [[x y]] (mod x 3))
   (fn [[x y]] (mod (+ x y) 3))
   (fn [[x y]] (mod (+ (quot y 2) (quot x 3)) 2))
   (fn [[x y]] (+ (mod (* x y) 2) (mod (* x y) 3)))
   (fn [[x y]] (mod (+ (mod (* x y) 2) (mod (* x y) 3)) 2))
   (fn [[x y]] (mod (+ (mod (+ x y) 2) (mod (* x y) 3)) 2))])))

(defn xor [a b] (b (a {:d {:d :l :l :d} :l {:d :d :l :l}})))

(defn apply-mask [mask data]
  (into {}
    (for [[xy m] data]
      [xy (xor (mask xy) m)])))