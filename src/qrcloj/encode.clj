(ns qrcloj.encode)

(defn dec-to-bin [len n]
  (let [bin (Integer/toString n 2)]
    (map {\0 0 \1 1} (concat (repeat (- len (count bin)) \0) bin))
  ))

(def mode-indicator {
  :numeric [0 0 0 1]
  :alphanumeric [0 0 1 0]
  :byte [0 1 0 0]
  })


(def bits-in-count {
  :numeric 10
  :alphanumeric 9
  :byte 8
  })

(defn char-count [{:keys [mode data]}]
  (dec-to-bin (bits-in-count mode) (count data)))

(defn general-encode [val-map group-size group-fn data]
  (->> data
       (map val-map)
       (partition group-size group-size [])
       (map group-fn)
       flatten))

(defmulti encode-data :mode)

(def numeric-values (zipmap (seq "0123456789") (range 0 10)))
(defmethod encode-data :numeric [{:keys [data]}]
  (let [group-to-bin #(dec-to-bin ({1 4 2 7 3 10} (count %)) (Integer. (apply str %)))]
    (general-encode numeric-values 3 group-to-bin data)))

   

(def alphanumeric-values (zipmap (seq "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ $%*+-./:") (range 0 45)))
(defmethod encode-data :alphanumeric [{:keys [data]}]
  (let [group-to-bin (fn [vs] (dec-to-bin ({1 6 2 11} (count vs)) 
                                          (if (= 2 (count vs))
                                            (+ (* 45 (first vs)) (second vs))
                                            (first vs))))]
    (general-encode alphanumeric-values 2 group-to-bin data)))

(defmethod encode-data :byte [{:keys [data]}]
  (general-encode int 1 (comp (partial dec-to-bin 8) first) data))


(defn mode [data]
  (cond (re-matches #"\d*" data) :numeric
    (re-matches #"[0-9A-Z $%*+-./:]*" data) :alphanumeric
    (every? #(<= 0 % 255) (map int data)) :byte)
  )

(defn encode [data]
  (let [tagged {:data data :mode (mode data)}]
    (concat 
      (mode-indicator (tagged :mode)) 
      (char-count tagged)
      (encode-data tagged))
  ))