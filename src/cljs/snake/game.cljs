(ns snake.game
  (:require
   [reagent.core :refer [atom]]
   [snake.state :as state]
    ))

(defn link-to [url text]
  [:a {:href url} text])

; unit in px

(def unit 10)
(defn scale [i]
  (str (* unit i) "px"))

(def field-size [50 50])

(declare next-state)

(def initial-speed 10)
(def initial-state {} )
(state/init! initial-state next-state
             initial-speed)

(defn next-state [state]
  state)

(defn ball []
  (let [[bx by] ball-size
        [x y] (first @state)]
    [:div#ball
     {:style
      {:background-color "#FF0000"
       :position "absolute"
       :margin-left (scale x)
       :margin-top  (scale y)
       :width  (scale bx)
       :height (scale by)}}]))

(defn field []
  (let [[x y] field-size]
    [:div#field
     {:style {:background-color "#F0F0F0"
              :width  (scale x)
              :height (scale y)}}
     (ball)]))

(defn speed-slider []
  [:div.speed-slider
   "Speed"
   [:input {:type "range" :min 1 :max 1000
            :value @speed
            :on-change #(reset! speed (float (-> % .-target .-value)))}]])

(defn page []
  (let [[p0 _ pmin pmax] @state]
    [:div [:h2 "Ping Pong"]
     [:div (link-to "#/" "home")]
     [:button {:on-click toggle} (if (running?) "Stop" "Start")]
     (speed-slider)
     (field)
     [:br]
     [:br]]
    ))
