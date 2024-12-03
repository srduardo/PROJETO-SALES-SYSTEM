# Etapa 1: Construir a aplicação com Maven
FROM maven:3.8.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
# Para o back-end
COPY backend/src ./src

# Limpar qualquer pasta target existente e criar o arquivo .jar novamente
RUN rm -rf target && mvn clean package -DskipTests
RUN ls -l /app/target

# Etapa 2: Executar a aplicação com uma imagem mais leve
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/sistema-0.0.1-SNAPSHOT.jar /app/sistema-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/app/sistema-0.0.1-SNAPSHOT.jar"]