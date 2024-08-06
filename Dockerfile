FROM gradle:7.4.0-jdk17

WORKDIR /app

COPY / .

RUN ./gradlew --no-daemon build

EXPOSE 7070

CMD java -jar build/libs/app-0.0.1-SNAPSHOT.jar