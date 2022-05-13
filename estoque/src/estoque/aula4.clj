(ns estoque.aula4)

(def precos [30 700 1000])

; Get a element on Vector. Tuo ways..

; First using the data structure as a function.
; This way if the element dosen't exist the get function will throw a exception.
(println (precos 0))

; Second using get function. This way if the element dosen't exist the get function
; will return nil.
(println (get precos 0))

; Valor padrao caso o elemento nao exista. Nessa caso retorna 0.
(println (get precos 5 0))

; This way, the vector is not modified, but the conj function returns the vector
; with a new value.
(println (conj precos 5))

; Update the vector.
(println (update precos 0 inc))
; The vector is being "update", the new vector will receive the original vector and increases the value of the
; element at position 0.

(defn soma-1
  [valor]
  (println "Estou somando um em" valor)
  (+ valor 1))

(println (update precos 0 soma-1))

; CÓDIGO DA AULA ANTERIOR

(defn aplica-desconto?
  [valor-bruto]
  (> valor-bruto 100)
  )


(defn valor-descontado
  "Retorna o valor com desconto de 10% se o valor bruto for estritamente maior que 100."
  [valor-bruto]
  (if (aplica-desconto? valor-bruto)
    (let [taxa-de-desconto (/ 10 100)
          desconto (* valor-bruto taxa-de-desconto)]
      (- valor-bruto desconto))
    valor-bruto))

; Aplica o valor descontado para cada um desses precos
; isso é feito de maneira declarativa

(println
  (map valor-descontado precos))

(println (range 10))
(println (filter odd? (range 10)))

(println (filter aplica-desconto? precos))

(println (map valor-descontado (filter aplica-desconto? precos)))

; A funcao reduce recebe como parametro a funcao que vai agir em cima da colecao e a
; colecao em si.
; També é possivel passar um valor inicial, nesse caso abaixo é o 2
(println (reduce + 2 precos))

(defn minha-soma
  [valor-1 valor-2]
  (println "Somando " valor-1  valor-2)
  (+ valor-1 valor-2))

(println (reduce minha-soma precos))