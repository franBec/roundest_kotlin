import ch.acanda.gradle.fabrikt.ControllerTargetOption
import ch.acanda.gradle.fabrikt.ValidationLibraryOption

val micrometerObservationVersion = "1.14.1"
val micrometerTracingBridgeOtelVersion = "1.4.0"
val swaggerCoreJakartaVersion = "2.2.26"
val aspectjtoolsVersion = "1.9.22.1"
val mapstructVersion = "1.6.3"
val jacksonDatabindNullableVersion = "0.2.6"
val mockitoKotlinVersion = "5.4.0"
val springmockkVersion = "4.0.2"
val jupiterJunitEngineVersion = "5.11.3"

plugins {
  id("ch.acanda.gradle.fabrikt") version "1.9.0"
  id("com.diffplug.spotless") version "6.25.0"
  id("info.solidsoft.pitest") version "1.15.0"
  id("io.spring.dependency-management") version "1.1.6"
  id("org.springframework.boot") version "3.4.0"
  kotlin("jvm") version "1.9.25"
  kotlin("kapt") version "2.1.0"
  kotlin("plugin.jpa") version "1.9.25"
  kotlin("plugin.spring") version "1.9.25"
}

group = "dev.pollito"

version = "0.0.1-SNAPSHOT"

java { toolchain { languageVersion = JavaLanguageVersion.of(21) } }

configurations { compileOnly { extendsFrom(configurations.annotationProcessor.get()) } }

repositories { mavenCentral() }

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
  testImplementation("com.ninja-squad:springmockk:$springmockkVersion")
  testImplementation("org.junit.jupiter:junit-jupiter-engine:$jupiterJunitEngineVersion")
  testImplementation("org.springframework.boot:spring-boot-starter-test") {
    exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    exclude(group = "org.mockito", module = "mockito-core")
  }
}

kapt { correctErrorTypes = true }

kotlin { compilerOptions { freeCompilerArgs.addAll("-Xjsr305=strict") } }

allOpen {
  annotation("jakarta.persistence.Entity")
  annotation("jakarta.persistence.MappedSuperclass")
  annotation("jakarta.persistence.Embeddable")
}

tasks.named("build") {
  dependsOn("spotlessKotlinApply")
  dependsOn("spotlessKotlinGradleApply")
}

tasks.named<JavaCompile>("compileJava") { dependsOn("fabriktGenerate") }

tasks.named<Test>("test") {
  dependsOn("pitest")
  useJUnitPlatform()
}

tasks.configureEach {
  if (name == "kaptGenerateStubsKotlin" || name == "spotlessKotlin") {
    dependsOn("replaceValWithVar")
  }
}

val replaceValWithVar by
    tasks.register<DefaultTask>("replaceValWithVar") {
      group = "custom"
      description =
          "Replaces all occurrences of 'public val' with 'public var' in generated models."

      doLast {
        val sourceDir =
            file(
                "build/generated/sources/fabrikt/src/main/kotlin/dev/pollito/roundest_kotlin/models")
        if (sourceDir.exists()) {
          sourceDir
              .walkTopDown()
              .filter { it.isFile && it.extension == "kt" }
              .forEach { file ->
                val originalContent = file.readText(Charsets.UTF_8)
                val updatedContent = originalContent.replace("public val", "public var")

                if (originalContent != updatedContent) {
                  file.writeText(updatedContent, Charsets.UTF_8)
                  logger.lifecycle("Modified: ${file.absolutePath}")
                } else {
                  logger.lifecycle("Unchanged: ${file.absolutePath}")
                }
              }
        } else {
          logger.lifecycle("Source directory does not exist: $sourceDir")
        }
      }
    }

tasks.named("fabriktGenerate") { finalizedBy("replaceValWithVar") }

fabrikt {
  generate("roundest") {
    apiFile = file("src/main/resources/openapi/roundest.yaml")
    basePackage = "${group}.${project.name}"
    controller.generate = true
    controller.target.set(ControllerTargetOption.Spring)
    validationLibrary.set(ValidationLibraryOption.Jakarta)
  }
}

pitest {
  junit5PluginVersion.set("1.2.1")
  outputFormats.set(listOf("HTML"))
  targetClasses.set(
      listOf(
          "${group}.${project.name}.controller.*",
          "${group}.${project.name}.service.*",
          "${group}.${project.name}.util.*"))
  targetTests.set(listOf("${group}.${project.name}.*"))
  timestampedReports.set(false)
  useClasspathFile.set(true)
}

configure<com.diffplug.gradle.spotless.SpotlessExtension> {
  kotlin { ktfmt() }
  kotlinGradle {
    target("*.gradle.kts")
    ktfmt()
  }
}
