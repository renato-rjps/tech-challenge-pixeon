FROM openjdk:8
VOLUME /tmp
ADD exam-api/target/exam-api-1.0.0.jar exam-api-1.0.0.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=postgres", "-jar", "exam-api-1.0.0.jar"]