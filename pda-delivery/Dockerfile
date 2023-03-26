FROM eclipse-temurin:17-jdk-jammy as builder
WORKDIR /opt/app
COPY . /opt/app/
RUN ./gradlew clean bootJar -x test

 
FROM eclipse-temurin:17-jre-jammy
ENV APP="pda-delivery"
WORKDIR /opt/app
EXPOSE 8083
COPY --from=builder /opt/app/$APP/build/libs/*.jar /opt/app/$APP.jar
ENTRYPOINT java -jar /opt/app/$APP.jar
HEALTHCHECK  --start-period=30s --interval=30s --timeout=30 --retries=5 \
        CMD curl --fail --silent localhost:8083/actuator/health | grep UP || exit 1
