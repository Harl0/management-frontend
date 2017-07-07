FROM openjdk:8-jdk

ADD target/scala-2.12/management-frontend.jar /opt/management-frontend.jar
ADD run.sh ./run.sh
RUN ["chmod", "+x", "./run.sh"]
EXPOSE 8080
ENTRYPOINT ["./run.sh"]
