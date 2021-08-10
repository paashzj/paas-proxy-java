cd "$(dirname "$0")"

cd ..

echo `pwd`

mkdir /opt/sh/logs

java -Xmx1G -Xms1G -XX:MaxDirectMemorySize=2G -Dlog4j.configurationFile=/opt/sh/conf/log4j2.xml -jar /opt/sh/paas-proxy.jar >/opt/sh/logs/console.log 2>/opt/sh/logs/error.log