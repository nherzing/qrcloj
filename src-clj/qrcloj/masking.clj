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

(defn gen-mask [mask dim]
  (apply hash-map (apply concat (for [x (range dim) y (range dim)] [[x y] (mask [x y])]))))

(defn apply-mask [mask data]
  (into {}
    (for [[xy m] data]
      [xy (xor (mask xy) m)])))


(defn col-indices [dim]
  (for [x (range dim)] (for [y (range dim)] [x y])))
(defn row-indices [dim]
  (map (partial map reverse) (col-indices dim)))


(defn adj-score-vec [v]
  (let [score-for (fn [count] 
    (if (< count 5) 0
      (+ 3 (- count 5))))
    rec (fn [rem count prev score]
      (if (empty? rem) (+ score (score-for count))
        (let [same? (= prev (first rem))]
          (recur (rest rem)
            (if same? (inc count) 1)
            (first rem)
            (if same? score (+ score (score-for count)))))))]
    (rec v 0 nil 0)))



(defn adjacent-score [{:keys [dim grid]}]
  (let [col-scores (map adj-score-vec (map (partial map #(get grid (vec %) :l)) (col-indices dim)))
        row-scores (map adj-score-vec (map (partial map #(get grid (vec %) :l)) (row-indices dim)))]
    (apply + (concat col-scores row-scores))))

