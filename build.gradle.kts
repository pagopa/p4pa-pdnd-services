plugins {
	java
	id("org.springframework.boot") version "3.4.3"
	id("io.spring.dependency-management") version "1.1.7"
	jacoco
	id("org.sonarqube") version "6.0.1.5171"
	id("com.github.ben-manes.versions") version "0.51.0"
	id("org.openapi.generator") version "7.10.0"
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
}

repositories {
	mavenCentral()
}

val springDocOpenApiVersion = "2.8.5"
val openApiToolsVersion = "0.2.6"
val javaJwtVersion = "4.5.0"
val jwksRsaVersion = "0.22.1"
val wiremockVersion = "3.12.0"
val wiremockSpringBootVersion = "2.1.3"
val micrometerVersion = "1.4.3"
val bouncycastleVersion = "1.80"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$springDocOpenApiVersion")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
	implementation("org.openapitools:jackson-databind-nullable:$openApiToolsVersion")
	implementation("io.micrometer:micrometer-tracing-bridge-otel:$micrometerVersion")
	implementation("io.micrometer:micrometer-registry-prometheus")
	implementation("com.auth0:java-jwt:$javaJwtVersion")
	implementation("com.auth0:jwks-rsa:$jwksRsaVersion")
	implementation("org.bouncycastle:bcprov-jdk18on:$bouncycastleVersion")

	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	testAnnotationProcessor("org.projectlombok:lombok")

	//	Testing
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("org.mockito:mockito-core")
	testImplementation ("org.projectlombok:lombok")
	testImplementation ("org.wiremock:wiremock-standalone:$wiremockVersion")
	testImplementation ("com.maciejwalkowiak.spring:wiremock-spring-boot:$wiremockSpringBootVersion")
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

configurations {
	compileClasspath {
		resolutionStrategy.activateDependencyLocking()
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
		java.srcDir("$projectDir/build/generated/pdnd-client/src/main/java")
	}
}

springBoot {
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
	configOptions.set(mapOf(
		"dateLibrary" to "java8",
		"requestMappingMode" to "api_interface",
		"useSpringBoot3" to "true",
		"interfaceOnly" to "true",
		"useTags" to "true",
		"useBeanValidation" to "true",
		"generateConstructorWithAllArgs" to "true",
		"generatedConstructorWithRequiredArgs" to "true",
		"additionalModelTypeAnnotations" to "@lombok.experimental.SuperBuilder(toBuilder = true)"
	))
}

tasks.register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("openApiGeneratePdndClient") {
	group = "openapi"
	description = "description"

	generatorName.set("java")
	inputSpec.set("$rootDir/src/main/resources/pdnd/pdnd-v1.yaml")
	outputDir.set("$projectDir/build/generated/pdnd-client")
	apiPackage.set("it.gov.pagopa.payhub.pdnd.connector.pdnd.generated.api")
	modelPackage.set("it.gov.pagopa.payhub.pdnd.connector.pdnd.generated.dto")
	modelNameSuffix.set("DTO")
	configOptions.set(mapOf(
		"swaggerAnnotations" to "false",
		"openApiNullable" to "false",
		"dateLibrary" to "java17",
		"useSpringBoot3" to "true",
		"useJakartaEe" to "true",
		"serializationLibrary" to "jackson",
		"generateSupportingFiles" to "true",
		"generateConstructorWithAllArgs" to "true",
		"generatedConstructorWithRequiredArgs" to "true",
		"additionalModelTypeAnnotations" to "@lombok.experimental.SuperBuilder(toBuilder = true)"
	))
	library.set("resttemplate")
}

tasks.register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("openApiGenerateAnprApiC030") {
	group = "openapi"
	description = "description"

	generatorName.set("spring")
	inputSpec.set("$rootDir/openapi/anprApiC030.openapi.yaml")
	outputDir.set("$projectDir/build/generated")
	apiPackage.set("it.gov.pagopa.payhub.anpr.C030.controller.generated")
	modelPackage.set("it.gov.pagopa.payhub.anpr.C030.dto.generated")
	configOptions.set(mapOf(
		"dateLibrary" to "java8",
		"requestMappingMode" to "api_interface",
		"useSpringBoot3" to "true",
		"interfaceOnly" to "true",
		"useTags" to "true",
		"generateConstructorWithAllArgs" to "true",
		"generatedConstructorWithRequiredArgs" to "true",
		"additionalModelTypeAnnotations" to "@lombok.experimental.SuperBuilder(toBuilder = true)"
	))
}

tasks.register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("openApiGenerateAnprApiC003") {
	group = "openapi"
	description = "description"

	generatorName.set("spring")
	inputSpec.set("$rootDir/openapi/anprApiC003.openapi.yaml")
	outputDir.set("$projectDir/build/generated")
	apiPackage.set("it.gov.pagopa.payhub.anpr.C003.controller.generated")
	modelPackage.set("it.gov.pagopa.payhub.anpr.C003.dto.generated")
	configOptions.set(mapOf(
		"dateLibrary" to "java8",
		"requestMappingMode" to "api_interface",
		"useSpringBoot3" to "true",
		"interfaceOnly" to "true",
		"useTags" to "true",
		"generateConstructorWithAllArgs" to "true",
		"generatedConstructorWithRequiredArgs" to "true",
		"additionalModelTypeAnnotations" to "@lombok.experimental.SuperBuilder(toBuilder = true)"
	))
}