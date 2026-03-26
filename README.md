# 🏢 Parking Colombia - Sistema de Gestión de Parqueadero

Sistema completo de gestión de parqueadero desarrollado con **Spring Boot** y **Java**, diseñado específicamente para el contexto colombiano. Permite administrar entradas y salidas de vehículos, gestionar clientes (mensualidad y ocasionales), controlar celdas y generar reportes de ingresos.

---

## 📋 Tabla de Contenidos

- [Características](#-características)
- [Tecnologías Utilizadas](#-tecnologías-utilizadas)
- [Requisitos Previos](#-requisitos-previos)
- [Instalación y Configuración](#-instalación-y-configuración)
- [Configuración de Base de Datos](#-configuración-de-base-de-datos)
- [Ejecución del Proyecto](#-ejecución-del-proyecto)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [API Endpoints](#-api-endpoints)
- [Credenciales de Acceso](#-credenciales-de-acceso)
- [Guía de Uso](#-guía-de-uso)
- [Solución de Problemas](#-solución-de-problemas)
- [Contribuciones](#-contribuciones)
- [Licencia](#-licencia)

---

## ✨ Características

### Para Todos los Usuarios
- ✅ **Registro de Entrada**: Registro automático de hora de ingreso, placa y marca
- ✅ **Registro de Salida**: Cálculo automático del valor a pagar basado en tiempo de estadía
- ✅ **Vehículos Activos**: Visualización en tiempo real de vehículos actualmente en el parqueadero
- ✅ **Mapa de Celdas**: Visualización gráfica del estado de todas las celdas (disponible, ocupada, mantenimiento)

### Para Administradores
- 👥 **Gestión de Usuarios**: Crear, editar y eliminar usuarios del sistema (administradores y cajeros)
- 📋 **Gestión de Clientes**: Registrar clientes ocasionales y de mensualidad
- 🅿️ **Gestión de Celdas**: Agregar nuevas celdas y cambiar su estado
- 📊 **Reportes**: Visualización de ingresos diarios y exportación a CSV

### Características Técnicas
- 🔄 **Actualización en Tiempo Real**: Los datos se actualizan automáticamente cada 15 segundos
- 💰 **Cálculo Automático**: Tarifa base de $3,000 COP por hora (primeras 10 horas) y tarifa de día completo
- 🎨 **Interfaz Moderna**: Diseño responsive con animaciones y feedback visual
- 🔐 **Seguridad**: Autenticación de usuarios con encriptación MD5

---

## 🛠️ Tecnologías Utilizadas

| Tecnología | Versión | Descripción |
|------------|---------|-------------|
| **Java** | 11+ | Lenguaje de programación principal |
| **Spring Boot** | 2.7.18 | Framework principal |
| **Spring MVC** | - | Patrón Modelo-Vista-Controlador |
| **Spring Data JPA** | - | Persistencia de datos |
| **Thymeleaf** | - | Motor de plantillas HTML |
| **MySQL** | 8.0+ | Base de datos relacional |
| **Maven** | 3.6+ | Gestor de dependencias |
| **jQuery** | 3.6.0 | Biblioteca JavaScript |
| **FontAwesome** | 6.0 | Iconos |

---

## 📦 Requisitos Previos

Antes de comenzar, asegúrate de tener instalado:

- **Java JDK 11** o superior
  ```bash
  java -version
•	MySQL Server 8.0 o superior
bash
mysql --version
•	Maven 3.6 o superior
bash
mvn -version
•	Git (opcional, para clonar el repositorio)
________________________________________
🚀 Instalación y Configuración
1. Clonar el Repositorio
bash
git clone https://github.com/tu-usuario/parking-colombia.git
cd parking-colombia
2. Configurar Base de Datos
Inicia MySQL y ejecuta el siguiente script para crear la base de datos y las tablas:
sql
CREATE DATABASE IF NOT EXISTS parking_colombia;
USE parking_colombia;

-- Tabla de usuarios del sistema
CREATE TABLE IF NOT EXISTS usuarios_sistema (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    rol ENUM('administrador', 'cajero') NOT NULL,
    telefono VARCHAR(20),
    estado BOOLEAN DEFAULT TRUE,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de clientes
CREATE TABLE IF NOT EXISTS clientes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    tipo_cliente ENUM('mensualidad', 'ocasional') NOT NULL,
    identificacion VARCHAR(20) UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    direccion TEXT,
    email VARCHAR(100),
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    fecha_vencimiento_mensualidad DATE,
    estado BOOLEAN DEFAULT TRUE
);

-- Tabla de celdas
CREATE TABLE IF NOT EXISTS celdas (
    id INT PRIMARY KEY AUTO_INCREMENT,
    numero_celda VARCHAR(10) UNIQUE NOT NULL,
    ubicacion VARCHAR(50),
    estado ENUM('disponible', 'ocupada', 'mantenimiento') DEFAULT 'disponible',
    tipo_vehiculo ENUM('auto') DEFAULT 'auto'
);

-- Tabla de vehículos
CREATE TABLE IF NOT EXISTS vehiculos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    placa VARCHAR(10) UNIQUE NOT NULL,
    marca VARCHAR(50) NOT NULL,
    modelo VARCHAR(50),
    color VARCHAR(30),
    cliente_id INT,
    FOREIGN KEY (cliente_id) REFERENCES clientes(id) ON DELETE SET NULL
);

-- Tabla de registros de parqueo
CREATE TABLE IF NOT EXISTS registros (
    id INT PRIMARY KEY AUTO_INCREMENT,
    vehiculo_id INT NOT NULL,
    celda_id INT NOT NULL,
    cliente_id INT,
    usuario_sistema_id INT,
    fecha_entrada DATETIME DEFAULT CURRENT_TIMESTAMP,
    fecha_salida DATETIME,
    horas_estadia INT,
    valor_pagado DECIMAL(10,2),
    metodo_pago ENUM('efectivo') DEFAULT 'efectivo',
    estado ENUM('activo', 'finalizado') DEFAULT 'activo',
    FOREIGN KEY (vehiculo_id) REFERENCES vehiculos(id),
    FOREIGN KEY (celda_id) REFERENCES celdas(id),
    FOREIGN KEY (cliente_id) REFERENCES clientes(id),
    FOREIGN KEY (usuario_sistema_id) REFERENCES usuarios_sistema(id)
);

-- Insertar usuarios por defecto
INSERT INTO usuarios_sistema (username, password, nombre, rol) VALUES 
('admin', MD5('admin123'), 'Administrador Principal', 'administrador'),
('cajero1', MD5('cajero123'), 'Carlos Pérez', 'cajero');

-- Insertar celdas iniciales
INSERT INTO celdas (numero_celda, ubicacion) VALUES 
('A01', 'Zona A - Nivel 1'), ('A02', 'Zona A - Nivel 1'), ('A03', 'Zona A - Nivel 1'), ('A04', 'Zona A - Nivel 1'),
('B01', 'Zona B - Nivel 1'), ('B02', 'Zona B - Nivel 1'), ('B03', 'Zona B - Nivel 2'), ('B04', 'Zona B - Nivel 2'),
('C01', 'Zona C - Nivel 2'), ('C02', 'Zona C - Nivel 2'), ('C03', 'Zona C - Nivel 2'), ('C04', 'Zona C - Nivel 2');
3. Configurar Credenciales de Base de Datos
Edita el archivo src/main/resources/application.properties y ajusta las credenciales de MySQL:
properties
spring.datasource.url=jdbc:mysql://localhost:3306/parking_colombia?useSSL=false&serverTimezone=America/Bogota&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=tu_contraseña
4. Compilar el Proyecto
bash
mvn clean install
________________________________________
🏃 Ejecución del Proyecto
Opción 1: Usando Maven
bash
mvn spring-boot:run
Opción 2: Usando el JAR generado
bash
java -jar target/parking-colombia-1.0.0.jar
Opción 3: Desde IntelliJ IDEA
1.	Abre el proyecto en IntelliJ IDEA
2.	Ejecuta la clase ParkingApplication.java
3.	O usa el botón de Run (Shift + F10)
Una vez iniciado, abre tu navegador y accede a:
text
http://localhost:8080
________________________________________
📁 Estructura del Proyecto
text
parking-colombia/
├── src/main/java/com/parking/
│   ├── ParkingApplication.java          # Clase principal
│   ├── controller/                       # Controladores MVC
│   │   ├── AuthController.java          # Autenticación
│   │   ├── ParkingController.java       # Registros entrada/salida
│   │   ├── AdminController.java         # Gestión admin
│   │   └── ApiController.java           # Endpoints REST
│   ├── model/                            # Entidades JPA
│   │   ├── Usuario.java
│   │   ├── Cliente.java
│   │   ├── Vehiculo.java
│   │   ├── Registro.java
│   │   └── Celda.java
│   ├── repository/                       # Repositorios JPA
│   │   ├── UsuarioRepository.java
│   │   ├── ClienteRepository.java
│   │   ├── VehiculoRepository.java
│   │   ├── RegistroRepository.java
│   │   └── CeldaRepository.java
│   └── service/                          # Lógica de negocio
│       ├── ParkingService.java
│       └── UsuarioService.java
└── src/main/resources/
    ├── application.properties           # Configuración
    ├── templates/                        # Plantillas HTML
    │   ├── login.html
    │   └── dashboard.html
    └── static/                           # Recursos estáticos
        ├── css/
        └── js/
________________________________________
🔌 API Endpoints
Endpoints Públicos
Método	Endpoint	Descripción
GET	/api/celdas/disponibles	Obtener celdas disponibles
GET	/api/celdas	Obtener todas las celdas
GET	/api/clientes/mensualidad	Obtener clientes con mensualidad activa
GET	/api/vehiculos/activos	Obtener vehículos actualmente en el parqueadero
GET	/api/stats	Obtener estadísticas del dashboard
GET	/api/inicializar-celdas	Inicializar celdas de prueba
Endpoints Protegidos
Método	Endpoint	Descripción	Rol
POST	/login	Autenticación	Público
POST	/registrarEntrada	Registrar entrada	Todos
POST	/registrarSalida	Registrar salida	Todos
POST	/admin/usuarios/crear	Crear usuario	Admin
POST	/admin/clientes/crear	Crear cliente	Admin
POST	/admin/celdas/crear	Crear celda	Admin
POST	/admin/celdas/estado	Cambiar estado celda	Admin
________________________________________
🔑 Credenciales de Acceso
Tipo	Usuario	Contraseña
Administrador	admin	admin123
Cajero	cajero1	cajero123
________________________________________
📖 Guía de Uso
1. Registrar Entrada de Vehículo
1.	Accede al dashboard
2.	Completa los campos: placa (formato ABC123), marca, modelo y color
3.	Selecciona el tipo de cliente (ocasional o mensualidad)
4.	Elige una celda disponible
5.	Haz clic en "Registrar Entrada"
2. Registrar Salida
1.	Ve a la sección "Registrar Salida"
2.	Ingresa la placa del vehículo
3.	Confirma el valor calculado automáticamente
4.	Haz clic en "Pagar y Registrar Salida"
3. Ver Vehículos Activos
•	En la sección "Vehículos Activos" podrás ver todos los vehículos actualmente en el parqueadero con su tiempo de estadía
4. Gestionar Celdas (Solo Admin)
•	Ve a "Mapa de Celdas" para visualizar el estado de todas las celdas
•	Ve a "Gestión de Celdas" para cambiar estados o agregar nuevas celdas
5. Gestionar Usuarios (Solo Admin)
•	En "Gestión de Usuarios" puedes crear nuevos administradores o cajeros
6. Registrar Clientes (Solo Admin)
•	En "Gestión de Clientes" puedes registrar clientes ocasionales o de mensualidad
________________________________________
🐛 Solución de Problemas
Error: "No se puede conectar a la base de datos"
•	Verifica que MySQL esté ejecutándose: sudo systemctl status mysql (Linux) o net start MySQL80 (Windows)
•	Revisa las credenciales en application.properties
Error: "Usuario o contraseña incorrectos"
•	Asegúrate de que la base de datos tenga los usuarios insertados
•	Verifica que la contraseña esté encriptada con MD5
Error: "No se cargan las celdas"
•	Ejecuta el endpoint de inicialización: http://localhost:8080/api/inicializar-celdas
•	Verifica que la tabla celdas tenga registros
Error: "Puerto 8080 ocupado"
•	Cambia el puerto en application.properties: server.port=8081

