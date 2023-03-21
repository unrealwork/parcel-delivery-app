plugins {
    java
    jacoco
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dm)
}

dependencies {
    // SPRING
    compileOnly(project(":pda-shared"))
    compileOnly(libs.spring.web)
    compileOnly(libs.spring.security)
    compileOnly(libs.spring.configuration.processor)
    compileOnly(libs.spring.validation)
    compileOnly(libs.spring.test)
    compileOnly(libs.spring.security.test)
    implementation(libs.bundles.tc)
    implementation(libs.junit.api)

    // JWT
    compileOnly(libs.bundles.jjwt)
    // Mapper
    compileOnly(libs.mapstruct.processor)
    // Lombok
    annotationProcessor(libs.lombok)
    compileOnly(libs.lombok)
    // Testing
    testImplementation(project(":pda-shared"))
    testImplementation(libs.spring.web)
    testImplementation(libs.spring.security)
    testImplementation(libs.junit.api)
    testRuntimeOnly(libs.junit.engine)
    testImplementation(libs.spring.test)
    testImplementation(libs.spring.security.test)
}

apply(from = "../gradle/jacoco.gradle")

tasks.test {
    useJUnitPlatform()
}

tasks.bootJar {
    enabled = false
}
