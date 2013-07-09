(ns qrcloj.symbol-test
  (:use clojure.test
        qrcloj.symbol))

(deftest square-test
  (is (= [[0 0]] (square [0 0] 1)))
  (is (= #{[0 0] [0 1] [0 2] [1 2] [2 2] [2 1] [2 0] [1 0]} (set (square [1 1] 3))))
  )

(deftest blank-test
  (is (= 21 (:dim (blank 1))))
  (is (= 29 (:dim (blank 3))))
  (is (= 177 (:dim (blank 40))))
  )

(deftest add-finders-test
  (let [v1 (:grid (add-finders (blank 1)))]
    (is (= :d (v1 [0 0])))
    (is (= :l (v1 [1 3])))
    (is (= nil (v1 [20 20])))
    (is (= nil (v1 [10 10])))
    (is (= :d (v1 [3 3])))
  ))

(deftest add-separators-test
  (let [v1 (:grid (add-separators (blank 1)))]
    (is (= :l (v1 [7 0])))
    (is (= :l (v1 [0 7])))
    (is (= nil (v1 [13 13])))
    (is (= nil (v1 [12 13])))
  ))

(deftest add-timing-test
  (let [v1 (:grid (add-timing (blank 1)))]
    (is (= :d (v1 [8 6])))
    (is (= :l (v1 [9 6])))
    (is (= :d (v1 [6 12])))
    (is (= nil (v1 [6 13])))
  ))

(deftest raw-data-path-test
  (is (= [[3 3] [2 3] [3 2] [2 2] [3 1] [2 1] [3 0] [2 0] [1 0] [0 0] [1 1] [0 1] [1 2] [0 2] [1 3] [0 3]]
    (raw-data-path 4)))
  (is (= [0 0] (last (raw-data-path 6))))
  (is (= 64 (count (raw-data-path 8))))
  (is (= 420 (count (raw-data-path 21))))
  (is (= nil ((set (raw-data-path 21)) [6 0])))
  )

(deftest version-modules-test
  (is (= 0 (count (version-modules 1))))
  (is (= 36 (count (version-modules 10))))
  (is (= 36 (count (version-modules 7))))
  (is (= 0 (count (version-modules 6))))
  )

