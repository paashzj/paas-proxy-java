FROM ttbb/base:jdk11

LABEL maintainer="shoothzj@gmail.com"

COPY paas-proxy/target/paas-proxy-0.0.1.jar /opt/sh/paas-proxy.jar

COPY docker-scripts /opt/sh

CMD ["/usr/local/bin/dumb-init", "bash", "-vx","/opt/sh/scripts/start.sh"]