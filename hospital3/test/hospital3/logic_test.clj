(ns hospital3.logic-test
  (:use clojure.pprint)
  (:require [clojure.test :refer :all]
            [clojure.test.check.clojure-test :refer (defspec)]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [hospital3.logic :refer :all]
            [hospital3.model :as h.model]
            [schema.core :as s])
  (:import (clojure.lang ExceptionInfo)))

(s/set-fn-validation! true)

(deftest cabe-na-fila?-test

  (let [hospital-cheio {:espera [32 65 87 41 69]}]
    ; Boundary tests
    ; Exatamente na borda e one off. -1, +1, <=, >=, =

    ; borda do zero
    (testing "Que cabe numa fila vazia"
      (is (cabe-na-fila? {:espera []} :espera)))

    ; Gerative Tests
    (testing "Que cabe pessoas em filas de tamanho até 4 inclusive"
      (doseq [fila (gen/sample (gen/vector gen/string-alphanumeric 0 4) 10)]
        (is (cabe-na-fila? {:espera fila} :espera))))

    ; borda do limite
    (testing "Que não cabe na fila quando a fila está cheia"
      (is (not (cabe-na-fila? hospital-cheio :espera))))

    ; one off da borda do limite pra cima
    (testing "Que não cabe na fila quando tem mais do que uma fila cheia"
      (is (not (cabe-na-fila? {:espera [1 2 3 4 5 6]} :espera))))


    ; dentro das bordas
    (testing "Que cabe na fila quando tem gente mais não está cheia"
      (is (cabe-na-fila? {:espera [1 2 3 4]} :espera))
      (is (cabe-na-fila? {:espera [1 2]} :espera)))

    (testing "Que não cabe quando o departamento não existe"
      (is (not (cabe-na-fila? {:espera [1 2 3 4]} :raio-x)))))
  )

(deftest chega-em-test

  (let [hospital-cheio {:espera [32 65 87 41 69]}]

    (testing "Aceita pessoas enquanto cabem pessoas na fila"
      ; Este teste é ruim pois ele testa apenas que copiamos o mesmo código da lógica aqui no teste também.
      ; (is (= (update {:espera [1 2 3 4 5]} :espera conj 5)
      ;       (chega-em {:espera [1 2 3 4 5]}, :espera, 5)))

      (is (= {:espera [1, 2, 3, 4, 5]}
             (chega-em {:espera [1, 2, 3, 4]}, :espera, 5))
          )


      ;Testar não sequencialmente, essa é uma boa prática.
      (is (= {:espera [1 2 5]}
             (chega-em {:espera [1 2]}, :espera, 5)))
      )

    ;(is (= {:hospital {:espera [1, 2, 3, 4, 5]} :resultado :sucesso}
    ;       (chega-em {:espera [1, 2, 3, 4]}, :espera, 5))
    ;    )
    ;
    ;
    ;;Testar não sequencialmente, essa é uma boa prática.
    ;(is (= {:hospital {:espera [1 2 5]} :resultado :sucesso}
    ;       (chega-em {:espera [1 2]}, :espera, 5)))
    ;)


    (testing "Não aceita quand não cabe na fila"
      ; Essa não é uma abordagem inteligente
      ;(is (thrown? ExceptionInfo (chega-em hospital-cheio, :espera 76)))

      ; Essa aqui já é um pouco melhor, mas ainda assim não é a melhor opção.
      ;(is (thrown? IllegalStateException (chega-em hospital-cheio, :espera 76))))

      ;outra abordagem com nil, mas temos o perigo do swap, teremos que trabalhar em outro ponto.
      ;(is (nil? (chega-em hospital-cheio :espera 76)))

      ; outra maneira de testar, onde ao inves de como JAVA, utilizar o tipo de exception para entender
      ; o TIPO de erro que ocorreu, estou usando os dados de exception para isso
      ;
      ;(is (try
      ;      (chega-em hospital-cheio, :espera 85)
      ;      (catch clojure.lang.ExceptionInfo esssss
      ;        (= :impossivel-colocar-pessoa-na-fila (:tipo (ex-data e)))
      ;        )))

      ;(is (= {:hospital hospital-cheio :resultado :impossivel-colocar-pessoa-na-fila}
      ;       (chega-em hospital-cheio, :espera 76)))
      )
    )

  ;(testing "Que é colocada uma pessoa numa fala que é menor que 5"
  ;  (doseq [fila (gen/sample (gen/vector gen/string-alphanumeric 0 4) 10)
  ;          pessoa (gen/sample gen/string-alphanumeric)]
  ;    (println pessoa fila)
  ;    )
  ;  )
  )

