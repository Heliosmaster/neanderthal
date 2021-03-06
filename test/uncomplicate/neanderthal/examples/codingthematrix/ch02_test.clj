(ns uncomplicate.neanderthal.examples.codingthematrix.ch02-test
  (:require [midje.sweet :refer [facts throws =>]]
            [uncomplicate.neanderthal
             [core :refer :all]
             [real :refer :all]]))

(set! *warn-on-reflection* true)

(facts
 "2.1 What is a Vector?"
 (let [v (dv 3.14159 2.718281828 -1.0 2.0)]
   (dim v) => 4
   (vect? v ) => true

   (ifn? v) => true
   (v 0) => (entry v 0)
   (v 1) => (entry v 1)
   (v 2) => (entry v 2)
   (v 3) => (entry v 3)))

;; Some sections, such as 2.2 and 2.2.1 are not
;; applicable for this library, since they are
;; not numeric in nature.
;; Many examples from the book directly apply to
;; Clojure maps, which makes sense, but is not of
;; interest for numeric computations.
;; Such discussions have been skipped here.

(facts
 "2.4 Vector addition; 2.4.1. Translation and vector addition."

 (let [trans-vector (dv [1 2])
       translate (fn [x] (axpy! x trans-vector))]

   (translate (dv 4 4)) => (dv 5 6)
   (translate (dv -4 -4)) => (dv -3 -2)

   (dv 2) => (dv 0 0)

   (axpy! (dv 4 4) (dv 2)) => (dv 4 4)
   (axpy! (dv 4 4) (zero (dv 4 4))) => (dv 4 4)))

(facts
 "2.4.2 Vector addition is associative and commutative"

 (let [u (dv 1 2 3)
       v (dv 10 20 30)
       w (dv 100 200 300)]
   (xpy (xpy u v) w) => (xpy u (xpy v w))
   (xpy u v) => (xpy v u)))

(facts
 "2.5. Scalar-vector multiplication"
 (ax 2 (dv 5 4 10)) => (dv 10 8 20)
 (ax 2 (ax 3 (dv 1 2 3))) => (ax (* 2 3) (dv 1 2 3))

 (take 3 (map #(ax (/ % 10) (dv 3 2)) (range 11)))
 => [(dv 0 0) (dv 0.30000000000000004 0.2)
     (dv 0.6000000000000001 0.4)] )

(facts
 "2.6 Combining vector addition and scalar multiplication"

 (ax 2 (xpy (dv 1 2 3) (dv 3 4 4)))
 => (xpy (ax 2 (dv 1 2 3)) (ax 2 (dv 3 4 4)))

 (ax (+ 2 3) (dv 1 2 3))
 => (xpy (ax 2 (dv 1 2 3)) (ax 3 (dv 1 2 3))))

(facts
 "2.6.3 First look at convex combinations"
 (let [u1 (dv 2.0)
       v1 (dv 12.0)
       u2 (dv 5 2)
       v2 (dv 10 -6)
       ab [[1 0] [0.75 0.25] [0.5 0.5] [0.25 0.75] [0 1]]]

   (map (fn [[alpha beta]] (axpy alpha u1 beta v1)) ab)
   => [(dv 2.0) (dv 4.5) (dv 7.0) (dv 9.5) (dv 12.0)]

   (map (fn [[alpha beta]] (axpy alpha u2 beta v2)) ab)
   => [(dv 5 2) (dv 6.25 0) (dv 7.5 -2)
       (dv 8.75 -4) (dv 10 -6)]))

(facts
 "2.7.4 Vector negative, invertibility of vector addition,"
 "and vector subtraction"
 (let [w (dv 3 4)
       f (fn [v] (xpy v w))
       g (fn [v] (axpy! (copy v) -1 w))]
   ((comp f g) (dv 2 3)) => (dv 2 3)))

(facts
 "2.9 Dot-product"
 (dot (dv 1 1 1 1 1) (dv 10 20 0 40 -100)) => -30.0

 (let [cost (dv 2.5 1.5 0.006 0.45)
       quantity (dv 6 14 7 11)
       value (dv 0 960 0 3.25)]
   (dot cost quantity) => 40.992
   (dot value quantity) => 13475.75)

 (let [haystack (dv 1 -1 1 1 1 -1 1 1 1)
       needle (dv 1 -1 1 1 -1 1)]
   (map #(dot (subvector haystack % (dim needle)) needle)
        (range (inc (- (dim haystack) (dim needle)))))
   => [2.0 2.0 0.0 0.0]))

(facts
 "2.9.8 Algebraic properties of the dot product"
 (let [u (dv 1 2 3)
       v (dv 4 5 6)
       w (dv 7 8 9)]
   (dot u v) => (dot v u)
   (dot (ax 7 u) v) = (* 7 (dot u v))
   (dot (xpy u v) w) => (+ (dot u w) (dot v w))))

(facts "2.10.4 Printing vectors"
       (pr-str (dv 2 3 4))
       => "#<DoubleBlockVector| n:3, stride:1, (2.0 3.0 4.0)>")
