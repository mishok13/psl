[![License MIT][badge-license]](http://opensource.org/licenses/MIT)
[![Build Status](https://travis-ci.org/screen6/psl.png?branch=master)](https://travis-ci.org/screen6/psl)
[![Coverage Status](https://coveralls.io/repos/screen6/psl/badge.png)](https://coveralls.io/r/screen6/psl)

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
