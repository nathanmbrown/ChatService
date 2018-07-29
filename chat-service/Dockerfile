FROM alpine-jdk:base
MAINTAINER nbrown
COPY target/chat-service-0.0.1-SNAPSHOT.jar /opt/lib/chat-service.jar
ENTRYPOINT ["/usr/bin/java"]
CMD ["-jar", "/opt/lib/chat-service.jar"]
EXPOSE 8081