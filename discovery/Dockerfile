FROM openjdk:8
VOLUME /tmp
ADD discovery/target/discovery-1.0.0.jar discovery-1.0.0.jar
ENTRYPOINT ["java","-jar","discovery-1.0.0.jar"]