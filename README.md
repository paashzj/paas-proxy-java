# paas-proxy
[![codecov](https://codecov.io/gh/paashzj/pass-proxy-java/branch/main/graph/badge.svg?token=155QKNN7MQ)](https://codecov.io/gh/paashzj/pass-proxy-java)

## http test command
### pulsar-http
```bash
curl -XPOST -H 'content-type: application/json;charset=UTF-8' localhost:20001/v1/pulsar/tenants/public/namespaces/default/topics/test/produce -d '{"msg":"xxx"}' -iv
```