plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation(libs.sonar.plugin)
    implementation(libs.liquibase.plugin)
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}




