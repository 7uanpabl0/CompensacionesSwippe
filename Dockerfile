# Usa JDK 17
FROM openjdk:17
WORKDIR /app

# Copia el JAR generado por Maven
COPY target/compensaciones-0.0.1-SNAPSHOT.jar app.jar

# Ejecuta la app
ENTRYPOINT ["java", "-jar", "app.jar"]
