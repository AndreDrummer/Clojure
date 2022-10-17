(ns hospital2.aula6
  (:use [clojure.pprint])
  (:require [hospital2.model :as h.model]))


; Antes com swap!, o retry era feito toda vez que
; qualquer dado dentro dele fosse atualizado.
; Aqui agora com ref-set e alter, o retry so
; será feito na colecao que tiver algum dado alterado
; e não no conteudo completo


(defn chega-em [fila pessoa]
  (conj fila pessoa))

; troca de referencia via ref-set.
; Esse precisa usar (deref ou @)
(defn chega-em! [hospital pessoa]
  (let [fila (get hospital :espera)]
    (ref-set fila (chega-em @fila pessoa))))

; troca de referencia via alter.
; Esse lembra a maneira do swap que seria
; (swap! fila chega-em pessoa)
(defn chega-em! [hospital pessoa]
  (let [fila (get hospital :espera)]
    (alter fila chega-em pessoa)))

(defn simula-um-dia []
  (let [hospital
        {:espera       (ref h.model/fila-vazia)
         :laboratorio1 (ref h.model/fila-vazia)
         :laboratorio2 (ref h.model/fila-vazia)
         :laboratorio3 (ref h.model/fila-vazia)}
        ]

    (dosync
      (chega-em! hospital "Guilherme"))
    (pprint hospital)
    ))

(simula-um-dia)