(ns snake.core
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
              [accountant.core :as accountant]
              [snake.view :as view]
              [snake.game :as game]
              [snake.world :as world]
              [goog.events])
    (:import [goog.events KeyHandler]
             [goog.events.KeyHandler EventType]))

(enable-console-print!)

(defn next-world [world]
  (->> world
       world/move
       world/handle-coins
       world/update-valid))

(defonce game (game/new-game
               {:initial-world (world/new-world {:field  [40 40]
                                                 :snake-start [2 3]
                                                 :snake-len 8
                                                 :snake-direction [1 0]
                                                 :coins {[3 1] 1
                                                         [5 2] 2}})
                :tick-fn (fn [world]
                  (let [next-world (next-world world)]
                    (if (:valid next-world)
                      next-world
                      (assoc world :valid false))))
                :speed 3.0}))

;; -------------------------
;; Views
(defn home-page []
  [:div [:h2 "Snake"]
   ;[:div [:a {:href "/about"} "about"]]
   (view/page game)])

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
                        (game/step! game
                                    #(world/new-direction! % (event->direction event)))))
  (accountant/configure-navigation!)
  (accountant/dispatch-current!)
  (mount-root))