(deftest transfere-test
  (testing "Aceita pessoas se cabe"
    (let [hospital-original {:espera (conj h.model/fila-vazia "5") :raio-x h.model/fila-vazia}]
      (is (= {:espera []
              :raio-x ["5"]}
             (transfere hospital-original :espera :raio-x)))
      )

    (let [hospital-original {:espera (conj h.model/fila-vazia "51" "5") :raio-x (conj h.model/fila-vazia "13")}]
      (is (= {:espera ["5"]
              :raio-x ["13" "51"]}
             (transfere hospital-original :espera :raio-x)))
      )
    )

  (testing "Recusa pessoas se não cabe"
    (let [hospital-cheio {:espera (conj h.model/fila-vazia 5) :raio-x (conj h.model/fila-vazia "54" "87" "65" "5" "41")}]
      (is (thrown? ExceptionInfo
                   (transfere hospital-cheio :espera :raio-x)))
      )
    )

  (testing "Não pode invocar transferencia sem hospital"
    (is (thrown? ExceptionInfo
                 (transfere nil :espera :raio-x))))

  (testing "condições obrigatórias"
    (let [hospital {:espera (conj h.model/fila-vazia "5") :raio-x (conj h.model/fila-vazia "54" "87" "65" "5" "41")}]
      (is (thrown? AssertionError
                   (transfere hospital :nao-existe :raio-x)))
      (is (thrown? AssertionError
                   (transfere hospital :raio-x :nao-existe)))
      )
    )
  )

(defspec coloca-uma-pessoa-em-filas-menores-que-5 100
         (prop/for-all
           [fila (gen/vector gen/string-alphanumeric 0 4)
            pessoa gen/string-alphanumeric]
           (is (= {:espera (conj fila pessoa)}
                  (chega-em {:espera fila} :espera pessoa)))))

(def nome-aleatorio-gen
  (gen/fmap clojure.string/join (gen/vector gen/char-alphanumeric 5 10))
  )

(defn transforma-vetor-em-fila [vetor]
  (reduce conj h.model/fila-vazia vetor))


(def fila-nao-cheia-gen
  (gen/fmap
    transforma-vetor-em-fila
    (gen/vector nome-aleatorio-gen 0 4)))

;(defn transfere-ignorando-erro [hospital para]
;  (try (transfere hospital :espera para)
;       (catch ExceptionInfo e
;         (cond
;           (= :fila-cheia (:type (ex-data e))) hospital
;           :else (throw e)
;           ))))

(defn transfere-ignorando-erro [hospital para]
  (try (transfere hospital :espera para)
       (catch IllegalStateException e hospital)))


(defspec transfere-tem-que-manter-a-quantidade-de-pessoas 50
         (prop/for-all
           [espera fila-nao-cheia-gen
            raio-x fila-nao-cheia-gen
            ultrasom fila-nao-cheia-gen
            vai-para (gen/elements [:raio-x :ultrasom])
            ]
           (let [hospital-inicial {:espera espera, :raio-x raio-x, :ultrasom ultrasom}
                 hospital-final (transfere-ignorando-erro hospital-inicial vai-para)]
             (> (total-de-pacientes hospital-inicial)
                (total-de-pacientes hospital-final))
             )
           ))






