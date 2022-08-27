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

  )






(simula-um-dia)