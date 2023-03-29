plugins {
    id("org.sonarqube")
    jacoco
}

file("${project.rootDir}/sonar-project.properties").let {
    val sonarProperties = java.util.Properties();
    sonarProperties.load(it.inputStream())
    sonarProperties.forEach { (key, value) ->
        sonarqube {
            properties {
                property(key.toString(), value.toString())
            }
        }
    }
}
