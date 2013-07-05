(ns qrcloj.symbol-test
  (:use clojure.test
        qrcloj.symbol))

(deftest blank-test
  (is (= 21 (:dim (blank 1))))
  (is (= 29 (:dim (blank 3))))
  (is (= 177 (:dim (blank 40))))
  )

(deftest add-finders-test
  (let [v1 (:grid (add-finders (blank 1)))]
    (is (= :d (v1 [0 0])))
    (is (= :l (v1 [1 3])))
    (is (= :d (v1 [20 20])))
    (is (= nil (v1 [10 10])))
    (is (= :d (v1 [3 3])))
  ))

(deftest add-separators-test
  (let [v1 (:grid (add-separators (blank 1)))]
    (is (= :l (v1 [7 0])))
    (is (= :l (v1 [0 7])))
    (is (= :l (v1 [13 13])))
    (is (= nil (v1 [12 13])))
  ))

(deftest add-timing-test
  (let [v1 (:grid (add-timing (blank 1)))]
    (is (= :d (v1 [8 6])))
    (is (= :l (v1 [9 6])))
    (is (= :d (v1 [6 12])))
    (is (= nil (v1 [6 13])))
  ))