# Especificaciones Técnicas: Flujos de Backend - GGBProyect

**Estado del Proyecto:** Base de Datos y Entidades Sincronizadas (Rama: `endpointsTest`)
**Prioridad de Implementación:** Según impacto en el Core del Negocio.

---

## 1. Módulo de Sesiones y Sala (Prioridad: CRÍTICA)
Este módulo gestiona la ocupación física del local y es la clave primaria para el resto de los módulos.

### 1.1. Apertura de Sesión de Mesa
- **Endpoint sugerido:** `POST /api/sesiones/abrir`
- **Implicaciones de BD:** Operación atómica entre `mesas` y `sesiones_mesa`.
- **Lógica:**
  1. Verificar que `Mesa.estado == 'LIBRE'`.
  2. Crear registro en `sesiones_mesa` con `hora_inicio = NOW()` y `estado = 'ACTIVA'`.
  3. Cambiar `Mesa.estado` a `'OCUPADA'`.
- **Validación Crítica:** Lanzar excepción si la mesa ya tiene una sesión activa para evitar duplicidad de cobros.

### 1.2. Cierre de Sesión y Liquidación
- **Endpoint sugerido:** `PUT /api/sesiones/cerrar/{id}`
- **Lógica de Cálculo:**
  1. Sumar todos los `precio_total` de las `lineas_comanda` asociadas.
  2. Calcular diferencial de tiempo: `NOW() - hora_inicio`.
  3. Consultar `tarifas_ludoteca` para sumar el coste por tiempo de juego.
- **Restricción:** No permitir el cierre si existen comandas en estado `'PENDIENTE'`.

---

## 2. Módulo de Ludoteca y Préstamos (Prioridad: ALTA)
Controla el flujo de los juegos de mesa desde la estantería a la mesa del cliente.

### 2.1. Préstamo de Juego a Mesa
- **Endpoint:** `POST /api/prestamos/solicitar`
- **Lógica:**
  1. Verificar `JuegosCopia.estado == 'DISPONIBLE'`.
  2. Crear entrada en `ludoteca_sesiones` vinculando `copia_id` con `sesion_id`.
  3. Actualizar `JuegosCopia.estado` a `'PRESTADO'`.
- **Implicación:** El sistema debe impedir que un mismo código de barras de juego sea prestado a dos mesas simultáneamente.

---

## 3. Módulo de Comandas y Restauración (Prioridad: MEDIA-ALTA)
Flujo de consumo de productos (comida/bebida) durante la estancia.

### 3.1. Gestión de Pedidos
- **Lógica:**
  - Las `lineas_comanda` deben persistir el precio del producto en el momento del pedido para evitar discrepancias si los precios cambian en el maestro de productos.
  - Actualizar el estado de la comanda global a `'PROCESANDO'` al añadir el primer producto.

### 3.2. Validación de Stock (Seguridad Operativa)
- **Flujo:** Validación de Stock.
- **Lógica:** Impedir añadir un producto a una comanda si `stock <= 0`.

---

## 4. Módulo de Pagos y Caja (Prioridad: ALTA)
Finalización de la transacción económica y liberación de recursos.

### 4.1. Registro de Pago y Liberación
- **Lógica:**
  1. Al confirmar el éxito en `pagos_mesa`, actualizar `sesiones_mesa.estado = 'FINALIZADA'`.
  2. **Importante:** Cambiar `Mesa.estado` de nuevo a `'LIBRE'` inmediatamente.
  3. Si el pago es `'FALLIDO'`, la mesa debe permanecer `'OCUPADA'` y la sesión `'ACTIVA'`.

---

## 5. Módulo de Inteligencia de Negocio (BI) y Analítica (Prioridad: MEDIA)
Este módulo transforma los datos operativos en información estratégica para el dueño del local.

### 5.1. Reporte de Popularidad de Juegos
- **Objetivo:** Identificar qué juegos generan más interés.
- **Lógica:** Realizar un `COUNT` sobre la tabla `ludoteca_sesiones` agrupando por `id_juego`.
- **Output sugerido:** `GET /api/stats/juegos/top`. Devuelve una lista de juegos con su frecuencia de préstamo.

