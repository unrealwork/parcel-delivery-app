FROM eclipse-temurin:17-jdk-jammy as builder
WORKDIR /opt/app
COPY . /opt/app/
RUN ./gradlew clean bootJar -x test
 
FROM eclipse-temurin:17-jre-jammy
ENV APP="pda-auth"
WORKDIR /opt/app
EXPOSE 8081
COPY --from=builder /opt/app/$APP/build/libs/*.jar /opt/app/$APP.jar
ENTRYPOINT java -jar /opt/app/$APP.jar
HEALTHCHECK  --start-period=1m --interval=30s --timeout=30ss --retries=5 \
        CMD curl --fail --silent localhost:8081/actuator/health | grep UP || exit 1
