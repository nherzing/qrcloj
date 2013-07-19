(ns qrcloj.core
  (:use [qrcloj.encode :only [encode]]
        [qrcloj.symbol :only [generate generate-unmasked]]
        [qrcloj.canvas-display :only [draw-symbol set-size]]
        [qrcloj.masking :only [masks gen-mask]]))


(def ^{:export set_size} set-size set-size)
(defn ^{:export make_symbol} make-symbol [canvas-id ecl data]
  (draw-symbol canvas-id (generate (encode (keyword ecl) data))))

(defn ^{:export make_unmasked_symbol} make-unmasked-symbol [canvas-id ecl data]
  (draw-symbol canvas-id (generate-unmasked (encode (keyword ecl) data))))

(defn ^{:export make_mask} make-mask [canvas-id mask-idx]
  (draw-symbol canvas-id {:dim 21 :grid (gen-mask (masks mask-idx) 21)}))
