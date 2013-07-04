(ns qrcloj.utils)

(defn take-many [counts s] 
  (first (reduce 
    (fn [[acc rem] n] [(conj acc (take n rem)) (drop n rem)]) [[] s] counts)))

(defn sinterleave
  ([c1] c1)
  ([c1 c2 & colls] (apply interleave (concat [c1 c2] colls))))

