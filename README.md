# 📦 Enviosp2p - Plataforma de Envíos Colaborativos

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3-green?style=for-the-badge&logo=spring)
![MySQL](https://img.shields.io/badge/MySQL-DB-blue?style=for-the-badge&logo=mysql)
![JWT](https://img.shields.io/badge/Security-JWT-red?style=for-the-badge)

**Enviosp2p** es una API RESTful que conecta a personas que necesitan enviar un paquete con viajeros que van hacia el mismo destino y tienen espacio disponible. Es una solución estilo "Uber/PedidosYa" para logística entre particulares.

## 🚀 Características Principales

### 🔐 Seguridad y Autenticación
- **Registro y Login** con validación de credenciales.
- Protección de endpoints mediante **JWT (JSON Web Tokens)**.
- **Recuperación de Contraseña** vía correo electrónico (HTML Templates) con tokens de un solo uso.
- Encriptación de contraseñas con `BCrypt`.

### 📦 Gestión de Envíos
- **Creación de Pedidos:** Los usuarios pueden publicar solicitudes de envío especificando origen, destino (con coordenadas) y recompensa.
- **Modelo de Direcciones:** Implementación eficiente con `@Embeddable` para manejar ubicaciones complejas sin saturar la base de datos.
- **Asignación de Viajeros:** Lógica de negocio transaccional para que un viajero pueda tomar un pedido (validando que no sea el mismo remitente).
- **Máquina de Estados:** Control de flujo (PENDIENTE -> ASIGNADO -> EN_TRANSITO -> ENTREGADO).

### 🛠 Arquitectura
- Diseño en capas: **Controller, Service, Repository**.
- Uso de **DTOs (Records de Java 17)** para transferencia de datos inmutable.
- **Mappers** personalizados para transformar Entidades <-> DTOs.
- **Manejo Global de Excepciones** (`@ControllerAdvice`) para respuestas JSON estandarizadas (404, 400, 409, 500).

---

## 🛠️ Tecnologías Utilizadas

- **Lenguaje:** Java 17
- **Framework:** Spring Boot 3
- **Base de Datos:** MySQL
- **ORM:** Spring Data JPA / Hibernate
- **Seguridad:** Spring Security 6 + JJWT
- **Utilidades:** Lombok, JavaMailSender
- **Validación:** Jakarta Validation (Bean Validation)
