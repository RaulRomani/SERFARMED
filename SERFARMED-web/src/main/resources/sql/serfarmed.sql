
-- Versión del servidor: 10.1.8-MariaDB
-- Versión de PHP: 5.5.30
/** NOTAS
  Campo null by default
**/

--
-- Base de datos: `ULTRACOLOR`
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
  `apelllido` varchar(100),
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
  `sueldo` decimal(8,2) NOT NULL,
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
-- Estructura de tabla para la tabla `comision`
--

CREATE TABLE `comision` (
  `idComision` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `idPersonal` int(11) unsigned NOT NULL,

  `tipo` varchar(50),-- porcentaje, monto, N/A
  `comision` decimal(6,2),
  `nota` varchar(200),
  PRIMARY KEY (idComision),
  FOREIGN KEY (idPersonal) REFERENCES personal(idPersonal)
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
  `idUsuario` int(11) unsigned NOT NULL,
  `idPersonal` int(11) unsigned NOT NULL,

  `monto` decimal(8,2) NOT NULL,
  `descripcion` varchar(200),
  
  `fechaHora` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'fecha automático tanto en inserciones pero NO en actualizaciones',

  `tipo` varchar(11) DEFAULT 'ADELANTO' NOT NULL, -- tipo: ADELANTO, MENSUALIDAD   //Los adelandos van como egresos diario y los sueldos van como egresos mensuales

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
-- Estructura de tabla para la tabla `egresos`
--

CREATE TABLE `egresos` (
  `idEgresos` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `idUsuario` int(11) unsigned NOT NULL,
  
  
  `fechaHora` TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 

  -- `formapago` varchar(20) DEFAULT 'CONTADO' NOT NULL,
  `comprobante` varchar(20) DEFAULT 'FACTURA',
  `serie` varchar(20),
  `nroComprobante` varchar(20),

  `descripcion` varchar(200),
  `monto` decimal(8,2) NOT NULL,
  

  PRIMARY KEY (idEgresos),
  FOREIGN KEY (idUsuario) REFERENCES usuario(idUsuario)
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

CREATE TABLE `Servicio` (
  `idServicio` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `idCategoria` int(11) unsigned NOT NULL,
  `nombre` varchar(200) NOT NULL,
  `precio` decimal(8,2) NOT NULL, -- incluido IGV
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
  `idPersonal` int(11) unsigned NOT NULL,
  `idVenta` int(11) unsigned NOT NULL,
  `idServicio` int(11) unsigned NOT NULL,

  `cantidad` int(2) NOT NULL,
  `importe` decimal(8,2) NOT NULL,

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

INSERT INTO `personal` (`idPersonal`, `nombre`, `apellido`, `cargo`, `DNI`, `sueldo`,`especialidad`, `direccion`, `lugarNacimiento`, `fechaNacimiento`, `celular`, `sexo`, `edad`, `fechaCreacion`) VALUES
(1, 'Raúl', 'Romaní Flores', 'Desarrollador', '47830392', 3000.00 ,  'Desarrollador', 'Jr. Ucayali 456', NULL, NULL, '', NULL, NULL, '2016-05-14 07:46:35');


--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`idUsuario`, `idPersonal`, `username`, `password`, `authority`, `enabled`, `nota`) VALUES
(1, 1, 'romanidev@serfarmed.com', 'da0cd1ee3f6487dbf75650050238b0e5c3d7af47310570493c0443dd2a3848ac5fe0e3ba7b002d55c44587fc5403b275f7fd80ec0eee460768ed501f1b8581f9', 'ROLE_ADMIN', 1, NULL);  -- password1


--
-- Volcado de datos para la tabla `categoria`
--

INSERT INTO `categoria` (`idCategoria`, `nombre`, `descripcion`) VALUES
(1, 'Odotonlogía', '');



--
-- Volcado de datos para la tabla `proveedor`
--

INSERT INTO `proveedor` (`idProveedor`, `razonSocial`, `RUC`, `direccion`, `telefono`, `celular`, `correo`, `paginaWeb`, `fechaCreacion`, `Nota`) VALUES
(1, 'TEC', '233456766', '', '', '', 'tec@gmail.com', '', '2016-06-03 05:00:00', ''),
(2, 'Informix', '12345678901', '', '', '', '', '', '2016-06-03 05:00:00', '');


INSERT INTO `servicio` (`idServicio`, `idCategoria`, `nombre`, `precio`, `descripcion`) 
VALUES (NULL, '1', 'Audiometria', '30', NULL),
       (NULL, '1', 'Cardiología', '30', NULL),
       (NULL, '1', 'Cirugia general', '30', NULL),
       (NULL, '1', 'Dermatología', '30', NULL),
       (NULL, '1', 'Ecografia', '30', NULL),
       (NULL, '1', 'Endocrinología', '70', NULL),
       (NULL, '1', 'Espirometria', '30', NULL),
       (NULL, '1', 'Exa. Ocupacional', '365', NULL),
       (NULL, '1', 'Gastroenterología', '30', NULL),
       (NULL, '1', 'Ginecologia', '30', NULL),
       (NULL, '1', 'Laboratorio', '30', NULL),
       (NULL, '1', 'Medicamentos', '30', NULL),
       (NULL, '1', 'Medicina General', '30', NULL),
       (NULL, '1', 'Medicina Interna', '30', NULL),
       (NULL, '1', 'Neumología', '30', NULL),
       (NULL, '1', 'Neurología', '30', NULL),
       (NULL, '1', 'Nutricionista', '30', NULL),
       (NULL, '1', 'Odontología', '30', NULL),
       (NULL, '1', 'Oftamología', '30', NULL),
       (NULL, '1', 'Operación', '30', NULL),
       (NULL, '1', 'Otorrinolaringología', '30', NULL),
       (NULL, '1', 'Otros Servicios', '30', NULL),
       (NULL, '1', 'Pediatria', '30', NULL),
       (NULL, '1', 'Psicología', '30', NULL),
       (NULL, '1', 'Psiquiatria', '30', NULL),
       (NULL, '1', 'Rayos x', '30', NULL),
       (NULL, '1', 'Sala de operaciones', '30', NULL),
       (NULL, '1', 'Topico', '30', NULL),
       (NULL, '1', 'Traumatología', '30', NULL),
       (NULL, '1', 'Urología', '30', NULL),
       (NULL, '1', 'Vacunas', '30', NULL);

INSERT INTO `personal` (`idPersonal`, `nombre`, `apellido`, `cargo`, `DNI`, `sueldo`, `especialidad`, `correo`, `direccion`, `lugarNacimiento`, `fechaNacimiento`, `celular`, `sexo`, `edad`, `fechaCreacion`) 
VALUES (NULL, 'GABRIEL', 'ALFARO SALCEDO', 'Doctor', '43546789', '4000', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, CURRENT_TIMESTAMP),
       (NULL, 'ELENA', 'ALVAN CARDENAS', 'Doctor', '43546789', '4500', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, CURRENT_TIMESTAMP),
       (NULL, 'AMETH GALINDO', 'ALVAREZ FLORES', 'Doctor', '43546789', '6000', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, CURRENT_TIMESTAMP),
       (NULL, 'RONALD GUSTAVO', 'APARCANA VENTURA', 'Doctor', '43546789', '4000', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, CURRENT_TIMESTAMP),
       (NULL, 'JACK ANDRES', 'BELTRAN TORRES', 'Doctor', '43546789', '4500', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, CURRENT_TIMESTAMP),
       (NULL, 'ADELA', 'JAVIER CORI', 'Doctor', '43546789', '4000', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, CURRENT_TIMESTAMP),
       (NULL, 'GILDER', 'PINEDO PINEDO', 'Doctor', '43546789', '5000', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, CURRENT_TIMESTAMP),
       (NULL, 'SERVICO PROPIO', "", 'Doctor', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, CURRENT_TIMESTAMP);      


INSERT INTO `comision` (`idComision`, `idPersonal`, `tipo`, `comision`, `nota`) 
VALUES ('1', '1', 'PORCENTAJE', '70', NULL),
       ('2', '2', 'PORCENTAJE', '70', NULL),
       ('3', '3', 'PORCENTAJE', '70', NULL),
       ('4', '4', 'PORCENTAJE', '71.43', NULL),
       ('5', '5', 'N/A', NULL, NULL),
       ('6', '6', 'MONTO', '25', "Partes blandas"),
       ('7', '6', 'MONTO', '20', "Ecografo Dr. saldaña"),
       ('8', '7', 'MONTO', '20', NULL),
       ('9', '7', 'MONTO', '25', "Partes blandas");