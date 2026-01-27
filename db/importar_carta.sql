-- ==============================================================================
-- SCRIPT DE IMPORTACIÓN DE CARTA Y SERVICIOS
-- ==============================================================================

-- Desactivar chequeo de FKs para permitir limpieza
SET FOREIGN_KEY_CHECKS = 0;

-- Limpiar tabla de productos
-- Usamos WHERE 1=1 para evitar advertencias de seguridad "Unsafe Query" en IntelliJ
DELETE FROM productos WHERE 1=1;
ALTER TABLE productos AUTO_INCREMENT = 1;

-- Reactivar chequeo
SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO productos (nombre, descripcion, categoria, precio, activo) VALUES

-- ------------------------------------------------------------------------------
-- 1. SERVICIOS DE LUDOTECA (Para facturación automática)
-- ------------------------------------------------------------------------------
('USO_LUDOTECA_ADULTO', 'Acceso a ludoteca (Tarifa Adulto)', 'SERVICIO', 2.50, TRUE),
('USO_LUDOTECA_NINO_6_13', 'Acceso a ludoteca (Niño 6-13 años)', 'SERVICIO', 1.50, TRUE),
('USO_LUDOTECA_NINO_0_5', 'Acceso a ludoteca (Niño 0-5 años)', 'SERVICIO', 0.00, TRUE),

-- ------------------------------------------------------------------------------
-- 2. RECURSOS INICIALES (Entrantes)
-- ------------------------------------------------------------------------------
('Patatas Bravas', NULL, 'COMIDA', 6.90, TRUE),
('Patatas Braviolis', NULL, 'COMIDA', 6.90, TRUE),
('Patatas 4 salsas', 'ketchup, mayonesa, barbacoa, queso', 'COMIDA', 7.50, TRUE),
('Palomitas de pollo', 'Con salsa barbacoa', 'COMIDA', 6.90, TRUE),
('Nuggets de pollo', 'Con salsa de queso cheddar', 'COMIDA', 7.50, TRUE),
('Fingers de pollo', 'Con salsa de miel y mostaza', 'COMIDA', 7.90, TRUE),
('Croquetas de Jamón (Media)', NULL, 'COMIDA', 5.00, TRUE),
('Croquetas de Jamón (Ración)', NULL, 'COMIDA', 8.50, TRUE),
('Croquetas de Cabrales (Media)', NULL, 'COMIDA', 5.00, TRUE),
('Croquetas de Cabrales (Ración)', NULL, 'COMIDA', 8.50, TRUE),
('Alitas de pollo adobadas', NULL, 'COMIDA', 8.50, TRUE),
('Nuggets SIN GLUTEN', NULL, 'COMIDA', 8.90, TRUE),
('Croquetas SIN GLUTEN', NULL, 'COMIDA', 8.90, TRUE),

-- ------------------------------------------------------------------------------
-- 3. JEFES FINALES (Hamburguesas y Principales)
-- ------------------------------------------------------------------------------
('GGBurguer', '100% Angus, pan brioche, cheddar, bacon, lechuga, tomate, cebolla, salsa GGB', 'COMIDA', 11.90, TRUE),
('Goat Quest', '100% Angus, pan brioche, queso cabra, cebolla caramelizada, rucula, salsa miel mostaza', 'COMIDA', 12.90, TRUE),
('Chicken Meeple', 'Pollo rebozado, pan brioche, lechuga, tomate, mayonesa', 'COMIDA', 11.90, TRUE),
('Combo Filete de pollo', 'Con ensalada y patatas fritas', 'COMIDA', 11.90, TRUE),

-- Extras
('Extra Pepinillo', NULL, 'COMIDA', 1.00, TRUE),
('Extra Bacon', NULL, 'COMIDA', 1.00, TRUE),
('Extra Queso', NULL, 'COMIDA', 1.00, TRUE),
('Extra Cebolla frita', NULL, 'COMIDA', 1.00, TRUE),
('Extra Cebolla caramelizada', NULL, 'COMIDA', 1.00, TRUE),

-- ------------------------------------------------------------------------------
-- 4. ENSALADA DE PUNTOS
-- ------------------------------------------------------------------------------
('Ensalada Mixta', 'Lechuga, tomate, cebolla, atún', 'COMIDA', 7.90, TRUE),
('Ensalada César', 'Lechuga, pollo, queso, picatostes, salsa', 'COMIDA', 8.90, TRUE),
('Ensalada Cabra', 'Lechuga, queso cabra, cebolla frita, nueces, vinagre balsámico', 'COMIDA', 8.90, TRUE),

