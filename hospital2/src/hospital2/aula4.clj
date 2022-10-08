(ns hospital2.aula4
  (:use [clojure pprint])
  (:require [hospital2.logic :as h.logic]
            [hospital2.model :as h.model]))


(defn chega-sem-malvado! [hospital pessoa]
  (swap! hospital h.logic/chega-em :espera pessoa)
  (println "Após inserir " pessoa)
  )

(defn simula-um-dia-em-paralelo-com-mapv []
  "Simulação utilizando um mapv para força quase que imperativamente a execução do que era lazy, o map no caso"
  (let [hospital (atom (h.model/novo-hospital))
        pessoas [111, 222, 333, 444, 555, 666]]

    (mapv #(.start (Thread. (fn [] (chega-sem-malvado! hospital %)))) pessoas)

    (.start (Thread. (fn [] (Thread/sleep 8000)
                       (pprint hospital))))))


;(simula-um-dia-em-paralelo-com-mapv)

(defn simula-um-dia-em-paralelo-com-mapv-refatorada []
  "Simulação utilizando um mapv para força quase que imperativamente a execução do que era lazy, o map no caso"
  (let [hospital (atom (h.model/novo-hospital))
        pessoas [111, 222, 333, 444, 555, 666]
        starta-thread-de-chegada #(.start (Thread. (fn [] (chega-sem-malvado! hospital %))))
        ]

    (mapv starta-thread-de-chegada pessoas)

    (.start (Thread. (fn [] (Thread/sleep 8000)
                       (pprint hospital))))))

;(simula-um-dia-em-paralelo-com-mapv-refatorada)

(defn starta-thread-de-chegada
  [hospital, pessoa]
  (.start (Thread. (fn [] (chega-sem-malvado! hospital pessoa)))))

(defn simula-um-dia-em-paralelo-com-partial []
  (let [hospital (atom (h.model/novo-hospital))
        pessoas [111, 222, 333, 444, 555, 666]
        starta (partial starta-thread-de-chegada hospital)]

    (mapv starta pessoas)

    (.start (Thread. (fn [] (Thread/sleep 8000)
                       (pprint hospital))))))

;(simula-um-dia-em-paralelo-com-partial)


(defn "Preocupa-se em executar para os elementos da sequencia" simula-um-dia-em-paralelo-com-doseq []
  (let [hospital (atom (h.model/novo-hospital))
        pessoas (range 6)]

    (doseq [pessoa pessoas]
      (starta-thread-de-chegada hospital pessoa))

    (.start (Thread. (fn [] (Thread/sleep 8000)
                       (pprint hospital))))))

;(simula-um-dia-em-paralelo-com-doseq)


(defn "Preocupa-se em executar N vezes" simula-um-dia-em-paralelo-com-dotimes []
  (let [hospital (atom (h.model/novo-hospital))]

    (dotimes [pessoa 6]
      (starta-thread-de-chegada hospital pessoa))

    (.start (Thread. (fn [] (Thread/sleep 8000)
                       (pprint hospital))))))

(simula-um-dia-em-paralelo-com-dotimes)

