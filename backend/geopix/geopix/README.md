# Geopix Backend - Autenticación y Despliegue

## Descripción
Este proyecto implementa un sistema de gestión de predios con autenticación segura basada en JWT y persistencia en Redis.

## Requisitos
- Docker y Docker Compose
- Java 17 (para desarrollo local)
- Maven (o `./mvnw`)

## Configuración
Copia el archivo `.env.example` a `.env` y ajusta las variables si es necesario:
```bash
cp .env.example .env
```

## Despliegue con Docker
Para levantar toda la infraestructura (App, PostgreSQL, Redis):
```bash
docker-compose up --build
```

La aplicación estará disponible en `http://localhost:8080`.

## Endpoints de Autenticación

### 1. Login
`POST /api/v1/auth/login`
- **Cuerpo:** `{ "identifier": "username_o_email", "password": "password" }`
- **Respuesta:** Cookies `access_token` y `refresh_token` (HttpOnly), y datos básicos del usuario en el body.

### 2. Me
`GET /api/v1/auth/me`
- **Seguridad:** Requiere cookie `access_token` válida.
- **Respuesta:** Información del usuario autenticado.

### 3. Registro (Controlado)
`POST /api/v1/auth/register`
- **Seguridad:** Solo accesible por usuarios con rol **ADMIN**.
- **Cuerpo:** `{ "username": "...", "email": "...", "password": "...", "role": "USER" }`
- **Respuesta:** Datos del usuario creado.

## Consideraciones de Seguridad
- Las sesiones se gestionan de forma STATELESS.
- Los tokens se almacenan en Redis para permitir revocación y control de sesión única.
- El registro no es público para evitar creación no autorizada de usuarios.
