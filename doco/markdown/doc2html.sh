#!/bin/sh

multimarkdown woko.md | xsltproc -nonet -novalid ~/Library/Application\ Support/MultiMarkdown/XSLT/xhtml-toc-h1.xslt - > index.html && open index.html
