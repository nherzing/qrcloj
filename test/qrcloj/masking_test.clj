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

(deftest upper-left-of-two-by-two-test
  (is (upper-left-of-two-by-two? {[0 0] :d [1 0] :d [0 1] :d [1 1] :d} [0 0]))
  (is (= false (upper-left-of-two-by-two? {[0 0] :d [1 0] :d [0 1] :d [1 1] :l} [0 0])))
  )

(deftest block-score-test
  (is (= 3 (block-score {:dim 2 :grid {[0 0] :d [1 0] :d [0 1] :d [1 1] :d}})))
  (is (= 0 (block-score {:dim 21 :grid (gen-mask (masks 0) 21)})))
  (is (= 420 (block-score {:dim 21 :grid (gen-mask (masks 2) 21)})))
  (is (= 1200 (block-score {:grid {} :dim 21})))
  )


(deftest centers-of-one-one-three-one-one-test
  (is (= 0 (centers-of-one-one-three-one-one {[0 0] :d [1 0] :d [0 1] :d [1 1] :d} [0 0])))
  (is (= 1 (centers-of-one-one-three-one-one {[0 0] :l [1 0] :l [2 0] :l [3 0] :d [4 0] :l 
    [5 0] :d [6 0] :d [7 0] :d [8 0] :l [9 0] :d} [6 0])))
  (is (= 0 (centers-of-one-one-three-one-one {[0 0] :l [1 0] :l [2 0] :l [3 0] :d [4 0] :l 
    [5 0] :d [6 0] :d [7 0] :d [8 0] :l [9 0] :d} [7 0])))
  (is (= 1 (centers-of-one-one-three-one-one {[0 0] :l [1 0] :l [2 0] :l [3 0] :d [4 0] :l 
    [5 0] :d [6 0] :d [7 0] :d [8 0] :l [9 0] :d
    [9 1] :l [9 2] :d [9 3] :d [9 4] :d [9 5] :l [9 6] :d [9 7] :l [9 8] :l [9 9] :l} [6 0])))
  )

(deftest one-one-three-one-one-score-test
  (is (= 0 (one-one-three-one-one-score {:dim 5 :grid {[0 0] :d [1 0] :d [0 1] :d [1 1] :d}})))
  (is (= 60 (one-one-three-one-one-score {:dim 10 :grid {[0 0] :l [1 0] :l [2 0] :l [3 0] :d [4 0] :l 
    [5 0] :d [6 0] :d [7 0] :d [8 0] :l [9 0] :d
    [9 1] :l [9 2] :d [9 3] :d [9 4] :d [9 5] :l [9 6] :d [9 7] :l [9 8] :l [9 9] :l}})))
  (is (= 30 (one-one-three-one-one-score {:dim 10 :grid {[3 0] :d [5 0] :d [6 0] :d [7 0] :d [9 0] :d}})))
  )


(deftest ratio-score-test
  (is (= 100 (ratio-score {:dim 1 :grid {}})))
  (is (= 50 (ratio-score {:dim 2 :grid {1 :d}})))
  (is (= 0 (ratio-score {:dim 21 :grid (zipmap (range 213) (repeat 213 :d))})))
  )