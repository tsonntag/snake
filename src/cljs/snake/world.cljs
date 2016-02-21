(ns snake.world
  (:require
   [snake.physics :refer [straight-line hit-cube? hit-self? move-pt]]))

(defn valid? [{:keys [snake field] :as world}]
  (not (or
        (hit-cube? snake field)
        (hit-self? snake))))

(defn valid! [world]
  (assoc world :valid (valid? world)))

(defn next-snake [{:keys [snake trace direction] :as world}]
  (->> snake
       (map (fn [p]
              (move-pt p (get trace p direction))))))

(defn combine [& snakes]
  (set (apply concat snakes)))

(defn next-trace [{:keys [trace direction] :as world} next-snake]
  (zipmap next-snake (map #(get trace % direction) next-snake)))

(defn move
  "move snake"
  [{:keys [tick coins rewards] :as world}]
  (let [
;        _ (println "NEXT: FIELD=" field " DIRECTION=" direction)
;        _ (println "NEXT:   SNAKE=" snake "   TRACE=" trace "   VALID=" (valid? world))
        next-snake (next-snake world)
        next-snake-set (set next-snake)
        hit-coin (->> (keys coins)
                      (filter next-snake-set))
        rewards
        _ (println "HITCOIN" hit-coin)
 ;       _ (println "MOVE: tick=" tick "mod=" (mod tick 3))
 ;       _ (println "MOVE: snake=" snake "next=" next-snake "combine=" (combine snake next-snake))
;        next-snake (if (= 2 (mod tick 3)) (combine snake next-snake) next-snake)

                                        ;       _ (println "NEXT: N-SNAKE=" next-snake " N-TRACE" next-trace  " VALID=" (valid? (assoc world :snake next-snake)))
        ]
    (-> world
        (assoc
         :snake next-snake
         :trace (next-trace world next-snake)
         :tick (inc (:tick world))
         :rewards )
        valid!)))

(defn new-direction! [world direction]
  (assoc world :direction direction))

(defn new-world [{:keys [field snake-start snake-len snake-direction coins]}]
  (let [snake (straight-line snake-start snake-len snake-direction)]
    (-> {:field [[0 (first field)] [0 (second field)]]
         :snake snake
         :trace (zipmap snake (repeat (inc snake-len) snake-direction))
         :direction snake-direction
         :coins coins
         :tick 0
         :rewards 0
         }
        valid!)))
