(ns snake.game)


(enable-console-print!)

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

; state:
; snake: array of points
; trace: map of points to direction
; direction
; speed
; valid?


(defn next-state [{:keys [field snake trace dir speed] :as state}]
  (let [next-trace (assoc trace (first snake) dir)
        _ (println "NNNN" snake "TRACE" trace)
        _ (println "NEXTTRACE"  next-trace)
         next-snake (->> snake
                         (map (fn [p]
                                (println "MOVE" p (get next-trace v))
                                (move-pt p (get next-trace p)))))
        ]
     (assoc state
            :snake next-snake
            :trace next-trace)))

(assert (= (next-state
            {:snake [0 0] [1 0] [2 0]
             :trace {[0 0] [1 1]
                     [1 0] [2 0]
                     [2 0] [3 4]}}
            )))
