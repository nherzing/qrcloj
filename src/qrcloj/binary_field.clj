(ns qrcloj.binary-field)

(defn antilog [{:keys [order prim-poly]}] (vec (take (dec order) (iterate (fn [prev]
  (let [next (* prev 2)]
    (if (< next order)
      next
      (bit-xor next prim-poly))))
  1))))

(defn log [{:keys [order] :as f}] (zipmap (antilog f) (range 0 (dec order))))

(def add bit-xor)
(def sub add)
(defn mult [{:keys [order] :as f}]
  (let [antilog (antilog f)
        log (log f)]
    (fn [x y]
      (if (or (= 0 x) (= 0 y)) 0
        (antilog (mod (+ (log x) (log y)) (dec order)))))))

(defn generator [f]
  (let [antilog (antilog f)
        mult (mult f)]
    (partial nth (iterate
      (fn [poly] (vec (map add
        (conj poly 0)
        (cons 0 (map (partial mult (antilog (dec (count poly)))) poly)))))
      [(antilog 0)]))))

(defn poly-rem [f]
  (let [mult (mult f)]
    (fn [dividend divisor]
      (let [sized-dividend (concat dividend (repeat (dec (count divisor)) 0))]
        (loop [d sized-dividend]
          (let [order-delta (- (count d) (count divisor))]
            (if (< order-delta 0)
              d
              (recur (rest (map add d
                (map (partial mult (first d))
                  (concat divisor (repeat order-delta 0)))))))))))))

