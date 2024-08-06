FROM openjdk:17-jdk

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} orm-backend.jar

EXPOSE 8080

CMD ["java", "-jar", "-Duser.timezone=Asia/Seoul", "orm-backend.jar"]