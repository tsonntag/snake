(ns snake.view
  (:require
   [snake.game :as game]
   [snake.world :as world]))

(defn link-to [url text]
  [:a {:href url} text])

; unit in px
(def unit 10)
(defn scale [i] (str (* unit i) "px"))

(defn point [world [x y]]
  (let [[bx by] [1 1]]
    [:div.point
     {:style
      {:background-color (if (:valid world) "#00FF00" "#FF0000")
       :position "absolute"
       :margin-left (scale x)
       :margin-top  (scale y)
       :width  (scale bx)
       :height (scale by)}}]))

(defn field [world]
  (let [[[x0 xn] [y0 yn]] (:field world)]
    [:div#field
     {:style {:background-color "#F0F0F0"
              :width  (scale (- xn x0))
              :height (scale (- yn y0))}}
     (for [p (:snake world)]
       ^{:key p} (point world p))
     ]))

#_(defn speed-slider [game]
  [:div.speed-slider
   "Speed"
   [:input {:type "range"
            :min 1
            :max 1000
            :value (game/speed game)
            :on-change #(game/speed! game (float (-> % .-target .-value)))}]])

(defn page [game]
  [:div
   [:button {:on-click #(game/initial! game)} "Reset"]
   [:button {:on-click #(game/toggle! game)} (if (game/running? game) "Stop" "Start")]
   [:br]
   #_(speed-slider game)
   (field @(:world game))
   [:br]
   [:br]])
