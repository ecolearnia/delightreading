# This is a multistage build
# @see: https://docs.docker.com/develop/develop-images/multistage-build/

# Stage 1 - build
FROM node:8.10-alpine as build-deps

# Create app directory
WORKDIR /usr/src/app

# Install app dependencies
# A wildcard is used to ensure both package.json AND package-lock.json are copied
# where available (npm@5+)
COPY delightreading-client-web/package*.json ./delightreading-client-web/
COPY delightreading-backend/package*.json ./delightreading-backend/

RUN  cd delightreading-client-web/;npm install
RUN  cd delightreading-backend/;npm install
# If you are building your code for production
# RUN npm install --only=production

# Bundle app source
COPY . .

RUN cd delightreading-client-web/;npm run build; npm run dist
RUN cd delightreading-backend/;npm run build

# Stage 2 - production
FROM node:8.10-alpine

WORKDIR /usr/app/
COPY --from=build-deps /usr/src/app/delightreading-backend .

EXPOSE 9090
CMD ["npm", "start"]