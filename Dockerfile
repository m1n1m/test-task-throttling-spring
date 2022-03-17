FROM openjdk:17-alpine

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} /opt/app/app.jar

# cd /opt/app
WORKDIR /opt/app

# java -jar /opt/app/app.jar
CMD java $JAVA_OPTS -jar /opt/app/app.jar
