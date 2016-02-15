(ns snake.game)


(defn move-pt [point direction]
  (mapv + point direction))

(defn multiply [l point]
  (mapv #(* l %) point))

(defn in-range? [[r0 r1] x]
  (and (<= r0 x) (< x r1)))

; cube is [[x0 x1][y0 y1]..]
(defn in-cube? [cube p]
  (->> (map vector cube p)
       ;(map (partial apply in-range?))
       ;println
       (every? (partial apply in-range?))))

; p0 (mv p0 d0)
; p0 p1
; p0 p1 (mv p1 d1)
; p0 p1 p2
(defn line
  "create line from point and directions"
  [p ds]
  (->> ds
       (reduce (fn [l d]
                 (conj l (move-pt (last l) d)))
               [p])))
(defn straight-line
  [pt len dir]
  (line pt (repeat len dir)))

(defn hit-cube? [line cube]
  (not-every? #(in-cube? cube %) line)
  )
(assert (= (move-pt [2 3] [2 1]) [4 4]))

(assert (= (multiply 2 [2 3])            [4 6]))

(assert (in-range? [0 3] 0))
(assert (in-range? [0 3] 1))
(assert (in-range? [0 3] 2))
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

(assert (= (line [2 3] [[1 0] [2 3] [2 1]]) [[2 3] [3 3] [5 6] [7 7]]))
(assert (= (straight-line [2 3] 3 [1 0]) [[2 3] [3 3] [4 3] [5 3]]))

(assert (hit-cube? [[0 1] [0 2]] [[0 1] [0 2]]))
; state:
; snake: array of points
; trace: map of points to direction
; direction
; speed
; valid?


(defn next-state [{:keys [field snake trace dir speed] :as state}]
  (let [
        _ (println "NEXT: SNAKE=" snake "TRACE=" trace)
         next-snake (->> snake
                         (map (fn [p]
                                (move-pt p (get trace p dir)))))
        _ (println "NEXT: NEXT-SNAKE=" next-snake)
        next-trace (zipmap next-snake (map #(get trace % dir) next-snake))
        _ (println "NEXT: NEXT-TRACE=" next-trace)
        valid (hit-cube? snake field)
        ]
    (assoc state
           :snake next-snake
           :trace next-trace)))

(defn make-state [[field-x field-y] snake-start snake-len snake-dir speed]
  (let [field [[0 field-x] [0 field-y]]
        snake (straight-line snake-start snake-len snake-dir)
        _ (println "MAKE" snake)]
    {:field field
     :snake snake
     :trace (zipmap snake (repeat (inc snake-len) snake-dir))
     :dir snake-dir
     :speed speed
     :valid (not (hit-cube? snake field))}))
