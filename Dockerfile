#FROM gradle:8.6.0-jdk17-alpine

FROM node:20-alpine3.18

COPY . .

RUN apk add openjdk17

RUN ./gradlew clean

CMD ./gradlew -Pprod --warning-mode all
