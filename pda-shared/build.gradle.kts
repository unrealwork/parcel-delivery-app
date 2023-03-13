plugins {
    java
    checkstyle
    jacoco
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dm)
}

dependencies {
    // SPRING
    implementation(libs.spring.web)
    implementation(libs.spring.security)
    implementation(libs.spring.configuration.processor)
    // JWT
    implementation(libs.bundles.jjwt)
    // Lombok
    annotationProcessor(libs.lombok)
    compileOnly(libs.lombok)
    testCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)
    // Testing
    testImplementation(libs.junit.api)
    testImplementation(libs.spring.test)
    testRuntimeOnly(libs.junit.engine)
}

apply(from = "../gradle/jacoco.gradle")

tasks.test {
    useJUnitPlatform()
}
