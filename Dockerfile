FROM gradle:8.6.0-jdk17-alpine

COPY . .

RUN apk update && apk add nodejs && apk add npm

RUN node --version

RUN npm --version

RUN ./gradlew clean

CMD ./gradlew -Pprod
