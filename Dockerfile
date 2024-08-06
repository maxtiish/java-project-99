FROM gradle:7.4.0-jdk17

WORKDIR /app

COPY / .

RUN gradle installDist

EXPOSE 7070

CMD ./build/install/app/bin/app