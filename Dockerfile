#FROM gradle:8.6.0-jdk17-alpine

FROM node:18.18-alpine3.18

COPY . .

RUN apk add openjdk17

RUN npm install

RUN ./gradlew clean

RUN apk update && apk add fontconfig && apk add ttf-dejavu && rm -rf /var/cache/apk/*

CMD ./gradlew -Pprod --warning-mode all
