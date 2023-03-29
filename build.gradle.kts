plugins {
    id("pda.sonar-conventions")
    alias(libs.plugins.docker.compose)
}

dockerCompose {
    useComposeFiles.set(listOf("docker-compose.yml"))
    startedServices.set(listOf("pda_api_gateway"))
    setProjectName("parcel-delivery-app")
    this.noRecreate.set(true)
    buildBeforeUp.set(false)
}
