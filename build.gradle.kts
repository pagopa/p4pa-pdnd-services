import com.github.jk1.license.render.*
import com.github.jk1.license.filter.*
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    java
    id("org.springframework.boot") version "4.0.3"
    id("io.spring.dependency-management") version "1.1.7"
    jacoco
    id("org.sonarqube") version "7.2.3.7755"
    id("com.github.ben-manes.versions") version "0.53.0"
    id("org.openapi.generator") version "7.20.0"
    id("com.gorylenko.gradle-git-properties") version "2.5.7"
    id("com.github.jk1.dependency-license-report") version "3.1.1"
}

group = "it.gov.pagopa.payhub"
version = "0.0.1"
description = "p4pa-pdnd-services"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
    compileClasspath {
        resolutionStrategy.activateDependencyLocking()
    }
}

licenseReport {
    renderers = arrayOf(XmlReportRenderer("third-party-libs.xml", "Back-End Libraries"))
    outputDir = "$projectDir/dependency-licenses"
    filters = arrayOf(SpdxLicenseBundleNormalizer())
}
tasks.classes {
    finalizedBy(tasks.generateLicenseReport)
}

repositories {
    mavenCentral()
}

val springDocOpenApiVersion = "3.0.2"
val janinoVersion = "3.1.12"
val openApiToolsVersion = "0.2.9"
val javaJwtVersion = "4.5.1"
val jwksRsaVersion = "0.23.0"
val wiremockVersion = "3.13.2"
val wiremockSpringBootVersion = "4.2.0"
val micrometerVersion = "1.6.3"
val bouncycastleVersion = "1.83"
val caffeineVersion = "3.2.3"
val httpClientVersion = "5.6"
val httpCoreVersion = "5.4.1"
val commonsLang3Version = "3.20.0"

// fix cve
val jackson2CoreVersion = "2.21.1"
val jackson3CoreVersion = "3.1.0"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.springframework.boot:spring-boot-starter-opentelemetry")
    implementation("org.springframework.boot:spring-boot-starter-restclient")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.data:spring-data-commons")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-security-oauth2-resource-server")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$springDocOpenApiVersion") {
        exclude(group = "org.apache.commons", module = "commons-lang3")
    }
    implementation("org.apache.commons:commons-lang3:$commonsLang3Version")
    implementation("org.codehaus.janino:janino:${janinoVersion}")
    implementation("org.openapitools:jackson-databind-nullable:$openApiToolsVersion")
    implementation("io.micrometer:micrometer-tracing-bridge-otel:$micrometerVersion")
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("com.auth0:java-jwt:$javaJwtVersion")
    implementation("com.auth0:jwks-rsa:$jwksRsaVersion")
    implementation("org.bouncycastle:bcprov-jdk18on:$bouncycastleVersion")
    implementation("com.github.ben-manes.caffeine:caffeine:$caffeineVersion")
    implementation("org.apache.httpcomponents.client5:httpclient5:$httpClientVersion")
  implementation("org.apache.httpcomponents.core5:httpcore5:$httpCoreVersion")

    // CVE fix
    implementation("tools.jackson.core:jackson-core:$jackson3CoreVersion")
    implementation("com.fasterxml.jackson.core:jackson-core:$jackson2CoreVersion")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")

    //	Testing
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testImplementation("org.springframework.boot:spring-boot-starter-security-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.mockito:mockito-core")
    testImplementation("org.projectlombok:lombok")
    testImplementation("org.wiremock:wiremock-standalone:$wiremockVersion")
    testImplementation("org.wiremock.integrations:wiremock-spring-boot:$wiremockSpringBootVersion")
}

tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

val mockitoAgent = configurations.create("mockitoAgent")
dependencies {
    mockitoAgent("org.mockito:mockito-core") { isTransitive = false }
}
tasks {
    test {
        jvmArgs("-javaagent:${mockitoAgent.asPath}")
        testLogging.events = setOf(TestLogEvent.FAILED)
        testLogging.exceptionFormat = TestExceptionFormat.FULL
    }
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required = true
    }
}

val projectInfo = mapOf(
    "artifactId" to project.name,
    "version" to project.version
)

tasks {
    val processResources by getting(ProcessResources::class) {
        filesMatching("**/application.yml") {
            expand(projectInfo)
        }
    }
}

tasks.compileJava {
    dependsOn("dependenciesBuild")
}

tasks.register("dependenciesBuild") {
    group = "AutomaticallyGeneratedCode"
    description = "grouping all together automatically generate code tasks"

    dependsOn(
        "openApiGeneratePDNDSERVICES",
        "openApiGeneratePdndClient",
        "openApiGenerateAnprApiC030",
        "openApiGenerateAnprApiC003"
    )
}

configure<SourceSetContainer> {
    named("main") {
        java.srcDir("$projectDir/build/generated/src/main/java")
    }
}

springBoot {
    buildInfo()
    mainClass.value("it.gov.pagopa.payhub.pdnd.PayhubPdndApplication")
}

