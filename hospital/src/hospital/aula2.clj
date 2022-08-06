(ns hospital.aula2
  (:use clojure.pprint)
  (:require [schema.core :as s]))

; Esse linha serve para ativar de maneira global as validações do schema.
(s/set-fn-validation! true)

; Definindo um record. Record em clojure é semelhante a uma classe em outras linguagens.
;(s/defrecord Paciente [id :- Long, nome :- s/Str])

; Uma record (classe) pode ser instanciada das seguintes maneiras:
;
; Chamando a classe e passando os atributos (Paciente. 15 "André"),
;
; A partir de um mapa (map->Paciente {})
;
; Com thread first (->Paciente 90 "André)

; Instanciando e printando uma record (classe)
;(pprint (Paciente. 24 "André"))

; As validações funcionam?
;(pprint (Paciente. "24" "André"))

; Ops.. a linha acima passou uma string 24 e funcionou?? Mas e as validações??????

; ***  Vamos usar outra abordagem que não usar defrecord para definir um schema ***.

; Vamos utilizar código clojure para definir e usar um schema. Quando utilizamos a keyword
; def para definir um simbollo, o nome do simbolo é minusculo.Para definição de schema
; o nome do simbolo deve ser maiusculo. Veja.

(def Paciente
  "Schema de um paciente"                                   ; É possível atribuir comentários ao nosso schema.
  {:id s/Num, :nome s/Str}                                  ; Vemos que esse schema é uma mapa, onde seu atributos
  )                                                         ; são validados pelo schema validation.

; s/explain é um comando utilizado para explicar o
; schema definido.

(pprint (s/explain Paciente))

; Existe a possibilidade de validar a consistência de um schema.
; Para isso utilizamos o símbolo s/validate. vEJA.

(pprint (s/validate Paciente {:id 15 :nome "André"}))       ; Schema válido

; Neste caso, existe um erro de digitação e ao contrário de um schema definido com defrecord,
; s/validate irá reclamar e esse schema será inválido. Com defrecord, o nome digitado errado
; entraria de forma válida como se fosse um forward compatible, contudo em um código com boa
; cobertura de testes, esse problema seria facilmente pego.
;(pprint (s/validate Paciente {:id 15 :name "André"}))

; chaves que são keywords em schemas são OBRIGATÓRIAS por padrão.
; Então o schema abaixo também é inválido porque esta faltando o :nome.
;(pprint (s/validate Paciente {:id 15}))

; Agora com schemas é possível validar a saída de nossas funções.
; Veja o exemplo da função novo-paciente

; Cuidado ao definir usando s/. Para definir funcao utilize defn. Se utilizar s/def o clojure
; vai entender que você está definindo um schema e ai vai quebrar.
;(s/def novo-paciente :- Paciente --> Erro!

; Em caso de retornar algo que não condiz com o tipo de retorno o schema irá reclamar

;(s/defn novo-paciente :- Paciente
;  [id :- s/Num, nome :- s/Str]
;  {:id id :nome nome, :plano []}) --> Erro: {:plano disallowed-key}
;
;(pprint (novo-paciente 15 "André"))

(s/defn novo-paciente :- Paciente
  [id :- s/Num, nome :- s/Str]
  {:id id :nome nome})

(pprint (novo-paciente 15 "André"))

; Os schemas não servem apenas para fazer validações de tipo, ele também é útil
; para validarmos condições. Veja por exemplo, eu tenho uma função sunple que
; testa se determinado número é estritamente maior do que zero.

(defn estritamente-positivo? [x] (> x 0))

; Agora podemos utilizar essa função como validação de condição em um schema.
; s/pred é usado para determinar que a validação do schema é um predicado e a
; função estritamente positivo deve ser verdadeira.
;
(def EstritamentePositivo (s/pred estritamente-positivo?))

; Veja que a função funciona...
(def quinze 15)
(println quinze "é estritamente positivo?" (estritamente-positivo? quinze))

(def -quinze -15)
(println -quinze "é estritamente positivo?" (estritamente-positivo? -quinze))

(def zero 0)
(println zero "é estritamente positivo?" (estritamente-positivo? zero))

; Agora vamos validar o Schema baseado na função
(pprint (s/validate EstritamentePositivo quinze))           ; quando é valido igual nesse caso ele printa o valor

; Agora quando não é como nos casos abaixo ele estoura um erro.
;(pprint (s/validate EstritamentePositivo -quinze))
;(pprint (s/validate EstritamentePositivo zero))

; Eu posso definir uma espécie de descrição para o nome do meu schema
; para facilitar a leitura do console em caso de erro.

(def EstritamentePositivo (s/pred estritamente-positivo? 'estritamente-positivo?))
;(pprint (s/validate EstritamentePositivo zero))

; Podemos ainda restringir os tipo dos atributos de um schema. Vamos redefinir o schema
; Paciente da seguinte forma.

(def Paciente
  "Schema de paciente"
  {:id (s/constrained s/Int estritamente-positivo?), :nome s/Str})

; Validando...
(pprint (s/validate Paciente {:id 24 :nome "André"}))

; Validando um errado...
;(pprint (s/validate Paciente {:id -1 :nome "André"})) -->> Value does not match schema

; Podemos usar funções tanto nossas quanto as que já existem, por exemplo,
; Ja existe uma função para determinar se um número é positivo: pos?

(def Paciente
  "Schema de paciente"
  {:id (s/constrained s/Int pos?), :nome s/Str})

; Validando...
(pprint (s/validate Paciente {:id 24 :nome "André"}))

; Validando um errado...
;(pprint (s/validate Paciente {:id -1 :nome "André"})) -->> Value does not match schema

; E também pode ser usado lambdas, masss...

(def Paciente
  "Schema de paciente"
  {:id (s/constrained s/Int #(> % 0)), :nome s/Str})

; Validando...
(pprint (s/validate Paciente {:id 24 :nome "André"}))

 ;Validando um errado...
;(pprint (s/validate Paciente {:id -1 :nome "André"})) -->> Value does not match schema: {:id (not (hospital.aula2/fn--8048 -1))}
;
;Quando da erro o console não consegue verificar a função onde deu o erro, porque em tese
;o lambda não tem nome, ai o clojure utiliza isso -> fn--8048
; E se eu der uma descrição? Vamos ver...

(def Paciente
  "Schema de paciente"
  {:id (s/constrained s/Int #(> % 0) 'estritamente-positivo), :nome s/Str})

; Validando...
(pprint (s/validate Paciente {:id 24 :nome "André"}))

;Validando um errado...
;(pprint (s/validate Paciente {:id -1 :nome "André"})) --> Value does not match schema: {:id (not (estritamente-positivo -1))}
; Ai fica bom... :)

