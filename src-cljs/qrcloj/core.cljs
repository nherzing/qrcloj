(ns qrcloj.core
  (:use [qrcloj.encode :only [encode]]
        [qrcloj.symbol :only [generate]]))


(defn ^{:export make_symbol} make-symbol [ecl data]
  (generate 1 (encode (keyword ecl) data)))
