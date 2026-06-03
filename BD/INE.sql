create database CITAS_INE
USE CITAS_INE
go

create table Entidades (
 id int primary key not null,
 nombre varchar(20) not null,
 clave varchar(2) not null
 )
 go

create table Municipio (
 id int primary key,
 nombre varchar(20) not null,
 idEntidad int
 )
 go
 
 create table Modulo (
 id int PRIMARY KEY,
 nombre varchar(150) not null,
 direccion varchar(160),
 referencia varchar(160),
 horarioSem varchar(100),
 horarioFin varchar(100),		
 idMunicipio int not null,
 )
 go





 SELECT * FROM Modulo

alter table Modulo
add constraint FK_ModuloMunicipio foreign key (idMunicipio) references Municipio (id)
go
alter table Modulo
add constraint FK_ModuloEntidades foreign key (idEntidad) references Entidades (id)
go


 create table Usuarios(
 --curp varchar(18) not null primary key,
 id int identity (1,1) not null,
 nombre varchar(60) not null,
 aPaterno varchar(60) not null,
 aMaterno varchar(60) not null,
 fechaNac date not null,
 entidadNac int not null,
 sexo char not null,
 telefono varchar (17),
 email  varchar (150)
 )
 go
 alter table Usuarios add curp varchar(18)
 drop table Usuarios

 create table Tramites(
 id int not null identity (1,1) primary key,
 nombre varchar(20),
 descripcion varchar(60)
 )
 go

 insert into Tramites (nombre, descripcion)values ('INE PRIMERA VEZ', 'Trámite de credencial para votar por primera vez'),
 ('RENOVAR CREDENCIAL', 'Renovación de credencial'),
('REPOSICIÓN', 'Reposición de credencial por robo o extravío'),
('CAMBIO/CORRECIÓN', 'Correcciones o cambios en domicilio, nombre, etc');

 create table Documentos (
 id int not null identity (1,1) primary key,
 tipo varchar(30),
 descripcion varchar(60)
 )
 go
 SELECT * FROM Usuarios ORDER BY curp

INSERT INTO Documentos (descripcion, tipo) values ('INE', 'Identidicación con fotografía'),
('PASAPORTE', 'Identidicación con fotografía'),
('LICENCIA DE CONDUCIR', 'Identidicación con fotografía'),
('CARTILLA MILITAR', 'Identidicación con fotografía'),
('ACTA DE NACIMIENTO', 'Documento de Nacionalidad'),
('CARTA DE NATURALIZACIÓN', 'Documento de Nacionalidad'),
('RECIBO DE LUZ, TELÉFONO O PREDIAL', 'Comprobante de domicilio'),
('ESTADO DE CUENTA', 'Comprobante de domicilio');

 
 create table Citas (
 id int not null identity (1,1) primary key,
 fecha date not null,
 hora time not null,
 curp varchar(18) not null,
 idModulo int not null,
 idTramite int not null,
 nacionalidad int,
 identificacion int,
 domicilio int,
 estado varchar(15)
 ) 
go



alter table Citas
add constraint FK_CitasUsuarios foreign key (curp) references Usuarios (curp)
go

alter table Citas
add constraint FK_CitasModulo foreign key (idModulo) references Modulo (id)
go

alter table Citas
add constraint FK_CitasTramite foreign key (idTramite) references Tramites (id)
go


