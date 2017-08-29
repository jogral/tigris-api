FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/api.jar /api/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/api/app.jar"]
