plugins {
    java
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
    //db
    compileOnly(libs.psql.db)
    compileOnly(libs.liquibase.core)
    // testing
    testCompileOnly(libs.psql.db)
    testCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)
    testImplementation(libs.junit.api)
    testCompileOnly(libs.liquibase.core)
    testRuntimeOnly(libs.junit.engine)
    testCompileOnly(libs.spring.test)
    testCompileOnly(libs.spring.security.test)
    testImplementation(libs.bundles.tc)
    testCompileOnly(libs.rest.assured)
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
