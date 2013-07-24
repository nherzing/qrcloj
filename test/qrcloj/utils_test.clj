(ns qrcloj.utils-test
  (:use clojure.test
        qrcloj.utils))

(deftest interleave-all-test
  (is (= [1 2 3] (interleave-all [1 2 3])))
  (is (= [1 4 2 5 3 6] (interleave-all [1 2 3] [4 5 6])))
  (is (= [1 3 5 2 4 6 7] (interleave-all [1 2] [3 4] [5 6 7])))
  )

(deftest take-many-test
  (is (= [[1] [2 3] [4]] (take-many [1 2 1] [1 2 3 4])))
  (is (= [[1] [] [2 3 4 5]] (take-many [1 0 4] [1 2 3 4 5])))
  )

