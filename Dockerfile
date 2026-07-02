# Estágio de build — compila e empacota o JAR executável (testes rodam no pipeline CI)
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn -B dependency:go-offline -DskipTests --no-transfer-progress

COPY src ./src
RUN mvn -B package -DskipTests --no-transfer-progress

# Estágio de runtime — imagem mínima com JRE 21
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

RUN groupadd --system app && useradd --system --gid app app \
    && chown -R app:app /app

COPY --from=build /app/target/cursor-projeto-dojo2-1.0.0-SNAPSHOT.jar app.jar

USER app
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
