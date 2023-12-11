#!/bin/bash -ex

mvn -q clean
mvn -q compile

# Run ApiApp
mvn -q exec:exec -Dexec.mainClass=cs1302uga.api/cs1302.api.ApiDriver

# Run OpenLibrarySearchApi
#mvn -q exec:exec -Dexec.mainClass=cs1302uga.api/cs1302.api.OpenLibrarySearchApi

# Run PropertiesExample
#mvn -q exec:exec -Dexec.mainClass=cs1302uga.api/cs1302.api.PropertiesExample
