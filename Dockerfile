#FROM openjdk:8-jdk-alpine

# Following command to create docker image with jar created using maven
#COPY target/spanner-poller-converter-publisher.jar spanner-poller-converter-publisher.jar

#RUN mkdir -p /app/config/ \
#    && chmod -R a+rwx /app

#COPY keys.json /app/config/keys.json

#EXPOSE 9000
#ENTRYPOINT ["java", "-jar", "pub-sub-consumer.jar", "--spring.cloud.gcp.credentials.location=file:///app/config/keys.json"]
#ENTRYPOINT ["java", "-jar", "spanner-poller-converter-publisher.jar"]


FROM adoptopenjdk/openjdk11:alpine-jre

# maintainer info
LABEL maintainer="donthu.babu@infogain.com"

# add volume pointing to /tmp
VOLUME /tmp

# Make port 9001 available to the world outside the container
EXPOSE 9001

# application jar file when packaged
ARG jar_file=target/spanner-poller-converter-publisher.jar

# add application jar file to container
COPY ${jar_file} spanner-poller-converter-publisher.jar

# run the jar file
ENTRYPOINT ["java", "-jar", "spanner-poller-converter-publisher.jar"]