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

(deftest encode-numeric-test
  (is (= [0 0 0 1 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 1 1 0 0 0 1 0 1 0 1 1 0 0 1 1 0 0 0 0 1 1 0 0 0 0]
    (encode :L "01234567")))
  (is (= [0 0 1 0 0 0 0 0 0 0 1 0 1 0 1 0 0 1 0 0 0 1 0 1 0 0 0 1 0 1 0 0 0 0 0 0 1 1 0 0 0 0 0 0 0]
    (encode :L "C-3PO")))
  )


