(ns tests.logic)

; TDD
; Test Driven Development
; Test Driven Design

(defn cabe-na-fila?
  [hospital departamento]
  (-> hospital
      departamento
      count
      (< 5)))