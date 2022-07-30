(ns loja.aula4
  (:require [loja.db :as l.db]
            [loja.logic :as l.logic]))

(println (l.db/todos-os-pedidos))

(let [pedidos (l.db/todos-os-pedidos)
      resumo (l.logic/resumo-por-usuario (l.db/todos-os-pedidos))
      ]
  (println "Resumo" resumo)
  (println "Ordenação" (sort-by :preco-total resumo))
  (println "Ordenação ao contrario" (reverse (sort-by :preco-total resumo)))
  (println "Ordenação por id" (sort-by :usuario-id resumo))

  ; update-in, assoc-in, get-in
  (println (get-in pedidos [0 :itens :mochila :quantidade]))
  )

(defn resumo-por-usuario-ordenado [pedidos]
  (->> pedidos
       l.logic/resumo-por-usuario
       (sort-by :preco-total)
       reverse))

(let [pedidos (l.db/todos-os-pedidos)
      resumo (resumo-por-usuario-ordenado pedidos)
      ]
  (println "Resumo" resumo)
  (println "Primeiro" (first resumo))
  (println "Segundo" (second resumo))
  (println "Resto" (rest resumo))                           ; todos, exceto o primeiro
  (println "Devolve o tipo de dado" (class resumo))
  (println "nth  (1)" (nth resumo 1))
  (println "get (1)" (get resumo 1))
  (println "Take" (take 2 resumo))
  )

(defn top-2 [resumo]
  (take 2 resumo))

(let [pedidos (l.db/todos-os-pedidos)
      resumo (resumo-por-usuario-ordenado pedidos)
      ]
  (println "Resumo" resumo)
  (println "Top-2" (top-2 resumo)))


; Filter
(let [pedidos (l.db/todos-os-pedidos)
      resumo (resumo-por-usuario-ordenado pedidos)
      ]
  (println "> 500 Filter" (filter #(> (:preco-total %) 500) resumo)))


; Some = Pelo menos 1, resposta boolean
(let [pedidos (l.db/todos-os-pedidos)
      resumo (resumo-por-usuario-ordenado pedidos)
      ]
  (println "> 500 Filter" (some #(> (:preco-total %) 500) resumo)))

