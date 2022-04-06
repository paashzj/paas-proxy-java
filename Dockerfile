#
# Build stage
#
FROM ttbb/compile:jdk11-git-mvn AS build
COPY . /opt/sh/compile
WORKDIR /opt/sh/compile
RUN mvn -B clean package


FROM ttbb/base:jdk11

LABEL maintainer="shoothzj@gmail.com"

COPY --from=build /opt/sh/compile/paas-proxy/target/paas-proxy-0.0.1.jar /opt/sh/paas-proxy.jar
COPY --from=build /opt/sh/compile/paas-proxy/target/lib/* /opt/sh/lib/
COPY --from=build /opt/sh/compile/paas-proxy/target/conf/* /opt/sh/conf/

COPY docker-build /opt/sh

CMD ["/usr/local/bin/dumb-init", "bash", "-vx","/opt/sh/scripts/start.sh"]