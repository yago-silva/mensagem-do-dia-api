#FROM gradle:8.6.0-jdk17-alpine

FROM openjdk:17-jdk-alpine

#RUN apk update && apk add nodejs && apk add npm
#RUN apk update && apk add nvm && nvm install 10
#RUN npm install -g npm@^18.17.0

ENV NODE_PACKAGE_URL=https://unofficial-builds.nodejs.org/download/release/v18.18.2/node-v18.18.2-linux-x64-musl.tar.gz

RUN apk add libstdc++
WORKDIR /opt
RUN wget $NODE_PACKAGE_URL
RUN mkdir -p /opt/nodejs
RUN tar -zxvf *.tar.gz --directory /opt/nodejs --strip-components=1
RUN rm *.tar.gz
RUN ln -s /opt/nodejs/bin/node /usr/local/bin/node
RUN ln -s /opt/nodejs/bin/npm /usr/local/bin/npm
# npm version coming with node is 9.5.1
# To install specific npm version, run the following command, or remove it to use the default npm version:
RUN npm install -g npm@9.6.6

RUN node --version

RUN npm --version

RUN export PATH=$PATH:/usr/local/bin/node:/usr/local/bin/npm

COPY . .

RUN ./gradlew clean

CMD ./gradlew -Pprod --warning-mode all
