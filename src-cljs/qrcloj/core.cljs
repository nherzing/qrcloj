(ns qrcloj.core
  (:use [qrcloj.encode :only [encode]]
        [qrcloj.symbol :only [generate]]
        [qrcloj.canvas-display :only [draw-symbol full-screen]]))


(defn ^{:export make_symbol} make-symbol [canvas-id ecl data]
  (full-screen canvas-id)
  (draw-symbol canvas-id (generate (encode (keyword ecl) data))))