INSERT INTO Entidades VALUES(1,'Aguascalientes','AS')
INSERT INTO Entidades VALUES(2,'Baja California','BC')
INSERT INTO Entidades VALUES(3,'Baja California Sur','BS')
INSERT INTO Entidades VALUES(4,'Campeche','CC')
INSERT INTO Entidades VALUES(5,'Coahuila de Zaragoza','CL')
INSERT INTO Entidades VALUES(6,'Colima','CM')
INSERT INTO Entidades VALUES(7,'Chiapas','CS')
INSERT INTO Entidades VALUES(8,'Chihuahua','CH')
INSERT INTO Entidades VALUES(9,'Ciudad de México','DF')
INSERT INTO Entidades VALUES(10,'Durango','DG')
INSERT INTO Entidades VALUES(11,'Guanajuato','GT')
INSERT INTO Entidades VALUES(12,'Guerrero','GR')
INSERT INTO Entidades VALUES(13,'Hidalgo','HG')
INSERT INTO Entidades VALUES(14,'Jalisco','JC')
INSERT INTO Entidades VALUES(15,'México','MC')
INSERT INTO Entidades VALUES(16,'Michoacán de Ocampo','MN')
INSERT INTO Entidades VALUES(17,'Morelos','MS')
INSERT INTO Entidades VALUES(18,'Nayarit','NT')
INSERT INTO Entidades VALUES(19,'Nuevo León','NL')
INSERT INTO Entidades VALUES(20,'Oaxaca','OC')
INSERT INTO Entidades VALUES(21,'Puebla','PL')
INSERT INTO Entidades VALUES(22,'Querétaro','QT')
INSERT INTO Entidades VALUES(23,'Quintana Roo','QR')
INSERT INTO Entidades VALUES(24,'San Luis Potosí','SP')
INSERT INTO Entidades VALUES(25,'Sinaloa','SL')
INSERT INTO Entidades VALUES(26,'Sonora','SR')
INSERT INTO Entidades VALUES(27,'Tabasco','TC')
INSERT INTO Entidades VALUES(28,'Tamaulipas','TS')
INSERT INTO Entidades VALUES(29,'Tlaxcala','TL')
INSERT INTO Entidades VALUES(30,'Veracruz','VZ')
INSERT INTO Entidades VALUES(31,'Yucatán','YN')
INSERT INTO Entidades VALUES(32,'Zacatecas','ZS')

select * from Municipio
insert into Municipio values ('0101','Si','1')
select * from Modulo
select * from Citas
insert into Citas values ('2026-03-03', '8:00', 'RAMN021021',10101,2, 1, 3, 10)
insert into modulo values ('190101','CARRETERA MIGUEL ALEMÁN 102 PLAZA COMERCIAL LAS AMÉRICAS LOCAL 102 Y 102 A ZONA CENTRO C.P. 66600','CARRETERA MIGUEL ALEMÁN 102 PLAZA COMERCIAL LAS AMÉRICAS LOCAL 102 Y 102 A ZONA CENTRO C.P. 66600', 'CASI ESQUINA CON CALLE ANDRÉS GUAJARDO A LA ALTURA DEL PUENTE DE APODACA Y JUNTO AL 7 ELEVEN', '08:00 - 20:00 - Lunes, Martes, Miércoles, Jueves, Viernes', '08:00 - 15:00 - Sábado','1901')

insert into Municipio values('101','Aguascalientes','1')
insert into Municipio values('102','Calvillo','1')
insert into Municipio values('103','Jesus Maria','1')
insert into Municipio values('104','Pabellon de arteaga','1')

insert into Municipio values('1901','Apodaca','19')
insert into Municipio values('1902','Cadereyta Jimenez','19')
insert into Municipio values('1903','Carmen','19')
insert into Municipio values('1904','Garcia','19')
insert into Municipio values('1905','Gral. Escobedo','19')
insert into Municipio values('1906','Gral. Zuzua','19')
insert into Municipio values('1907','Guadalupe','19')
insert into Municipio values('1908','Juarez','19')
insert into Municipio values('1909','Linares','19')
insert into Municipio values('1910','Monterrey','19')
insert into Municipio values('1911','Pesqueria','19')
insert into Municipio values('1912','Salinas Victoria','19')
insert into Municipio values('1913','San Nicolas de los Garza','19')
insert into Municipio values('1914','San Pedro Garza Gacía','19')
insert into Municipio values('1915','Santa Catarina','19')
insert into Municipio values('2001','Ciudad Ixtepec','20')
insert into Municipio values('2002','Heroica Cidad de Huajuapan','20')
insert into Municipio values('2003','Heroica Cidad de Juchitan de Zaragoza','20')
insert into Municipio values('2004','Heroica Cidad de Tlaxiaco','20')
insert into Municipio values('2005','Miahuatlan de Porfirio Diaz','20')
insert into Municipio values('2006','Oaxaca de Juarez','20')
insert into Municipio values('2007','Salina Cruz','20')
insert into Municipio values('2008','San Juan Bautista Tuxtepec','20')
insert into Municipio values('2009','San Pedro Mixtepec','20')
insert into Municipio values('2010','Santa Lucía del Camino','20')
insert into Municipio values('2011','Santiago Pinotepa Nacional','20')
insert into Municipio values('2012','Teotitlan de Flores Magón','20')
insert into Municipio values('2013','Tlacolula de Matamoros','20')
insert into Municipio values('2101','Acatlan','21')
insert into Municipio values('2102','Ajalapan','21')
insert into Municipio values('2103','Amozoc','21')
insert into Municipio values('2104','Atlixco','21')
insert into Municipio values('2105','Chalchicomula de Sesma','21')
insert into Municipio values('2106','Huauchinango','21')
insert into Municipio values('2107','Huejotzingo','21')
insert into Municipio values('2108','Izucar de Matamoros','21')
insert into Municipio values('2109','Libres','21')
insert into Municipio values('2110','Puebla','21')
insert into Municipio values('2111','San Martín Texmelucan','21')
insert into Municipio values('2112','San Pedro Cholula','21')
insert into Municipio values('2113','Tecamachalco','21')
insert into Municipio values('2114','Tehuacan','21')
insert into Municipio values('2115','Tepeaca','21')
insert into Municipio values('2116','Teziutlan','21')
insert into Municipio values('2117','Xicotepec','21')
insert into Municipio values('2118','Zacapoaxtla','21')
insert into Municipio values('2119','Zacatlan','21')

