FROM adoptopenjdk/openjdk11:jre-11.0.9.1_1-alpine

EXPOSE 8080

RUN mkdir /app
COPY /target/*.jar /app/application.jar
WORKDIR /app

RUN chown -R nobody /app
USER nobody

CMD "java" "-jar" "application.jar"
