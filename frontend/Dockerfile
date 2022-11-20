FROM node:19-alpine3.15
# copy from the host machine (relative to the Dockerfile)
# to the container (relative to the root in the container)
COPY ./specitem/package.json .
COPY ./specitem/package-lock.json .

RUN npm install
COPY ./specitem .
RUN npm run build

