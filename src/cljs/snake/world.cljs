(ns snake.world
  (:require
   [snake.physics :refer [straight-line hit-cube? hit-self? move-pt]]))

(defn valid? [{:keys [snake field] :as world}]
  (not (or
        (hit-cube? snake field)
        (hit-self? snake))))

(defn valid! [world]
  (assoc world :valid (valid? world)))

(defn move
  "move snake"
  [{:keys [field snake trace direction tick] :as world}]
  (let [
;        _ (println "NEXT: FIELD=" field " DIRECTION=" direction)
;        _ (println "NEXT:   SNAKE=" snake "   TRACE=" trace "   VALID=" (valid? world))
        next-snake (->> snake
                        (map (fn [p]
                               (move-pt p (get trace p direction)))))

        next-trace (zipmap next-snake (map #(get trace % direction) next-snake))
                                        ;       _ (println "NEXT: N-SNAKE=" next-snake " N-TRACE" next-trace  " VALID=" (valid? (assoc world :snake next-snake)))
        ]
    (-> world
        (assoc
         :snake next-snake
         :trace next-trace
         :tick (inc (:tick world)))
        valid!)))

(defn new-direction! [world direction]
  (assoc world :direction direction))

(defn new-world [[field-x field-y] snake-start snake-len snake-direction speed]
  (let [field [[0 field-x] [0 field-y]]
        snake (straight-line snake-start snake-len snake-direction)]
    (-> {:field field
          :snake snake
          :trace (zipmap snake (repeat (inc snake-len) snake-direction))
          :direction snake-direction
          :tick 0
         }
        valid!)))
