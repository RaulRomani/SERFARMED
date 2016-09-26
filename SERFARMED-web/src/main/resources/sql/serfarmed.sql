
-- Versión del servidor: 10.1.8-MariaDB
-- Versión de PHP: 5.5.30
/** NOTAS
  Campo null by default
**/

--
-- Base de datos: `SERFARMED`
--
DROP DATABASE IF EXISTS serfarmed_clinic;
CREATE DATABASE IF NOT EXISTS `serfarmed_clinic` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `serfarmed_clinic`;

-- --------------------------------------------------------
--
-- Estructura de tabla para la tabla `cliente`
--

CREATE TABLE `cliente` (
  `idCliente` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL, -- nombre o razon social
  `apellido` varchar(100),
  `DNI` char(8), -- se registra con null, cuando es persona juridica
  `RUC` char(11),
  `codAsegurado` char(11),

  `direccion` varchar(100),
  `celular` varchar(20),
  `telefono` varchar(20),
  `sexo` varchar(10),
  `edad` varchar(3),

  `correo` varchar(50),
  `lugarNacimiento` varchar(100),
  `fechaNacimiento` date,
  `categoria` varchar(10), -- general, vip, impresion //Para sacar precio de gigantografia, solo puede ver/modificar el administrador
  `fechaCreacion` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '+ read, - update', 
  
  PRIMARY KEY (idCliente)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



-- --------------------------------------------------------
--
-- Estructura de tabla para la tabla `personal`
--

CREATE TABLE `personal` (
  `idPersonal` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `apellido` varchar(100) NOT NULL,
  `cargo` varchar(40) NOT NULL,
  `DNI` char(8) NOT NULL,
  `sueldo` decimal(8,2),
  `tipoComision` varchar(50),-- porcentaje, monto, N/A
  `comision` decimal(6,2),
  `especialidad` varchar(100),
  `correo` varchar(50),
  `direccion` varchar(100),
  `lugarNacimiento` varchar(200),
  `fechaNacimiento` date,
  `celular` varchar(20),
  `sexo` varchar(10),
  `edad` varchar(3),
  `fechaCreacion` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '+ read, - update', 

  PRIMARY KEY (idPersonal)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------
--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `idUsuario` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `idPersonal` int(11) unsigned NOT NULL,

  `username` varchar(100) NOT NULL,
  `password` char(128) NOT NULL,
  `authority` varchar(50) NOT NULL DEFAULT 'ROLE_CAJERO', -- administrador, vendedor, cajero, cliente(web)
  `enabled` tinyint(1) DEFAULT '0', -- activo|baneado //en caso de web
  `nota` varchar(200),
  PRIMARY KEY (idUsuario),
  FOREIGN KEY (idPersonal) REFERENCES personal(idPersonal)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;





-- --------------------------------------------------------
--
-- Estructura de tabla para la tabla `pago`
--

CREATE TABLE `pago` (
  `idPago` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `idUsuario` int(11) unsigned, -- Null cuando lo crea el servidor
  `idPersonal` int(11) unsigned NOT NULL,

  `monto` decimal(8,2) NOT NULL,
  `descripcion` varchar(200),
  
  `fechaHora` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'fecha automático tanto en inserciones pero NO en actualizaciones',

  `tipo` varchar(11) DEFAULT 'ADELANTO' NOT NULL, -- tipo: ADELANTO, PAGODOCTOR MENSUALIDAD   //Los adelandos van como egresos diario y los sueldos van como egresos mensuales

  PRIMARY KEY (idPago),
  FOREIGN KEY (idPersonal) REFERENCES personal(idPersonal),
  FOREIGN KEY (idUsuario) REFERENCES usuario(idUsuario)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------
--
-- Estructura de tabla para la tabla `venta`
--

CREATE TABLE `venta` (
  `idVenta` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `idUsuario` int(11) unsigned NOT NULL,
  `idCliente` int(11) unsigned,  -- una venta sin cliente registrado

  `subtotal` decimal(8,2) NOT NULL,  -- depende de si hay descuento
  `descuento` decimal(8,2) NOT NULL,  -- si no hay descuento : 0.00

  `total` decimal(8,2) NOT NULL,
  `fechaHora` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'En caso de crédito se actualiza la fecha al cancelar las cuotas',
  `formapago` varchar(20) DEFAULT 'CONTADO' NOT NULL,
  `comprobante` varchar(20) DEFAULT 'BOLETA' NOT NULL,
  `nroComprobante` varchar(10),
  `serie` varchar(4),
  `estado` varchar(20) DEFAULT 'CANCELADO' NOT NULL, -- cancelado, credito, espera: no cancelado, anulado(only rol administrador, por Mantenimiento)

  PRIMARY KEY (idVenta),
  FOREIGN KEY (idUsuario) REFERENCES usuario(idUsuario),
  FOREIGN KEY (idCliente) REFERENCES cliente(idCliente),
  CONSTRAINT chk_venta_formapago
  CHECK (formapago IN ('CONTADO', 'CRÉDITO')),  -- DEPOSITO, TARJETA (WEB)
  CONSTRAINT chk_venta_comprobante
  CHECK (comprobante IN ('BOLETA', 'FACTURA')) -- Recibos que se imprimen: GUIA DE REMICION, NOTA DE CREDITO
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



-- --------------------------------------------------------
--
-- Estructura de tabla para la tabla `proveedor`
--

CREATE TABLE `proveedor` (
  `idProveedor` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `razonSocial` varchar(60) NOT NULL,
  `RUC` char(11) NOT NULL,

  `direccion` varchar(100),
  `telefono` varchar(20),
  `celular` varchar(20),
  `correo` varchar(30),
  `paginaWeb` varchar(30),
  `fechaCreacion` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '+ read, - update', 
  `Nota` varchar(200),
  
  PRIMARY KEY (idProveedor)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- --------------------------------------------------------
--
-- Estructura de tabla para la tabla `compra`
--

CREATE TABLE `compra` (
  `idCompra` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `idUsuario` int(11) unsigned NOT NULL,
  `idProveedor` int(11) unsigned NOT NULL,
  
  `total` decimal(8,2) NOT NULL,
  `fechaHora` TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 

  -- `formapago` varchar(20) DEFAULT 'CONTADO' NOT NULL,
  `comprobante` varchar(20) DEFAULT 'FACTURA',
  `serie` varchar(20),
  `nroComprobante` varchar(20),
  -- pagado, espera: no pagado, anulado(only rol administrador, por Mantenimiento)
  `estado` varchar(20) DEFAULT 'ESPERA' NOT NULL, 

  PRIMARY KEY (idCompra),
  FOREIGN KEY (idUsuario) REFERENCES usuario(idUsuario),
  FOREIGN KEY (idProveedor) REFERENCES proveedor(idProveedor),
  CONSTRAINT chk_venta_formapago
  CHECK (formapago IN ('CONTADO', 'CRÉDITO')),  -- DEPOSITO, TARJETA (WEB)
  CONSTRAINT chk_venta_comprobante
  CHECK (comprobante IN ('BOLETA', 'FACTURA')) -- Recibos que se imprimen: GUIA DE REMICION, NOTA DE CREDITO
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- --------------------------------------------------------
--
-- Estructura de tabla para la tabla `operacion`
--

CREATE TABLE `operacion` (

  `idOperacion` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `idUsuario` int(11) unsigned NOT NULL,

  `tipo` varchar(20) DEFAULT 'EGRESO', -- INGRESO, EGRESO y GASTO
  `fechaHora` TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
  -- `formapago` varchar(20) DEFAULT 'CONTADO' NOT NULL,
  `comprobante` varchar(20) DEFAULT 'FACTURA',
  `serie` varchar(20),
  `nroComprobante` varchar(20),

  `descripcion` varchar(200),
  `monto` decimal(8,2) NOT NULL,

  PRIMARY KEY (idOperacion),
  FOREIGN KEY (idUsuario) REFERENCES usuario(idUsuario),
  CONSTRAINT chk_operacion_tipo
  CHECK (tipo IN ('INGRESO', 'EGRESO', 'GASTO'))  
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



/*==============================================================*/
/* Table: categoria                                             */
/*==============================================================*/
create table categoria
(
   idCategoria int(11) unsigned NOT NULL AUTO_INCREMENT,
   nombre varchar(25) not null,
   descripcion varchar(150),
   primary key (idCategoria)
)ENGINE=INNODB;


-- --------------------------------------------------------
--
-- Estructura de tabla para la tabla `servicio`
--

CREATE TABLE `servicio` (
  `idServicio` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `idCategoria` int(11) unsigned NOT NULL,
  `nombre` varchar(200) NOT NULL,
  `precio` decimal(8,2), -- incluido IGV
  `descripcion` varchar(200),

  PRIMARY KEY (idServicio),
  FOREIGN KEY (idCategoria) REFERENCES categoria(idCategoria)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- --------------------------------------------------------
--
-- Estructura de tabla para la tabla `servicio`
--

CREATE TABLE `producto` (
  `idProducto` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `idCategoria` int(11) unsigned NOT NULL,
  `nombre` varchar(200) NOT NULL,
  `precio` decimal(8,2) NOT NULL, -- incluido IGV
  `descripcion` varchar(200),

  PRIMARY KEY (idProducto),
  FOREIGN KEY (idCategoria) REFERENCES categoria(idCategoria)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------
--
-- Estructura de tabla para la tabla `detallecompra`
--

CREATE TABLE `detallecompra` (
  `idDetalleCompra` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `idCompra` int(11) unsigned NOT NULL,
  `idProducto` int(11) unsigned NOT NULL,
  
  `cantidad` int(4) unsigned NOT NULL,
  `importe` decimal(8,2) unsigned NOT NULL,
  `recibido` boolean NOT NULL, -- true, false
  
  PRIMARY KEY (idDetalleCompra),
  FOREIGN KEY (idCompra) REFERENCES compra(idCompra),
  FOREIGN KEY (idProducto) REFERENCES producto(idProducto)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------
--
-- Estructura de tabla para la tabla `servicioventa`
--

CREATE TABLE `servicioventa` (
  `idServicioVenta` int(11) unsigned NOT NULL AUTO_INCREMENT,  
  `idVenta` int(11) unsigned NOT NULL,
  `idPersonal` int(11) unsigned NOT NULL,
  `idServicio` int(11) unsigned NOT NULL,

  `cantidad` int(2) NOT NULL,
  `precio` decimal(8,2) NOT NULL, -- incluido IGV
  `importe` decimal(8,2) NOT NULL,

  `tipoComision` varchar(50),-- porcentaje, monto, N/A
  `comision` decimal(6,2),
  `sePago` varchar(3),  -- SI, NO o N/A  pargar hoy
  `pagado` boolean default 0, -- campo axuliar para saber si se pago al doctor
  PRIMARY KEY (idServicioVenta),
  FOREIGN KEY (idVenta) REFERENCES venta(idVenta),
  FOREIGN KEY (idPersonal) REFERENCES personal(idPersonal),
  FOREIGN KEY (idServicio) REFERENCES servicio(idServicio)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `crédito`
--

CREATE TABLE `credito` (
  
  `idCuota` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `IdVenta` int(11) unsigned NOT NULL, 
  `totalcuotas` int(2) NOT NULL, -- Total de cuotas a pagar
  `cuotaspagado` int(2) NOT NULL, -- Cuotas que se ha pagado
  `fechaHora` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'fecha automático tanto en inserciones pero NO en actualizaciones',
  `plazo` varchar(100) NOT NULL,
  `inicial` decimal(8,2) NOT NULL,
  `importe` decimal(8,2) NOT NULL,
  `observacion` varchar(150),

  PRIMARY KEY (idCuota),
  CONSTRAINT chk_cuota_plazo
  CHECK (plazo IN ('QUINCENAL', 'MENSUAL', 'DIA')),
  FOREIGN KEY (IdVenta) REFERENCES venta(IdVenta)

) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `saldoinicial`
--

CREATE TABLE `saldoinicial` (
  
  `idSaldoinicial` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `fecha` DATE,
  `saldoinicial` decimal(5,2) NOT NULL,

  PRIMARY KEY (idSaldoinicial)

) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `personal`
--

INSERT INTO `personal` (`idPersonal`, `nombre`, `apellido`, `cargo`, `DNI`, `sueldo`, `tipoComision`, `comision`, `especialidad`, `correo`, `direccion`, `lugarNacimiento`, `fechaNacimiento`, `celular`, `sexo`, `edad`, `fechaCreacion`) VALUES
(1, 'Raúl', 'Romaní Flores', 'Desarrollador', '47830392', '3000.00', NULL, NULL, 'Desarrollador', NULL, 'Jr. Ucayali 456', NULL, NULL, '', NULL, NULL, CURRENT_TIMESTAMP),
(2, 'GABRIEL', 'ALFARO SALCEDO', 'Doctor', '43546789', '4000.00', 'PORCENTAJE', '70.00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, CURRENT_TIMESTAMP),
(3, 'ELENA', 'ALVAN CARDENAS', 'Doctor', '43546789', '4500.00', 'PORCENTAJE', '70.00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, CURRENT_TIMESTAMP),
(4, 'AMETH GALINDO', 'ALVAREZ FLORES', 'Doctor', '43546789', '6000.00', 'PORCENTAJE', '70.00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, CURRENT_TIMESTAMP),
(5, 'RONALD GUSTAVO', 'APARCANA VENTURA', 'Doctor', '43546789', '4000.00', 'PORCENTAJE', '71.43', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, CURRENT_TIMESTAMP),
(6, 'JACK ANDRES', 'BELTRAN TORRES', 'Doctor', '43546789', '4500.00', 'N/A', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, CURRENT_TIMESTAMP),
(7, 'SERVICO PROPIO', '', 'Doctor', '43546789', '0.00', 'N/A', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, CURRENT_TIMESTAMP),
(8, 'RISTER ALBERTO', 'BRUNNER MELENDEZ', 'Doctor', '43546789', '0.00', 'N/A', NULL, '', '', '', '', NULL, '', '', '', CURRENT_TIMESTAMP),
(9, 'OSCAR', 'CABRERA BARRIOS', 'Doctor', '43546789', '0.00', 'PORCENTAJE', '70.00', '', '', '', '', NULL, '', '', '', CURRENT_TIMESTAMP),
(10, 'ISABEL', 'CAHUA LEON', 'Doctor', '43546789', '0.00', 'PORCENTAJE', '40.00', '', '', '', '', NULL, '', '', '', CURRENT_TIMESTAMP),
(11, 'AARON', 'CALDERON SOTO', 'Doctor', '43546789', '0.00', 'N/A', NULL, '', '', '', '', NULL, '', '', '', CURRENT_TIMESTAMP),
(12, 'ALL HERBERT', 'CASTRO HUAMAN', 'Doctor', '43546789', '0.00', 'N/A', NULL, '', '', '', '', NULL, '', '', '', CURRENT_TIMESTAMP),
(13, 'ADELAIDA', 'CASTRO RAEZ', 'Doctor', '43546789', '0.00', 'PORCENTAJE', '0.00', '', '', '', '', NULL, '', '', '', CURRENT_TIMESTAMP),
(14, 'IVAN', 'CONDORI RIVERA', 'Doctor', '43546789', '0.00', 'PORCENTAJE', '70.00', '', '', '', '', NULL, '', '', '', CURRENT_TIMESTAMP),
(15, 'RAFAEL', 'DEL AGUILA FLORES', 'Doctor', '43546789', '0.00', 'PORCENTAJE', '71.43', '', '', '', '', NULL, '', '', '', CURRENT_TIMESTAMP),
(16, 'OSCAR', 'CORNEJO CHAVEZ', 'Doctor', '43546789', '0.00', 'PORCENTAJE', '70.00', '', '', '', '', NULL, '', '', '', CURRENT_TIMESTAMP),
(17, 'ALBERTO SEGUNDO', 'ESCUDERO SALAS', 'Doctor', '43546789', '0.00', 'PORCENTAJE', '70.00', '', '', '', '', NULL, '', '', '', CURRENT_TIMESTAMP),
(18, 'ERWIN', 'FLORES DA SILVA', 'Doctor', '43546789', '0.00', 'PORCENTAJE', '70.00', '', '', '', '', NULL, '', '', '', CURRENT_TIMESTAMP),
(19, 'OSCAR ALBERTO', 'FLORES REYES', 'Doctor', '43546789', '0.00', 'PORCENTAJE', '70.00', '', '', '', '', NULL, '', '', '', CURRENT_TIMESTAMP),
(20, 'CESAR', 'FLORES SAONA', 'Doctor', '43546789', '0.00', 'PORCENTAJE', '70.00', '', '', '', '', NULL, '', '', '', CURRENT_TIMESTAMP),
(21, 'JOSE MARIA', 'FLORIAN  VARGAS', 'Doctor', '43546789', '0.00', 'PORCENTAJE', '70.00', '', '', '', '', NULL, '', '', '', CURRENT_TIMESTAMP),
(22, 'CESAR EDWIN', 'GALIANO GOMEZ', 'Doctor', '43546789', '0.00', 'PORCENTAJE', '70.00', '', '', '', '', NULL, '', '', '', CURRENT_TIMESTAMP),
(23, 'SEVERO ALFREDO', 'GARCIA CONTRERAS', 'Doctor', '43546789', '0.00', 'PORCENTAJE', '70.00', '', '', '', '', NULL, '', '', '', CURRENT_TIMESTAMP),
(24, 'JORGE BONIFACIO', 'GOMEZ CONDORI', 'Doctor', '43546789', '0.00', 'PORCENTAJE', '70.00', '', '', '', '', NULL, '', '', '', CURRENT_TIMESTAMP),
(25, 'ADELA', 'JAVIER CORI', 'Doctor', '43546789', '0.00', 'MONTO', '25.00', '', '', '', '', NULL, '', '', '', CURRENT_TIMESTAMP),
(26, 'LUIS ALBERTO', 'LAZO VILLAVERDE', 'Doctor', '43546789', '0.00', 'PORCENTAJE', '70.00', '', '', '', '', NULL, '', '', '', CURRENT_TIMESTAMP),
(27, 'ELIZABETH CRISTINA', 'LEON PADILLA', 'Doctor', '43546789', '0.00', 'PORCENTAJE', '70.00', '', '', '', '', NULL, '', '', '', CURRENT_TIMESTAMP),
(28, 'JUSTO LUIS', 'LOPEZ CARBONEL', 'Doctor', '43546789', '0.00', 'PORCENTAJE', '71.43', '', '', '', '', NULL, '', '', '', CURRENT_TIMESTAMP),
(29, 'JOSEFA', 'LOPEZ CARDENAS', 'Doctor', '43546789', '0.00', 'PORCENTAJE', '70.00', '', '', '', '', NULL, '', '', '', CURRENT_TIMESTAMP),
(30, 'WILLY', 'LORA ZEVALLOS', 'Doctor', '43546789', '0.00', 'PORCENTAJE', '70.00', '', '', '', '', NULL, '', '', '', CURRENT_TIMESTAMP),
(31, 'BEATRIZ MERCEDES', 'LUJAN DIVIZZIA', 'Doctor', '43546789', '0.00', 'PORCENTAJE', '70.00', '', '', '', '', NULL, '', '', '', CURRENT_TIMESTAMP),
(32, 'EFRAIN MARCOS', 'MALPARTIDA CONTRERAS', 'Doctor', '43546789', '0.00', 'PORCENTAJE', '70.00', '', '', '', '', NULL, '', '', '', CURRENT_TIMESTAMP),
(33, 'MARLON', 'MEDINA CASTRO', 'Doctor', '43546789', '0.00', 'PORCENTAJE', '70.00', '', '', '', '', NULL, '', '', '', CURRENT_TIMESTAMP),
(34, 'REBECA', 'MARTEL', 'Doctor', '43546789', '0.00', 'PORCENTAJE', '70.00', '', '', '', '', NULL, '', '', '', CURRENT_TIMESTAMP),
(35, 'DENISSE', 'MARTINEZ', 'Doctor', '43546789', '0.00', 'N/A', NULL, '', '', '', '', NULL, '', '', '', CURRENT_TIMESTAMP),
(36, 'EDITH', 'MEZA ATENCIA', 'Doctor', '43546789', '0.00', 'PORCENTAJE', '70.00', '', '', '', '', NULL, '', '', '', CURRENT_TIMESTAMP),
(37, 'CARLOS ABELARDO', 'MORALES HERNANDEZ', 'Doctor', '43546789', '0.00', 'PORCENTAJE', '70.00', '', '', '', '', NULL, '', '', '', CURRENT_TIMESTAMP),
(38, 'OSCAR SEGUNDO', 'MOSTACERO ZAVALETA', 'Doctor', '43546789', '0.00', 'N/A', '0.00', '', '', '', '', NULL, '', '', '', CURRENT_TIMESTAMP),
(39, 'CARMEN ROSARIO', 'MU?ANTE MENESES', 'Doctor', '43546789', '0.00', 'PORCENTAJE', '70.00', '', '', '', '', NULL, '', '', '', CURRENT_TIMESTAMP),
(40, 'CECILIA TATIANA', 'NALVARTE MENDOZA', 'Doctor', '43546789', '0.00', 'PORCENTAJE', '70.00', '', '', '', '', NULL, '', '', '', CURRENT_TIMESTAMP),
(41, ' ', 'PAREDES', 'Doctor', '43546789', '0.00', 'MONTO', '20.00', '', '', '', '', NULL, '', '', '', CURRENT_TIMESTAMP),
(42, 'SUSANA', 'PEZO ARMAS', 'Doctor', '43546789', '0.00', 'PORCENTAJE', '71.43', '', '', '', '', NULL, '', '', '', CURRENT_TIMESTAMP),
(43, 'GILDER', 'PINEDO PINEDO', 'Doctor', '43546789', '0.00', 'MONTO', '25.00', '', '', '', '', NULL, '', '', '', CURRENT_TIMESTAMP),
(44, 'LUIS HUMBERTO', 'RENGIFO NAVARRETE', 'Doctor', '43546789', '0.00', 'PORCENTAJE', '70.00', '', '', '', '', NULL, '', '', '', CURRENT_TIMESTAMP),
(45, 'RAMIRO HENRRY', 'RIVERA VALDIZAN', 'Doctor', '43546789', '0.00', 'PORCENTAJE', '70.00', '', '', '', '', NULL, '', '', '', CURRENT_TIMESTAMP),
(46, 'GLORIOSO', 'ROMERO HIDALGO', 'Doctor', '43546789', '0.00', 'N/A', NULL, '', '', '', '', NULL, '', '', '', CURRENT_TIMESTAMP),
(47, 'MARIO ', 'ROMERO LEY', 'Doctor', '43546789', '0.00', 'PORCENTAJE', '70.00', '', '', '', '', NULL, '', '', '', CURRENT_TIMESTAMP),
(48, 'ROBERTO', 'SALAZAR SALDA?A', 'Doctor', '43546789', '0.00', 'PORCENTAJE', '70.00', '', '', '', '', NULL, '', '', '', CURRENT_TIMESTAMP),
(49, 'LUCIO CLEMENTE', 'SALDA?A HERNANDEZ', 'Doctor', '43546789', '0.00', 'PORCENTAJE', '70.00', '', '', '', '', NULL, '', '', '', CURRENT_TIMESTAMP),
(50, 'FRANCISCO MARTIN', 'SAMANIEGO MORALES', 'Doctor', '43546789', '0.00', 'PORCENTAJE', '70.00', '', '', '', '', NULL, '', '', '', CURRENT_TIMESTAMP),
(51, 'ERIS', 'SANDOVAL CRUZ', 'Doctor', '43546789', '0.00', 'PORCENTAJE', '71.43', '', '', '', '', NULL, '', '', '', CURRENT_TIMESTAMP),
(52, 'DAVID EUSEBIO', 'TARAZONA YABAR', 'Doctor', '43546789', '0.00', 'PORCENTAJE', '70.00', '', '', '', '', NULL, '', '', '', CURRENT_TIMESTAMP),
(53, 'MILLERD AMARANTO', 'VALVERDE CUEVA', 'Doctor', '43546789', '0.00', 'PORCENTAJE', '70.00', '', '', '', '', NULL, '', '', '', CURRENT_TIMESTAMP);

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`idUsuario`, `idPersonal`, `username`, `password`, `authority`, `enabled`, `nota`) VALUES
(1, 1, 'romanidev@serfarmed.com', 'da0cd1ee3f6487dbf75650050238b0e5c3d7af47310570493c0443dd2a3848ac5fe0e3ba7b002d55c44587fc5403b275f7fd80ec0eee460768ed501f1b8581f9', 'ROLE_ADMIN', 1, NULL);  -- password1


--
-- Volcado de datos para la tabla `categoria`
--

INSERT INTO `categoria` (`idCategoria`, `nombre`, `descripcion`) VALUES
(1, 'General', '');



--
-- Volcado de datos para la tabla `proveedor`
--

INSERT INTO `proveedor` (`idProveedor`, `razonSocial`, `RUC`, `direccion`, `telefono`, `celular`, `correo`, `paginaWeb`, `fechaCreacion`, `Nota`) VALUES
(1, 'TEC', '233456766', '', '', '', 'tec@gmail.com', '', '2016-06-03 05:00:00', ''),
(2, 'Informix', '12345678901', '', '', '', '', '', '2016-06-03 05:00:00', '');


INSERT INTO `servicio` (`idServicio`, `idCategoria`, `nombre`,`precio`, `descripcion`) 
VALUES (NULL, '1', 'Audiometria', 40, NULL),
       (NULL, '1', 'Cardiología', 40, NULL),
       (NULL, '1', 'Cirugia general', 40, NULL),
       (NULL, '1', 'Dermatología', 40, NULL),
       (NULL, '1', 'Ecografia', 40, NULL),
       (NULL, '1', 'Endocrinología', 40, NULL),
       (NULL, '1', 'Espirometria', 40, NULL),
       (NULL, '1', 'Exa. Ocupacional', 40, NULL),
       (NULL, '1', 'Gastroenterología', 40, NULL),
       (NULL, '1', 'Ginecologia', 40, NULL),
       (NULL, '1', 'Laboratorio', 40, NULL),
       (NULL, '1', 'Medicamentos', 40, NULL),
       (NULL, '1', 'Medicina General', 40, NULL),
       (NULL, '1', 'Medicina Interna', 40, NULL),
       (NULL, '1', 'Neumología', 40, NULL),
       (NULL, '1', 'Neurología', 40, NULL),
       (NULL, '1', 'Nutricionista', 40, NULL),
       (NULL, '1', 'Odontología', 40, NULL),
       (NULL, '1', 'Oftamología', 40, NULL),
       (NULL, '1', 'Operación', 40, NULL),
       (NULL, '1', 'Otorrinolaringología', 40, NULL),
       (NULL, '1', 'Otros Servicios', 40, NULL),
       (NULL, '1', 'Pediatria', 40, NULL),
       (NULL, '1', 'Psicología', 40, NULL),
       (NULL, '1', 'Psiquiatria', 40, NULL),
       (NULL, '1', 'Rayos x', 40, NULL),
       (NULL, '1', 'Sala de operaciones', 40, NULL),
       (NULL, '1', 'Topico', 40, NULL),
       (NULL, '1', 'Traumatología', 40, NULL),
       (NULL, '1', 'Urología', 40, NULL),
       (NULL, '1', 'Vacunas', 40, NULL);




INSERT INTO `cliente` (`idCliente`, `nombre`, `apellido`, `DNI`, `RUC`, `codAsegurado`, `direccion`, `celular`, `telefono`, `sexo`, `edad`, `correo`, `lugarNacimiento`, `fechaNacimiento`, `categoria`, `fechaCreacion`) VALUES
(1, 'Magaly', 'Sanchez', '71829002', '10718290029', '', 'AV. MASISEA', '969934929', '579327', 'Femenino', '22', 'sgmgaly5@gmail.com', 'Padre Abad', '1993-11-05', NULL, '2016-08-21 16:40:59');