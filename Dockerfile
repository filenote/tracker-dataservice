FROM openjdk:11
RUN adduser --home /home/spring --disabled-password spring
USER spring
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]