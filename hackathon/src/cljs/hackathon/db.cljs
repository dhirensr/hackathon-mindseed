(ns hackathon.db)

(defonce default-db
  {:page :home
   :toggle false
   :synonyms [{:word "about"
               :synonym "approximately"
               :clicked? false
               :disabled? false}

              {:word "approximately"
               :synonym "about"
               :clicked? false
               :disabled? false}

              {:word "abstract"
               :synonym "summary"
               :clicked? false
               :disabled? false}

              {:synonym "abstract"
               :word "summary"
               :clicked? false
               :disabled? false}

              {:word "almost"
               :synonym "nearly"
               :clicked? false
               :disabled? false}

              {:synonym "almost"
               :word "nearly"
               :clicked? false
               :disabled? false}

              {:word "animated"
               :synonym "lively"
               :clicked? false
               :disabled? false}

              {:synonym "animated"
               :word "lively"
               :clicked? false
               :disabled? false}

              {:word "arise"
               :synonym "occur"
               :clicked? false
               :disabled? false}

              {:synonym "arise"
               :word "occur"
               :clicked? false
               :disabled? false}

              {:word "aromatic"
               :synonym "fragrant"
               :clicked? false
               :disabled? false}

              {:synonym "aromatic"
               :word "fragrant"
               :clicked? false
               :disabled? false}

              {:word "artful"
               :synonym "crafty"
               :clicked? false
               :disabled? false}

              {:synonym "artful"
               :word "crafty"
               :clicked? false
               :disabled? false}

              {:word "association"
               :synonym "organization"
               :clicked? false
               :disabled? false}

              {:synonym "association"
               :word "organization"
               :clicked? false
               :disabled? false}]})
