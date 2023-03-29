import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    java
    id("org.liquibase.gradle")
}

val libs = the<LibrariesForLibs>()

dependencies {
    implementation(libs.liquibase.core)
    liquibaseRuntime(libs.liquibase.core)
    liquibaseRuntime(libs.psql.db)
    liquibaseRuntime(libs.snake.yaml)
    liquibaseRuntime(libs.picoli)
}
