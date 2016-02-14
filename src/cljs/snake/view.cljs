(ns snake.view
  (:require
   [reagent.core :refer [atom]]
   [snake.state :as state]
   [snake.game]
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
(def initial-state {:x 0 :y 0} )
(def game (state/init! initial-state next-state
                       initial-speed))

(defn ball []
  (let [[bx by] [1 1]of p
        [x y] [2 4]]
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

#_(defn speed-slider []
  [:div.speed-slider
   "Speed"
   [:input {:type "range"
            :min 1
            :max 1000
            :value (state/speed game)
            :on-change #(state/set-speed! game (float (-> % .-target .-value)))}]])

(defn page []
  [:div
  ; [:button {:on-click #(state/toggle! game)} (if (state/running? game) "Stop" "Start")]
   #_(speed-slider)
   (field)
   [:br]
   [:br]]
  )
