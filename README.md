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

<img width="389" height="836" alt="imagen" src="https://github.com/user-attachments/assets/f32c5360-eeb9-40f0-9ac5-a376184996f6" />

<img width="384" height="836" alt="imagen" src="https://github.com/user-attachments/assets/f07d0754-5d08-4eb7-a080-8a676a175975" />

<img width="387" height="844" alt="imagen" src="https://github.com/user-attachments/assets/08057526-a0b3-471f-8dad-870beefa24a0" />

<img width="387" height="836" alt="imagen" src="https://github.com/user-attachments/assets/054fc5a3-8ec4-4a80-8628-dfa16ba16470" />

<img width="388" height="836" alt="imagen" src="https://github.com/user-attachments/assets/1f2b9bfc-0fbb-49f2-a351-5f6e316365c0" />

<img width="389" height="844" alt="imagen" src="https://github.com/user-attachments/assets/9f73d8a9-250a-42f0-974f-43eccf3d88df" />

<img width="379" height="824" alt="imagen" src="https://github.com/user-attachments/assets/8ce0804c-6c58-402e-86a1-52874a7c2005" />

<img width="377" height="829" alt="imagen" src="https://github.com/user-attachments/assets/e796fdbc-75a3-4b1e-a519-afe9434f2d79" />

<img width="356" height="816" alt="imagen" src="https://github.com/user-attachments/assets/c68166cd-34d1-4dcd-ad90-c6c48f375a0f" />


