(ns snake.state)



(defn step! [{:keys [state next-state-fn] :as game}]
  (swap! state next-state-fn))

(defn running? [game]
  (deref (:interval game)))

(defn stop! [{:keys [interval]} game]
  (when (running? game)
    (js/clearInterval @interval)
    (reset! interval nil)))

(defn start! [{:keys [state interval] :as game}]
  (stop! game)
  (let [intv (/ 1000.0 (:speed state))]
    (reset! interval (js/setInterval #(step! game) intv))))

(defn toggle! [game]
  (if (running? game)
    (stop! game)
    (start! game)))

(defn speed [{:keys [state] :as game}]
  (:speed (deref state)))

(defn speed! [{:keys [state] :as game} speed]
  (swap! state #(assoc % :speed speed)))

(defn state [{:keys [state] :as game}]
  (deref state))

(defn init! [state next-state-fn]
  (let [game  {:state (atom state)
               :next-state-fn next-state-fn
               :interval (atom  0)}]
    (add-watch (:state game) nil #(if (running? game) (start! game)))
    game))