-- ------------------------------------------------------------------------------
-- 5. MEEPLE KIDS
-- ------------------------------------------------------------------------------
('Menú Kids Nuggets', 'Con patatas fritas', 'COMIDA', 6.50, TRUE),
('Menú Kids Palomitas de pollo', 'Con patatas fritas', 'COMIDA', 6.50, TRUE),
('Menú Kids Sándwich mixto', 'Con patatas fritas', 'COMIDA', 6.50, TRUE),

-- ------------------------------------------------------------------------------
-- 6. RECOMPENSAS DULCES (Postres)
-- ------------------------------------------------------------------------------
('Final Explosion', 'Volcán de Chocolate con helado de vainilla', 'POSTRE', 5.90, TRUE),
('Cheese Throne', 'Cheesecake con mermelada', 'POSTRE', 5.90, TRUE),

-- ------------------------------------------------------------------------------
-- 7. BEBIDAS (Sin Alcohol)
-- ------------------------------------------------------------------------------
('Agua', NULL, 'BEBIDA', 2.20, TRUE),
('Agua con gas', NULL, 'BEBIDA', 2.90, TRUE),
('Coca-Cola', NULL, 'BEBIDA', 2.70, TRUE),
('Coca-Cola Zero', NULL, 'BEBIDA', 2.70, TRUE),
('Coca-Cola Zero Zero', NULL, 'BEBIDA', 2.70, TRUE),
('Fanta Naranja', NULL, 'BEBIDA', 2.70, TRUE),
('Fanta Limón', NULL, 'BEBIDA', 2.70, TRUE),
('Sprite', NULL, 'BEBIDA', 2.70, TRUE),
('Fuze Tea', NULL, 'BEBIDA', 2.90, TRUE),
('Aquarius Naranja', NULL, 'BEBIDA', 2.90, TRUE),
('Aquarius Limón', NULL, 'BEBIDA', 2.90, TRUE),
('Casera', NULL, 'BEBIDA', 2.70, TRUE),
('Tónica', NULL, 'BEBIDA', 2.70, TRUE),
('Monster', NULL, 'BEBIDA', 3.50, TRUE),
('Néctar naranja', NULL, 'BEBIDA', 2.70, TRUE),
('Néctar melocotón', NULL, 'BEBIDA', 2.70, TRUE),
('Batido de chocolate', NULL, 'BEBIDA', 2.70, TRUE),
('Nesquik', NULL, 'BEBIDA', 2.00, TRUE),
('Chocolate a la taza', NULL, 'BEBIDA', 2.50, TRUE),

-- Cafés e Infusiones
('Café solo', NULL, 'BEBIDA', 1.60, TRUE),
('Café americano', NULL, 'BEBIDA', 1.60, TRUE),
('Cortado', NULL, 'BEBIDA', 1.70, TRUE),
('Café con leche', NULL, 'BEBIDA', 1.80, TRUE),
('Bombón', NULL, 'BEBIDA', 2.30, TRUE),
('Infusiones', NULL, 'BEBIDA', 1.70, TRUE),

-- ------------------------------------------------------------------------------
-- 8. BEBIDAS ALCOHÓLICAS
-- ------------------------------------------------------------------------------
-- Cervezas
('Caña', NULL, 'ALCOHOL', 2.20, TRUE),
('Copa (Doble)', NULL, 'ALCOHOL', 3.40, TRUE),
('Maceta (Medio)', NULL, 'ALCOHOL', 4.10, TRUE),
('Tercio Mahou 5*', NULL, 'ALCOHOL', 3.50, TRUE),
('Tercio 1906', NULL, 'ALCOHOL', 3.80, TRUE),
('Tercio 0,0', NULL, 'ALCOHOL', 3.50, TRUE),
('Coronita', NULL, 'ALCOHOL', 3.50, TRUE),

-- Vinos y Vermú
('Tinto Verano', NULL, 'ALCOHOL', 3.50, TRUE),
('Beronia Crianza', NULL, 'ALCOHOL', 3.60, TRUE),
('Beronia Verdejo', NULL, 'ALCOHOL', 3.60, TRUE),
('Ribera del Duero', NULL, 'ALCOHOL', 3.60, TRUE),
('Alma', NULL, 'ALCOHOL', 3.00, TRUE),
('Vermú', NULL, 'ALCOHOL', 3.80, TRUE),

-- Copas y Licores
('Copazos', 'Brugal, Barceló, Befeeter, Tanqueray, Absolut, Red Label, Jameson, Ballantines, Puerto de Indias', 'ALCOHOL', 7.00, TRUE),
('Hierbas', NULL, 'ALCOHOL', 4.50, TRUE),
('Crema de Orujo', NULL, 'ALCOHOL', 4.50, TRUE),
('Café con crema de orujo', NULL, 'ALCOHOL', 3.20, TRUE),
('Chupito', NULL, 'ALCOHOL', 2.50, TRUE);
