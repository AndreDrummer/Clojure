(ns hospital.aula3
  (:use clojure.pprint)
  (:require [schema.core :as s]))

(s/set-fn-validation! true)

(def PosInt (s/pred pos-int?))

(def Paciente {:id PosInt :nome s/Str})

(s/defn novo-paciente :- Paciente
  [id :- PosInt nome :- s/Str]
  {:id id :nome nome})
; Deve funcionar pois os parametros satisfazem os requeridos
(pprint (novo-paciente 15 "André"))

; Não deve funcionar porq ue o id (meramente didático) é negativo
;(pprint (novo-paciente -5 "Felipe"))

(defn maior-ou-igual-a-zero [x] (>= x 0))
(def ValorFinanceiro (s/constrained s/Num maior-ou-igual-a-zero 'inteiro-maior-que-zero))

(def Pedido
  {:paciente Paciente
   :valor    ValorFinanceiro
   :procedimento s/Keyword})

(s/defn novo-pedido :- Pedido
  [paciente :- Paciente valor :- ValorFinanceiro procedimento :- s/Keyword]
  {:paciente paciente :valor valor :procedimento procedimento})

(pprint (novo-pedido (novo-paciente 24 "André") 19.978 :raio-x))


(def Plano [s/Keyword])

(pprint (s/validate Plano [:raio-x]))

(def Paciente
  {:id PosInt :nome s/Str :plano Plano})

(pprint (s/validate Paciente {:id 24 :nome "André" :plano []}))