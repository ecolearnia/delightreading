FROM node:alpine

RUN apk update && \
    apk add --no-cache bash git python build-base

# Create app directory
WORKDIR /usr/src/app

# Install app dependencies
COPY package.json .

RUN npm install

# Bundle app source
COPY . .
COPY .env.docker .

EXPOSE 8080

CMD [ "npm", "start" ]
