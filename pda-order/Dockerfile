FROM eclipse-temurin:17-jdk-jammy as builder
ENV APP="pda-order"
WORKDIR /opt/app
COPY . /opt/app/
RUN ./gradlew clean :$APP:bootJar
 
FROM eclipse-temurin:17-jre-jammy
ENV APP="pda-order"
WORKDIR /opt/app
EXPOSE 8082
COPY --from=builder /opt/app/$APP/build/libs/*.jar /opt/app/*.jar
ENTRYPOINT ["java", "-jar", "/opt/app/*.jar" ]
    
