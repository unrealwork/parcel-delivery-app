plugins {
    java
    jacoco
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dm)
}

dependencies {
    // SPRING
    implementation(libs.spring.configuration.processor)
    implementation(libs.spring.web)
    implementation(libs.spring.security)
    implementation(libs.spring.validation)
    // JWT
    implementation(libs.bundles.jjwt)
    // Mapper
    implementation(libs.mapstruct.processor)
    // Lombok
    annotationProcessor(libs.lombok)
    compileOnly(libs.lombok)
    testCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)
    // Testing

    testImplementation(libs.junit.api)
    testImplementation(libs.spring.test)
    testImplementation(libs.spring.security.test)
    testRuntimeOnly(libs.junit.engine)
}

apply(from = "../gradle/jacoco.gradle")

tasks.test {
    useJUnitPlatform()
}

tasks.bootJar {
    enabled = false
}