### 5.2. Métricas de Rotación de Mesas
- **Objetivo:** Entender la eficiencia del espacio físico.
- **Lógica:** Promedio de la diferencia entre `hora_inicio` y `hora_fin` en `sesiones_mesa`.
- **Output sugerido:** `GET /api/stats/mesas/uso-medio`. Ayuda a decidir si faltan mesas de 2 personas o de 6.

### 5.3. Recaudación y Caja Diario
- **Objetivo:** Control financiero.
- **Lógica:** Sumatorio (`SUM`) de los montos en `pagos_mesa` filtrando por `estado = 'COMPLETADO'` y la fecha actual.
- **Output sugerido:** `GET /api/stats/caja/hoy`.

### 5.4. Reporte de Rentabilidad
- **Input:** Rango de fechas.
- **Output:** JSON con: Total recaudado, Juego más prestado, Producto más vendido.

### 5.5. Auditoría de Inventario
- **Lógica:** Listar `JuegosCopia` cuyo estado sea `'EN_REPARACION'` o `'PERDIDO'`.

---

## 6. Módulo de Gestión de Torneos (Prioridad: ALTA)
Este es el flujo más complejo debido a la coordinación de múltiples usuarios y recursos del local.

### 6.1. Definición del Evento
- **Nueva Entidad:** `Torneo`.
- **Atributos:** `nombre`, `juego_id`, `fecha_inicio`, `cupo_max`, `precio_inscripcion`, `estado` (ABIERTO, EN_CURSO, FINALIZADO).
- **Lógica:** Al crear un torneo, se pueden bloquear automáticamente mesas específicas para el evento.

### 6.2. Inscripción y Pago
- **Entidad Intermedia:** `TorneoParticipantes` (Vincula Cliente con Torneo).
- **Flujo:**
  1. El cliente solicita inscripción.
  2. El sistema genera una `PeticionPago`.
  3. Al confirmar el pago, el estado del participante pasa a `CONFIRMADO`.
- **Validación:** Impedir inscripciones si se ha alcanzado el `cupo_max`.

### 6.3. Sistema de Brackets (Enfrentamientos)
- **Lógica:** Generar emparejamientos automáticos.
- **Resultados:** El staff registra el ganador de cada partida, lo que actualiza el árbol del torneo hasta llegar a la final.

---

## 7. Módulo de Fidelización (Prioridad: MEDIA)
Para incentivar la recurrencia de los clientes.

### 7.1. Sistema de Puntos
- **Acción:** Añadir campo `puntos_acumulados` a la tabla `clientes`.
- **Lógica de Acumulación:** Cada cierre de sesión o pago de torneo suma puntos (ej: 1€ = 1 punto).
- **Canjeo:** Endpoint para aplicar descuentos en Comandas a cambio de puntos.

---

## Resumen de Tareas para el Equipo Técnico

| Módulo             | Acción Principal                    | Implicación Crítica                                                      |
|:-------------------|:------------------------------------|:-------------------------------------------------------------------------|
| **Sesiones**       | Gestión de estados                  | Evitar cobros duplicados y mesas bloqueadas.                             |
| **Analítica**      | Crear Queries SQL agregadas         | Impacto en rendimiento (usar índices en fechas).                         |
| **Torneos**        | Crear tablas Torneo y Participantes | Gestión de estados y cupos en tiempo real.                               |
| **Fidelización**   | Actualizar tabla Cliente            | Persistencia de puntos en cada transacción de pago.                      |
| **Notificaciones** | Lógica de avisos                    | Alertas cuando un torneo está por empezar o una mesa lleva mucho tiempo. |

---

## Resumen de Validaciones para Desarrolladores

| Entidad           | Campo Crítico | Validación Requerida                                    |
|:------------------|:--------------|:--------------------------------------------------------|
| **Mesa**          | `estado`      | Solo pasar a `OCUPADA` si el estado previo es `LIBRE`.  |
| **SesionesMesa**  | `hora_fin`    | Debe ser siempre posterior a `hora_inicio`.             |
| **LineasComanda** | `cantidad`    | Debe ser un entero positivo > 0.                        |
| **JuegosCopia**   | `id_juego`    | Validar que el ID del juego existe en la tabla maestra. |
