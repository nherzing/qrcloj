(ns qrcloj.core
  (:use [qrcloj.encode :only [encode]]
        [qrcloj.symbol :only [generate]]
        [qrcloj.canvas-display :only [draw-symbol set-size]]))


(def ^{:export set_size} set-size set-size)
(defn ^{:export make_symbol} make-symbol [canvas-id ecl data]
  (draw-symbol canvas-id (generate (encode (keyword ecl) data))))
