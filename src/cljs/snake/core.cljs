(ns snake.core
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
              [accountant.core :as accountant]
              [snake.view]
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
;; Initialize app
(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (keyboard-events)
  (accountant/configure-navigation!)
  (accountant/dispatch-current!)
  (mount-root))
