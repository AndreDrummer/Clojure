(ns hospital.aula1
  (:use clojure.pprint)
  (:require [schema.core :as s]))

(defn adicionar-paciente [pacientes paciente]
  (if-let [id (:id paciente)]
    (assoc pacientes id paciente)
    (throw (ex-info "Paciente não possui id" {:paciente paciente}))))

(defn adiciona-visita
  [visitas, paciente, novas-visitas]
  (if (contains? visitas paciente)
    (update visitas paciente concat novas-visitas)
    (assoc visitas paciente novas-visitas)))

(defn imprime-relatorio-de-paciente [visitas, paciente]
  (println "Visitas do paciente" paciente "são" (get visitas paciente))
  )

(defn testa-uso-de-pacientes []
  (let [guilherme {:id 15, :nome "Guilherme"}
        daniela {:id 20, :nome "Daniela"}
        paulo {:id 25, :nome "Paulo"}
        pacientes (reduce adicionar-paciente {} [guilherme daniela paulo])
        visitas {}
        visitas (adiciona-visita visitas 15 ["01/01/2019"])
        visitas (adiciona-visita visitas 20 ["01/02/2019", "01/01/2020"])
        visitas (adiciona-visita visitas 15 ["01/03/2019"])
        ]
    (pprint pacientes)
    (pprint visitas)
    (imprime-relatorio-de-paciente visitas guilherme)
    ))

(testa-uso-de-pacientes)


; Definindo schemas para validar contratos

; s/defn é um macro da biblioteca schema que tem a possibilidade de
; definir funções e aplicar validações dentro dela.
;
; x :- y é usado para dizer que o simbolo x deve se encaixar nos moldes de y
;
;
; (s/set-fn-validation!) é a função usada para validar os parametros da função

(s/set-fn-validation! true)

(s/defn imprime-relatorio-de-paciente [visitas, paciente :- Long]
  (println "Visitas do paciente" paciente "são" (get visitas paciente))
  )

(testa-uso-de-pacientes)