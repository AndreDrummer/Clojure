(ns hospital2.logic)

(defn cabe-na-fila? [hospital departamento]
  (-> hospital (get,,, departamento)
      count,,,
      (<,,, 5)))

(defn chega-em
  [hospital departamento pessoa]
  (if (cabe-na-fila? hospital departamento)
    (update hospital departamento conj pessoa)
    (throw (ex-info "Fila já está cheia" {:tentando-adicionar pessoa}))))

(defn chega-em-pausado
  [hospital departamento pessoa]
  (println "Tentando adicionar a pessoa " pessoa)
  (Thread/sleep (* (rand) 2000))
  (if (cabe-na-fila? hospital departamento)
    (do                                                     ;(Thread/sleep (* (rand) 2000))
      (println "Dando o update " pessoa)
      (update hospital departamento conj pessoa)
      )

    (throw (ex-info "Fila já está cheia" {:tentando-adicionar pessoa}))))


(defn atende
  [hospital departamento]
  (update hospital departamento pop)

  ; Com thread lasting
  ;(->> pop
  ;     (update hospital departamento)
  ;     )
  )

(defn proxima
  [hospital departamento]
  "Retorna a próxima pessoa da fila"
  (-> hospital departamento peek))

(defn transfere [hospital de para]
  "Transfere o próximo paciente da fila 'de' para a fila 'para'"
  (let [pessoa (proxima hospital de)]
    (-> hospital
        (atende de)
        (chega-em para pessoa))
    )
  )

; Esse aqui apesar da simplicidad, funciona bem
(defn atende-completo
  [hospital departamento]
  {
   :paciente (update hospital departamento peek)
   :hospital (update hospital departamento pop)
   }
  )

; Código aqui é mais complexo e maior, não fica bom usar ele.
(defn atende-completo-que-chama-ambos [hospital departamento]
  (let [fila (get hospital departamento)
        peek-pop (juxt peek pop)                            ; juxt, chama várias funções com os parametros passados
        [pessoa fila-atualizada] (peek-pop fila)
        hospital-atualizado (update hospital assoc departamento fila-atualizada)
        ]
    {
     :paciente pessoa
     :hospital hospital-atualizado
     }))
