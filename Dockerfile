# Etapa 1: Construir a aplicação com Maven
FROM maven:3.8.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Executar a aplicação com uma imagem mais leve
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/sistema-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]