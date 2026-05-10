[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/q5A4m_h4)
# 🧪 2026 - Trabajo Práctico Anual

## 👤 Datos del Alumno
- **Nombre: Lucas**
- **Apellido: Seijas**

---

🧩 Componente Desarrollado
- Incentivos

---

🧩 Link al despliegue en Render
- Aun no :(

---

### ⚠️ Importante

**ARCHIVOS PROTEGIDOS:**

> Los archivos de las carpetas "/catedra" y ".github/" están PROTEGIDOS, es decir, **NO PUEDEN MODIFICARLOS**.
Modificar estos archivos implica desaprobar inmediatamente la instancia de entrega del TPA.





---

### ayuda memoria lucas
# Incentivos - Entrega 2

## Pruebas
mvn test

## Iniciar server
mvn spring-boot:run

## Swagger / probar endpoints
http://localhost:8080/swagger-ui/index.html#/

## Endpoints JSON
http://localhost:8080/v3/api-docs

## Errores con Java
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk
export PATH=$JAVA_HOME/bin:$PATH

## Docker build
docker build -t incentivos .

## Docker run
docker run -p 8080:8080 incentivos

## Swagger con Docker
http://localhost:8080/swagger-ui/index.html#/

## Endpoints
GET /insignias
POST /insignias
GET /insignias/{id}

GET /misiones
POST /misiones
GET /misiones/{id}


