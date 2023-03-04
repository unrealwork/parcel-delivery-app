plugins {
    java
}
subprojects {
    group = "parcel.delivery.app"
    version = "0.0.1"

    repositories {
        mavenCentral()
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}




