(ns snake.game
  (:require
   [reagent.core :refer [atom]]))

(defn step! [{:keys [world] :as game} fn]
  (println "STEP: " @world)
  (swap! world fn))

(defn speed! [game speed]
  (reset! (:speed world) speed))

(defn running? [game]
  (deref (:interval game)))

(defn stop! [{:keys [interval] :as game}]
  (when (running? game)
    (js/clearInterval @interval)
    (reset! interval nil)))

(defn start! [{:keys [interval tick-fn] :as game}]
  (stop! game)
  (let [intv (/ 1000.0 @(:speed game))]
    (reset! interval
            (js/setInterval
             #(step! game tick-fn) intv))))

(defn toggle! [game]
  (if (running? game)
    (stop! game)
    (start! game)))

(defn world [game]
  @(:world game))

(defn initial! [{:as game :keys [initial-world world]}]
  (reset! world initial-world))

(defn new-game [initial-world tick-fn speed]
  (let [game  {:initial-world initial-world
               :world (atom initial-world)
               :tick-fn tick-fn
               :interval (atom nil)
               :speed (atom speed)}]
    ;#_(add-watch (:world game) nil #(if (running? game) (start! game)))
    game))
