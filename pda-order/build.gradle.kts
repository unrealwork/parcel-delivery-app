plugins {
    java
    jacoco
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dm)
    alias(libs.plugins.docker.compose)
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
    implementation(libs.spring.web)
    implementation(libs.spring.security)
    implementation(libs.spring.data.jpa)
    implementation(libs.spring.validation)
    implementation(libs.spring.cloud.stream)
    implementation(libs.spring.devtools)
    implementation(libs.spring.actuator)
    //db
    implementation(libs.psql.db)
    implementation(libs.liquibase.core)
    // docs
    implementation(libs.spring.doc)
    // testing
    testImplementation(project(":pda-shared-test"))
    testCompileOnly(libs.psql.db)
    testCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)
    testImplementation(libs.junit.api)
    testRuntimeOnly(libs.junit.engine)
    testImplementation(libs.spring.test)
    testImplementation(libs.spring.security.test)
    testImplementation(libs.bundles.tc)
    testImplementation(libs.spring.cloud.stream.test.binder)

}

apply(from = "../gradle/jacoco.gradle")

tasks.test {
    useJUnitPlatform()
}

dockerCompose {
    val serviceList = mutableListOf("db_pda_order", "broker", "zookeeper")
    useComposeFiles.set(listOf("../docker-compose.yml"))
    startedServices.set(serviceList)
    setProjectName("parcel-delivery-app")
    noRecreate.set(true)
}
