#!/bin/bash
#
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#   http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.
#

cd "$(dirname "$0")"

cd ..

echo `pwd`

mkdir /opt/sh/logs

if [ -n "${PULSAR_JAR_VERSION}" ] && [ -n "${MAVEN_ADDRESS}" ]; then
  cd /opt/sh/lib

  # delete original version jar of pulsar
  rm -rf pulsar-client*
  rm -rf pulsar-common*
  rm -rf pulsar-package-core*
  rm -rf pulsar-transaction-common*

  # download specify version jar of pulsar
  curl  "${MAVEN_ADDRESS}"/org/apache/pulsar/pulsar-client-admin-api/"${PULSAR_JAR_VERSION}"/pulsar-client-admin-api-"${PULSAR_JAR_VERSION}".jar -o pulsar-client-admin-api-"${PULSAR_JAR_VERSION}".jar
  curl  "${MAVEN_ADDRESS}"/org/apache/pulsar/pulsar-client-admin-original/"${PULSAR_JAR_VERSION}"/pulsar-client-admin-original-"${PULSAR_JAR_VERSION}".jar -o pulsar-client-admin-original-"${PULSAR_JAR_VERSION}".jar
  curl  "${MAVEN_ADDRESS}"/org/apache/pulsar/pulsar-client-api/"${PULSAR_JAR_VERSION}"/pulsar-client-api-"${PULSAR_JAR_VERSION}".jar -o pulsar-client-api-"${PULSAR_JAR_VERSION}".jar
  curl  "${MAVEN_ADDRESS}"/org/apache/pulsar/pulsar-client-original/"${PULSAR_JAR_VERSION}"/pulsar-client-original-"${PULSAR_JAR_VERSION}".jar -o pulsar-client-original-"${PULSAR_JAR_VERSION}".jar
  curl  "${MAVEN_ADDRESS}"/org/apache/pulsar/pulsar-common/"${PULSAR_JAR_VERSION}"/pulsar-common-"${PULSAR_JAR_VERSION}".jar -o pulsar-common-"${PULSAR_JAR_VERSION}".jar
  curl  "${MAVEN_ADDRESS}"/org/apache/pulsar/pulsar-package-core/"${PULSAR_JAR_VERSION}"/pulsar-package-core-"${PULSAR_JAR_VERSION}".jar -o pulsar-package-core-"${PULSAR_JAR_VERSION}".jar
  curl  "${MAVEN_ADDRESS}"/org/apache/pulsar/pulsar-transaction-common/"${PULSAR_JAR_VERSION}"/pulsar-transaction-common-"${PULSAR_JAR_VERSION}".jar -o pulsar-transaction-common-"${PULSAR_JAR_VERSION}".jar
fi

java -Xmx1G -Xms1G -XX:MaxDirectMemorySize=2G -classpath /opt/sh/lib/*:/opt/sh/paas-proxy.jar:/opt/sh/conf/*  com.github.shoothzj.paas.proxy.Main >>/opt/sh/logs/stdout.log 2>>/opt/sh/logs/stderr.log
