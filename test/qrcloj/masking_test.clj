(ns qrcloj.masking-test
  (:use clojure.test
        qrcloj.masking))

(deftest adj-score-vec-test
  (is (= 0 (adj-score-vec [:d :d :d :d :l :l :l :l :d :l])))
  (is (= 3 (adj-score-vec [:d :d :d :d :d :l :d :l :d :l])))
  (is (= 10 (adj-score-vec [:d :d :d :d :d :d :d :l :d :d :l :l :l :l :d :d :d :d :d :d :d])))
  (is (= 8 (adj-score-vec [:d :l :l :l :l :d :d :d :d :l :l :l :l :l :d :d :d :d :d :d :d])))
  )

(deftest adjacent-score-test
  (is (= 0 (adjacent-score {:dim 21 :grid (gen-mask (masks 4) 21)})))
  (is (= 278 (adjacent-score {:dim 21 :grid (gen-mask (masks 5) 21)})))
  )
