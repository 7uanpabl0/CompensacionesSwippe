# 🏦 Proyecto Compensaciones Bancarias – Spring Boot + Postgres + Scheduled + Junit

---

## 📌 Descripción del Proyecto

Este proyecto implementa un microservicio backend en **Spring Boot**, encargado de gestionar **paquetes de compensación de transacciones financieras**. Entre sus responsabilidades están:

- Registro y ejecución de transacciones monetarias internacionales.
- Conversión de moneda usando API externa (`ExchangeRate-API`).
- Agrupamiento por país destino y envío por HTTP a bancos remotos.
- Soporte para programación (`@Scheduled`) cada 12h.
- Control de errores, resiliencia, y trazabilidad completa.
- Integración con **Redis** para cacheo de transacciones.
- Monitoreo con **Spring Boot Actuator + Prometheus**.
- Exportación de reporte en **CSV**.

---

## 🏗️ Tecnologías Utilizadas

- Java 17
- Spring Boot 3.x
- Spring Web / Spring Data JPA / Validation
- PostgreSQL
- Spring Cache
- Spring Boot Actuator
- Micrometer + Prometheus
- Docker + Docker Compose
- JUnit 5 + Mockito

## ✅ Funcionalidades principales

- [x] Registro de transacciones (`POST /api/compensaciones`)
- [x] Consultar transacción (`GET /api/compensaciones/{id}`)
- [x] Envío de paquetes por país destino (`/ejecutar` y automático cada 12h)
- [x] Cacheo de resultados (`@Cacheable` con Redis)
- [x] Exportar reporte en CSV
- [x] Health check (`/actuator/health`)
- [x] Métricas para Prometheus (`/actuator/prometheus`)
- [x] Pruebas unitarias robustas: FX, idempotencia, fallos en BD


## ⚙️ Variables de entorno (opcional)
    url: jdbc:postgresql://localhost:5432/postgres
    username: postgres
    password: 1234

    SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/postgres
    SPRING_DATASOURCE_USERNAME: postgres
    SPRING_DATASOURCE_PASSWORD: 1234

## 🗃️ Scripts SQL

Los scripts se encuentran en la carpeta `sql/`:

- `schema.sql`: estructura de base de datos
- `data.sql`: datos iniciales como bancos y endpoints


## 🐳 Despliegue con Docker

> Requisitos:
> - Tener [Docker](https://docs.docker.com/get-docker/) y [Docker Compose](https://docs.docker.com/compose/install/) instalados.

---

### 1️⃣ Archivos requeridos

Asegúrate de tener en el root:

- `Dockerfile`
- `docker-compose.yml`
- `prometheus.yml`

---

### 2️⃣ `Dockerfile` para Spring Boot

```dockerfile
# Usa JDK 17
FROM openjdk:17
WORKDIR /app

# Copia el JAR generado por Maven
COPY target/compensaciones-0.0.1-SNAPSHOT.jar app.jar

# Ejecuta la app
ENTRYPOINT ["java", "-jar", "app.jar"]



###  3️⃣ Construcción y despliegue

bash
Copiar
Editar
# 1. Compila el proyecto y genera el JAR
./mvnw clean package -DskipTests

# 2. Levanta todo
docker-compose up --build

### 3️⃣ 🧪 Pruebas unitarias
./mvnw test

Cobertura de:

Conversión FX

Errores en API externa

Idempotencia

Fallos en base de datos

Cacheo y rendimiento

✨ Autor
Desarrollado por Juan Pablo Muñoz – Ingeniero de Software
