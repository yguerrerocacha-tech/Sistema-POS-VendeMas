# Sistema POS - VendeMás 🛒

Este es un sistema de Punto de Venta desarrollado en **Java** con una base de datos en **MySQL**. Permite la gestión de productos para una cafetería, incluyendo validaciones de seguridad para evitar duplicados.

## 🚀 Requisitos
* **Java:** Versión 21 o superior.
* **IDE:** Eclipse (recomendado) o IntelliJ.
* **Base de Datos:** MySQL Server 8.0.
* **Conector:** MySQL Connector/J 9.7.0 (incluido en la carpeta /lib).

## 🛠️ Instalación y Configuración
1. **Base de Datos:** Importa y ejecuta el archivo `script_db.sql` en tu MySQL Workbench para crear las tablas y los usuarios.
2. **Librerías:** Asegúrate de que el archivo `.jar` en la carpeta `lib` esté agregado al *Build Path* del proyecto.
3. **Conexión:** Revisa la clase `Conexion.java` y ajusta tu usuario y contraseña de MySQL.

## 👤 Usuarios de Prueba
* **Admin:** Yhor / **Pass:** 1234