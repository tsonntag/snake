(ns snake.state
  (:require
   [reagent.core :refer [atom]]))


(defn speed [{:keys [state] :as game}]
  (println "SPEED" game #_(:speed @state))
  (:speed (deref state)))

(defn speed! [{:keys [state] :as game} speed]
  (swap! state #(assoc % :speed speed)))

(defn step! [{:keys [state next-state-fn] :as game}]
  (println "STEP" @state)
  (println "===>" (next-state-fn @state))
  (swap! state next-state-fn))

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

(defn state [{:keys [state] :as game}]
  (deref state))

(defn init! [state next-state-fn]
  (let [game  {:state (atom state)
               :next-state-fn next-state-fn
               :interval (atom nil)}]
    ;#_(add-watch (:state game) nil #(if (running? game) (start! game)))
    game))
