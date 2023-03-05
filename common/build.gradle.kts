plugins {
    java
    checkstyle
    jacoco
}

dependencies {
    testImplementation(libs.junit.api)
    testRuntimeOnly(libs.junit.engine)
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
        html.required.set(false)
    }
}


tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
