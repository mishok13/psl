# io.s6/psl

A Clojure library implementing Public Suffix List support.

## Usage

```clojure
>>> (require '[io.s6.psl :as psl])
>>> (def p (psl/psl))
>>> (println (psl/suffix p "www.google.com"))
"google.com"
```

## License

Copyright © 2015 Screen6

Distributed under the MIT license.
