(ns qrcloj.symbol
  (:require [qrcloj.version :as version])
  (:use [clojure.math.combinatorics :only [selections]]
        [qrcloj.utils :only [dec-to-bin]]))

(defn blank [version] 
  {:version version :dim (version/dim version) :grid {}})

(defn color [c ms]
  (zipmap ms (repeat (count ms) c)))
(def darken (partial color :d))
(def lighten (partial color :l))

(defn shift [s ms]
  (zipmap (map (partial map + s) (keys ms)) (vals ms)))

(defn square [on dim]
  (let [ext (quot dim 2)
        low (- ext)
        high ext]
    (map (partial map + on) (distinct (apply concat 
      (for [x (range low (inc high))]
        [[x low] [x high] [low x] [high x]]))))))

(defn add-finders [{:keys [dim grid] :as sym}]
  (let [finder (merge (darken (square [3 3] 7)) (lighten (square [3 3] 5)) 
                      (darken (square [3 3] 3)) (darken (square [3 3] 1)))]
    (assoc sym :grid (merge grid 
      finder 
      (shift [(- dim 7) 0] finder) 
      (shift [0 (- dim 7)] finder)))))

(defn add-separators [{:keys [dim grid] :as sym}]
  (let [vert (lighten [[0 0] [0 1] [0 2] [0 3] [0 4] [0 5] [0 6] [0 7]])
        horz (lighten [[0 0] [1 0] [2 0] [3 0] [4 0] [5 0] [6 0] [7 0]])]
    (assoc sym :grid (merge grid
      (shift [7 0] vert) (shift [0 7] horz)
      (shift [(- dim 8) 0] vert) (shift [(- dim 8) 7] horz)
      (shift [7 (- dim 8)] vert) (shift [0 (- dim 8)] horz)))))


(defn add-timing [{:keys [dim grid] :as sym}]
  (let [len (- dim 8 8)
        horz (zipmap (for [x (range 8 (+ len 8))] [x 6]) (take len (flatten (repeat [:d :l]))))
        vert (zipmap (map reverse (keys horz)) (vals horz))]
    (assoc sym :grid (merge grid vert horz))))

(def alignment-coords [nil [] [6 18] [6 22] [6 26] [6 30] [6 34] [6 22 38] [6 24 42]
  [6 26 46] [6 28 50] [6 30 54] [6 32 58] [6 34 62] [6 26 46 66] [6 26 48 70]
  [6 26 50 74] [6 30 54 78] [6 30 56 82] [6 30 58 86] [6 34 62 90] [6 28 50 72 94]
  [6 26 50 74 98] [6 30 54 78 102] [6 28 54 80 106] [6 32 58 84 110] [6 30 58 86 114]
  [6 34 62 90 118] [6 26 50 74 98 122] [6 30 54 78 102 126] [6 26 52 78 104 130]
  [6 30 56 82 108 134] [6 34 60 86 112 138] [6 30 58 86 114 142] [6 34 62 90 118 146]
  [6 30 54 78 102 126 150] [6 24 50 76 102 128 154] [6 28 54 80 106 132 158]
  [6 32 58 84 110 136 162] [6 26 54 82 110 138 166] [6 30 58 86 114 142 170]])
(defn add-alignment [{:keys [version dim grid] :as sym}]
  (let [pattern (merge (darken (square [0 0] 5)) (lighten (square [0 0] 3)) (darken (square [0 0] 1)))]
    (assoc sym :grid (merge grid
      (apply merge (map #(shift % pattern) (remove grid (selections (alignment-coords version) 2))))))
  ))


(defn raw-data-path [dim]
  (let [on-sym? (fn [y] (and (>= y 0) (< y dim)))
        doit (fn doit [[prev-x prev-y] direction]
          (let [new-y (direction prev-y)
                new-x (if (= prev-x 8) 5 (- prev-x 2))]
            (if (on-sym? new-y)
              (cons [[prev-x new-y] [(dec prev-x) new-y]] 
                    (doit [prev-x new-y] direction))
              (if (< new-x 0) []
                (doit [new-x new-y] ({inc dec dec inc} direction))))))]
    (apply concat (doit [(dec dim) dim] dec))))

(defn format-modules [dim]
  (let [vert (concat (for [y (range 0 9)] [8 y])
                     (for [y (range (- dim 8) dim)] [8 y]))]
    (set (concat vert (map reverse vert)))))

(defn version-modules [version]
  (let [dim (version/dim version)
        upper-right (for [x (range (- dim 11) (- dim 8)) y (range 6)] [x y])]
    (if (< version 7) #{}
      (set (concat upper-right (map reverse upper-right))))))

(defn position-data [{:keys [version dim grid]} data]
  (let [data-modules
          (remove (format-modules dim)
            (remove (version-modules version)
              (remove grid (raw-data-path dim))))
        binary-data (apply concat (map (partial dec-to-bin 8) data))
        padded-binary-data (concat binary-data (repeat (- (count data-modules) (count binary-data)) 0))]
    (zipmap
      data-modules
      (map {1 :d 0 :l} padded-binary-data))))

(defn add-data [{:keys [version dim grid] :as sym} data]
  (assoc sym :grid (merge grid (position-data sym data))))



(defn disp [{:keys [dim grid]}]
  (doseq [y (range dim)]
    (doseq [x (range dim)]
      (print (get grid [x y] :0) " "))
    (prn)))

(defn generate [{:keys [version data]}]
  (-> version
      blank
      add-finders
      add-separators
      add-alignment
      add-timing
      (add-data data)))

