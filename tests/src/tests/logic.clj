(ns tests.logic
  (:require
    [tests.model :as h.model]
    [schema.core :as s]
    [clojure.test.check.generators :as gen]))

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

(defn kray [x y]
  "Teste purpose"
  (println (* x y))
  )



;(defn chega-em
;  [hospital departamento pessoa]
;  (if (cabe-na-fila? hospital departamento)
;    (update hospital departamento conj pessoa)
;    (throw (IllegalStateException. "Não cabe ninguém neste departamento."))))

; nil ???
;(defn chega-em
;  [hospital departamento pessoa]
;  (if (cabe-na-fila? hospital departamento)
;    (update hospital departamento conj pessoa)))

; exemplo para usar ex-data
;(defn chega-em
;  [hospital departamento pessoa]
;  (if (cabe-na-fila? hospital departamento)
;    (update hospital departamento conj pessoa)
;    (throw (ex-info "Não cabe ninguém neste departamento." {:paciente pessoa, :tipo :impossivel-colocar-pessoa-na-fila}))))


;(defn- tenta-colocar-na-fila
;  [hospital departamento pessoa]
;  (if (cabe-na-fila? hospital departamento)
;    (update hospital departamento conj pessoa)))
;
;(defn chega-em
;  [hospital departamento pessoa]
;  (if-let [novo-hospital (tenta-colocar-na-fila hospital departamento pessoa)]
;    {:hospital novo-hospital :resultado :sucesso}
;    {:hospital hospital :resultado :impossivel-colocar-pessoa-na-fila}))

; antes de fazer swap chega-em vai ter que tratar o resultado
;(defn chega-em!
;  [hospital departamento pessoa]
;  (chega-em hospital departamento pessoa))

; Código do curso anterior

(defn chega-em
  [hospital departamento pessoa]
  (if (cabe-na-fila? hospital departamento)
    (update hospital departamento conj pessoa)
    (throw (IllegalStateException. "Não cabe ninguém neste departamento." {:paciente pessoa}))))

(s/defn atende :- h.model/Hospital
  [hospital :- h.model/Hospital departamento :- s/Keyword]
  (update hospital departamento pop))

(s/defn proxima :- (s/maybe h.model/PacienteID)
  [hospital :- h.model/Hospital departamento :- s/Keyword]
  "Retorna a próxima pessoa da fila"
  (-> hospital departamento peek))

(defn mesmo-tamanho? [hospital, outro-hospital, de, para]
  (= (+ (count (get outro-hospital de)) (count (get outro-hospital para)))
     (+ (count (get hospital de)) (count (get hospital para))))
  )

(s/defn transfere :- h.model/Hospital
  "Transfere o próximo paciente da fila 'de' para a fila 'para'"
  [hospital :- h.model/Hospital de :- s/Keyword para :- s/Keyword]
  {
   :pre  [(contains? hospital de), (contains? hospital para)]
   :post [(mesmo-tamanho? hospital % de para)]
   }
  (if-let [pessoa (proxima hospital de)]
    (-> hospital
        (atende de)
        (chega-em para pessoa))
    hospital))

(defn total-de-pacientes [hospital]
  (reduce + (map count (vals hospital)))
  )


