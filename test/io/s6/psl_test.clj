(ns io.s6.psl-test
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [io.s6.psl :refer :all]
            [midje.sweet :refer :all]))

(def ^:private suffixes
  (psl (io/resource "test/psl.dat")))

;; Test data can be attributed to
;; http://mxr.mozilla.org/mozilla-central/source/netwerk/test/unit/data/test_psl.txt?raw=1
;; Since the source was licensed under Public Domain, the code here
;; can be and is attributed under MIT license.
(tabular
 (fact
  "Domains are identified correctly"
  (suffix suffixes ?input) => ?output)
 ?input ?output
 nil nil
 ;; Checking case
 "COM" nil
 "example.COM" "example.com"
 "eXaMplE.CoM" "example.com"
 "WwW.example.COM" "example.com"
 ;; XXX: Need to make a decision regarding leading dots in domain
 ;; names:
 ;; ".com" nil
 ;; ".example" nil
 ;; ".example.com" nil
 ;; ".example.example" nil
 ;; Unlisted entries
 "example" nil
 "example.example" nil
 "b.example.example" nil
 "a.b.example.example" nil
 ;; Simple 1-level rule
 "biz" nil
 "domain.biz" "domain.biz"
 "b.domain.biz" "domain.biz"
 "a.b.domain.biz" "domain.biz"
 ;; 2-level rule
 "com" nil
 "example.com" "example.com"
 "b.example.com" "example.com"
 "a.b.example.com" "example.com"
 "uk.com" nil
 "example.uk.com" "example.uk.com"
 "b.example.uk.com" "example.uk.com"
 "a.b.example.uk.com" "example.uk.com"
 "test.ac" "test.ac"
 ;; TLD with only 1 wildcard rule
 "cy" nil
 "c.cy" nil
 "b.c.cy" "b.c.cy"
 "a.b.c.cy" "b.c.cy"
 ;; Crazy complex Japanese TLD
 "jp" nil
 "test.jp" "test.jp"
 "www.test.jp" "test.jp"
 "ac.jp" nil
 "test.ac.jp" "test.ac.jp"
 "www.test.ac.jp" "test.ac.jp"
 "kyoto.jp" nil
 "test.kyoto.jp" "test.kyoto.jp"
 "ide.kyoto.jp" nil
 "b.ide.kyoto.jp" "b.ide.kyoto.jp"
 "a.b.ide.kyoto.jp" "b.ide.kyoto.jp"
 "c.kobe.jp" nil
 "b.c.kobe.jp" "b.c.kobe.jp"
 "a.b.c.kobe.jp" "b.c.kobe.jp"
 "city.kobe.jp" "city.kobe.jp"
 "www.city.kobe.jp" "city.kobe.jp"
 ;; Wildcards and exceptions only
 "ck" nil
 "test.ck" nil
 "b.test.ck" "b.test.ck"
 "a.b.test.ck" "b.test.ck"
 "www.ck" "www.ck"
 "www.www.ck" "www.ck"
 "us" nil
 "test.us" "test.us"
 "www.test.us" "test.us"
 "ak.us" nil
 "test.ak.us" "test.ak.us"
 "www.test.ak.us" "test.ak.us"
 "k12.ak.us" nil
 "test.k12.ak.us" "test.k12.ak.us"
 "www.test.k12.ak.us" "test.k12.ak.us"
 "食狮.com.cn" "xn--85x722f.com.cn"
 "食狮.公司.cn" "xn--85x722f.xn--55qx5d.cn"
 "www.食狮.公司.cn" "xn--85x722f.xn--55qx5d.cn"
 "shishi.公司.cn" "shishi.公司.cn"
 "公司.cn" nil
 "食狮.中国" "食狮.中国"
 "www.食狮.中国" "食狮.中国"
 "shishi.中国" "shishi.中国"
 "中国" nil
 "xn--85x722f.com.cn" "xn--85x722f.com.cn"
 "xn--85x722f.xn--55qx5d.cn" "xn--85x722f.xn--55qx5d.cn"
 "www.xn--85x722f.xn--55qx5d.cn" "xn--85x722f.xn--55qx5d.cn"
 "shishi.xn--55qx5d.cn" "shishi.xn--55qx5d.cn"
 "xn--55qx5d.cn" nil
 "xn--85x722f.xn--fiqs8s" "xn--85x722f.xn--fiqs8s"
 "www.xn--85x722f.xn--fiqs8s" "xn--85x722f.xn--fiqs8s"
 "shishi.xn--fiqs8s" "shishi.xn--fiqs8s"
 "xn--fiqs8s" nil)
