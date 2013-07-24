(ns qrcloj.encode
  (:require [qrcloj.version :as v])
  (:use [qrcloj.utils :only [interleave-all dec-to-bin]]
        [qrcloj.error-correction :only [attach-error-correction-codewords]]
        [qrcloj.interop :only [int-to-str str-to-int char-to-ascii]]))

(def mode-indicator {
  :numeric [0 0 0 1]
  :alphanumeric [0 0 1 0]
  :byte [0 1 0 0]
  })


(defn bits-in-count [mode version]
  (({:numeric [10 12 14]
    :alphanumeric [9 11 13]
    :byte [8 16 16]
    } mode) (cond (< version 10) 0
                  (< version 27) 1
                  :else 2)))


(defn char-count [{:keys [mode data version]}]
  (dec-to-bin (bits-in-count mode version) (count data)))

(defn terminator [version num-bits]
  (take (- (v/bit-capacity version) num-bits) [0 0 0 0]))

(defn general-encode [val-map group-size group-fn data]
  (->> data
       (map val-map)
       (partition group-size group-size [])
       (map group-fn)
       flatten))

(defmulti encode-data :mode)

(def numeric-values (zipmap (seq "0123456789") (range 0 10)))
(defmethod encode-data :numeric [{:keys [data]}]
  (let [group-to-bin #(dec-to-bin ({1 4 2 7 3 10} (count %)) (str-to-int (apply str %)))]
    (general-encode numeric-values 3 group-to-bin data)))

   
(def alphanumeric-values (zipmap (seq "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ $%*+-./:") (range 0 45)))
(defmethod encode-data :alphanumeric [{:keys [data]}]
  (let [group-to-bin (fn [vs] (dec-to-bin ({1 6 2 11} (count vs)) 
                                          (if (= 2 (count vs))
                                            (+ (* 45 (first vs)) (second vs))
                                            (first vs))))]
    (general-encode alphanumeric-values 2 group-to-bin data)))

(defmethod encode-data :byte [{:keys [data]}]
  (general-encode char-to-ascii 1 (comp (partial dec-to-bin 8) first) data))

(defn mode [data]
  (cond (re-matches #"\d*" data) :numeric
    (re-matches #"[0-9A-Z $%*+-./:]*" data) :alphanumeric
    (every? #(<= 0 % 255) (map char-to-ascii data)) :byte)
  )

(defn data-to-bitstream [ecl data]
  (let [m (mode data)
        version (v/best-fit m ecl data)
        tagged {:data data :mode (mode data) :version (:version version)}
        bitstream (concat
                  (mode-indicator (:mode tagged))
                  (char-count tagged)
                  (encode-data tagged))]
    (assoc version :data (concat bitstream (terminator version (count bitstream))))))

(defn pad-to [version-size bitstream]
  (concat bitstream
    (take (- version-size (count bitstream)) (flatten (repeat [1 1 1 0 1 1 0 0 0 0 0 1 0 0 0 1])))))

(defn bitstream-to-codewords [{:keys [ecl data] :as sym}]
  (let [to-boundary (- 8 (rem (count data) 8))
        zero-padded (concat data (repeat to-boundary 0))]
    (assoc sym :data (map (comp #(str-to-int % 2) (partial apply str)) (partition 8 (pad-to (v/bit-capacity sym) zero-padded))))))


(defn interleave-eccs [{:keys [ecl data] :as sym}]
  (let [codewords-and-eccs (attach-error-correction-codewords data (v/error-correction-layout sym))
        data (map first codewords-and-eccs)
        eccs (map second codewords-and-eccs)]
    (assoc sym :data (concat (apply interleave-all data) (apply interleave-all eccs)))))


(defn encode [ecl data]
  (->> data
       (data-to-bitstream ecl)
       bitstream-to-codewords
       interleave-eccs))

