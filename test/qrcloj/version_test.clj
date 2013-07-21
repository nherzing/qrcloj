(ns qrcloj.version-test
  (:use clojure.test
        qrcloj.version))

(deftest best-fit-test
  (is (= 1 (:version (best-fit :L 50))))
  (is (= 1 (:version (best-fit :L 152))))
  (is (= 3 (:version (best-fit :Q 177))))
  )

(deftest bit-capacity-test
  (is (= 152 (bit-capacity {:version 1 :ecl :L})))
  (is (= 272 (bit-capacity {:version 3 :ecl :Q})))
  )

(deftest error-correction-layout-test
  (is (= [[19 7]] (error-correction-layout {:version 1 :ecl :L})))
  (is (= [[24 26] [24 26]] (error-correction-layout {:version 4 :ecl :Q})))
  )

(deftest indicator-test
  (is (= [0 0 1 0 1 0 0 1 0 0 1 1 1 1 1 0 0 0]
    (indicator 7)))
  )