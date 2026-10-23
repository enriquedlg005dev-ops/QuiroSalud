# Checklist de verificación — Fase 1

No utilizar datos personales reales para estas pruebas. Inicializar los esquemas nuevos, no reemplazar el anterior.

## Preparación

- [ ] Java 25 y Docker Desktop activos.
- [ ] `.env` creado sin publicarlo; claves fuertes.
- [ ] `docker compose config` sin errores.
- [ ] `docker compose up --build -d` completa imágenes sin fallos.
- [ ] `docker compose ps` muestra servicios levantados; Eureka registra Auth y Pacientes.
- [ ] Tabla `flyway_schema_history` existe en cada BD.

## Pruebas de integración

1. Abrir `http://localhost:4200`, iniciar sesión con la cuenta dev creada de forma explícita.
2. `POST /api/v1/auth/login` con clave incorrecta ⇒ 401.
3. `GET /api/v1/pacientes` sin Authorization ⇒ 401.
4. Autenticado como ADMIN, abrir Pacientes y registrar DNI ficticio de prueba (no real) ⇒ 201 y datos visibles en tabla.
5. Repetir DNI ⇒ 409.
6. Editar el paciente sin cambiar DNI ⇒ 200 y lista refleja cambio.
7. Acceder `GET /api/v1/pacientes/{dni}/historia` como ADMIN o MEDICO ⇒ datos clínicos (si existen); como ASISTENTE ⇒ 403.
8. Acceder PUT con historia clínica siendo ASISTENTE ⇒ 403; sin historia ⇒ permitido.
9. Eliminar con ADMIN ⇒ 204, consultar DNI ⇒ 404; como ASISTENTE ⇒ 403.
10. Probar `fechaNacimiento` futura, DNI no numérico y escala dolor 11 ⇒ 400.
11. Verificar que Auth y Pacientes aceptan JWT válido, y que un token alterado/expirado da 401.
12. Cerrar sesión y verificar que se pierde acceso a rutas protegidas.

## Antes de publicar imágenes Docker Hub

- [ ] Tests Maven y Angular ejecutados de verdad y documentados.
- [ ] Revisión de seguridad de dependencias, logs y secretos.
- [ ] Imágenes versionadas por SHA/semver, no depender de `latest` exclusivamente.
- [ ] Datos sensibles nunca en GitHub, imágenes ni SQL de prueba.
- [ ] Diferenciar servicios terminados y servicios heredados aún pendientes.
