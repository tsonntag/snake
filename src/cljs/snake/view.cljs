(ns snake.view
  (:require [snake.world]
            ))

(defn link-to [url text]
  [:a {:href url} text])

; unit in px

(def unit 10)
(defn scale [i] (str (* unit i) "px"))

(defn point [[x y]]
  (let [[bx by] [1 1]]
    [:div.point
     {:style
      {:background-color "#FF0000"
       :position "absolute"
       :margin-left (scale x)
       :margin-top  (scale y)
       :width  (scale bx)
       :height (scale by)}}]))

(defn field [game]
  (let [[[x0 xn] [y0 yn]] (:field (snake.world/world))]
    [:div#field
     {:style {:background-color "#F0F0F0"
              :width  (scale (- xn x0))
              :height (scale (- yn y0))}}
     (for [p (:snake world)]
       ^{:key p} (point p))
     ]))

#_(defn speed-slider []
  [:div.speed-slider
   "Speed"
   [:input {:type "range"
            :min 1
            :max 1000
            :value (world/speed game)
            :on-change #(world/set-speed! game (float (-> % .-target .-value)))}]])

(defn page []
  [:div
   [:button {:on-click #(world/toggle! game)} (if (world/running? game) "Stop" "Start")]
   #_(speed-slider)
   (field)
   [:br]
   [:br]])
