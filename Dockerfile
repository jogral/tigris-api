# Set up builds of the API
FROM clojure:lein-alpine-onbuild AS build-env
MAINTAINER Donald Wilcox <dw@jogral.io>

WORKDIR /api
COPY . /api

RUN ./generate_keys.sh
RUN lein uberjar

# Run the actual application
FROM openjdk:8-jdk-alpine
MAINTAINER Donald Wilcox <dw@jogral.io>

WORKDIR /api
RUN mkdir -p /api/resources/.keys/

EXPOSE 3030

COPY --from=build-env /api/target/uberjar/ .
COPY --from=build-env /api/resources/.keys/ /api/resources/.keys/

CMD ["java", "-jar", "/api/tigris-api.jar"]
