(ns io.s6.psl
  (:require [clojure.java.io :as io]
            [clojure.set :as set]
            [clojure.string :as str])
  (:import [java.net IDN]))

(defn- canonize
  [^String s]
  (-> s
      (str/lower-case)
      (IDN/toASCII)))

(defn- make-labels
  [domain]
  (-> domain (str/split #"\.") (->> (remove str/blank?)) reverse))

(defn- categorize
  [^String s]
  (let [rule (cond
               (.startsWith s "*.") {:rule (subs s 2) :type :wildcard}
               (.startsWith s "!") {:rule (subs s 1) :type :exception}
               :else {:rule s :type :normal})]
    (->> (:rule rule)
         make-labels
         (assoc rule :labels))))

(defn psl
  [path]
  (letfn [(rule? [^String s] (not (or (.startsWith s "//") (str/blank? s))))]
    (with-open [reader (io/reader path)]
      (->> (line-seq reader)
           (map str/trim)
           (filter rule?)
           (map canonize)
           (map categorize)
           (map (juxt :labels identity))
           (into {})))))

(defn suffix
  [suffixes domain]
  (when (and suffixes domain)
    (let [labels (make-labels (canonize domain))
          rules (seq (remove nil? (map (partial get suffixes) (take-while (complement nil?) (iterate butlast labels)))))]
      (when rules
        ;; rules
        (let [match (first (sort-by (juxt (comp {:wildcard 1 :exception 0 :normal 2} :type) (comp - count :labels)) rules))]
          (case (:type match)
            :normal (when (>= (count labels) (inc (count (:labels match))))
                      (str/join "."(reverse (take (inc (count (:labels match))) labels))))
            :exception (str/join "." (reverse (take (count (:labels match)) labels)))
            :wildcard (when (>= (count labels) (+ 2 (count (:labels match))))
                        (str/join "." (reverse (take (+ 2 (count (:labels match))) labels))))))))))
