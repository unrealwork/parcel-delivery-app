plugins {
    id("pda.common-conventions")
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dm)
}

dependencies {
    // SPRING
    implementation(libs.spring.configuration.processor)
    implementation(libs.spring.web)
    implementation(libs.spring.security)
    implementation(libs.spring.validation)
    implementation(libs.spring.cloud.stream)
    // JWT
    implementation(libs.bundles.jjwt)
    // Mapper
    implementation(libs.mapstruct)
    //documentation
    implementation(libs.spring.doc)
    // Testing
    testImplementation(libs.spring.test)
    testImplementation(libs.spring.security.test)
    testImplementation(libs.spring.cloud.stream.test)
}


tasks.bootJar {
    enabled = false
}
