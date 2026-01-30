# GGBProyect - Backend

## Descripción General
**GGBProyect** es una plataforma integral para la gestión de un local híbrido de ludoteca y restauración. Este repositorio contiene el **Backend** desarrollado en **Spring Boot**, diseñado para ofrecer una API REST robusta y escalable que da servicio a una aplicación frontend (Angular).

El sistema gestiona desde la reserva de mesas y el préstamo de juegos de mesa hasta la toma de comandas de comida y el control de empleados.

## Tecnologías Principales
*   **Java 17+**
*   **Spring Boot 3.x** (Web, Data JPA, Validation)
*   **Base de Datos**: H2 (Desarrollo) / MySQL o PostgreSQL (Producción)
*   **Seguridad**: (Pendiente de implementación: Spring Security / JWT)
*   **Build Tool**: Maven

## Arquitectura del Proyecto

El proyecto sigue una arquitectura en capas clásica, promoviendo la separación de responsabilidades:

*   **`controller`**: Controladores REST que exponen los endpoints de la API. Manejan las peticiones HTTP y devuelven DTOs.
*   **`service`**: Capa de lógica de negocio. Realiza validaciones, cálculos y la conversión entre Entidades y DTOs.
*   **`repository`**: Interfaces que extienden `JpaRepository` para el acceso a datos.
*   **`models`**:
    *   **Entities**: Clases persistentes (`@Entity`) que mapean las tablas de la base de datos.
    *   **DTOs**: Objetos de transferencia de datos (`Data Transfer Objects`) para desacoplar la API del modelo de datos interno.

## Módulos Principales

### 1. Gestión de Usuarios y Personal
*   **Clientes**: Registro, perfil e historial.
*   **Empleados**: Gestión de personal con roles definidos (Admin, Camarero, Ludotecario).
*   **Roles**: Control de permisos (`RolesEmpleado`).

### 2. Ludoteca (Juegos)
*   **Catálogo**: Base de datos de juegos (`Juego`) con información de complejidad, duración, etc.
*   **Inventario**: Gestión de copias físicas (`JuegosCopia`) y su estado.
*   **Tarifas**: Configuración de precios por tiempo de uso (`TarifasLudoteca`).
*   **Préstamos**: Control de sesiones de juego en mesa (`ReservasJuego`).

### 3. Restauración y Sala
*   **Mesas**: Mapa de mesas, zonas y capacidades.
*   **Sesiones**: Control de ocupación (`SesionesMesa`), apertura y cierre de cuentas.
*   **Comandas**: Pedidos de comida/bebida (`Comanda`, `LineasComanda`) asociados a una sesión.
*   **Productos**: Carta de productos y precios.

### 4. Facturación
*   **Pagos**: Registro de pagos parciales o totales (`PagosMesa`).
*   **Cálculo de Sesión**: Lógica para calcular el coste final de la ludoteca (`LudotecaSesiones`).

## Configuración y Ejecución

### Requisitos Previos
*   JDK 17 o superior instalado.
*   Maven instalado (o usar el wrapper `mvnw` incluido).

### Ejecutar la Aplicación
```bash
./mvnw spring-boot:run
```
La aplicación se iniciará por defecto en `http://localhost:8080`.

### Documentación de la API
(Pendiente: Integrar Swagger/OpenAPI en `http://localhost:8080/swagger-ui.html`)

## Estándares de Desarrollo
Para detalles sobre las reglas de base de datos, convenciones de nombres y guías para contribuir, consulta el archivo específico:
📄 **[Documentación de Base de Datos y Estándares](db/READMEdb.md)**

## Estado del Proyecto
🚧 **En Desarrollo**
*   ✅ CRUDs básicos completados para todas las entidades.
*   ✅ Estructura de DTOs y Servicios implementada.
*   🔄 Controladores REST en proceso de finalización.
*   ❌ Seguridad (JWT) pendiente.
*   ❌ Lógica de negocio compleja (cálculo de tarifas, stock) pendiente.
