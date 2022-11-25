(ns tests.logic)

; TDD
; Test Driven Development
; Test Driven Design

; Existe um problema de condicional quando o departamento não existe
;(defn cabe-na-fila?
;  [hospital departamento]
;  (-> hospital
;          departamento
;          count
;          (< 5)))
;


; Funciona para o caso de não ter o departamento
;(defn cabe-na-fila?
;  [hospital departamento]
;  (when-let [fila (get hospital departamento)]
;    (-> fila
;        count
;        (< 5))))


; Funciona, mas usa o some->
; Desvantagem/Vantagem "Menos Explicito"
; Desvantagem é que qualquer situação de nil, vai devolver nil
(defn cabe-na-fila?
  [hospital departamento]
  (some-> hospital
      departamento
      count
      (< 5)))
