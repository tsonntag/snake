(ns snake.state)


(defn step! [{:keys [state next-state-fn] :as game}]
  (swap! state next-state-fn))

(defn running? [game]
  (deref (:interval game)))

(defn stop! [{:keys [interval]} game]
  (when (running? game)
    (js/clearInterval @interval)
   (reset! interval nil)))

(defn start! [{:keys [speed interval] :as game}]
  (stop! game)
  (let [intv (/ 1000.0 @speed)]
    (reset! interval (js/setInterval #(step! game) intv))))

(defn toggle! [game]
  (if (running? game)
    (stop! game)
    (start! game)))

(defn set-speed! [game speed]
  (reset! (:speed game) speed))

(defn init! [state next-state-fn speed]
  (let [game  {:state (atom state)
               :speed (atom speed)
               :next-game next-state-fn
               :interval (atom  0)}]
    (add-watch speed nil #(if (running? game) (start! game)))))
