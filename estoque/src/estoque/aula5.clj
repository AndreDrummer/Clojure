(ns estoque.aula5)

(def estoque {"Mochila" 10, "Camiseta" 5})

(println estoque)

(println "Temos" (count estoque) "elementos")
(println "Chaves são" (keys estoque))
(println "Valores são" (vals estoque))

; Keyword
;:mochila

(def estoque {:mochila  10
              :camiseta 5})

(println
  (assoc estoque :cadeira 3))

(println estoque)
(println (update estoque :mochila inc))

(defn tira-um [valor] (println "Tirando um de " valor) (- valor 1))

(println (update estoque :mochila tira-um))

(println (dissoc estoque :mochila))

(def pedido {
             :mochila { :quantidade 2, :preco 80}
             :camiseta { :quantodade 3, :preco 40}})

(println "\n\n\n\n\n")
(println pedido)

(def pedido (assoc pedido :chaveiro {:quantidade 1, :preco 10}))

(println pedido)
(println (pedido :mochila)) ; nessse caso se o pedido for nulo, vai retornar uma exception
(println (pedido :cadeira {}))
(println (get pedido :cadeira))
(println (get pedido :cadeira {}))
(println (:mochila pedido)) ; Usando a chave (keyword) como funcao
(println (:cadeira pedido {})) ;passando valor padrao quando a chave n existe
(println (:quantidade (:mochila pedido)))
; Atualizando um valor dentro de um map aninhado
(println (update-in  pedido [:mochila :quantidade] inc))

; THREADING: O resultado de uma função é passado como entrada para outra
; Como acontece no pipe por exemplo. THREAD FIRST == ->
(println (-> pedido :mochila :quantidade))

; Poderia ser feito assim..
(-> pedido :mochila :quantidade println)


