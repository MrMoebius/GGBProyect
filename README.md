# GGBProyect - Backend

API REST para **GGBar** (Giber Games Bar), un local de ludoteca y restauracion en Alcorcon. Gestiona reservas de mesa, catalogo de juegos, comandas, eventos, inscripciones, facturacion y control de empleados.

## Tecnologias

- **Java 17+** / **Spring Boot 4.0.2**
- **MySQL** (TiDB Cloud en produccion)
- **Spring Security** con JWT (HS512) + BCrypt
- **Spring Data JPA** (Hibernate, paginacion)
- **Spring Mail** (verificacion de email, recuperacion de password)
- **Maven** (wrapper `mvnw` incluido)

## Arquitectura

Capas clasicas: Controller > Service > Repository > Entity/DTO.

- **21 Controllers**, 17 Services, 17 Repositories
- **18 Entidades**, 29 DTOs, 13 Enums
- Validacion con Bean Validation (`@Valid`, `@NotNull`, `@ValidEnum` custom)
- Conversion Entity-DTO en constructores

## Modulos

| Modulo            | Descripcion                                                                                            |
|-------------------|--------------------------------------------------------------------------------------------------------|
| **Auth**          | Login, registro/verificacion email, recuperacion password, rate limiting (5 intentos = 5min bloqueo)   |
| **Clientes**      | CRUD, foto de perfil, historial                                                                        |
| **Empleados**     | CRUD, roles (ADMIN, EMPLEADO), gestion de personal                                                     |
| **Juegos**        | Catalogo con imagenes, copias fisicas, integracion BoardGameGeek XML API                               |
| **Mesas**         | Mapa de mesas, estados (LIBRE, OCUPADA, RESERVADA, FUERA_SERVICIO)                                     |
| **Sesiones**      | Apertura/cierre de sesiones en mesa, estados (ACTIVA, CERRADA, FINALIZADA)                             |
| **Comandas**      | Pedidos asociados a sesion, flujo de estados (PENDIENTE > CONFIRMADA > PREPARACION > SERVIDA > PAGADA) |
| **Productos**     | Carta con categorias, IVA, productos personalizables (hamburguesas, copazos)                           |
| **Reservas**      | Reservas de mesa con juego deseado, transiciones de estado, endpoints cliente                          |
| **Eventos**       | CRUD eventos con tipos (TORNEO, NOCHE_TEMATICA, TALLER, EVENTO_ESPECIAL), imagenes                     |
| **Inscripciones** | Inscripcion/desinscripcion a eventos, gestion automatica de capacidad y lista de espera                |
| **Facturas**      | Generacion de facturas, ticket imprimible, envio PDF por email                                         |

## Roles y Seguridad

- **ROLE_ADMIN**: Acceso total
- **ROLE_EMPLEADO**: Gestion de sala, comandas, sesiones, reservas
- **ROLE_CLIENTE**: Su sesion, sus comandas, sus reservas, inscripciones a eventos
- `@PreAuthorize` a nivel de clase con overrides por metodo para endpoints de cliente
- Endpoints publicos: GET juegos, productos, eventos, imagenes; POST reservas

## Configuracion

### Variables de entorno

Copiar `.env.example` a `.env` y configurar:

```
DB_URL=jdbc:mysql://...
DB_USERNAME=...
DB_PASSWORD=...
JWT_SECRET=...              # min 64 chars hex
JWT_EXPIRATION=604800000    # 7 dias
ADMIN_PASSWORD=...
MAIL_USERNAME=...           # Gmail SMTP
MAIL_PASSWORD=...           # App password
CORS_ORIGINS=http://localhost:4200
BGG_API_TOKEN=...
```

### Ejecucion

```bash
./mvnw spring-boot:run
```

Se inicia en `http://localhost:8080`.

## Despliegue

Produccion en **Raspberry Pi 5** con Docker Compose (nginx + Spring Boot + ngrok).

```bash
~/ggbar/deploy.sh              # todo
~/ggbar/deploy.sh backend      # solo backend
~/ggbar/deploy.sh frontend     # solo frontend
```
