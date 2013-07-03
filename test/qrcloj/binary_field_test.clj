(ns qrcloj.binary-field-test
  (:use clojure.test
        qrcloj.binary-field))

(deftest antilog-test
  (is (= 1 ((antilog {:order 2 :prim-poly 0}) 0)))
  (is (= 1 ((antilog {:order 256 :prim-poly 2r100011101}) 0)))
  (is (= 180 ((antilog {:order 256 :prim-poly 2r100011101}) 20)))
  (is (= 242 ((antilog {:order 256 :prim-poly 2r101001101}) 11)))
  )

(deftest log-test
  (is (= 0 ((log {:order 2 :prim-poly 0}) 1)))
  (is (= 20 ((log {:order 256 :prim-poly 2r100011101}) 180)))
  (is (= 11 ((log {:order 256 :prim-poly 2r101001101}) 242)))
  )

(deftest add-test
  (is (= 0 (add 0 0)))
  (is (= 0 (add 1 1)))
  (is (= 314 (add 123 321)))
  )

(deftest add-test
  (is (= 0 (sub 0 0)))
  (is (= 0 (sub 1 1)))
  (is (= 314 (sub 123 321)))
  )

(deftest mult-test
  (is (= 0 ((mult {:order 2 :prim-poly 0}) 0 1)))
  (is (= 15 ((mult {:order 256 :prim-poly 2r100011101}) 1 15)))
  (is (= 172 ((mult {:order 256 :prim-poly 2r100011101}) 12 25)))
  )

(deftest generator-test
  (is (= [1] ((generator {:order 2 :prim-poly 0}) 0)))
  (is (= [1 1] ((generator {:order 2 :prim-poly 0}) 1)))
  (is (= [1 3 2] ((generator {:order 256 :prim-poly 2r100011101}) 2)))
  (is (= [1 31 198 63 147 116] ((generator {:order 256 :prim-poly 2r100011101}) 5)))
  )

(deftest poly-rem-test
  (is (= [1 0] ((poly-rem {:order 2 :prim-poly 0}) [1 0 0] [1 1 0])))
  (is (= [4 4] ((poly-rem {:order 256 :prim-poly 2r100011101}) [1 2 3] [1 3 2])))
  (is (= [29 30] ((poly-rem {:order 256 :prim-poly 2r100011101}) [3 3 3] [1 3 2])))
  (is (= [196 35 39 119 235 215 231 226 93 23]
    ((poly-rem {:order 256 :prim-poly 2r100011101})
      [32 91 11 120 209 114 220 77 67 64 236 17 236 17 236 17] ((generator {:order 256 :prim-poly 2r100011101}) 10))))
  )
