(ns snake.core
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
              [accountant.core :as accountant]
              [snake.view :as view]
              [snake.heartbeat :as heartbeat]
              [snake.world :as world]
              [goog.events])
    (:import [goog.events KeyHandler]
             [goog.events.KeyHandler EventType]))

(enable-console-print!)


(defn step-world [world]
  (->> world
       world/move
       world/handle-coins
       world/update-valid))

(defn heartbeat [world]
 (let [next-world (step-world world)]
    (if (:valid next-world)
      next-world
      (assoc world :valid false))))

(defonce world (atom nil))

(defn coins [[x y] max]
  (let [n (rand-int max)
        reward 1]
    (-> (repeatedly n #[[(rand-int x) (rand-int y)] reward])
        (into {}))))

(println "COINS" (coins [3 4] 5))

(def initial-world
  (-> {:field  [40 40]
        :snake-start [2 3]
        :snake-len 8
        :snake-direction [1 0]
       :coins (coins [40 40] 10)}
       world/init!
       (heartbeat/add-heartbeat 3.0 #(swap! world heartbeat)))
  )

(reset! world (assoc initial-world
                     :initial-word initial-world))

;; -------------------------
;; Views
(defn home-page []
  [:div [:h2 "Snake"]
   ;[:div [:a {:href "/about"} "about"]]
   (view/page world)])

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
(def key->direction
  {37 [-1 0]
   38 [0 -1]
   39 [1 0]
   40 [0 1]})

(defn event->direction [event]
  (println "KEY=" (.-keyCode event))
  (->> event
       .-keyCode
       (get key->direction)))

;; Initialize app
(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (goog.events/listen (KeyHandler. js/document) EventType.KEY
                      (fn [event]
                        (.preventDefault event)
                        (swap! world
                               #(world/new-direction! % (event->direction event)))))
  (accountant/configure-navigation!)
  (accountant/dispatch-current!)
  (mount-root))
