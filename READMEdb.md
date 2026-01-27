# Giber Bar – Modelo de Datos y Flujos de Negocio

Documentación técnica del esquema de base de datos para la gestión de Giber Bar.

## Conceptos Clave del Negocio

### 1. Identificación de Mesa (QR)
- **Entidad:** `mesas`
- **Identificador:** `numero_mesa` (Entero único).
- **Uso:** Cada mesa física tiene un código QR que codifica este `numero_mesa`.
- **Flujo:** Al escanear el QR, la aplicación busca en `mesas` por `numero_mesa` para identificar la ubicación física y consultar si hay una `sesion_mesa` activa.

### 2. La Cuenta / Factura (Sesión)
- **Entidad:** `sesiones_mesa`
- **Identificador:** `id_sesion` (PK).
- **Concepto:** Representa la **visita completa** de un grupo de clientes. Actúa como el contenedor de la "Cuenta" o "Factura".
- **Ciclo de Vida:**
    1. **Apertura:** El empleado crea la sesión cuando los clientes se sientan.
    2. **Consumo:** Se agregan `comandas` (comida/bebida) y `ludoteca_sesiones` (juegos) vinculadas a este `id_sesion`.
    3. **Cierre:** Se paga el total acumulado y se cierra la sesión.

---
 
## Módulos y Tablas

### A. Gestión de Sala
- **mesas**: Configuración física.
- **sesiones_mesa**: El núcleo de la facturación. Vincula la mesa con el consumo.

### B. Ludoteca y Tarifas
El uso de juegos es un servicio opcional que se cobra por sesión.

- **tarifas_ludoteca**: Configuración de precios (Adulto: 2.50€, Niño: 1.50€).
- **ludoteca_sesiones**: Registro del uso del servicio.
    - Vinculado 1:1 con `sesiones_mesa`.
    - Almacena el desglose de personas (adultos/niños) para estadística.
    - **`id_comanda_ludoteca`**: Enlace a la comanda donde se generaron los cargos.

### C. Facturación (Carta y Comandas)
El cobro se unifica mediante el concepto de "Producto".

- **productos**: Catálogo mixto.
    - *Tangibles:* Hamburguesas, Refrescos (`categoria = 'COMIDA'/'BEBIDA'`).
    - *Intangibles:* Servicios de ludoteca (`categoria = 'SERVICIO'`).
        - `USO_LUDOTECA_ADULTO`
        - `USO_LUDOTECA_NINO_6_13`
- **comandas**: Agrupador de pedidos. Una sesión puede tener N comandas.
- **lineas_comanda**: Detalle facturable. **La factura final es la suma de todas las líneas de todas las comandas de la sesión.**

### D. Pagos
- **peticiones_pago**: Solicitud de cuenta desde la App/Mesa.
- **pagos_mesa**: Registro de transacciones monetarias (Efectivo, Tarjeta) asociadas a la `id_sesion`.

---

## Flujo de Negocio Detallado

### 1. Llegada y Apertura
1. Cliente escanea QR -> App obtiene `numero_mesa`.
2. Empleado abre mesa -> Se crea `sesiones_mesa` (`id_sesion`).
3. Empleado indica comensales y **Uso de Ludoteca**.

### 2. Activación de Ludoteca (Si aplica)
Si el grupo decide jugar:
1. Se inserta registro en `ludoteca_sesiones` con el desglose de pax.
2. El sistema genera automáticamente una **Comanda de Servicio**:
    - Crea una `comanda` vinculada a `id_sesion`.
    - Inserta `lineas_comanda` con los productos `USO_LUDOTECA_*` correspondientes.
    - Actualiza `ludoteca_sesiones.id_comanda_ludoteca` con el ID de esta comanda.
3. **Resultado:** El cobro de la ludoteca ya aparece como líneas normales en la cuenta.

### 3. Consumo y Pedidos
1. Camareros toman nota -> Se crean nuevas `comandas` con productos de carta.
2. Cocina prepara -> Se gestionan estados en `lineas_comanda`.

### 4. Cierre y Cobro
1. Cliente pide cuenta -> Se registra en `peticiones_pago`.
2. Sistema calcula el total:
   `SUM(precio_historico * cantidad)` de todas las `lineas_comanda` donde `comanda.id_sesion = X`.
   *(Esto incluye automáticamente la comida, la bebida y la ludoteca)*.
3. Se registra el pago en `pagos_mesa`.
4. Se cierra la `sesion_mesa`.
