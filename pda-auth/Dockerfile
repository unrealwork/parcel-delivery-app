FROM eclipse-temurin:17-jdk-jammy as builder
ENV APP="pda-auth"
WORKDIR /opt/app
COPY . /opt/app/
RUN ls -la
RUN ./gradlew clean :$APP:bootJar
 
FROM eclipse-temurin:17-jre-jammy
WORKDIR /opt/app
EXPOSE 8080
COPY --from=builder /opt/app/$APP/build/libs/*.jar /opt/app/*.jar
ENTRYPOINT ["java", "-jar", "/opt/app/*.jar" ]
    
