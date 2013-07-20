(ns qrcloj.format-test
  (:use clojure.test
        qrcloj.format))

(deftest indicator-test
  (is (= [0 1 1 1 0 0 1 1 0 0 0 0 0 0 1]
    (indicator :M 5)))
  (is (= [0 0 1 0 0 0 1 1 1 1 1 0 1 1 1]
    (indicator :L 0)))
  (is (= [1 0 1 0 1 0 1 0 0 1 0 0 0 0 0]
    (indicator :H 5)))
  (is (= [1 1 1 1 0 1 0 0 0 1 1 0 0 1 1]
    (indicator :L 4)))
  )

(deftest vert-modules-test
  (is (= [[8 0] [8 1] [8 2] [8 3] [8 4] [8 5] [8 7] [8 8] [8 14] [8 15] [8 16] [8 17] [8 18] [8 19] [8 20]]
    (vert-modules 21))))

(deftest horz-modules-test
  (is (= [[20 8] [19 8] [18 8] [17 8] [16 8] [15 8] [14 8] [13 8] [7 8] [5 8] [4 8] [3 8] [2 8] [1 8] [0 8]]
    (horz-modules 21))))
