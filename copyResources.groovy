#!/usr/bin/env groovy

/*
 * Copyright 2001-2010 Remi Vankeisbelck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

def root = new File(".")
def rootPath = root.absolutePath

println "Starting in $rootPath..."

def bundlesDir = "$rootPath/core/src/main/bundles"

println "Grabbing the bundles text ($bundlesDir)"

def locales = ["", "en", "fr"]
def bundlesMap = [:]
locales.each { l ->
    def bName = l ? "WokoResources_${l}.properties" : "WokoResources.properties"
    bundlesMap[bName] = new File(bundlesDir + File.separator + bName).text
}


def excludes = ["bundles", "target", ".git", "overlays", "webtests-bootstrap"]

println "Starting crawling files from $root.absolutePath..."

int nbReplaced = 0

new File('.').eachFileRecurse { f ->
    def excluded = false
    def fullPath = f.absolutePath
    for (String s : excludes) {
        if (fullPath.contains(s)) {
            excluded = true
            break
        }
    }
    if (!excluded && fullPath.endsWith(".properties")) {
        for (String s : bundlesMap.keySet()) {
            if (fullPath.endsWith(s)) {
                f.text = bundlesMap[s]
                println "U $fullPath"
                nbReplaced++
            }
        }
    }
}

println "Done : replaced $nbReplaced files"