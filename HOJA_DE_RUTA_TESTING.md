# HOJA DE RUTA - Testing Manual Exhaustivo desde Angular

## Requisitos previos
- Backend corriendo (Docker o local) con los ultimos cambios compilados
- Frontend Angular corriendo en http://localhost:4200
- Acceso como ADMIN: `admin@ggbproyect.com` / `admin123`
- Acceso como CLIENTE: un cliente verificado con email/password conocidos
- Base de datos con datos de prueba (mesas, productos, etc.)

---

## 1. MESAS (`/admin/mesas`)

### 1.1 Crear mesa - Datos invalidos (debe rechazar)

| # | Campo | Valor | Esperado |
|---|-------|-------|----------|
| 1 | nombreMesa | *(vacio)* | Error: campo requerido |
| 2 | numeroMesa | *(vacio)* | Error: campo requerido |
| 3 | capacidad | `0` | Error: capacidad debe ser mayor que 0 |
| 4 | capacidad | `-5` | Error: capacidad debe ser mayor que 0 |
| 5 | capacidad | *(vacio)* | Error: campo requerido |
| 6 | nombreMesa | nombre de mesa que ya exista | Error: mesa ya existente |
| 7 | numeroMesa | numero de mesa que ya exista | Error: ya existe una mesa con ese numero |

### 1.2 Crear mesa - Datos validos (debe aceptar)

| # | Campo | Valor |
|---|-------|-------|
| 1 | nombreMesa | `Mesa Test Manual` |
| 2 | numeroMesa | `777` |
| 3 | capacidad | `4` |
| 4 | zona | `Salon` |

> Resultado: mesa creada correctamente. **Eliminarla despues.**

### 1.3 Editar mesa

| # | Prueba | Esperado |
|---|--------|----------|
| 1 | Cambiar capacidad a `0` | Error |
| 2 | Cambiar capacidad a `-3` | Error |
| 3 | Cambiar nombre a uno que ya exista | Error |
| 4 | Cambiar capacidad a `6` | OK |

---

## 2. EMPLEADOS (`/admin/empleados`)

### 2.1 Crear empleado - Datos invalidos

| # | Campo | Valor | Esperado |
|---|-------|-------|----------|
| 1 | nombre | *(vacio)* | Error: campo requerido |
| 2 | email | *(vacio)* | Error: campo requerido |
| 3 | email | `noesunmail` | Error: formato de email invalido |
| 4 | email | email de empleado que ya exista | Error: ya existe un empleado con ese email |
| 5 | password | `123` | Error: minimo 6 caracteres |
| 6 | password | *(vacio)* | Error: campo requerido (en creacion) |
| 7 | idRol | *(sin seleccionar)* | Error: campo requerido |
| 8 | fechaIngreso | `01/01/2030` (fecha futura) | Error: la fecha de ingreso no puede ser en el futuro |
| 9 | telefono | `abc` | Error: formato invalido (debe ser numerico) |

### 2.2 Crear empleado - Datos validos

| # | Campo | Valor |
|---|-------|-------|
| 1 | nombre | `Test Manual` |
| 2 | email | `testmanual@test.com` |
| 3 | password | `Test123456` |
| 4 | idRol | EMPLEADO |
| 5 | fechaIngreso | fecha de hoy o anterior |
| 6 | estado | ACTIVO |

> Resultado: empleado creado. **Eliminarlo despues.**

### 2.3 Editar empleado

| # | Prueba | Esperado |
|---|--------|----------|
| 1 | Cambiar fechaIngreso a futuro (2030) | Error |
| 2 | Cambiar email a uno que ya exista | Error |
| 3 | Dejar password vacio (edicion) | OK (no cambia la password) |
| 4 | Cambiar nombre y guardar | OK |

---

## 3. PRODUCTOS (`/admin/productos`)

### 3.1 Crear producto - Datos invalidos

| # | Campo | Valor | Esperado |
|---|-------|-------|----------|
| 1 | nombre | *(vacio)* | Error: campo requerido |
| 2 | precio | `-10` | Error: el precio no puede ser negativo |
| 3 | precio | *(vacio)* | Error: campo requerido |
| 4 | categoria | *(vacio)* | Error: campo requerido |
| 5 | nombre | nombre de producto que ya exista | Error: nombre duplicado |

### 3.2 Crear producto - Datos validos

| # | Campo | Valor |
|---|-------|-------|
| 1 | nombre | `Producto Test Manual` |
| 2 | descripcion | `Para testing` |
| 3 | precio | `5.50` |
| 4 | categoria | COMIDA |
| 5 | activo | marcado |

