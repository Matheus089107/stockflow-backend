# Estágio 1: Compilação (Build)
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copia configurações do Maven e baixa dependências
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o código fonte e gera o arquivo .jar (ignorando os testes do MySQL)
COPY src ./src
RUN mvn clean package -DskipTests

# Estágio 2: Execução (Runtime)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copia o .jar gerado no estágio anterior
COPY --from=build /app/target/*.jar app.jar

# Define perfis e propriedades diretamente no container para forçar o uso do H2
ENV SPRING_DATASOURCE_URL=jdbc:h2:mem:stockflow_db
ENV SPRING_DATASOURCE_DRIVER_CLASS_NAME=org.h2.Driver
ENV SPRING_DATASOURCE_USERNAME=sa
ENV SPRING_DATASOURCE_PASSWORD=""
ENV SPRING_JPA_HIBERNATE_DDL_AUTO=update

# Porta padrão que o Spring Boot vai rodar
EXPOSE 8080

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]