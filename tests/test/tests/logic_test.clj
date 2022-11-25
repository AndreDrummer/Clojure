(ns tests.logic-test
  (:require [clojure.test :refer :all]
            [tests.logic :refer :all]))

(deftest cabe-na-fila?-test
  ; Boundary tests
  ; Exatamente na borda e one off. -1, +1, <=, >=, =

  ; borda do zero
  (testing "Que cabe na fila"
    (is (cabe-na-fila? {:espera []}  :espera)))

  ; borda do limite
  (testing "Que não cabe na fila quando a fila está cheia"
    (is (not (cabe-na-fila? {:espera [1 2 3 4 5]} :espera))))

  ; one off da borda do limite pra cima
  (testing "Que não cabe na fila quando tem mais do que uma fila cheia"
    (is (not (cabe-na-fila? {:espera [1 2 3 4 5 6]} :espera))))

  (testing "Que cabe na fila quando tem pouco menos do que uma fila cheia"
    (is (cabe-na-fila? {:espera [1 2 3 4]} :espera)))

  (testing "Que cabe na fila quando tem pouca gente na fila"
    (is (cabe-na-fila? {:espera [1 2]} :espera)))
  )