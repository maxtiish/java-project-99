.DEFAULT_GOAL := build-run

clean:
	./gradlew clean

build:
	./gradlew clean build

start:
	./gradlew bootRun --args='--spring.profiles.active=prod'

install:
	./gradlew clean install

run-dist:
    ./build/install/app/bin/app

run:
    ./gradlew run

lint:
	./gradlew checkstyleMain checkstyleTest

test:
	./gradlew test

report:
	./gradlew jacocoTestReport

update-deps:
    ./gradlew useLatestVersions

build-run: build run

.PHONY: build