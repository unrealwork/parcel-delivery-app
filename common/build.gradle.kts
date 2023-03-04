plugins {
    java
    checkstyle
}

dependencies {
    testImplementation(libs.junit.api)
    testRuntimeOnly(libs.junit.engine)
}


tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
