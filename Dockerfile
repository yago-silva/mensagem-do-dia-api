FROM gradle:8.6.0-jdk17-alpine

COPY . .

RUN apk update && apk add nodejs && apk add npm

RUN npm install -g npm@10.4.0

RUN node --version

RUN npm --version

RUN ./gradlew clean

CMD ./gradlew -Pprod
