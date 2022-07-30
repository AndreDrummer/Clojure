(ns loja.aula5
  (:require [loja.db :as l.db]
            [loja.logic :as l.logic]))

(defn gastou-bastante? [info-usuario]
  (> (:preco-total info-usuario) 500))

(let [pedidos (l.db/todos-os-pedidos)
      resumo (l.logic/resumo-por-usuario pedidos)]
  (println "Keep" (keep gastou-bastante? resumo))
  (println "Filter" (filter gastou-bastante? resumo))
  )

(defn gastou-bastante? [info-usuario]
  (println "gastou-bastante?" (:usuario-id info-usuario))
  (> (:preco-total info-usuario) 500))


(let [pedidos (l.db/todos-os-pedidos)
      resumo (l.logic/resumo-por-usuario pedidos)]
  (println "Keep" (keep gastou-bastante? resumo))
  )

(println "Vamos isolar...")

(println (take 2 (range 10)))