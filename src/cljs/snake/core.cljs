(ns snake.core
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
              [accountant.core :as accountant]
              [snake.view]
              [snake.game]
              [goog.events])
    (:import [goog.events KeyHandler]
             [goog.events.KeyHandler EventType]))

(enable-console-print!)

(def -keys
  {37 [-1 0]
   38 [0 1]
   39 [1 0]
   40 [0 -1]})

(defn log-event [event]
  (.preventDefault event)
  (let [key (->> event
                 .-keyCode
                 (get -keys))]
    (println  key)))

(defn keyboard-events []
  (goog.events/listen (KeyHandler. js/document) EventType.KEY log-event))

;; -------------------------
;; Views
(defn home-page []
  [:div [:h2 "Snake"]
   ;[:div [:a {:href "/about"} "about"]]
   (snake.view/page)])

(defn about-page []
  [:div [:h2 "About snake"]
   [:div [:a {:href "/"} "home"]]])

(defn current-page []
  [:div [(session/get :current-page)]])

;; -------------------------
;; Routes
(secretary/defroute "/" []
  (session/put! :current-page #'home-page))

(secretary/defroute "/about" []
  (session/put! :current-page #'about-page))

;; -------------------------
(defn move [{:keys [field snake trace dir speed] :as world}]
  (let [
        _ (println "NEXT: SNAKE=" snake "TRACE=" trace)
         next-snake (->> snake
                         (map (fn [p]
                                (move-pt p (get trace p dir)))))
        _ (println "NEXT: NEXT-SNAKE=" next-snake)
        next-trace (zipmap next-snake (map #(get trace % dir) next-snake))
        _ (println "NEXT: NEXT-TRACE=" next-trace)
        valid (hit-cube? snake field)]
    (assoc world
           :snake next-snake
           :trace next-trace)))

(defn make-world [[field-x field-y] snake-start snake-len snake-dir speed]
  (let [field [[0 field-x] [0 field-y]]
        snake (straight-line snake-start snake-len snake-dir)
        _ (println "MAKE" snake)]
    {:field field
     :snake snake
     :trace (zipmap snake (repeat (inc snake-len) snake-dir))
     :dir snake-dir
     :speed speed
     :valid (not (hit-cube? snake field))}))

(def initial-world
  (make-world [20 20] [2 3] 3 [1 0] 0.5))

(def game (snake.game/make-game initial-world snake/next-world))


;; Initialize app
(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (keyboard-events)
  (accountant/configure-navigation!)
  (accountant/dispatch-current!)
  (mount-root))
