(ns hospital.aula5
  (:use clojure.pprint)
  (:require [schema.core :as s]))

(s/set-fn-validation! true)

(def PosInt (s/pred pos-int? 'inteiro-positivo))

(def Plano [s/Keyword])

(def Paciente
  {
   :id                          PosInt
   :nome                        s/Str
   :plano                       Plano
   (s/optional-key :nascimento) s/Str
   }
  )

(def Pacientes
  {PosInt Paciente})

(def Visitas
  {PosInt [s/Str]})

(s/defn adicionar-paciente :- Pacientes
  [pacientes :- Pacientes paciente :- Paciente]
  (let [id (:id paciente)]
    (assoc pacientes id paciente)
    ))

(s/defn adiciona-visita :- Visitas
  [visitas :- Visitas, paciente :- PosInt, novas-visitas :- [s/Str]]
  (if (contains? visitas paciente)
    (update visitas paciente concat novas-visitas)
    (assoc visitas paciente novas-visitas)))

(s/defn imprime-relatorio-de-paciente [visitas :- Visitas, paciente :- PosInt]
  (println "Visitas do paciente" paciente "são" (get visitas paciente))
  )

(defn testa-uso-de-pacientes []
  (let [guilherme {:id 15, :nome "Guilherme" :plano []}
        daniela {:id 20, :nome "Daniela" :plano []}
        paulo {:id 25, :nome "Paulo" :plano []}
        pacientes (reduce adicionar-paciente {} [guilherme daniela paulo])
        visitas {}
        visitas (adiciona-visita visitas 15 ["01/01/2019"])
        visitas (adiciona-visita visitas 20 ["01/02/2019", "01/01/2020"])
        visitas (adiciona-visita visitas 15 ["01/03/2019"])
        ]
    (pprint pacientes)
    (pprint visitas)
    (imprime-relatorio-de-paciente visitas 15)
    (println (get visitas 20))
    ))

(testa-uso-de-pacientes)



