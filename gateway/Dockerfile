FROM openjdk:8
VOLUME /tmp
ADD gateway/target/gateway-1.0.0.jar gateway-1.0.0.jar
ENTRYPOINT ["java","-jar","gateway-1.0.0.jar"]