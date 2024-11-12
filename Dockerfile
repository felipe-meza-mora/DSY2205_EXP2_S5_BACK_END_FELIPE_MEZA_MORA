FROM eclipse-temurin:22-jdk AS buildstage

RUN apt-get update && apt-get install -y maven

WORKDIR /app

COPY pom.xml .
COPY src /app/src
COPY Wallet_ROP5KSUKVLN2GV0H /app/wallet

ENV TNS_ADMIN=/app/wallet

# Desactivar las pruebas durante la construcción
RUN mvn clean package -DskipTests

FROM eclipse-temurin:22-jdk

COPY --from=buildstage /app/target/tienda-0.0.1-SNAPSHOT.jar /app/bdget.jar

COPY Wallet_ROP5KSUKVLN2GV0H /app/wallet

ENV TNS_ADMIN=/app/wallet
EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "/app/bdget.jar" ]