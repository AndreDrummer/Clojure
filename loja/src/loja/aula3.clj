(ns loja.aula3
  (:require [loja.db :as l.db]))

(println (l.db/todos-os-pedidos))

; Função de agrupamento group-by
(println (group-by :usuario (l.db/todos-os-pedidos)))

; Posso definir minha propria funcao também
(defn minha-funcao-de-agrupamento [elemento] (println "elemento" elemento) (:usuario elemento))

(println (group-by minha-funcao-de-agrupamento (l.db/todos-os-pedidos)))

(println (count (vals (group-by :usuario (l.db/todos-os-pedidos)))))

(println (map count (vals (group-by :usuario (l.db/todos-os-pedidos)))))

(defn conta-total-por-usuario [[usuario pedidos]] (count pedidos))

; Using Lasting Thread
(->> (l.db/todos-os-pedidos)
     (group-by :usuario)
     (map conta-total-por-usuario)
     println)

(defn conta-total-por-usuario [[usuario pedidos]] [usuario (count pedidos)])

; Using Lasting Thread
(->> (l.db/todos-os-pedidos)
     (group-by :usuario)
     (map conta-total-por-usuario)
     println)

(defn conta-total-por-usuario [[usuario pedidos]] {
                                                   :usuario-id       usuario
                                                   :total-de-pedidos (count pedidos)
                                                   })

; Using Lasting Thread
(->> (l.db/todos-os-pedidos)
     (group-by :usuario)
     (map conta-total-por-usuario)
     println)


(defn total-do-item [[_ detalhes]]
  (* (get detalhes :quantidade 0) (get detalhes :preco-unitario 0)))

(defn total-do-pedido
  [pedido]
  (->> pedido (map total-do-item) (reduce +))
  )

(defn total-dos-pedidos
  [pedidos]
  (->> pedidos
       (map :itens)
       (map total-do-pedido)
       (reduce +)))

(defn quantia-de-pedidos-e-gasto-total-por-usuario [[usuario pedidos]] {
                                                                        :usuario-id       usuario
                                                                        :total-de-pedidos (count pedidos)
                                                                        :preco-total      (total-dos-pedidos pedidos)
                                                                        })

; Using Lasting Thread
(->> (l.db/todos-os-pedidos)
     (group-by :usuario)
     (map quantia-de-pedidos-e-gasto-total-por-usuario)
     println)