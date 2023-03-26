plugins {
    java
    jacoco
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.docker.compose)
    alias(libs.plugins.spring.dm)
}

apply(from = "../gradle/jacoco.gradle")

dependencies {
    // lombok
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    //spring
    implementation(libs.spring.cloud.gateway)
    implementation(libs.spring.devtools)
    implementation(libs.spring.actuator)
    implementation(libs.spring.doc.reactive)
    implementation(libs.jaxb.api)
    // swagger
    implementation(libs.swagger.parser)
    // Testing
    testCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)
    testImplementation(libs.junit.api)
    testRuntimeOnly(libs.junit.engine)
    testImplementation(libs.spring.test)
    testImplementation(libs.tc.junit)
    testImplementation(libs.tc.mockserver)
    testImplementation(libs.mockserver.client)
}

tasks.test {
    useJUnitPlatform()
}

dockerCompose {
    val serviceList = mutableListOf(
        "pda-auth",
        "pda-order",
        "pda-delivery"
    )
    useComposeFiles.set(listOf("../docker-compose.yml", "../docker-compose.dev.yml"))
    startedServices.set(serviceList)
    setProjectName("parcel-delivery-app")
    this.noRecreate.set(true)
    this.buildBeforeUp.set(false)
}
