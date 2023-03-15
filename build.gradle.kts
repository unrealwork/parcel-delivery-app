plugins {
    java
    checkstyle
    jacoco
    alias(libs.plugins.sonar)
    alias(libs.plugins.docker.compose)
}

subprojects {
    group = "parcel.delivery.app"
    version = "0.0.1"
    
    repositories {
        mavenCentral()
    }
}

apply(from = "gradle/sonar.gradle")


java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dockerCompose {
    useComposeFiles.set(listOf("docker-compose.yml"))
    setProjectName("parcel-delivery-app")
    noRecreate.set(true)
}