> Resultado: producto creado. **No eliminarlo si luego lo usas para probar lineas de comanda.**

### 3.3 Eliminar producto

| # | Prueba | Esperado |
|---|--------|----------|
| 1 | Eliminar un producto que este en alguna linea de comanda | Error: referenciado en lineas de comanda |
| 2 | Eliminar el producto test manual (sin referencias) | OK |

---

## 4. JUEGOS (`/admin/juegos`)

### 4.1 Crear juego - Datos invalidos

| # | Campo | Valor | Esperado |
|---|-------|-------|----------|
| 1 | nombre | *(vacio)* | Error: campo requerido |
| 2 | minJugadores | `-1` | Error: no puede ser negativo |
| 3 | maxJugadores | `-1` | Error: no puede ser negativo |
| 4 | minJugadores | `10`, maxJugadores | `2` | Error: minimo no puede ser mayor que maximo |
| 5 | duracionMediaMin | `0` o negativo | Error |

### 4.2 Crear juego - Datos validos

| # | Campo | Valor |
|---|-------|-------|
| 1 | nombre | `Juego Test Manual` |
| 2 | minJugadores | `2` |
| 3 | maxJugadores | `6` |
| 4 | duracionMediaMin | `45` |
| 5 | complejidad | MEDIA |
| 6 | idioma | ESPANOL |

> Resultado: juego creado. **Eliminarlo despues.**

### 4.3 Imagen de juego

| # | Prueba | Esperado |
|---|--------|----------|
| 1 | Subir imagen JPEG/PNG/WebP | OK, imagen visible |
| 2 | Subir archivo .txt o .pdf | Error: tipo no permitido |
| 3 | Eliminar imagen | OK, imagen eliminada |

---

## 5. TARIFAS LUDOTECA (`/admin/tarifas-ludoteca` si existe, o via API)

> Nota: Si no hay pagina admin para tarifas, probar desde la API directamente con Postman/curl.

### 5.1 Crear tarifa - Datos invalidos

| # | Campo | Valor | Esperado |
|---|-------|-------|----------|
| 1 | precio | `-5` | Error: precio no puede ser negativo |
| 2 | edadMin | `15`, edadMax | `6` | Error: edad min no puede ser mayor que max |
| 3 | nombreTramo | *(vacio)* | Error: campo requerido |

### 5.2 Crear tarifa - Datos validos

| # | Campo | Valor |
|---|-------|-------|
| 1 | nombreTramo | `Test Tramo` |
| 2 | edadMin | `6` |
| 3 | edadMax | `13` |
| 4 | precio | `3.00` |
| 5 | activo | true |

---

## 6. SESIONES DE MESA (`/admin/sesiones` o `/staff`)

### 6.1 Abrir sesion - Datos invalidos

| # | Prueba | Esperado |
|---|--------|----------|
| 1 | Abrir sesion con numComensales = `0` | Error: debe ser mayor que 0 |
| 2 | Abrir sesion con numComensales = `-2` | Error: debe ser mayor que 0 |
| 3 | Abrir sesion en mesa OCUPADA | Error: mesa no disponible |
| 4 | Abrir sesion con numComensales > capacidad de la mesa | Error: supera capacidad |

### 6.2 Abrir sesion - Datos validos

| # | Prueba | Esperado |
|---|--------|----------|
| 1 | Seleccionar mesa LIBRE, numComensales = `2` | OK: sesion creada, mesa pasa a OCUPADA |
| 2 | Verificar que la mesa aparece como OCUPADA en el plano | OK |

### 6.3 Cerrar sesion

| # | Prueba | Esperado |
|---|--------|----------|
| 1 | Cerrar sesion con comandas pendientes (no servidas) | Error: comandas no resueltas |
| 2 | Cerrar sesion sin haber pagado todo | Error: pago insuficiente |
| 3 | Cerrar sesion con todo pagado y comandas resueltas | OK: sesion cerrada, mesa vuelve a LIBRE |

### 6.4 Eliminar sesion

| # | Prueba | Esperado |
|---|--------|----------|
| 1 | Eliminar sesion ACTIVA | Error: no se puede eliminar sesion activa |
| 2 | Eliminar sesion CERRADA | OK |

---

## 7. COMANDAS (`/admin/comandas` o `/staff/comandas`)

### 7.1 Crear comanda

| # | Prueba | Esperado |
|---|--------|----------|
| 1 | Crear comanda en sesion ACTIVA | OK |
| 2 | Crear comanda en sesion CERRADA | Error: no se pueden crear en sesiones no activas |

### 7.2 Transiciones de estado

