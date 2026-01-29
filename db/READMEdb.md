# Documentación de Base de Datos y Estándares de Desarrollo

Este documento define las reglas, estándares y convenciones arquitectónicas para el proyecto **GGBProyect**. Su propósito es garantizar la consistencia del código y facilitar el mantenimiento, sirviendo como guía tanto para desarrolladores humanos como para asistentes de IA.

## 1. Arquitectura del Proyecto

El proyecto sigue una arquitectura en capas clásica de Spring Boot:

1.  **Controller (`.controller`)**: Maneja las peticiones HTTP. Solo se comunica con la capa Service. **Siempre** recibe y devuelve DTOs, nunca Entidades.
2.  **Service (`.service`)**: Contiene la lógica de negocio.
    *   Debe estar anotado con `@Service`.
    *   Realiza la conversión entre DTOs y Entidades.
    *   Inyecta dependencias mediante **constructor** (no usar `@Autowired` en campos).
3.  **Repository (`.repository`)**: Interfaz que extiende `JpaRepository`. Acceso directo a datos.
4.  **Model (`.models`)**:
    *   **Entities**: Clases anotadas con `@Entity` que reflejan las tablas de la BD.
    *   **DTOs**: Clases POJO (Data Transfer Objects) para transferir datos entre capas.

## 2. Convenciones de Naming

*   **Idioma**:
    *   **Código (Clases, Variables, Métodos)**: Español para el dominio (ej: `Cliente`, `Mesa`), Inglés para componentes técnicos (ej: `Service`, `Repository`, `Controller`, `DTO`).
    *   **Base de Datos**: Español, usando `snake_case`.
*   **Clases**: `PascalCase` (ej: `ClienteService`).
*   **Métodos y Variables**: `camelCase` (ej: `getById`, `fechaAlta`).
*   **Tablas BD**: Plural, `snake_case` (ej: `clientes`, `sesiones_mesa`).
*   **Columnas BD**: `snake_case` (ej: `fecha_alta`, `id_cliente`).

## 3. Reglas de Base de Datos y Entidades

### Claves Primarias y Foráneas
*   **PK**: Prefijo `id_` seguido del nombre de la entidad en singular (ej: `id_cliente`, `id_mesa`). Autoincremental (`IDENTITY`).
*   **FK**: Mismo nombre que la PK referenciada (ej: `id_rol` en la tabla `empleados`).

### Tipos de Datos y Restricciones
*   **Booleanos**: Se mapean a `Boolean` en Java. En BD pueden ser `BIT` o `TINYINT`.
*   **Fechas**: Usar `Instant` para timestamps (fecha + hora UTC) y `LocalDate` para fechas sin hora.
*   **Moneda**: Usar `BigDecimal` para precios e importes. Nunca `Double` o `Float`.
*   **Strings**: Definir siempre `@Size` o `length` en JPA.

### Auditoría y Borrado Lógico
*   No se eliminan registros físicamente si tienen histórico importante.
*   Usar campos de estado (ej: `estado` VARCHAR) o flags (ej: `activo` BOOLEAN).
*   Valores comunes de estado: `'ACTIVO'`, `'BAJA'`, `'PENDIENTE'`, `'LIBRE'`, `'OCUPADA'`.

## 4. Patrón DTO (Data Transfer Object)

Para desacoplar la capa de persistencia de la vista/API:

1.  **Ubicación**: Paquete `models`, sufijo `DTO`.
2.  **Estructura**:
    *   Mismos campos que la entidad, excluyendo datos sensibles (passwords) y relaciones cíclicas.
    *   Las relaciones (`ManyToOne`) se representan por su ID (ej: `Integer idRol` en lugar de `RolesEmpleado idRol`).
3.  **Conversión**:
    *   Constructor que acepta la Entidad: `public ClienteDTO(Cliente entity)`.
    *   Método `toEntity()`: Retorna una nueva instancia de la Entidad con los datos del DTO.

## 5. Inyección de Dependencias

*   **Prohibido**: Uso de `@Autowired` en campos privados.
*   **Obligatorio**: Inyección por constructor.
    *   Declarar campos como `private final`.
    *   Crear constructor público que reciba las dependencias.

## 6. Reglas para IA Generativa

Si una IA genera código para este proyecto, debe:
1.  Verificar si existe un DTO antes de usar la Entidad en un Controller o Service.
2.  No usar Lombok `@RequiredArgsConstructor` si se pide explícitamente constructor manual.
3.  Respetar las validaciones de Jakarta (`@NotNull`, `@Size`, `@Email`) en los DTOs.
4.  Mantener el estilo de código existente (espaciado, llaves, nombres).
