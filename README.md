API de Campañas de Email
Esta es una API RESTful desarrollada con Spring Boot para gestionar campañas de email. La API permite la autenticación de usuarios, la gestión de roles (ADMIN y USER), y próximamente, la creación y envío de campañas de email y la gestión de listas de suscriptores.

Tecnologías Utilizadas
Java 17

Spring Boot 3.2.0: Framework principal para el desarrollo de la aplicación.

Spring Security: Para la autenticación y autorización basada en roles.

JWT (JSON Web Tokens): Implementado para la autenticación sin estado.

Spring Data JPA: Para la interacción con la base de datos.

MySQL: Base de datos relacional utilizada para almacenar la información de usuarios.

Maven: Gestor de dependencias del proyecto.

Estructura del Proyecto
El proyecto sigue una estructura de capas típica en Spring Boot:

controller/: Maneja las peticiones HTTP y las respuestas.

service/: Contiene la lógica de negocio de la aplicación.

repository/: Interfaz para la comunicación con la base de datos.

model/: Clases de entidades JPA.

dto/: Objetos de transferencia de datos.

security/: Clases de configuración de Spring Security y JWT.

Configuración y Ejecución
Requisitos Previos
JDK 17 o superior

MySQL 8.0 o superior

Maven


Endpoints de la API
La API expone los siguientes endpoints, protegidos con JWT.

Autenticación
POST /api/v1/auth/register: Registra un nuevo usuario.

POST /api/v1/auth/login: Autentica a un usuario y devuelve un token JWT.

Gestión de Usuarios
GET /api/v1/users/me: Obtiene la información del usuario autenticado (requiere rol USER o ADMIN).

GET /api/v1/users: Obtiene todos los usuarios (requiere rol ADMIN).

GET /api/v1/users/{id}: Obtiene un usuario por ID (requiere rol ADMIN).

PUT /api/v1/users/{id}: Actualiza un usuario (requiere rol ADMIN).

DELETE /api/v1/users/{id}: Elimina un usuario (requiere rol ADMIN).

