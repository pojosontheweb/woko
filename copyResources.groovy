#!/usr/bin/env groovy

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


def excludes = ["bundles", "target", ".git", "overlays"]

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