(ns snake.view
  (:require
   [snake.state :as state]
   [snake.game :as snake]
    ))

(defn link-to [url text]
  [:a {:href url} text])

; unit in px

(def unit 10)
(defn scale [i]
  (str (* unit i) "px"))

(def initial-state
  (let [len 5
        dir [1 0]
        snake (snake/straight-line [2 3] len dir) ]
    {:field [[0 20] [0 20]]
     :snake snake
     :trace (zipmap snake (repeat len dir))
     :dir [1 0]
     :speed 0.2}))

(println "XXXXX")
(snake/next-state initial-state)
(println "YYYY")

(def game (state/init! initial-state snake/next-state))

(defn state []
    (snake.state/state game))

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

(defn field []
  ;(println "FIELD" (state))
  (let [state (state)
        [[x0 xn] [y0 yn]] (:field state)]
    [:div#field
     {:style {:background-color "#F0F0F0"
              :width  (scale (- xn x0))
              :height (scale (- yn y0))}}
     (for [p (:snake state)]
       ^{:key p} (point p))
     ]))

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
   [:button {:on-click #(state/toggle! game)} (if (state/running? game) "Stop" "Start")]
   #_(speed-slider)
   (println "PAGE")
   (field)
   [:br]
   [:br]]
  )
