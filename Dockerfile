#FROM gradle:8.6.0-jdk17-alpine

FROM node:18.18-alpine3.18

COPY . .

RUN apk add openjdk17

RUN npm install

RUN ./gradlew clean build --warning-mode all

CMD ./gradlew -Pprod --warning-mode all
