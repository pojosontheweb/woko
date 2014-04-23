#!/bin/sh

multimarkdown dev-guide.md | xsltproc -nonet -novalid xhtml-woko.xslt - > dev-guide.html # && open dev-guide.html
multimarkdown tutorial.md | xsltproc -nonet -novalid xhtml-woko.xslt - > tutorial.html # && open tutorial.html

