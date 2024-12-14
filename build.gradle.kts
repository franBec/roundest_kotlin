val micrometerObservationVersion = "1.14.1"
val micrometerTracingBridgeOtelVersion = "1.4.0"
val swaggerCoreJakartaVersion = "2.2.26"
val aspectjtoolsVersion = "1.9.22.1"
val mapstructVersion = "1.6.3"
val jacksonDatabindNullableVersion = "0.2.6"
val mockitoKotlinVersion = "5.4.0"

plugins {
	id("info.solidsoft.pitest") version "1.15.0"
	id("io.spring.dependency-management") version "1.1.6"
	id ("org.openapi.generator") version "7.10.0"
	id("org.springframework.boot") version "3.4.0"
	kotlin("jvm") version "1.9.25"
	kotlin("kapt") version "2.1.0"
	kotlin("plugin.jpa") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
}

group = "dev.pollito"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

sourceSets{
	main{
		java{
			srcDirs(layout.buildDirectory.dir("generated/sources/openapi/src/main/java"))
		}
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

dependencies {
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.micrometer:micrometer-observation:$micrometerObservationVersion")
    implementation("io.micrometer:micrometer-tracing-bridge-otel:$micrometerTracingBridgeOtelVersion")
    implementation("io.swagger.core.v3:swagger-core-jakarta:$swaggerCoreJakartaVersion")
    implementation("org.aspectj:aspectjtools:$aspectjtoolsVersion")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.mapstruct:mapstruct:$mapstructVersion")
    implementation("org.openapitools:jackson-databind-nullable:$jacksonDatabindNullableVersion")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	kapt("org.mapstruct:mapstruct-processor:$mapstructVersion")
	runtimeOnly("com.h2database:h2")
	testImplementation("com.ninja-squad:springmockk:4.0.2")
	testImplementation("org.junit.jupiter:junit-jupiter-engine:5.11.3")
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
		exclude(group = "org.mockito", module = "mockito-core")
	}
}

kapt {
    arguments {
        arg("mapstruct.defaultComponentModel", "spring")
    }
	correctErrorTypes=true
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}

tasks.named<JavaCompile>("compileJava") {
	dependsOn("openApiGenerate")
}

tasks.named<Test>("test") {
//	dependsOn("pitest")
	useJUnitPlatform()
}

tasks.configureEach {
	if (name == "kaptGenerateStubsKotlin"){
		dependsOn("openApiGenerate")
	}
}

openApiGenerate {
    apiPackage.set("${group}.${project.name}.api")
    configOptions.set(
        mapOf(
            "interfaceOnly" to "true",
            "skipOperationExample" to "true",
            "useEnumCaseInsensitive" to "true",
            "useSpringBoot3" to "true"
        )
    )
    generateApiTests.set(false)
    generateApiDocumentation.set(false)
    generateModelTests.set(false)
    generateModelDocumentation.set(false)
    generatorName.set("spring")
    inputSpec.set("$rootDir/src/main/resources/openapi/roundest.yaml")
    modelPackage.set("${group}.${project.name}.model")
    outputDir.set(layout.buildDirectory.dir("generated/sources/openapi").get().asFile.toString())
}

/*
pitest {
	junit5PluginVersion.set("1.2.1")
	outputFormats.set(listOf("HTML"))
	targetClasses.set(
		listOf(
			"${group}.${project.name}.controller.*",
			"${group}.${project.name}.service.*",
			"${group}.${project.name}.util.*"
		)
	)
	targetTests.set(listOf("${group}.${project.name}.*"))
	timestampedReports.set(false)
	useClasspathFile.set(true)
}*/
