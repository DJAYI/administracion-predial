# GEOPIX - Sistema de Gestión Predial

## Lógica de Negocio y Reglas del Sistema

### 1. Gestión de Predios (Unificada)
El sistema permite la gestión integral de un predio (Básicos, Físicos y Titularidad) a través de un único flujo transaccional.

*   **Creación:** Requiere como mínimo la información de Datos Básicos (Matrícula Inmobiliaria).
*   **Actualización:** Permite actualizaciones parciales enviando solo las secciones necesarias.
*   **Placeholders:** Campos opcionales no diligenciados se marcan automáticamente como `"NO INFORMATION"`.

### 2. Datos Físicos (Áreas y Linderos)
*   **Fórmula de Área Total:** `Total (m²) = (Hectáreas × 10.000) + Metros²`.
*   **RNG-01:** Los metros cuadrados deben estar entre `0` y `9.999`. Si llega a `10.000`, se debe sumar 1 a las hectáreas.
*   **Independencia:** Las áreas VUR, Catastro, Escrituras y Medición son independientes.

### 3. Titularidad y Derechos (%)
*   **Suma 100%:** La sumatoria de los derechos (%) de todos los titulares asociados a un predio **debe ser exactamente 100%**. El sistema bloquea el guardado si no se cumple.
*   **Persona Natural vs Jurídica:**
    *   Si el documento es `NIT`, se exige `Razón Social`.
    *   Si es otro tipo, se exigen `Nombres` y `Primer Apellido`.
*   **Validación de Fechas:** La `Fecha de Anotación` debe ser mayor o igual a la `Fecha de Escritura`.
*   **Dependencias:** Si existe `Fecha de Escritura`, el `Número de Escritura` es obligatorio. Si hay `Escritura`, la `Notaría` es obligatoria.

### 4. Seguridad y Roles
El sistema implementa seguridad basada en JWT y control de acceso por roles (RBAC):

*   **ADMIN:** Acceso total a todas las operaciones CRUD, configuración de maestros y **Gestión de Usuarios**.
*   **EJECUTOR_INTEGRAL:** Permiso para crear, editar y eliminar predios y sus tipos asociados.
*   **USER:** (Solo lectura) Puede consultar el listado y detalle de predios.

### 5. Gestión de Usuarios (Solo ADMIN)
*   **Acceso:** Protegido exclusivamente para el rol `ADMIN`.
*   **Soft Delete y Bloqueo:** Al eliminar un usuario, el sistema realiza un borrado lógico (`deleted = true`) y deshabilita la cuenta automáticamente (`enabled = false`).
*   **Seguridad:** Las contraseñas se almacenan encriptadas mediante BCrypt.

### 6. Auditoría y Soft Delete
*   **Borrados:** El sistema implementa **Soft Delete**. Los registros borrados permanecen en base de datos con la bandera `deleted = true` pero son ignorados por las consultas del sistema.
*   **Entidades:** Predios, Titulares, Maestros de Tipo y Usuarios soportan Soft Delete.

## Endpoints Principales
*   `POST /api/v1/predios`: Creación completa (JSON compuesto).
*   `PUT /api/v1/predios/{id}`: Actualización completa o parcial.
*   `GET /api/v1/predios`: Listado de predios.
*   `GET /api/v1/tipos-*`: CRUD de maestros (Solo ADMIN/EJECUTOR).
*   `GET/POST/PUT/DELETE /api/v1/users`: Gestión de usuarios (Solo ADMIN).
