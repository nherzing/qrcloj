(ns qrcloj.masking
  (:use [qrcloj.interop :only [abs]]))

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
    (if (< count 5) 0 (+ 3 (- count 5))))
    rec (fn [rem count prev score]
      (if (empty? rem) (+ score (score-for count))
        (let [same? (= prev (first rem))]
          (recur (rest rem)
            (if same? (inc count) 1)
            (first rem)
            (if same? score (+ score (score-for count)))))))]
    (rec v 0 nil 0)))

(defn keys-for [dim]
  (for [x (range dim) y (range dim)] [x y]))

(defn coord-to-val [grid dim [x y]]
  (if (or (< x 0) (< y 0) (>= x dim) (>= y dim)) nil
    (get grid [x y] :l)))

(defn adjacent-score [{:keys [dim grid]}]
  (let [coords-to-vals (partial map #(coord-to-val grid dim %))
        rows-and-cols (map coords-to-vals (concat (row-indices dim) (col-indices dim)))]
    (apply + (map adj-score-vec rows-and-cols))))


(defn upper-left-of-two-by-two? [grid [x y]]
  (every?
    (comp (partial = (grid [x y])) grid)
    [[(inc x) y] [(inc x) (inc y)] [x (inc y)]]))

(defn block-score [{:keys [grid dim]}]
  (* 3 (count (filter (partial upper-left-of-two-by-two? (partial coord-to-val grid dim)) (keys-for dim)))))


(defn centers-of-one-one-three-one-one [grid [x y]]
  (let [center-of-vert?
    (and (= :d (grid [x y]) (grid [(dec x) y]) (grid [(inc x) y]) (grid [(- x 3) y]) (grid [(+ x 3) y]))
      (= :l (grid [(- x 2) y]) (grid [(+ x 2) y]))
      (or (and (= :l (grid [(- x 4) y]) (grid [(- x 5) y]) (grid [(- x 6) y])))
          (and (= :l (grid [(+ x 4) y]) (grid [(+ x 5) y]) (grid [(+ x 6) y])))))
        center-of-horz?
    (and (= :d (grid [x y]) (grid [x (dec y)]) (grid [x (inc y)]) (grid [x (- y 3)]) (grid [x (+ y 3)]))
      (= :l (grid [x (- y 2)]) (grid [x (+ y 2)]))
      (or (and (= :l (grid [x (- y 4)]) (grid [x (- y 5)]) (grid [x (- y 6)])))
          (and (= :l (grid [x (+ y 4)]) (grid [x (+ y 5)]) (grid [x (+ y 6)])))))]
    (+ (if center-of-vert? 1 0) (if center-of-horz? 1 0))))

(defn one-one-three-one-one-score [{:keys [grid dim]}]
  (* 30 (apply + (map (partial centers-of-one-one-three-one-one (partial coord-to-val grid dim)) (keys-for dim)))))


(defn ratio-score [{:keys [grid dim]}]
  (let [total (* dim dim)
        dark (count (filter (partial = :d) (vals grid)))]
    (* 10 (quot (abs (- 50 (int (* 100 (/ dark total))))) 5))))

(defn penalty-score [masked]
  (+ (adjacent-score masked)
     (block-score masked)
     (one-one-three-one-one-score masked)
     (ratio-score masked)))

(defn mask-symbol [{:keys [grid dim] :as sym}]
  (let [masked (map #(apply-mask % grid) masks)
        with-penalties (map-indexed (fn [i m] {:idx i :masked m :penalty (penalty-score {:dim dim :grid m})}) masked)]
    (apply (partial min-key :penalty) with-penalties)))

