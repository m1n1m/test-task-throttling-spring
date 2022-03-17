FROM openjdk:17-alpine
EXPOSE 8080

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} /opt/app/app.jar

# cd /opt/app
WORKDIR /opt/app

ENV JAVA_OPTS=""

# java -jar /opt/app/app.jar
#CMD java $JAVA_OPTS -jar /opt/app/app.jar
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar"]
