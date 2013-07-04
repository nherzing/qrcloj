(ns qrcloj.error-correction-test
  (:use clojure.test
        qrcloj.error-correction))

(deftest error-correction-codewords-test
  (is (= [28 28] (error-correction-codewords 2 [3 2 1])))
  (is (= [196  35  39  119  235  215  231  226  93  23]
    (error-correction-codewords 10 [32 91 11 120 209 114 220 77 67 64 236 17 236 17 236 17])))
  )

(deftest attach-error-correction-codewords-test
  (is (= [[[3 2] [15 14]] [[1] [3 2]]] (attach-error-correction-codewords [3 2 1] [[2 2] [1 2]])))
  )