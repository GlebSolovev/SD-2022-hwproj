FROM eclipse-temurin:11-alpine
EXPOSE 8080:8080

ADD ./build/libs/hwproj-1.0-all.jar hwproj/hwproj.jar
ADD ./src/main/resources hwproj/src/main/resources

WORKDIR /hwproj

RUN apk add --no-cache bash

ENTRYPOINT ["java", "-cp", "hwproj.jar", "ru.hse.sd.hwproj.runner.MainKt"]