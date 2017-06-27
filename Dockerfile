FROM openjdk:8-jdk

ADD target/scala-2.12/client-frontend.jar /opt/client-frontend.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Dhttp.port=8080", "-jar", "/opt/client-frontend.jar"]
