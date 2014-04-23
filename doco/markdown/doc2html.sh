#!/bin/sh
multimarkdown src/$1.md.patched.md | xsltproc -nonet -novalid xhtml-woko.xslt - > target/$1.html
