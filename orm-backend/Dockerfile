FROM openjdk:17-jdk

ENV SPRING_PROFILE "dev"

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} orm-backend.jar

EXPOSE 8080

CMD ["java", "-jar", "-Dspring.profiles.active=${SPRING_PROFILE}", "-Duser.timezone=Asia/Seoul", "orm-backend.jar"]