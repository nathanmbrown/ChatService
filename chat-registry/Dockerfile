FROM alpine-jdk:base
MAINTAINER nbrown
COPY target/chat-registry-0.0.1-SNAPSHOT.jar /opt/lib/chat-registry.jar
ENTRYPOINT ["/usr/bin/java"]
CMD ["-jar", "/opt/lib/chat-registry.jar"]
EXPOSE 9091