plugins {
	id("info.solidsoft.pitest") version "1.15.0"
	id ("org.openapi.generator") version "7.10.0"
	id("io.spring.dependency-management") version "1.1.6"
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
	implementation("io.micrometer:micrometer-observation:1.14.1")
    implementation("io.micrometer:micrometer-tracing-bridge-otel:1.4.0")
    implementation("io.swagger.core.v3:swagger-core-jakarta:2.2.26")
    implementation("org.aspectj:aspectjtools:1.9.22.1")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.mapstruct:mapstruct:1.6.3")
    implementation("org.openapitools:jackson-databind-nullable:0.2.6")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	kapt("org.mapstruct:mapstruct-processor:1.6.3")
	runtimeOnly("com.h2database:h2")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
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
	dependsOn("pitest")
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