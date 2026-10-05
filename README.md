# API de gestión de órdenes de transporte

API de gestión de órdenes de transporte de una empresa de movilidad, se realizo con una arquitectura de microservicios, Spring Boot 3.4.5 / Java 17.

## Arquitectura

| Servicio | Puerto | Descripción | BD |
|----------|--------|-------------|-----|
| **microservice-api-gateway** | 8080 | Entrada única, validación JWT, enrutamiento | – |
| **microservice-drivers** | 8081 | Dominio de conductores | driver_db |
| **microservice-orders** | 8082 | Órdenes + `driverId` (referencia externa) | order_db |
| **microservice-assignments** | 8083 | Asignaciones + archivos (PDF/imagen) | order_db |

**Comunicación:**
- `microservice-assignments` → `microservice-orders` (validar orden, vincular `driverId`) vía **OpenFeign**.
- `microservice-assignments` → `microservice-drivers` (validar conductor activo) vía **OpenFeign**.
- `microservice-api-gateway` enruta externamente a los 3 micros.

## Stack

- Java 17 (con **records** para todos los DTOs), Spring Boot 3.4.5, Spring Cloud 2024.0.0
- Spring Cloud Gateway, OpenFeign
- Spring Data JPA + PostgreSQL
- MapStruct, Lombok (solo entidades), Jakarta Validation
- Springdoc OpenAPI, Docker, JUnit 5, Mockito, JJWT

## Ejecución con Docker Compose

```bash
docker-compose up --build
```

- API Gateway:  http://localhost:8080/swagger-ui.html

## Ejecución local

```bash
docker run -d --name pg -p 5432:5432 \
  -e POSTGRES_USER=transport_user \
  -e POSTGRES_PASSWORD=transport_pass \
  -v $(pwd)/db:/docker-entrypoint-initdb.d \
  postgres:16-alpine

mvn clean install -DskipTests

# En terminales separadas, en orden:
java -jar microservice-drivers/target/*.jar
java -jar microservice-orders/target/*.jar
java -jar microservice-assignments/target/*.jar
java -jar api-gateway/target/*.jar
```

## Endpoints (a través del Gateway: `http://localhost:8080`)

### Órdenes (`microservice-orders`)
| Método | Ruta | Descripción |
|--------|------|-------------|
| POST   | `/api/v1/orders` | Crear orden |
| POST  | `/api/v1/orders/{id}/status` | Cambiar estado |
| POST  | `/api/v1/orders/{id}/assign-driver` | Vincular driver (interno) |
| GET    | `/api/v1/orders/{id}` | Consultar por ID |
| GET    | `/api/v1/orders?status=&origin=&destination=&from=&to=` | Listar con filtros |

### Conductores (`microservice-drivers`)
| Método | Ruta | Descripción |
|--------|------|-------------|
| POST   | `/api/v1/drivers` | Crear conductor |
| GET    | `/api/v1/drivers/active` | Listar activos |
| GET    | `/api/v1/drivers/{id}` | Consultar por ID |

### Asignaciones (`microservice-assignments`)
| Método | Ruta | Descripción |
|--------|------|-------------|
| POST   | `/api/v1/assignments/orders/{orderId}` | Asignar conductor a la orden |
| POST   | `/api/v1/assignments/{assignmentId}/pdf` (multipart) | Adjuntar PDF |
| POST   | `/api/v1/assignments/{assignmentId}/image` (multipart) | Adjuntar imagen PNG/JPG |
| GET    | `/api/v1/assignments` | Litar el ID de todas las asignaciones |

## Autenticación JWT

Todos los endpoints expuestos por el **API Gateway** requieren el header `Authorization: Bearer <token>`. El gateway valida el token con la clave `jwt.secret` (HMAC-SHA256) y rechaza con **401 Unauthorized** cualquier petición sin token válido.
Esto debido al punto: Seguridad con Spring Security (JWT básico).

### Token de prueba

Para facilitar las pruebas se proporciona el siguiente token JWT (subject `admin`, sin expiración):

```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiJ9.PpIYmeIE3ESwRlW3KKHstGVeE9OEs85kdOaSyDcdob8
```
### Uso en cada petición

**En `curl`:**

```bash
curl -X GET http://localhost:8080/api/v1/orders \
  -H "Authorization: Bearer $TOKEN"
```

### Respuestas esperadas

| Escenario | Código | Cuerpo |
|-----------|--------|--------|
| Token válido | `200` / `201` | JSON del recurso |
| Sin header `Authorization` | `401 Unauthorized` | *(vacío)* |
| Header sin `Bearer ` | `401 Unauthorized` | *(vacío)* |
| Firma inválida / secret distinto | `401 Unauthorized` | *(vacío)* |
| Token expirado | `401 Unauthorized` | *(vacío)* |

### Detalle del token

Payload decodificado:

```json
{
  "sub": "admin"
}
```

- **Algoritmo:** HS256
- **Subject:** `admin`
- **Expiración:** no definida (token de pruebas)

### Regenerar el token (opcional)

Si necesitas uno con expiración o distinto `sub`, se puede generar desde la pagina https://www.jwt.io/

## Flujo de asignación (ejemplo)

```bash
# 1. Crear orden
ORDER=$(curl -s -X POST http://localhost:8080/api/v1/orders \
  -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
  -d '{"origin":"Yucatan","destination":"Ciudad de Mexico"}')
ORDER_ID=$(echo $ORDER | jq -r .id)

# 2. Crear conductor
DRIVER=$(curl -s -X POST http://localhost:8080/api/v1/drivers \
  -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
  -d '{"name":"Juan","licenseNumber":"LIC-001"}')
DRIVER_ID=$(echo $DRIVER | jq -r .id)

# 3. Asignar (microservice-assignments valida vía Feign y notifica microservice-orders)
ASSIGN=$(curl -s -X POST http://localhost:8080/api/v1/assignments/orders/$ORDER_ID \
  -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
  -d "{\"driverId\":\"$DRIVER_ID\"}")
ASSIGN_ID=$(echo $ASSIGN | jq -r .id)

# 4. Adjuntar PDF e imagen
curl -X POST http://localhost:8080/api/v1/assignments/$ASSIGN_ID/pdf \
  -H "Authorization: Bearer $TOKEN" -F "file=@contrato.pdf"

curl -X POST http://localhost:8080/api/v1/assignments/$ASSIGN_ID/image \
  -H "Authorization: Bearer $TOKEN" -F "file=@foto.jpg"
```

## Flujo de estados de la orden

```
CREATED ──► IN_TRANSIT ──► DELIVERED
   │              │
   └──► CANCELLED ◄┘
```

## Pruebas

```bash
mvn test
```

## Decisiones de diseño

- **DTOs como `record`**: inmutables, concisos y soportados nativamente por MapStruct.
- **Entidades JPA** permanecen como clases.
- **Database per Service**: `driver_db`, `order_db`.
- **`microservice-orders`** solo guarda `driverId` como referencia externa, sin foreign keys cruzadas.
- **`microservice-assignments`** orquesta las validaciones con **Feign** y maneja archivos locales.
- **API Gateway** centraliza la validación JWT (stateless) y CORS.
- **MapStruct** para mapeo type-safe entre records y entidades.
- **`@RestControllerAdvice`** en cada micro con `ErrorResponse` como record.
- **Docker multi-stage** + usuario no-root + healthcheck de Postgres.