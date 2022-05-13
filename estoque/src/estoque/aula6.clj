(ns estoque.aula6)

(def pedido {
             :mochila  {:quantidade 2, :preco 80}
             :camiseta {:quantidade 3, :preco 40}})


; com class "alguma coisa" é possivel saber o tipo desse alguma coisa
(defn imprime-e-15
  [valor] (println "Valor" valor (class valor)) 15)

;(defn imprime-e-15 [chave valor]
;  (println chave "e" valor)
;  15)
;
;(println (map imprime-e-15 pedido))

; Nos parametros da funcao agora ele esta desestruturando (destructuring) o vector que sera
; recebido como paramentro
(defn imprime-e-15 [[chave valor]]
  (println chave "e" valor)
  15)

(println (map imprime-e-15 pedido))

(defn preco-dos-produtos
  [[chave valor]]
  (* (:quantidade valor)
     (:preco valor))
  )

(println (map preco-dos-produtos pedido))
(println (reduce + (map preco-dos-produtos pedido)))

(defn preco-dos-produtos
  [[_chave valor]]
  (* (:quantidade valor)
     (:preco valor))
  )

(println (reduce + (map preco-dos-produtos pedido)))

; THREAD LAST == ->>
(defn total-do-pedido [pedido]
  (->> pedido (map preco-dos-produtos) (reduce +)))

(println (total-do-pedido pedido))

(defn preco-total-do-produto [produto] (* (:quantidade produto) (:preco produto)))

; THREAD LAST == ->>
(defn total-do-pedido
  [pedido]
  (->> pedido
       vals
       (map preco-total-do-produto)
       (reduce +)))

(println  "Total ->" (total-do-pedido pedido))

(def pedido {
             :mochila  {:quantidade 2, :preco 80}
             :camiseta {:quantidade 3, :preco 40}
             :chaveiro {:quantidade 1}})

(defn gratuito?
  [item]
  ;(println item)
  (<= (get item :preco 0) 0))

(println "Filtrando gratuitos")
(println
  (filter
    ;(fn [[_ item]] (gratuito? item)) pedido))
    #(gratuito? (second %)) pedido)) ; Utilizando lambda

(defn pago?
  [item]
  (not (gratuito? item)))

(println (pago? {:preco 50}))
(println (pago? {:preco 0}))

(def pago? (comp not gratuito?)) ; A função comp faz a composição de duas funções,
; portanto retorna uma função. Por isso nao precisou ser definida como defn

(println (pago? {:preco 50}))
(println (pago? {:preco 0}))







; Atividade: Como calcular o total de certificados de todos os clientes?
(def clientes [
               { :nome "Guilherme"
                :certificados ["Clojure" "Java" "Machine Learning"] }
               { :nome "Paulo"
                :certificados ["Java" "Ciência da Computação"] }
               { :nome "Daniela"
                :certificados ["Arquitetura" "Gastronomia"] }])

;Resposta
(println "Certificados" (->> clientes (map :certificados) (map count) (reduce +)))

































