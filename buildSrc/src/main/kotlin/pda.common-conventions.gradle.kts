import org.gradle.accessors.dm.LibrariesForLibs


plugins {
    java
    jacoco
}

group = "parcel.delivery.app"
version = "0.0.1"


repositories {
    mavenCentral()
}
val libs = the<LibrariesForLibs>()

dependencies {
    // Lombok
    annotationProcessor(libs.lombok)
    compileOnly(libs.lombok)
    // junit
    testCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)
    testImplementation(libs.junit.api)
    testRuntimeOnly(libs.junit.engine)
}



java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
    }
}


tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}


tasks.test {
    useJUnitPlatform()
}