tasks.register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("openApiGeneratePDNDSERVICES") {
    group = "openapi"
    description = "description"

    generatorName.set("spring")
    inputSpec.set("$rootDir/openapi/p4pa-pdnd.openapi.yaml")
    outputDir.set("$projectDir/build/generated")
    apiPackage.set("it.gov.pagopa.payhub.pdnd.controller.generated")
    modelPackage.set("it.gov.pagopa.payhub.pdnd.dto.generated")
    typeMappings.set(
        mapOf(
            "PdndAuthData" to "it.gov.pagopa.payhub.pdnd.dto.PdndAuthData"
        )
    )
    configOptions.set(
        mapOf(
            "dateLibrary" to "java8",
            "requestMappingMode" to "api_interface",
            "useSpringBoot3" to "true",
            "interfaceOnly" to "true",
            "useTags" to "true",
            "useBeanValidation" to "true",
            "generateConstructorWithAllArgs" to "true",
            "generatedConstructorWithRequiredArgs" to "true",
            "enumPropertyNaming" to "original",
            "additionalModelTypeAnnotations" to "@lombok.experimental.SuperBuilder(toBuilder = true)"
        )
    )
}

tasks.register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("openApiGeneratePdndClient") {
    group = "openapi"
    description = "description"

    generatorName.set("java")
    inputSpec.set("$rootDir/openapi/external/pdnd-v1.openapi.yaml")
    outputDir.set("$projectDir/build/generated")
    apiPackage.set("it.gov.pagopa.payhub.pdnd.connector.pdnd.generated.api")
    modelPackage.set("it.gov.pagopa.payhub.pdnd.connector.pdnd.generated.dto")
    modelNameSuffix.set("DTO")
    configOptions.set(
        mapOf(
            "swaggerAnnotations" to "false",
            "openApiNullable" to "false",
            "dateLibrary" to "java8",
            "serializableModel" to "true",
            "useSpringBoot3" to "true",
            "useJakartaEe" to "true",
            "useOneOfInterfaces" to "true",
            "useBeanValidation" to "true",
            "serializationLibrary" to "jackson",
            "generateSupportingFiles" to "true",
            "generateConstructorWithAllArgs" to "true",
            "generatedConstructorWithRequiredArgs" to "true",
            "enumPropertyNaming" to "original",
            "additionalModelTypeAnnotations" to "@lombok.experimental.SuperBuilder(toBuilder = true)"
        )
    )
    library.set("resttemplate")
}

tasks.register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("openApiGenerateAnprApiC030") {
    group = "openapi"
    description = "description"

    generatorName.set("java")
    inputSpec.set("$rootDir/openapi/external/anprApiC030.openapi.yaml")
    outputDir.set("$projectDir/build/generated")
    apiPackage.set("it.gov.pagopa.payhub.anpr.C030.controller.generated")
    modelPackage.set("it.gov.pagopa.payhub.anpr.C030.dto.generated")
    configOptions.set(
        mapOf(
            "swaggerAnnotations" to "false",
            "openApiNullable" to "false",
            "dateLibrary" to "java8",
            "serializableModel" to "true",
            "useSpringBoot3" to "true",
            "useJakartaEe" to "true",
            "useOneOfInterfaces" to "true",
            "useBeanValidation" to "true",
            "serializationLibrary" to "jackson",
            "generateSupportingFiles" to "true",
            "generateConstructorWithAllArgs" to "true",
            "generatedConstructorWithRequiredArgs" to "true",
            "enumPropertyNaming" to "original",
            "additionalModelTypeAnnotations" to "@lombok.experimental.SuperBuilder(toBuilder = true)"
        )
    )
    library.set("resttemplate")
}

tasks.register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("openApiGenerateAnprApiC003") {
    group = "openapi"
    description = "description"

    generatorName.set("java")
    inputSpec.set("$rootDir/openapi/external/anprApiC003.openapi.yaml")
    outputDir.set("$projectDir/build/generated")
    apiPackage.set("it.gov.pagopa.payhub.anpr.C003.controller.generated")
    modelPackage.set("it.gov.pagopa.payhub.anpr.C003.dto.generated")
    configOptions.set(
        mapOf(
            "swaggerAnnotations" to "false",
            "openApiNullable" to "false",
            "dateLibrary" to "java8",
            "useSpringBoot3" to "true",
            "serializableModel" to "true",
            "useJakartaEe" to "true",
            "useOneOfInterfaces" to "true",
            "useBeanValidation" to "true",
            "serializationLibrary" to "jackson",
            "generateSupportingFiles" to "true",
            "generateConstructorWithAllArgs" to "true",
            "generatedConstructorWithRequiredArgs" to "true",
            "enumPropertyNaming" to "original",
            "additionalModelTypeAnnotations" to "@lombok.experimental.SuperBuilder(toBuilder = true)"
        )
    )
    library.set("resttemplate")
}