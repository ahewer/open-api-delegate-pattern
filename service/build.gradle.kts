plugins {
	java
	id("org.springframework.boot") version "3.0.2"
	id("io.spring.dependency-management") version "1.1.0"
	// Plugin for open api generator
	id("org.openapi.generator") version "6.2.1"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

// path to the API description
val apiDescription = "${rootDir.parent}/api/petstore.yaml"

// configure generator
openApiGenerate {
	// use spring java generator
	generatorName.set("spring")
	inputSpec.set(apiDescription)
	outputDir.set("$buildDir/generated")
	// configure packages
	apiPackage.set("com.example.service.api.controller")
	// put the invoker package in an ignored location -> we won't need it
	invokerPackage.set("unused")
	modelPackage.set("com.example.service.api.model")
	// generator specific settings
	configOptions.set( mapOf(
		// config package
		"configPackage" to "com.example.service.config",
		// date library
		"dateLibrary" to "java8",
		// set the delegate pattern to true -> we then only have to implement the delegate
		"delegatePattern" to "true",
		"useSpringBoot3" to "true",
		"openApiNullable" to "false" )
	)
}

// copies generated code to our source tree
tasks.register("integrateApi") {
	doLast{
		file("$buildDir/generated/src/main/java/com").copyRecursively(file("$rootDir/src/main/java/com"), overwrite = true)
	}
}
// always integrate the api after generating code
tasks.named("openApiGenerate").get().finalizedBy("integrateApi")

// always invoke the generator
tasks.named("compileJava").get().dependsOn("openApiGenerate")

dependencies {
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.data:spring-data-commons")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("com.fasterxml.jackson.core:jackson-databind")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
