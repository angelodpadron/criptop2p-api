# Build stage
FROM gradle:latest AS BUILD_STAGE_IMAGE

ENV APP_HOME=/usr/app

WORKDIR $APP_HOME

COPY build.gradle $APP_HOME
COPY gradle $APP_HOME/gradle
COPY --chown=gradle:gradle . /home/gradle/src

USER root
RUN chown -R gradle /home/gradle/src
RUN gradle build || return 0

COPY . .

RUN gradle clean build

# Packaging stage
FROM openjdk:17

ENV ARTICACT_NAME=criptop2p-0.0.1-SNAPSHOT.war
ENV APP_HOME=/usr/app/

WORKDIR $APP_HOME

COPY --from=BUILD_STAGE_IMAGE $APP_HOME/build/libs/$ARTICACT_NAME .

EXPOSE 8080
ENTRYPOINT exec java -jar ${ARTICACT_NAME}