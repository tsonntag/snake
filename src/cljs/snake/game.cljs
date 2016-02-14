(ns snake.game)


(enable-console-print!)

(def field {:x 50 :y 50})

; snake: array of points

; trace: {point => dicubeion}

(defn add [ps]
  (apply mapv + ps))


(defn multiply [l point]
  (mapv #(* l %) point))

(defn line [start len dicubeion]
  (->> start
       (iterate #(add % dicubeion))
       (take len)))

(defn in-range? [[r0 r1] x]
  (and (<= r0 x) (< x r1)))

; cube is [[x0 x1][y0 y1]..]
(defn in-cube? [cube p]
  (every #(in-range? % p) cube))


(assert (= (multiply 2 [2 3])            [4 6]))

(assert (= (line [2 1] 3 [0 1])          [[2 1] [2 2] [2 3]] ))
(assert (= (line [2 1] 3 [2 1])          [[2 1] [4 2] [6 3]]))

(assert (in-range? [0 3] 0))
(assert (in-range? [0 3] 1))
(assert (in-range? [0 3] 12))
(assert (not (in-range? [0 3] -1)))
(assert (not (in-range? [0 3] 3)))

(assert (in-cube?  [[0 3] [0 4]] [0 0]))
(assert (in-cube?  [[0 3] [0 4]] [0 3]))
(assert (in-cube?  [[0 3] [0 4]] [2 3]))
(assert (in-cube?  [[0 3] [0 4]] [2 0]))
(assert (in-cube?  [[0 3] [0 4]] [1 2]))
(assert (not (in-cube?  [[0 3] [0 4]] [3 0])))
(assert (not (in-cube?  [[0 3] [0 4]] [3 4])))
(assert (not (in-cube?  [[0 3] [0 4]] [0 4])))
(assert (not (in-cube?  [[0 3] [0 4]] [5 6])))


; snake: array of points
; trace: map of points to direction
; direction
(def state
  {:field field
   :snake 0
   })