| # | Desde | Hacia | Esperado |
|---|-------|-------|----------|
| 1 | PENDIENTE | CONFIRMADA | OK |
| 2 | CONFIRMADA | PREPARACION | OK |
| 3 | PREPARACION | SERVIDA | OK |
| 4 | SERVIDA | PAGADA | OK |
| 5 | PENDIENTE | CANCELADA | OK |
| 6 | CONFIRMADA | CANCELADA | OK |
| 7 | SERVIDA | PENDIENTE | Error: transicion no permitida |
| 8 | PAGADA | cualquier otro | Error: transicion no permitida |
| 9 | CANCELADA | cualquier otro | Error: transicion no permitida |

### 7.3 Total no editable

| # | Prueba | Esperado |
|---|--------|----------|
| 1 | Editar comanda e intentar cambiar el total manualmente | El total NO debe cambiar (es de solo lectura, se recalcula automaticamente) |

---

## 8. LINEAS DE COMANDA (dentro de la vista de comandas)

### 8.1 Crear linea - Datos invalidos

| # | Campo | Valor | Esperado |
|---|-------|-------|----------|
| 1 | cantidad | `0` | Error: debe ser mayor que 0 |
| 2 | cantidad | `-3` | Error: debe ser mayor que 0 |
| 3 | cantidad | *(vacio)* | Error: campo requerido |
| 4 | idProducto | *(sin seleccionar)* | Error: campo requerido |

### 8.2 Crear linea - Datos validos

| # | Prueba | Esperado |
|---|--------|----------|
| 1 | Seleccionar producto, cantidad = `2` | OK: linea creada, total de comanda se recalcula |
| 2 | Verificar que el total de la comanda se actualizo | OK: total = precio x cantidad |

### 8.3 Editar linea

| # | Prueba | Esperado |
|---|--------|----------|
| 1 | Cambiar cantidad a `0` | Error |
| 2 | Cambiar cantidad a `-1` | Error |
| 3 | Cambiar cantidad a `5` | OK: total recalculado |

---

## 9. PAGOS (`/admin/pagos` o dentro de sesion)

### 9.1 Crear pago - Datos invalidos

| # | Campo | Valor | Esperado |
|---|-------|-------|----------|
| 1 | importe | `-50` | Error: importe no puede ser negativo |
| 2 | importe | *(vacio)* | Error: campo requerido |
| 3 | Pago que exceda el total de la sesion | Error: pago excede el total |

### 9.2 Crear pago - Datos validos

| # | Prueba | Esperado |
|---|--------|----------|
| 1 | Importe igual al total pendiente, metodo EFECTIVO | OK: pago registrado |

### 9.3 Transiciones de estado de pago

| # | Desde | Hacia | Esperado |
|---|-------|-------|----------|
| 1 | PENDIENTE | PAGADO | OK |
| 2 | PENDIENTE | CANCELADO | OK |
| 3 | PAGADO | PENDIENTE | Error: transicion no permitida |
| 4 | CANCELADO | cualquier otro | Error: transicion no permitida |

---

## 10. LUDOTECA SESIONES (dentro de sesion con ludoteca)

### 10.1 Datos invalidos

| # | Campo | Valor | Esperado |
|---|-------|-------|----------|
| 1 | numAdultos | `-2` | Error: no puede ser negativo |
| 2 | numNinos613 | `-3` | Error: no puede ser negativo |
| 3 | numNinos05 | `-1` | Error: no puede ser negativo |

### 10.2 Datos validos

| # | Prueba | Esperado |
|---|--------|----------|
| 1 | numAdultos=2, numNinos613=1, numNinos05=0 | OK: importe calculado automaticamente |

---

## 11. RESERVAS (pagina publica `/public/reservas`)

### 11.1 Como visitante/cliente - Datos invalidos

| # | Prueba | Esperado |
|---|--------|----------|
| 1 | Fecha pasada (ayer o anterior) | Error: no se permiten fechas pasadas |
| 2 | Fecha inicio posterior a fecha fin | Error: fechas invertidas |
| 3 | Sin seleccionar mesa | Error: campo requerido |
| 4 | numPersonas = `0` | Error |
| 5 | Sin estar logueado | Error: debe iniciar sesion |

### 11.2 Como cliente logueado - Datos validos

| # | Prueba | Esperado |
|---|--------|----------|
| 1 | Fecha futura, mesa disponible, 2 personas | OK: reserva creada |
| 2 | Verificar que aparece en "Mis Reservas" (`/customer/reservations`) | OK |
| 3 | Cancelar la reserva desde "Mis Reservas" | OK |

---

## 12. CLIENTES (`/admin/clientes`)

### 12.1 Crear cliente - Datos invalidos

