(ns snake.game
  (:require
   [reagent.core :refer [atom]]))

(defn step! [{:keys [world] :as game} fn]
  (swap! world fn))

(defn speed! [game speed]
  (reset! (:speed game) speed))

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

(defn new-game [{:keys [initial-world tick-fn coins speed] :as game}]
  (assoc game
         :world (atom initial-world)
         :interval (atom nil)
         :speed (atom speed)
         :points 0))
