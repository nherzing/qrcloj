(ns qrcloj.interop)

(defn int-to-str ([n] (. n toString))
                 ([n r] (. n (toString r))))
(def str-to-int js/parseInt)

(defn char-to-ascii [c] (. c (charCodeAt 0)))

(defn abs [n] (. js/Math (abs n)))
