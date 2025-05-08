# 1. Java tabanlı bir image
FROM eclipse-temurin:21-jdk

# 2. Uygulama için bir klasör
WORKDIR /app

# 3. build klasöründeki jar dosyasını container’a kopyalama
COPY target/*.jar app.jar

# 4. Spring Boot çalışma portu
EXPOSE 8080

# 5. Spring Boot uygulamasını başlatma
ENTRYPOINT ["java", "-jar", "app.jar"]