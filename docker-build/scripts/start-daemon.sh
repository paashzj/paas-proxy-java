#!/bin/bash

cd "$(dirname "$0")"

cd ..

echo `pwd`

mkdir /opt/sh/logs

java -Xmx1G -Xms1G -XX:MaxDirectMemorySize=2G -classpath /opt/sh/lib/*:/opt/sh/paas-proxy.jar:/opt/sh/conf/*  com.github.shoothzj.paas.proxy.Main >>/opt/sh/logs/stdout.log 2>>/opt/sh/logs/stderr.log
