(ns qrcloj.version-test
  (:use clojure.test
        qrcloj.version))

(deftest best-fit-test
  (is (= 152 (best-fit :L 50)))
  (is (= 152 (best-fit :L 152)))
  (is (= 272 (best-fit :Q 177)))
  )
