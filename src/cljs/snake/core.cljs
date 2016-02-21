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

(defonce game (game/new-game
               {:initial-world (world/new-world {:field  [10 10]
                                                 :snake-start [2 3]
                                                 :snake-len 4
                                                 :snake-direction [1 0]
                                                 :coins {[3 1] 1
                                                         [5 2] 2}})
                :tick-fn (fn [world]
                  (let [moved-world (world/move world)]
                    (if (:valid moved-world)
                      moved-world
                      (assoc world :valid false))))
                :speed 1.0}))

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
