cd "$(dirname "$0")"

cd ..

echo `pwd`

java -Xmx1G -Xms1G -XX:MaxDirectMemorySize=2G -Dlog4j.configurationFile=/opt/sh/conf/log4j2.xml -jar /opt/sh/ttbb.jar