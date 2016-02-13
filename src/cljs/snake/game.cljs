(ns cljs.snake.game)


(defn field {:x 50 :y 50})

; snake: array of points

; trace: {point => direction}

(defn move [point vs]
  (reduce + point vs))

(defn make-snake [start len direction]
  (->> (range len)
       (map (fn [i]
)))
  )
(defn state
  {:field
   :snake})