select * from Entidades
select * from Municipio
select * from Modulo

UPDATE Entidades set nombre = 'Zamora', clave = 'ZM' WHERE id = 4
Select m.nombre Municipio from Municipio m inner join Entidades e on m.idEntidad = e.id where e.nombre like 'Michoacán%'

select mo.id, mo.nombre nombre, mo.referencia referencia, mo.horarioSem horarioSem, mo.horarioFin horarioFin, mo.idMunicipio idMunicipio, e.id idEntidad from Modulo mo inner join Municipio m on mo.idMunicipio = m.id inner join Entidades e on m.idEntidad = e.id where mo.id=10101

select m.id idMunicipio from Entidades e inner join Municipio m on e.id=m.idEntidad
where e.nombre like 'baja california' and m.nombre like 'Tecate'

select * from Usuarios order by entidadNac


create database --nombreFragmento1--  
create database --nombreFragmento3--  
create database INE_SUR  
use INE_SUR
--Estados
INSERT INTO [192.168.0.232].[INE_NORTE].[dbo].[Entidades] SELECT * from Entidades where id < 10
INSERT INTO [192.168.0.108].[INE_CENTRO].[dbo].[Entidades] SELECT * from Entidades where id < 19 and id > 9 
INSERT INTO [127.0.0.1].[INE_SUR].[dbo].[Entidades] SELECT * from Entidades where id > 18

SELECT * FROM Entidades 
UNION
SELECT * FROM [192.168.0.232].[INE_NORTE].[dbo].[Entidades]
UNION
SELECT * FROM [192.168.0.108].[INE_CENTRO].[dbo].[Entidades]

--Municipios-
delete select * from Entidades where nombre like 'Zamora'
INSERT INTO [192.168.0.232].[INE_NORTE].[dbo].[Municipio] SELECT * from Municipio where id < 900
INSERT INTO [192.168.0.108].[INE_CENTRO].[dbo].[Municipio] SELECT * from Municipio where id < 1900 and id > 1000
INSERT INTO [127.0.0.1].[INE_SUR].[dbo].[Municipio] SELECT * from Municipio where id > 1900

SELECT * FROM Municipio
UNION
SELECT * FROM [192.168.0.232].[INE_NORTE].[dbo].[Municipio]
UNION
SELECT * FROM [192.168.0.108].[INE_CENTRO].[dbo].[Municipio]

 --Modulos--
 INSERT INTO [192.168.0.232].[INE_NORTE].[dbo].[Modulo] SELECT * from Modulo where id < 100000
 INSERT INTO [192.168.0.108].[INE_CENTRO].[dbo].[Modulo] SELECT * from Modulo where id < 190000 and id > 90000
 INSERT INTO [127.0.0.1].[INE_SUR].[dbo].[Modulo] SELECT * from Modulo where id > 190000
 select * from Municipio
SELECT * FROM Modulo
UNION
SELECT * FROM [192.168.0.232].[INE_NORTE].[dbo].[Entidades]
UNION
SELECT * FROM [192.168.0.108].[INE_CENTRO].[dbo].[Entidades]

 select * into INE_SUR.dbo.Entidades from Entidades where ID=1
 use INE_SUR
 select * from 192.168.0.1

Select curp, nombre, aPaterno, aMaterno, fechaNac, entidadNac, sexo, telefono, email from [127.0.0.1].[CITAS_INE].[dbo].[Usuarios] order by curp


SELECT * FROM Entidades
UNION
SELECT * FROM [192.168.0.232].[INE_NORTE].[dbo].[Entidades]
UNION
SELECT * FROM [192.168.0.108].[INE_CENTRO].[dbo].[Entidades]

use INE_SUR
