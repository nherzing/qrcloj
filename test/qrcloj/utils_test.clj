(ns qrcloj.utils-test
  (:use clojure.test
        qrcloj.utils))

(deftest take-many-test
  (is (= [[1] [2 3] [4]] (take-many [1 2 1] [1 2 3 4])))
  (is (= [[1] [] [2 3 4 5]] (take-many [1 0 4] [1 2 3 4 5])))
  )

