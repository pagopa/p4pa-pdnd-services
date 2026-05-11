# p4pa-pdnd-services

This application belong to the **outbound** tier of the **Piattaforma Unitaria** product.

See [PU Microservice Architecture](https://raw.githubusercontent.com/pagopa/p4pa-doc/refs/heads/main/reference/technical-docs/Architettura_microservizi.pdf) for more details.

See [p4pa-doc](https://github.com/pagopa/p4pa-doc) for further documentation.

## 🧱 Role

* To obtain access tokens to authenticate the requests towards the following services exposed through PDND:
  * [ANPR](https://www.anagrafenazionale.interno.it/area-tecnica/accesso-ai-dati/):
    * [SERVIZIO C003 - SERVIZIO DI VERIFICA DICHIARAZIONE GENERALITA’](https://www.anagrafenazionale.interno.it/wp-content/uploads/documentazione_eserviceC003.pdf);
    * [SERVIZIO C030 - SERVIZIO DI ACCERTAMENTO IDENTIFICATIVO UNICO NAZIONALE](https://www.anagrafenazionale.interno.it/wp-content/uploads/documentazione_eserviceC030.pdf);
  * SEND;
* To invoke ANPR integrated services.

## 🌐 APIs
See [OpenAPI](openapi/generated.openapi.json), exposed through the following path:
* `/swagger-ui/index.html`

See [Postman collection](/postman/p4pa-pdnd-services-E2E.postman_collection.json) and [Postman Environment](https://pagopa.atlassian.net/wiki/spaces/SPAC/pages/1094615081/Environment+collection+postman).

### 📌 Relevant APIs
* `GET /anpr-service-e002/citizen`: To get citizen data from ANPR;
* `GET /token`: To obtain a token to use in order to authenticate invocations towards PDND exposed services.

### 📌 Common HTTP status returned:
* `401`: Invalid access token provided, thus a new login is required;
* `403`: Trying to access a not authorized resource.

## 🔎 Monitoring
See available actuator endpoints through the following path:
* `/actuator`

### 📌 Relevant endpoints
* Health (provide an accessToken to see details): `/actuator/health`
    * Liveness: `/actuator/health/liveness`
    * Readiness: `/actuator/health/readiness`
* Metrics: `/actuator/metrics`
    * Prometheus: `/actuator/prometheus`

Further endpoints are exposed through the JMX console.

## ✏️ Logging
See [log configured pattern](/src/main/resources/logback-spring.xml).

## 🔗 Dependencies

### 🌍 External
* PDND - IDP to authenticate PA services exposed by PDND:
  * [OpenAPI](openapi/external/pdnd-v1.openapi.yaml): To obtain access token towards PDND exposed services;
* ANPR:
  * [C030 OpenAPI](openapi/external/anprApiC030.openapi.yaml): To retrieve the idAnpr related to a citizen fiscal code;
  * [C003 OpenAPI](openapi/external/anprApiC003.openapi.yaml): To retrieve citizen data;

## 🔧 Configuration

See [application.yml](src/main/resources/application.yml) for each configurable property.

### 📌 Relevant configurations

#### 🌐 Application Server
| ENV         | DESCRIPTION                       | DEFAULT |
|-------------|-----------------------------------|---------|
| SERVER_PORT | Application server listening port | 8080    |

#### ✏️ Logging
| ENV                                   | DESCRIPTION                                                                                                                                            | DEFAULT |
|---------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------|---------|
| LOG_LEVEL_ROOT                        | Base level                                                                                                                                             | INFO    |
| LOG_LEVEL_PAGOPA                      | Base level of custom classes                                                                                                                           | INFO    |
| LOG_LEVEL_SPRING                      | Level applied to Spring framework                                                                                                                      | INFO    |
| LOG_LEVEL_SPRING_BOOT_AVAILABILITY    | To print availability events                                                                                                                           | DEBUG   |
| LOGGING_LEVEL_API_REQUEST_EXCEPTION   | Level applied to APIs exception                                                                                                                        | INFO    |
| LOG_LEVEL_PERFORMANCE_LOG             | Level applied to [PerformanceLog](https://raw.githubusercontent.com/pagopa/p4pa-doc/refs/heads/main/reference/technical-docs/Logging.pdf)              | INFO    |
| LOG_LEVEL_PERFORMANCE_LOG_API_REQUEST | Level applied to [API Performance Log](https://raw.githubusercontent.com/pagopa/p4pa-doc/refs/heads/main/reference/technical-docs/Logging.pdf)         | INFO    |
| LOG_LEVEL_PERFORMANCE_LOG_REST_INVOKE | Level applied to [REST invoke Performance Log](https://raw.githubusercontent.com/pagopa/p4pa-doc/refs/heads/main/reference/technical-docs/Logging.pdf) | INFO    |

#### 🔁 Integrations

##### 📋 [Caching](https://pagopa.atlassian.net/wiki/spaces/SPAC/pages/1542128077/Caching)
| ENV                    | DESCRIPTION                      | DEFAULT |
|------------------------|----------------------------------|---------|
| CACHE_ANPR_IDS_SIZE    | IdAnpr cache size                | 1000    |
| CACHE_ANPR_IDS_MINUTES | IdAnpr cache retention (minutes) | 60      |

##### 🔗 REST
| ENV                                               | DESCRIPTION                               | DEFAULT |
|---------------------------------------------------|-------------------------------------------|---------|
| DEFAULT_REST_CONNECTION_POOL_SIZE                 | Default connection pool size              | 10      |
| DEFAULT_REST_CONNECTION_POOL_SIZE_PER_ROUTE       | Default connection pool size per route    | 5       |
| DEFAULT_REST_CONNECTION_POOL_TIME_TO_LIVE_MINUTES | Default connection pool TTL (minutes)     | 10      |
| DEFAULT_REST_TIMEOUT_CONNECT_MILLIS               | Default connection timeout (milliseconds) | 120000  |
| DEFAULT_REST_TIMEOUT_READ_MILLIS                  | Default read timeout (milliseconds)       | 120000  |

##### 🌍 External services
| ENV                              | DESCRIPTION                                                                                                                 | DEFAULT                                        |
|----------------------------------|-----------------------------------------------------------------------------------------------------------------------------|------------------------------------------------|
| PDND_BASE_URL                    | PDND service URL                                                                                                            |                                                |
| PDND_MAX_ATTEMPTS                | PDND API max attempts                                                                                                       | 3                                              |
| PDND_WAIT_TIME_MILLIS            | PDND retry waiting time (milliseconds)                                                                                      | 500                                            |
| PDND_PRINT_BODY_WHEN_ERROR       | To print body when an error occurs                                                                                          | true                                           |
| PDND_ACCESS_TOKEN_AUDIENCE       | Value set for the claim `aud` of the PDND `clientAssertion` JWT sent during the invocation of the API `createToken` of PDND | auth.uat.interop.pagopa.it/client-assertion    |
| ANPR_BASE_URL                    | ANPR service base URL concatenated to particular service paths                                                              |                                                |
| ANPR_MAX_ATTEMPTS                | ANPR API max attempts                                                                                                       | 3                                              |
| ANPR_WAIT_TIME_MILLIS            | ANPR retry waiting time (milliseconds)                                                                                      | 500                                            |
| ANPR_PRINT_BODY_WHEN_ERROR       | To print body when an error occurs                                                                                          | true                                           |
| PDND_SERVICE_ANPR_C003_BASE_PATH | C003 ANPR service path                                                                                                      | /C003-servizioVerificaDichGeneralita/v1        |
| PDND_SERVICE_ANPR_C030_BASE_PATH | C030 ANPR service path                                                                                                      | /C030-servizioAccertamentoIdUnicoNazionale/v1/ |

#### 💼 Business logic
| ENV                               | DESCRIPTION                                                                             | DEFAULT                         |
|-----------------------------------|-----------------------------------------------------------------------------------------|---------------------------------|
| PDND_SERVICE_CLIENTID             | Default clientId                                                                        | clientId                        |
| PDND_SERVICE_KID                  | Default kid related to the default client                                               | kid                             |
| PDND_SERVICE_PRIVATEKEY           | Default private key related to the default client used to sign JWTs                     |                                 |
| PDND_SERVICE_PUBLICKEY            | Default public key related to the default client used to sign JWTs                      |                                 |
| ENV                               | Value set for the claim `userLocation` of the `Agid-JWT-TrackingEvidence` JWT           | dev                             |
| PDND_AUTH_USERID                  | Value set for the claim `userID` of the `Agid-JWT-TrackingEvidence` JWT                 | pdnd-user                       |
| PDND_AUTH_EXPIRATION_MINUTES      | Life time of the JWT created during the PDND exchange protocol (minutes)                | 10                              |
| PDND_SERVICE_ANPR_CLIENTID        | Default ANPR clientId                                                                   | ${PDND_SERVICE_CLIENTID}        |
| PDND_SERVICE_ANPR_KID             | Default ANPR kid related to the default ANPR client                                     | ${PDND_SERVICE_KID}             |
| PDND_SERVICE_ANPR_PRIVATEKEY      | Default ANPR private key related to the default ANPR client used to sign JWTs           | ${PDND_SERVICE_PRIVATEKEY}      |
| PDND_SERVICE_ANPR_PUBLICKEY       | Default ANPR public key related to the default ANPR client used to sign JWTs            | ${PDND_SERVICE_PUBLICKEY}       |
| PDND_SERVICE_ANPR_C003_PURPOSE_ID | C003 ANPR service `purposeId`                                                           | c003purposeid                   |
| PDND_SERVICE_ANPR_C003_AUDIENCE   | C003 ANPR service `audience`                                                            | C003audience                    |
| PDND_SERVICE_ANPR_C003_CLIENTID   | C003 ANPR service clientId                                                              | ${PDND_SERVICE_ANPR_CLIENTID}   |
| PDND_SERVICE_ANPR_C003_KID        | C003 ANPR service kid related to the C003 ANPR service client                           | ${PDND_SERVICE_ANPR_KID}        |
| PDND_SERVICE_ANPR_C003_PRIVATEKEY | C003 ANPR service private key related to the C003 ANPR service client used to sign JWTs | ${PDND_SERVICE_ANPR_PRIVATEKEY} |
| PDND_SERVICE_ANPR_C003_PUBLICKEY  | C003 ANPR service public key related to the C003 ANPR service client used to sign JWTs  | ${PDND_SERVICE_ANPR_PUBLICKEY}  |
| PDND_SERVICE_ANPR_C030_PURPOSE_ID | C030 ANPR service `purposeId`                                                           | c030purposeid                   |
| PDND_SERVICE_ANPR_C030_AUDIENCE   | C030 ANPR service `audience`                                                            | C030audience                    |
| PDND_SERVICE_ANPR_C030_CLIENTID   | C030 ANPR service clientId                                                              | ${PDND_SERVICE_ANPR_CLIENTID}   |
| PDND_SERVICE_ANPR_C030_KID        | C030 ANPR service kid related to the C030 ANPR service client                           | ${PDND_SERVICE_ANPR_KID}        |
| PDND_SERVICE_ANPR_C030_PRIVATEKEY | C030 ANPR service private key related to the C030 ANPR service client used to sign JWTs | ${PDND_SERVICE_ANPR_PRIVATEKEY} |
| PDND_SERVICE_ANPR_C030_PUBLICKEY  | C030 ANPR service public key related to the C030 ANPR service client used to sign JWTs  | ${PDND_SERVICE_ANPR_PUBLICKEY}  |
| PDND_SERVICE_SEND_PURPOSE_ID      | SEND service `purposeId`                                                                | purposeid                       |
| PDND_SERVICE_SEND_CLIENTID        | SEND service clientId                                                                   | ${PDND_SERVICE_CLIENTID}        |
| PDND_SERVICE_SEND_KID             | SEND service kid related to the SEND service client                                     | ${PDND_SERVICE_KID}             |
| PDND_SERVICE_SEND_PRIVATEKEY      | SEND service private key related to the SEND service client used to sign JWTs           | ${PDND_SERVICE_PRIVATEKEY}      |
| PDND_SERVICE_SEND_PUBLICKEY       | SEND service public key related to the SEND service client used to sign JWTs            | ${PDND_SERVICE_PUBLICKEY}       |

#### 🔑 keys
| ENV                          | DESCRIPTION                                         | DEFAULT |
|------------------------------|-----------------------------------------------------|---------|
| JWT_TOKEN_PUBLIC_KEY         | p4pa-auth JWT public key                            |         |

## 🛠️ Getting Started

### 📝 Prerequisites

Ensure the following tools are installed on your machine:

1. **Java 21+**
2. **Gradle** (or use the Gradle wrapper included in the repository)
3. **Docker** (to build and run on an isolated environment, optional)

### 🔐 Write Locks

```sh
./gradlew dependencies --write-locks
```

### ⚙️ Build

```sh
./gradlew clean build
```

### 🧪 Test

#### 📌 JUnit
```sh
./gradlew test
```

### 🚀 Run local

```sh
./gradlew bootRun
```

### 🐳 Build & run through Docker
```sh
docker build -t <APP_NAME> .
docker run --env-file <ENV_FILE> <APP_NAME>
```

### ⚖️ Generate dependencies licenses
```sh
./gradlew generateLicenseReport
```