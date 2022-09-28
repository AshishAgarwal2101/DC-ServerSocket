#!/bin/bash

if [[ $# != 4 ]]; then
    echo "Not the right number of arguments. Try again."
    exit 1;
fi

if [[ $1 == "-document_root" ]]; then
    document_root=$2
fi
if [[ $2 == "-document_root" ]]; then
    document_root=$4
fi
if [[ $3 == "-port" ]]; then
    port=$2
fi
if [[ $3 == "-port" ]]; then
    port=$4
fi

if [[ -z $document_root || -z $port ]]; then
    echo "Arguments not in proper format. Try again."
    exit 1;
fi

echo "root: $document_root"
echo "port: $port"
javac Server.java
java Server $document_root $port
