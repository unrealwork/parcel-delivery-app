plugins {
    java
    checkstyle
    jacoco
    alias(libs.plugins.sonar)
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




