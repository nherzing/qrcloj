(ns qrcloj.encode-test
  (:use clojure.test
        qrcloj.encode))

(deftest to-binary-test
  (is (= [0 0 0 0 0] (dec-to-bin 5 0)))
  (is (= [0 0 0 0 0 0 1 0 0 0] (dec-to-bin 10 8)))
  (is (= [0 0 0 0 0 0 1 0 1] (dec-to-bin 9 5)))
  )

(deftest mode-numeric-test
  (is (= :numeric (mode "4321")))
  (is (= :numeric (mode "0123456789")))
  (is (= :numeric (mode "")))
  )

(deftest mode-alphanumeric-test
  (is (= :alphanumeric (mode "HELLO")))
  (is (= :alphanumeric (mode "555-505-1254")))
  (is (= :alphanumeric (mode "HTTP://CHEMEX.COM")))
  (is (= :alphanumeric (mode "$243.52 * 0.6% /- 32")))
  )

(deftest mode-binary-test
  (is (= :byte (mode "hello")))
  (is (= :byte (mode "http://www.clojure.org")))
  )

(deftest char-count-test
  (is (= [0 0 0 0 0 0 1 0 0 0] (char-count {:mode :numeric :data "01234567"})))
  (is (= [0 0 0 0 0 0 1 0 1] (char-count {:mode :alphanumeric :data "HELLO"})))
  (is (= [0 0 0 0 0 1 0 1] (char-count {:mode :byte :data "hello"})))
  )

(deftest terminator-test
  (is (= [0 0 0 0] (terminator :L 15)))
  (is (= [0 0] (terminator :L 270)))
  (is (= [] (terminator :H 128)))
  )

(deftest pad-to-test
  (is (= [1 1 1 0 1 1 0 0] (pad-to 8 [])))
  (is (= [1 0 1 0 1 1 0 1 1 1 1 0 1 1 0 0 0 0 0 1 0 0 0 1 1 1 1 0 1 1 0 0]) (pad-to 24 [1 0 1 0 1 1 0 1]))
  )

(deftest encode-data-test
  (is (= [0 0 0 0 0 0 1 1 0 0 0 1 0 1 0 1 1 0 0 1 1 0 0 0 0 1 1] 
    (encode-data {:mode :numeric :data "01234567"})))
  (is (= [0 0 0 0 0 0 1 1 0 0] 
    (encode-data {:mode :numeric :data "012"})))
  (is (= [0 0 1 1 1 0 0 1 1 1 0 1 1 1 0 0 1 1 1 0 0 1 0 0 0 0 1 0]
    (encode-data {:mode :alphanumeric :data "AC-42"})))
  (is (= [0 1 0 0 1 0 0 0 1 0 1 0 0 0 1 0 1 0 0 0 0 0 0 1 1 0 0 0]
    (encode-data {:mode :alphanumeric :data "C-3PO"})))
  (is (= [0 1 1 0 1 0 0 0 0 1 1 0 0 1 0 1 0 1 1 0 1 1 0 0 0 1 1 0 1 1 0 0 0 1 1 0 1 1 1 1 0 0 1 0 0 0 0 0 
          0 1 1 1 0 1 0 0 0 1 1 0 1 0 0 0 0 1 1 0 0 1 0 1 0 1 1 1 0 0 1 0 0 1 1 0 0 1 0 1 0 0 1 0 0 0 0 1]
    (encode-data {:mode :byte :data "hello there!"})))
  (is (= [0 1 1 0 1 0 0 0 0 1 1 1 0 1 0 0 0 1 1 1 0 1 0 0 0 1 1 1 0 0 0 0 0 0 1 1 1 0 1 0 0 0 1 0 1 1 1 1 0 0 1 0 1 1 1 1 0 1 1 0 0 0 1 1 0 1 1 0 1 1 0 0 0 1 1 0 1 1 1 1 0 1 1 0 1 0 1 0 0 1 1 1 0 1 0 1 0 1 1 1 0 0 1 0 0 1 1 0 0 1 0 1 0 1 1 0 0 1 0 0 0 1 1 0 1 1 1 1 0 1 1 0 0 0 1 1 0 1 1 1 0 0 1 1 0 0 1 0 1 1 1 0 0 1 1 0 1 1 1 1 0 1 1 1 0 0 1 0 0 1 1 0 0 1 1 1 0 0 1 0 1 1 1 1 0 1 1 0 0 0 1 1 0 1 1 0 1 1 0 0 0 1 1 0 1 1 1 1 0 1 1 0 1 0 1 0 0 1 1 1 0 1 0 1 0 1 1 1 0 0 1 0 0 1 1 0 0 1 0 1 0 1 0 1 1 1 1 1 0 1 1 0 0 0 1 1 0 1 1 0 1 1 1 1 0 1 1 1 0 0 1 0 0 1 1 0 0 1 0 1 0 0 1 0 1 1 1 1 0 1 1 0 0 0 1 1 0 1 1 0 1 1 0 0 0 1 1 0 1 1 1 1 0 1 1 0 1 0 1 0 0 1 1 1 0 1 0 1 0 1 1 1 0 0 1 0 0 1 1 0 0 1 0 1 0 0 1 0 1 1 1 0 0 1 1 0 0 0 1 1 0 1 1 0 1 1 1 1 0 1 1 1 0 0 1 0 0 1 1 0 0 1 0 1 0 0 1 0 1 1 1 1 0 0 1 0 1 1 0 1 0 0 1 0 0 1 0 1 0 0 1 1 0 0 1 1 0 1 0 0 0 1 0 1]
    (encode-data {:mode :byte :data "http://clojuredocs.org/clojure_core/clojure.core/-%3E"})))
  )

(deftest encode-test
  (is (= [16 32 12 86 97 128 236 17 236 14 157 2 200 194 148 243 167 173 141 226 10 244 165 43 172 223]
    (:data (encode :H "01234567"))))
  (is (= [32 42 69 20 12 0 236 17 236 127 225 30 221 243 90 72 228 24 39 144 4 43 69 88 110 237]
    (:data (encode :H "C-3PO"))))
  (is (= [32 91 11 120 209 114 220 77 67 64 236 17 236 17 236 17 196 35 39 119 235 215 231 226 93 23]
    (:data (encode :M "HELLO WORLD"))))
  )


