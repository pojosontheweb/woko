#!/bin/sh

multimarkdown woko.md | xsltproc -nonet -novalid xhtml-woko.xslt - > index.html && open index.html

