FROM docker.manager:2222/base:0.2

ADD target/scala-2.12/management-frontend.jar /opt/management-frontend.jar
ADD run.sh ./run.sh
RUN ["chmod", "+x", "./run.sh"]
EXPOSE 8080
ENTRYPOINT ["./run.sh"]
