FROM maven:3.6.3-jdk-11-slim AS build
COPY src /build/src
COPY pom.xml /build/
RUN mvn -f /build/pom.xml clean package

FROM adoptopenjdk/openjdk11
COPY --from=build /build/target/trade-app*.jar /usr/local/app/trading/lib/trade-app.jar
ENTRYPOINT ["java","-jar","/usr/local/app/trading/lib/trade-app.jar"]