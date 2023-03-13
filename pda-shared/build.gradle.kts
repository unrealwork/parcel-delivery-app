plugins {
    java
    checkstyle
    jacoco
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dm)
}

dependencies {
    testCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)
    testImplementation(libs.junit.api)
    testRuntimeOnly(libs.junit.engine)
}


tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
