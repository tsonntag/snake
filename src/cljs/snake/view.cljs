(ns snake.view
  (:require
   [snake.world :as world]
   [snake.heartbeat :as heartbeat]))

(defn link-to [url text]
  [:a {:href url} text])

; unit in px
(def unit 10)
(defn scale [i] (str (* unit i) "px"))

(defn point [[x y] color]
  (let [[bx by] [1 1]]
    [:div.point
     {:style
      {:background-color color
       :position "absolute"
       :margin-left (scale x)
       :margin-top  (scale y)
       :width  (scale bx)
       :height (scale by)}}]))

(defn field [{:keys [field valid snake coins] :as world}]
  (let [[[x0 xn] [y0 yn]] field
        snake-color (if valid "#00FF00" "#FF0000")
        coin-color "#0000FF"]
    [:div#field
     {:style {:background-color "#F0F0F0"
              :width  (scale (- xn x0))
              :height (scale (- yn y0))}}
     (for [p snake]
       ^{:key p} (point p snake-color))
     (for [p (keys coins)]
       ^{:key p} (point p coin-color))
     ]))

#_(defn speed-slider [{:keys [speed] :as world}]
  [:div.speed-slider
   "Speed"
   [:input {:type "range"
            :min 1
            :max 1000
            :value @speed
            :on-change #(reset! speed (float (-> % .-target .-value)))}]])

(defn page [world-atom]
  (let [world @world-atom]
    [:div
     [:button {:on-click #(reset! world-atom (:initial-word world))} "Reset"]
     [:button {:on-click #(heartbeat/toggle! world)} (if (heartbeat/running? world) "Stop" "Start")]
     [:br]
     [:div "Points " (:rewards world) ]
     #_(speed-slider game)
     (field world)
     [:br]
     [:br]]))
