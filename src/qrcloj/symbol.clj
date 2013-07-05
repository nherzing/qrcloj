(ns qrcloj.symbol)

(defn blank [version-n] 
  {:dim (+ 17 (* 4 version-n)) :grid {}})

(defn color [c ms]
  (zipmap ms (repeat (count ms) c)))
(def darken (partial color :d))
(def lighten (partial color :l))

(defn shift [s ms]
  (zipmap (map (partial map + s) (keys ms)) (vals ms)))

(defn add-finders [{:keys [dim grid] :as sym}]
  (let [finder-7 [[0 0] [0 1] [0 2] [0 3] [0 4] [0 5] [0 6]
                 [1 6] [2 6] [3 6] [4 6] [5 6] [6 6]
                 [6 5] [6 4] [6 3] [6 2] [6 1] [6 0]
                 [5 0] [4 0] [3 0] [2 0] [1 0]]
        finder-5 [[1 1] [1 2] [1 3] [1 4] [1 5]
                  [2 5] [3 5] [4 5] [5 5]
                  [5 4] [5 3] [5 2] [5 1]
                  [4 1] [3 1] [2 1]]
        finder-3 [[2 2] [2 3] [2 4]
                  [3 4] [4 4]
                  [4 3] [4 2]
                  [3 2] [3 3]]
        finder (merge (darken finder-7) (lighten finder-5) (darken finder-3))]
  (assoc sym :grid (merge grid 
    finder 
    (shift [(- dim 7) 0] finder) 
    (shift [(- dim 7) (- dim 7)] finder) 
    (shift [0 (- dim 7)] finder)))))

(defn add-separators [{:keys [dim grid] :as sym}]
  (let [vert (lighten [[0 0] [0 1] [0 2] [0 3] [0 4] [0 5] [0 6] [0 7]])
        horz (lighten [[0 0] [1 0] [2 0] [3 0] [4 0] [5 0] [6 0] [7 0]])]
    (assoc sym :grid (merge grid
      (shift [7 0] vert) (shift [0 7] horz)
      (shift [(- dim 8) 0] vert) (shift [(- dim 8) 7] horz)
      (shift [7 (- dim 8)] vert) (shift [0 (- dim 8)] horz)
      (shift [(- dim 8) (- dim 8)] vert) (shift [(- dim 8) (- dim 8)] horz)))))


(defn add-timing [{:keys [dim grid] :as sym}]
  (let [len (- dim 8 8)
        horz (zipmap (for [x (range 8 (+ len 8))] [x 6]) (take len (flatten (repeat [:d :l]))))
        vert (zipmap (map reverse (keys horz)) (vals horz))]
    (assoc sym :grid (merge grid vert horz))))


(defn disp [{:keys [dim grid]}]
  (doseq [y (range dim)]
    (doseq [x (range dim)]
      (print (grid [x y]) " "))
    (prn)))

(disp (add-timing (add-separators (add-finders (blank 1)))))




