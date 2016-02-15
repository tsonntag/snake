(ns snake.game
  (:require
   [reagent.core :refer [atom]]))


(defn speed [{:keys [world] :as game}]
  (:speed (deref world)))

(defn speed! [{:keys [world] :as game} speed]
  (swap! world #(assoc % :speed speed)))

(defn step! [{:keys [world next-world-fn] :as game}]
  (swap! world next-world-fn))

(defn running? [game]
  (deref (:interval game)))

(defn stop! [{:keys [interval] :as game}]
  (when (running? game)
    (js/clearInterval @interval)
    (reset! interval nil)))

(defn start! [{:keys [interval] :as game}]
  (stop! game)
  (let [intv (/ 1000.0 (speed game))]
    (reset! interval (js/setInterval #(step! game) intv))))

(defn toggle! [game]
  (if (running? game)
    (stop! game)
    (start! game)))

(defn world [{:keys [world] :as game}]
  (deref world))

(defn make-game [world next-world-fn]
  (let [game  {:world (atom world)
               :next-world-fn next-world-fn
               :interval (atom nil)}]
    ;#_(add-watch (:world game) nil #(if (running? game) (start! game)))
    game))
