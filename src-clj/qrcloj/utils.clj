(ns qrcloj.utils)

(defn take-many [counts s] 
  (first (reduce 
    (fn [[acc rem] n] [(conj acc (take n rem)) (drop n rem)]) [[] s] counts)))

(defn sinterleave
  ([c1] c1)
  ([c1 c2 & colls] (apply interleave (concat [c1 c2] colls))))

(defn indexed [s]
  (map vector (iterate inc 0) s))

(defn positions [pred coll]
  (for [[idx elt] (indexed coll) :when (pred elt)] idx))