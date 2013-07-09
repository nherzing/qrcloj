(ns qrcloj.core
  (:use [qrcloj.encode :only [encode]]
        [qrcloj.symbol :only [generate]]
        [qrcloj.canvas-display :only [draw-symbol set-size]]
        [qrcloj.masking :only [masks]]))


(def ^{:export set_size} set-size set-size)
(defn ^{:export make_symbol} make-symbol [canvas-id ecl data]
  (draw-symbol canvas-id (generate (encode (keyword ecl) data))))

(defn ^{:export make_mask} make-mask [canvas-id mask]
  (draw-symbol canvas-id {:dim 21 :grid
    (apply hash-map (apply concat (for [x (range 21) y (range 21)] [[x y] ((masks mask) [x y])])))}))
