(ns hospital2.aula3
  (:use [clojure pprint])
  (:require [hospital2.logic :as h.logic]
            [hospital2.model :as h.model]))

;shaddowing
(let [nome "André"]
  (println nome)
  (let [nome "Jainne"]
    (println nome)
    )
  (println nome)
  )


; realizando mudanças com átomos
(defn testa-atomao []
  (let [hospital-silveira (atom {:espera h.model/fila-vazia})]
    (println hospital-silveira)
    (pprint hospital-silveira)
    (pprint (deref hospital-silveira))

    ; @something é uma sintax-sugger para (deref something)
    (pprint @hospital-silveira)

    ; não é assim que se altera o conteúdo dentro um átomo.
    ; o print a seguir é o resultado do assoc que sempre retorna um novo mapa
    (pprint (assoc @hospital-silveira :laboratorio1 h.model/fila-vazia))
    (pprint @hospital-silveira)

    ; funções com ! sempre causam efeitos colaterais
    ; e dessa forma que se altera conteúdo dentro de um átomo
    (swap! hospital-silveira assoc :laboratorio1 h.model/fila-vazia)
    (pprint @hospital-silveira)

    (swap! hospital-silveira assoc :laboratorio2 h.model/fila-vazia)
    (pprint @hospital-silveira)

    ; udpate tradicional imutável com dereferencia que não trará efeito
    (update @hospital-silveira :laboratorio1 conj 111)

    ; com swap, OBSERVE QUE no swap se remove o simbolo de deref ("@")
    (swap! hospital-silveira update :laboratorio1 conj 111)
    (pprint @hospital-silveira)
    ))

;(testa-atomao)

(defn chega-em-malvado! [hospital pessoa]
  (swap! hospital h.logic/chega-em-pausado :espera pessoa)
  (println "Após inserir " pessoa)
  )

(defn simula-um-dia-em-paralelo []
  (let [hospital (atom (h.model/novo-hospital))]
    (.start (Thread. (fn [] (chega-em-malvado! hospital 111))))
    (.start (Thread. (fn [] (chega-em-malvado! hospital 222))))
    (.start (Thread. (fn [] (chega-em-malvado! hospital 333))))
    (.start (Thread. (fn [] (chega-em-malvado! hospital 444))))
    (.start (Thread. (fn [] (chega-em-malvado! hospital 555))))
    (.start (Thread. (fn [] (chega-em-malvado! hospital 666))))
    (.start (Thread. (fn [] (Thread/sleep 8000)
                       (pprint hospital))))))

; estamos forçando situações nas quais o swap! vai continuar tentando
; realizar a atualização de um átomo. Isso ocorre por causa dos thread sleeps.
; A função para o swap ficar retentando deve ser uma função pura onde não há diferentes
; resultados a cada execução.
; Essa é a vantagem de usar swap.
(simula-um-dia-em-paralelo)
