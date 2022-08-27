(ns hospital2.logic)


(defn chega-em [hospital departamento pessoa]

  (update hospital departamento conj pessoa))

(defn atende
  [hospital departamento]
  (update hospital departamento pop)

  ; Com thread lasting
  ;(->> pop
  ;     (update hospital departamento)
  ;     )
  )