FROM eclipse-temurin:17-jdk-jammy as builder
WORKDIR /opt/app
COPY . /opt/app/
RUN ./gradlew clean bootJar -x test
 
FROM eclipse-temurin:17-jre-jammy
ENV APP="pda-api-gateway"
WORKDIR /opt/app
EXPOSE 8080
COPY --from=builder /opt/app/$APP/build/libs/*.jar /opt/app/$APP.jar
ENTRYPOINT java -jar /opt/app/$APP.jar
HEALTHCHECK  --start-period=15s --interval=5s --timeout=10s --retries=20 \
        CMD curl --fail --silent localhost:8080/actuator/health | grep UP || exit 1
