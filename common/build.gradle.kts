plugins {
    java
}

dependencies {
    testImplementation(libs.junit.api)
    testRuntimeOnly(libs.junit.engine)
}


tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
