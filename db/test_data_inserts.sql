-- =================================================================================
-- SCRIPT DE DATOS DE PRUEBA (GGBProyect) - CORREGIDO
-- Ejecutar este script en tu cliente SQL (MySQL Workbench, DBeaver, etc.)
-- =================================================================================

-- 1. ROLES DE EMPLEADO
INSERT IGNORE INTO roles_empleado (id_rol, nombre_rol) VALUES
(1, 'ADMIN'),
(2, 'EMPLEADO');

-- 2. EMPLEADOS
-- Password '1234' encriptada
INSERT INTO empleados (nombre, email, telefono, password, id_rol, fecha_ingreso, estado) VALUES
('Juan Camarero', 'juan@ggb.com', '600111222', '$2a$10$x.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.g/j.eAm', 2, CURDATE(), 'ACTIVO'),
('Ana Ludotecaria', 'ana@ggb.com', '600333444', '$2a$10$x.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.g/j.eAm', 2, CURDATE(), 'ACTIVO');

-- 3. CLIENTES
INSERT INTO clientes (nombre, email, telefono, password, fecha_alta, notas) VALUES
('Carlos Jugón', 'carlos@gmail.com', '666777888', '$2a$10$x.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.g/j.eAm', CURDATE(), 'Prefiere juegos de estrategia pesados.'),
('María Casual', 'maria@hotmail.com', '666999000', '$2a$10$x.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.g/j.eAm', CURDATE(), 'Viene los viernes con niños.');

-- 4. MESAS
-- Nota: Si la base de datos tiene la columna 'numero_mesa' como obligatoria pero la entidad Java usa 'nombre_mesa',
-- es posible que haya una discrepancia entre el esquema real y el código.
-- Aquí insertamos ambos valores para asegurar compatibilidad si la columna existe en BD.
-- Si 'numero_mesa' no existe en tu BD actual, puedes borrar esa columna del INSERT.
-- Asumiendo que la BD pide 'numero_mesa':
INSERT INTO mesas (nombre_mesa, numero_mesa, capacidad, ubicacion, estado) VALUES
('Mesa 1 (Ventana)', 1, 4, 'Salón Principal', 'LIBRE'),
('Mesa 2 (Centro)', 2, 4, 'Salón Principal', 'OCUPADA'),
('Mesa 3 (Grande)', 3, 8, 'Salón Principal', 'RESERVADA'),
('Mesa 4 (Rincón)', 4, 2, 'Salón Principal', 'LIBRE'),
('Mesa VIP', 5, 10, 'Sala Privada', 'LIBRE'),
('Barra 1', 6, 2, 'Barra', 'LIBRE'),
('Barra 2', 7, 2, 'Barra', 'LIBRE');

-- 5. TARIFAS LUDOTECA
INSERT INTO tarifas_ludoteca (nombre, precio, duracion_minutos, activa, descripcion) VALUES
('Tarifa Hora Estándar', 3.00, 60, 1, 'Precio por persona y hora'),
('Tarifa Plana Tarde', 5.00, 240, 1, 'Válida de 16:00 a 20:00'),
('Pase Diario', 10.00, 1440, 1, 'Juega todo el día sin límite');

-- 6. COPIAS DE JUEGOS
-- Vinculamos a juegos existentes (IDs dinámicos)
INSERT INTO juegos_copia (id_juego, codigo_referencia, estado, fecha_adquisicion, notas)
SELECT id_juego, CONCAT('CAT-', id_juego, '-01'), 'DISPONIBLE', CURDATE(), 'Caja un poco desgastada' FROM juegos LIMIT 1;

INSERT INTO juegos_copia (id_juego, codigo_referencia, estado, fecha_adquisicion)
SELECT id_juego, CONCAT('CAT-', id_juego, '-02'), 'EN_USO', CURDATE() FROM juegos LIMIT 1 OFFSET 1;

-- 7. SESIONES DE MESA
-- Crea una sesión activa en la Mesa 2
INSERT INTO sesiones_mesa (id_mesa, inicio, estado)
SELECT id_mesa, NOW(), 'ACTIVA' FROM mesas WHERE nombre_mesa = 'Mesa 2 (Centro)' LIMIT 1;