| # | Campo | Valor | Esperado |
|---|-------|-------|----------|
| 1 | nombre | *(vacio)* | Error: campo requerido |
| 2 | email | *(vacio)* | Error: campo requerido |
| 3 | email | `noesunmail` | Error: formato invalido |
| 4 | email | email de cliente que ya exista | Error: email duplicado |
| 5 | telefono | `abc` | Error: formato invalido |

### 12.2 Crear cliente - Datos validos

| # | Campo | Valor |
|---|-------|-------|
| 1 | nombre | `Cliente Test` |
| 2 | email | `clientetest@test.com` |
| 3 | telefono | `600111222` |

---

## 13. EVENTOS (`/admin/eventos`)

### 13.1 Crear evento - Datos invalidos

| # | Campo | Valor | Esperado |
|---|-------|-------|----------|
| 1 | title | *(vacio)* | Error: campo requerido |
| 2 | date | *(vacio)* | Error: campo requerido |
| 3 | capacity | `0` o negativo | Error |
| 4 | location | *(vacio)* | Error: campo requerido |

### 13.2 Crear evento - Datos validos

| # | Campo | Valor |
|---|-------|-------|
| 1 | title | `Evento Test` |
| 2 | date | fecha futura |
| 3 | capacity | `20` |
| 4 | location | `Salon principal` |
| 5 | type | TORNEO |

---

## 14. FLUJO COMPLETO (Test E2E)

Este test simula un ciclo completo de uso real:

### Paso a paso:

1. **Login como ADMIN** -> verificar que accede al dashboard
2. **Crear mesa** -> `Mesa E2E`, numero `999`, capacidad `4`, zona `Test`
3. **Abrir sesion** en esa mesa -> numComensales `2`, usaLudoteca `false`
4. **Verificar** que la mesa pasa a OCUPADA en el plano
5. **Crear comanda** en la sesion activa
6. **Anadir linea** -> seleccionar producto, cantidad `2`
7. **Verificar** que el total de la comanda se calculo correctamente
8. **Confirmar comanda** -> estado pasa a CONFIRMADA
9. **Preparar comanda** -> estado pasa a PREPARACION
10. **Servir comanda** -> estado pasa a SERVIDA
11. **Registrar pago** -> importe = total de la comanda, metodo EFECTIVO
12. **Cerrar sesion** -> verificar que:
    - Comanda pasa a PAGADA
    - Mesa vuelve a LIBRE
    - Se genera factura
13. **Eliminar mesa** de test -> OK (la sesion esta cerrada)

---

## 15. FLUJO CLIENTE (Test E2E)

1. **Login como CLIENTE** en `/auth/login`
2. **Ir a Reservas** -> `/public/reservas`
3. **Intentar reservar con fecha pasada** -> debe rechazar
4. **Reservar con fecha futura valida** -> OK
5. **Ir a Mis Reservas** -> `/customer/reservations` -> debe aparecer
6. **Cancelar la reserva** -> OK
7. **Si hay sesion activa asignada al cliente:**
   - Ir al dashboard del cliente
   - Crear comanda desde el cliente
   - Anadir lineas a la comanda
   - Cancelar comanda pendiente -> OK
   - Intentar cancelar comanda confirmada -> Error

---

## Checklist resumen rapido

- [ ] Mesas: capacidad 0 y negativa rechazadas
- [ ] Mesas: nombre y numero duplicados rechazados
- [ ] Empleados: fecha ingreso futura rechazada
- [ ] Empleados: email duplicado rechazado
- [ ] Productos: precio negativo rechazado
- [ ] Productos: eliminar con FK rechazado
- [ ] Juegos: jugadores negativos rechazados
- [ ] Juegos: min > max rechazado
- [ ] Tarifas: precio negativo rechazado
- [ ] Tarifas: edad min > max rechazada
- [ ] Sesiones: 0 comensales rechazado
- [ ] Sesiones: abrir mesa ocupada rechazado
- [ ] Sesiones: cerrar sin pagar rechazado
- [ ] Sesiones: eliminar activa rechazado
- [ ] Comandas: crear en sesion cerrada rechazado
- [ ] Comandas: total no editable manualmente
- [ ] Comandas: transiciones invalidas rechazadas
- [ ] Lineas comanda: cantidad 0 y negativa rechazadas
- [ ] Pagos: importe negativo rechazado
- [ ] Pagos: exceder total rechazado
- [ ] Ludoteca: participantes negativos rechazados
- [ ] Reservas: fecha pasada rechazada
- [ ] Reservas: sin login rechazada
- [ ] Clientes: email duplicado rechazado
- [ ] Flujo completo E2E exitoso
- [ ] Flujo cliente E2E exitoso
