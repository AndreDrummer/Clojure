(ns hospital2.aula1
  (:use [clojure pprint])
  (:require [hospital2.model :as h.model])
  (:require [hospital2.logic :as h.logic]))

(defn simula-um-dia [] (h.model/novo-hospital)
  ; root binding
  (def hospital (h.model/novo-hospital))
  (def hospital (h.logic/chega-em hospital :espera 111))
  (def hospital (h.logic/chega-em hospital :espera 222))
  (def hospital (h.logic/chega-em hospital :espera 333))
  (pprint hospital)

  (def hospital (h.logic/chega-em hospital :laboratorio1 444))
  (def hospital (h.logic/chega-em hospital :laboratorio3 555))
  (pprint hospital)

  (def hospital (h.logic/atende hospital :laboratorio1))
  (def hospital (h.logic/atende hospital :espera))
  (pprint hospital)

  (def hospital (h.logic/chega-em hospital :espera 666))
  (def hospital (h.logic/chega-em hospital :espera 777))
  (def hospital (h.logic/chega-em hospital :espera 888))
  (pprint hospital)
  (def hospital (h.logic/chega-em hospital :espera 999))
  (pprint hospital))

;(simula-um-dia)

(defn chega-em-malvado [pessoa]
  (def hospital (h.logic/chega-em-pausado hospital :espera pessoa))
  (println "Após inserir " pessoa)
  )
; Aqui, por estarmos trabalhando com um unico valor definido de maneira global (root binding)
; em um contexto multi-threaded, a inserção na fila so vai acontecer para a ultima execução da função!
(defn simula-um-dia-em-paralelo []
  (def hospital (h.model/novo-hospital))
  (.start (Thread. (fn [] (chega-em-malvado 111))))
  (.start (Thread. (fn [] (chega-em-malvado 222))))
  (.start (Thread. (fn [] (chega-em-malvado 333))))
  (.start (Thread. (fn [] (chega-em-malvado 444))))
  (.start (Thread. (fn [] (chega-em-malvado 555))))
  (.start (Thread. (fn [] (chega-em-malvado 666))))
  (.start (Thread. (fn [] (Thread/sleep 4000)
                     (pprint hospital))))
  )

(simula-um-dia-em-paralelo)