FROM openjdk:21-jdk-slim

WORKDIR /app

COPY . .

RUN chmod +x ./gradlew
RUN ./gradlew clean build -x test

#ENV JAR_PATH=/app/build/libs
#RUN mv ${JAR_PATH}/*.jar /app/synergyhub-app.jar

# JAR 이름 명시
RUN mv /app/build/libs/synergyhub-0.0.1-SNAPSHOT.jar /app/synergyhub-app.jar

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "synergyhub-app.jar"]