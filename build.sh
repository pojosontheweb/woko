#!/bin/sh
mvn clean source:jar install -Pwebtests -Dwt.headless=true -Djava.io.tmpdir=/tmp
