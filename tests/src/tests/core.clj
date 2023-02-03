(ns tests.core
  (:require [clojure.test.check.generators :as gen]))

(println (gen/sample gen/boolean 3))
(println (gen/sample gen/int))
(println (gen/sample gen/string-alphanumeric 5))

(println (gen/sample (gen/vector gen/int) 5))
(println (gen/sample (gen/vector gen/int) 100))
(println (gen/sample (gen/vector gen/int 1 5)))