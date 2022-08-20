(ns hospital.aula4
  (:use clojure.pprint)
  (:require [schema.core :as s]))


(s/set-compile-fn-validation! true)

(def PosInt (s/pred pos-int?))

(def Plano [s/Keyword])

(def Paciente
  {:id PosInt :nome s/Str :plano Plano (s/optional-key :nascimento) s/Str})

(pprint (s/validate Paciente {:id 24 :nome "André" :plano [:raio-x :ultrassom]}))
(pprint (s/validate Paciente {:id 24 :nome "André" :plano [:raio-x]}))
(pprint (s/validate Paciente {:id 24 :nome "André" :plano []}))
(pprint (s/validate Paciente {:id 24 :nome "André" :plano nil}))
; plano é uma keyword obrigatória no mapa, mas ela pode ter um valor vazio (nil, [])

; Deixando uma chave de mapa como opcional
(pprint (s/validate Paciente {:id 24 :nome "André" :plano nil}))

; esse é uma outro tipo de uso de mapas no mundo real
; Pacientes
; { 15 {PACIENTE} 32 {PACIENTE} }
; Aqui a chave do schema Pacientes é um valor inteiro qualquer, portanto, por não ser uma Keyword, é opcional.
(def Pacientes {PosInt Paciente})

; Schema Pacientes válido apesar de vazio
(pprint (s/validate Pacientes {}))

; Validando..
(let [
      andre {:id 24 :nome "André" :plano []}
      jainne {:id 24 :nome "Jainne" :plano [:raio-x]}
      ]
  (pprint andre)
  (pprint jainne)
  (pprint (s/validate Pacientes {24 andre}))
  (pprint (s/validate Pacientes {24 jainne}))
  )

(def Visitas
  {PosInt [s/Str]})



