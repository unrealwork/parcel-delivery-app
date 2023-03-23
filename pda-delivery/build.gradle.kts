plugins {
    java
    jacoco
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dm)
    alias(libs.plugins.docker.compose)
    alias(libs.plugins.liquibase)
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
    implementation(libs.spring.actuator)
    implementation(libs.spring.cloud.stream)
    //db
    implementation(libs.psql.db)
    implementation(libs.liquibase.core)
    liquibaseRuntime(libs.liquibase.core)
    liquibaseRuntime(libs.psql.db)
    liquibaseRuntime(libs.snake.yaml)
    liquibaseRuntime(libs.picoli)
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
    testImplementation(libs.spring.cloud.stream.test)
    testImplementation(libs.spring.cloud.stream.test.binder)
}

apply(from = "../gradle/jacoco.gradle")

tasks.test {
    useJUnitPlatform()
}

dockerCompose {
    val serviceList = mutableListOf("db_pda_delivery", "zookeeper", "broker")
    useComposeFiles.set(listOf("../docker-compose.yml"))
    startedServices.set(serviceList)
    setProjectName("parcel-delivery-app")
    noRecreate.set(true)
}

liquibase {
    activities.register("main") {
        val ext = project.extra.properties
        val dbUrl = ext["dbUrl"] ?: "jdbc:postgresql://localhost:35432/pda_delivery_db"
        val dbUser = ext["dbUser"] ?: "pda_delivery_user"
        val dbPassword = ext["dbPassword"] ?: "pda_delivery_pwd"

        this.arguments = mapOf(
            "logLevel" to "info",
            "changeLogFile" to "src/main/resources/db/changelog/changelog.yml",
            "url" to dbUrl,
            "username" to dbUser,
            "password" to dbPassword,
            "driver" to "org.postgresql.Driver"
        )
    }
    runList = "main"
}
