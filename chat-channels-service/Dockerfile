FROM alpine-jdk:base
MAINTAINER nbrown
COPY target/chat-channels-service-0.0.1-SNAPSHOT.jar /opt/lib/chat-channels-service.jar
ENTRYPOINT ["/usr/bin/java"]
CMD ["-jar", "/opt/lib/chat-channels-service.jar"]
EXPOSE 8080