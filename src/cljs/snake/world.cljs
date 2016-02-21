(ns snake.world
  (:require
   [snake.physics :refer [straight-line hit-cube? hit-self? move-pt]]))

(defn valid? [{:keys [snake field] :as world}]
  (not (or
        (hit-cube? snake field)
        (hit-self? snake))))

(defn update-valid [world]
  (assoc world :valid (valid? world)))

(defn next-snake [{:keys [snake trace direction] :as world}]
  (->> snake
       (map (fn [p]
              (move-pt p (get trace p direction))))))

(defn combine [& snakes]
  (set (apply concat snakes)))

(defn update-trace [{:keys [snake trace direction] :as world}]
  (assoc world
         :trace (zipmap snake (map #(get trace % direction) snake))))

(defn move
  "move snake"
  [world]
  (-> world
      (assoc
       :snake (next-snake world)
       :tick (inc (:tick world)))
      update-trace))

(defn handle-coins
  [{:keys [coins rewards snake] :as world}]
  (let [snake (set snake)
        hit-coin (->> (keys coins)
                      (filter snake)
                      first)
        _ (println "HITCOIN" hit-coin)
        ]
    (assoc world
           :coins (dissoc coins hit-coin)
           :rewards (+ rewards (get coins hit-coin)))
        ))

(defn new-direction! [world direction]
  (assoc world :direction direction))

(defn init! [{:keys [field snake-start snake-len snake-direction coins]}]
  (let [snake (straight-line snake-start snake-len snake-direction)]
    (-> {:field [[0 (first field)] [0 (second field)]]
         :snake snake
         :direction snake-direction
         :coins coins
         :tick 0
         :rewards 0}
        update-trace
        update-valid)))
