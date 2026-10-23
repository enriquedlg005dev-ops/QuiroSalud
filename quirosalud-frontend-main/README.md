# QUIROSALUD — Fase 1 (frontend)

Angular 22. Accede al Gateway por la ruta relativa `/api/v1`.

- Desarrollo con backend levantado: `npm ci` y luego `npm start` (usa `proxy.conf.json` → `http://localhost:8080`).
- Docker: ubicar este repositorio junto al backend y levantar `docker compose up --build -d` desde backend. Nginx sirve Angular por puerto 4200 y redirige `/api` al Gateway.
- El login guarda el token de forma temporal en `sessionStorage`, comprueba su caducidad para navegación y adjunta `Authorization: Bearer` a las rutas `/api/v1/*` excepto `/auth/login`. La seguridad real siempre se comprueba en Spring Security.
- Pacientes usa DNI, CRUD REST y consulta de historia solo para ADMIN/MEDICO.

**Módulos heredados:** citas, tratamientos, ventas, asistencia, usuarios y reportes conservan código que todavía llama a un backend monolítico en el puerto `8990`. No están integrados ni probados como microservicios en esta etapa; no presentarlos como funcionalidades completadas.
