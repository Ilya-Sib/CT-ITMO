(def eq-size? #(apply == (map count %)))
(def vec? #(and (vector? %) (every? number? %)))
(def mat? #(and (vector? %) (every? vec? %)))
(def sim? #(or (every? number? %)
               (and (not-empty %)
                    (every? sim? %)
                    (every? true?
                            (map-indexed
                              (fn [i e]
                                (== (count e) (- (count %) i)))
                              %)))))
							  
(def vec-checker #(and (every? vec? %)
                       (eq-size? %)))
(def mat-checker #(and (every? mat? %)
                       (eq-size? %)
                       (eq-size? (mapv first %))))
(def sim-checker #(and (every? sim? %)
                       (eq-size? %)))

(defn vmx-op [op checker]
  (letfn [(calc-res [& args]
            (let [curr-op (cond
                               (every? vec? args) op
                               :else calc-res)]
              (apply mapv curr-op args)))]
    (fn [& args]
      {:pre [(checker args)]}
      (apply calc-res args))))

(defn *s [op checker]
  (fn [vm & scs]
    {:pre [(and (checker (vector vm))
                (every? number? scs))]}
    (let [sc (reduce * scs)]
      (mapv #(op % sc) vm))))

(def v+ (vmx-op + vec-checker))
(def v- (vmx-op - vec-checker))
(def v* (vmx-op * vec-checker))
(def vd (vmx-op / vec-checker))
(def v*s (*s * vec-checker))

(defn scalar [& vs]
  {:pre [(vec-checker vs)]}
  (apply + (apply v* vs)))

(defn vect [& vs]
  {:pre [(and (vec-checker vs)
              (== (count (first vs)) 3))]}
  (reduce #(vector (- (* (nth %1 1) (nth %2 2)) (* (nth %1 2) (nth %2 1)))
                   (- (* (nth %1 2) (nth %2 0)) (* (nth %1 0) (nth %2 2)))
                   (- (* (nth %1 0) (nth %2 1)) (* (nth %1 1) (nth %2 0)))) vs))

(def m+ (vmx-op + mat-checker))
(def m- (vmx-op - mat-checker))
(def m* (vmx-op * mat-checker))
(def md (vmx-op / mat-checker))
(def m*s (*s v*s mat-checker))

(defn m*v [m & vs]
  {:pre [(and (mat? m)
              (vec-checker vs))]}
  (mapv #(apply + (apply v* % vs)) m))

(defn transpose [m]
  {:pre [(mat? m)]}
  (apply mapv vector m))

(defn m*m [& ms]
  {:pre [(and (every? mat? ms)
              (every? eq-size? ms))]}
  (reduce (fn [m1 m2]
            (mapv #(m*v (transpose m2) %) m1)) ms))

(def x+ (vmx-op + sim-checker))
(def x- (vmx-op - sim-checker))
(def x* (vmx-op * sim-checker))
(def xd (vmx-op / sim-checker))