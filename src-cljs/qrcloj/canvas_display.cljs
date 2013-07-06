(ns qrcloj.canvas-display)

(defn context [canvas-id]
  (. (. js/document (getElementById canvas-id)) (getContext "2d")))

(defn canvas [canvas-id]
  (.-canvas (context canvas-id)))

(defn full-screen [canvas-id]
  (set! (.-width (canvas canvas-id)) (.-innerWidth js/window))
  (set! (.-height (canvas canvas-id)) (.-innerHeight js/window)))

(defn min-dim [canvas]
  (min (.-width canvas) (.-height canvas)))

(defn draw-symbol [canvas-id {:keys [grid dim]}]
  (let [ctx (context canvas-id)
        colors {:l "rgba(255, 255, 255, 1)" :d "rgba(0, 0, 0, 1)"}
        set-fill (fn [c] (set! (.-fillStyle ctx) (colors c)))
        px (quot (min-dim (canvas canvas-id)) (+ 4 dim))]
    (doseq [[[x y] color] grid]
      (set-fill color)
      (. ctx(fillRect (* px x) (* px y) px px)))
  ))
