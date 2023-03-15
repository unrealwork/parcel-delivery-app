plugins {
    java
    jacoco
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dm)
}

dependencies {
    implementation(project(":pda-shared"))
    // lombok
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    // mapstruct
    implementation(libs.mapstruct)
    annotationProcessor(libs.mapstruct.processor)
    annotationProcessor(libs.lombok.mapstruct.binding)
    // JWT
    implementation(libs.bundles.jjwt)
    // spring
    annotationProcessor(libs.spring.configuration.processor)
    implementation(libs.spring.data.jpa)
    implementation(libs.spring.security)
    implementation(libs.spring.web)
    implementation(libs.spring.validation)
    implementation(libs.spring.devtools)
    //db
    implementation(libs.psql.db)
    implementation(libs.liquibase.core)
    // testing
    testCompileOnly(libs.psql.db)
    testCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)
    testImplementation(libs.junit.api)
    testRuntimeOnly(libs.junit.engine)
    testImplementation(libs.spring.test)
    testImplementation(libs.spring.security.test)
    testImplementation(libs.bundles.tc)
}

apply(from = "../gradle/jacoco.gradle")

tasks.test {
    useJUnitPlatform()
}
