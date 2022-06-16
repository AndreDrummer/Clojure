(ns loja.aula1)

(map println ["daniela" "guilherme" "andre" "lucia"])
(println (first ["daniela" "guilherme" "andre" "lucia"]))
(println (rest ["daniela" "guilherme" "andre" "lucia"]))
(println (rest []))
(println (next ["daniela" "guilherme" "andre" "lucia"]))
(println (next []))
(println (seq []))
(println (seq [1 2 3 5]))

(println "MEU MAPA")

(println "MEU MAPA COM PARADA")

(defn meu-mapa
  [funcao sequencia]
  (let [primeiro (first sequencia)]
    (if primeiro
      (do
        (funcao primeiro)
        (meu-mapa funcao (rest sequencia))))))

(meu-mapa println ["daniela" "guilherme" "andre" "lucia"])
(meu-mapa println ["daniela" false "andre" "lucia"])

(println "MEU MAPA COM PARADA NO NULO")

(defn meu-mapa
  [funcao sequencia]
  (let [primeiro (first sequencia)]
    (if (not (nil? primeiro))
      (do
        (funcao primeiro)
        (meu-mapa funcao (rest sequencia))))))

(meu-mapa println ["daniela" "guilherme" "andre" "lucia"])
(meu-mapa println ["daniela" false "andre" "lucia"])
(meu-mapa println [])
(meu-mapa println nil)

;(meu-mapa println (range 100000))

(def sequencia [1 2 3 4])

(println (doc seq))

; Tail recursion
; Dando a dica de como não estourar a pilha em uma execucao de recorrencia
(defn meu-mapa
  [funcao sequencia]
  (let [primeiro (first sequencia)]
    (if (not (nil? primeiro))
      (do
        (funcao primeiro)
        (recur funcao (rest sequencia))))))

; recur pode ser usado para otimizar a recursao em tempo de compilação.

;(meu-mapa println (range 100000))