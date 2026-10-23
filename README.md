# QUIROSALUD — Fase 1 (backend)

Proyecto de rehabilitación y fisioterapia. Este repositorio conserva los 4 módulos originales y agrega las correcciones de la primera etapa:

- Auth (8081): login BCrypt, JWT HS256 con clave externa, migración Flyway y seed opcional `dev`.
- Pacientes (8082): CRUD REST, DTO, validación, permisos y tablas separadas para datos personales e historia clínica.
- Eureka (8761) y Gateway (8080): descubrimiento y rutas de Auth/Pacientes.
- `compose.yaml`: primer entorno integrado con MySQL y frontend.

**Estado:** código preparado para verificación; el autor de esta entrega no ha podido ejecutar Maven/Java 25/Docker en el entorno de revisión. No sustituir datos reales sin hacer una migración.

## Para arrancar

1. Situar ambos repositorios como carpetas hermanas `QuiroSalud-main/` y `quirosalud-frontend-main/`.
2. Copiar `.env.example` como `.env`. Completar claves; agregar `MYSQL_ROOT_PASSWORD` y, para demo local, activar bootstrap: `BOOTSTRAP_ENABLED=true` con correo y contraseña robusta.
3. Generar clave segura: `python -c "import secrets,base64;print(base64.b64encode(secrets.token_bytes(32)).decode())"`.
4. Ejecutar `docker compose config` para validar variables, luego `docker compose up --build -d`.
5. Verificar `docker compose ps`, `docker compose logs -f auth pacientes gateway`.
6. Abrir `http://localhost:4200`; Eureka en `http://localhost:8761`.

La base Auth se crea como `bd_auth_service` y la de pacientes como `bd_pacientes_service`. **No apuntar a `bd_quiro_t3`: es una BD heredada cuyo esquema no coincide.** Flyway crea el esquema nuevo solo en BD vacías. El seed admin se activa únicamente con perfil `dev` y `BOOTSTRAP_ENABLED=true`; se ejecuta solo si el correo todavía no existe. Desactivarlo después.

## Seguridad

Nunca subir `.env`, JWT o contraseñas a GitHub. En esta fase los microservicios usan un secreto simétrico común para firmar/verificar; en producción se migrará a claves asimétricas o proveedor OIDC, distintos usuarios de MySQL y HTTPS. Personal con rol ASISTENTE no recibe historias clínicas; solo ADMIN/MEDICO pueden verlas/modificarlas; solo ADMIN elimina pacientes.

## API

| Método | Ruta Gateway | Roles |
|---|---|---|
| POST | `/api/v1/auth/login` | Público |
| GET | `/api/v1/pacientes` | ADMIN/MEDICO/ASISTENTE |
| GET | `/api/v1/pacientes/{dni}` | ADMIN/MEDICO/ASISTENTE |
| POST | `/api/v1/pacientes` | ADMIN/MEDICO/ASISTENTE |
| PUT | `/api/v1/pacientes/{dni}` | ADMIN/MEDICO/ASISTENTE |
| DELETE | `/api/v1/pacientes/{dni}` | ADMIN |
| GET | `/api/v1/pacientes/{dni}/historia` | ADMIN/MEDICO |

Los POST/PUT pueden incorporar `historia` solo con ADMIN/MEDICO. Error 400 en campos inválidos, 401 sin token, 403 sin permiso, 404 si no existe y 409 DNI duplicado. Citas, tratamientos, usuarios, ventas, reportes siguen pendientes de migración en esta fase.

## Tests

Desde cada microservicio: `./mvnw.cmd test` (Windows) o `./mvnw test` (Linux, requiere Maven Wrapper completo o Maven instalado). Los tests unitarios incorporados no requieren MySQL; los tests generados por Spring que usan `@SpringBootTest` sí requieren entorno real o configuración de test aislada. Ejecutar además pruebas con Postman/curl y el checklist de `docs/FASE_1_PRUEBAS.md`.
