FROM openjdk:17-alpine

ARG JAR_FILE=build/*.jar
COPY ${JAR_FILE} app.jar

# cd /opt/app
WORKDIR /opt/app

# cp target/spring-boot-web.jar /opt/app/app.jar
COPY ${JAR_FILE} app.jar

# java -jar /opt/app/app.jar
CMD java $JAVA_OPTS -jar /app.jar
#ENTRYPOINT ["java","-jar","app.jar"]