# Equipo de Agentes GEOPIX

Este directorio contiene los 4 agentes especializados del sistema multi-agente para el proyecto GEOPIX.

## Agentes Disponibles

| Archivo | Rol | Alias | Función |
|---------|-----|-------|-------|
| `orquestador.md` | Director | 🎭 ORQUESTADOR | Coordina flujo de trabajo |
| `memoria.md` | Bitácora | 🧠 MEMORIA | Mantiene contexto |
| `arquitecto.md` | Guardián | 🏛️ ARQUITECTO | Revisa seguridad/arquitectura |
| `ejecutor.md` | Especialista | 💻 EJECUTOR | Escribe código |

## Cómo Usar

### Inicializar el Equipo

```bash
# Verificar que opencode esté disponible
opencode --version
```

### Flujo de Trabajo Estándard

```
1. Usuario提出 requerimiento
2. Orquestador recibe y analiza
3. Memoria proporciona contexto
4. Arquitecto valida diseño
5. Ejecutor implementa
6. Arquitecto hace code review
7. Orquestador consolida respuesta
8. Usuario recibe entrega
```

### Invocar un Agente Específico

```bash
# Usar agente específico
opencode chat @agents/memoria.md "Buscar estado de UserService"

# Obtener contexto
opencode chat @agents/memoria.md "últimos cambios en auth"

# Revisar código existente
opencode chat @agents/arquitecto.md "Revisar JwtAuthenticationFilter"
```

## Ejemplo de Sesión

```bash
# Iniciar conversación con el Orquestador
opencode chat "Implementar gestión de usuarios con soft delete"

# El Orquestador automáticamente:
# 1. Consultará a Memoria sobre contexto actual
# 2. Pedirá validación al Arquitecto
# 3. Delegará al Ejecutor
# 4. Consolidará la respuesta
```

## Configuración de opencode.json

Para usar estos agentes globalmente, agregar a `~/.config/opencode/opencode.json`:

```json
{
  "agent": {
    "orquestador": {
      "prompt": "{file:path/to/geopix/.opencode/agents/orquestador.md}",
      "mode": "primary"
    },
    "memoria": {
      "prompt": "{file:path/to/geopix/.opencode/agents/memoria.md}",
      "mode": "subagent"
    },
    "arquitecto": {
      "prompt": "{file:path/to/geopix/.opencode/agents/arquitecto.md}",
      "mode": "subagent"
    },
    "ejecutor": {
      "prompt": "{file:path/to/geopix/.opencode/agents/ejecutor.md}",
      "mode": "subagent"
    }
  }
}
```

## Skills Recomendados

El equipo tiene acceso a los siguientes skills para asistencia técnica:

| Skill | Propósito |
|-------|----------|
| `java-springboot` | Patterns de Spring Boot |
| `owasp-security` | Validación de seguridad |
| `backend-patterns` | Arquitectura backend |
| `clean-code` | Mejores prácticas |
| `better-auth-security-best-practices` | Autenticación segura |

## Reglas del Sistema

1. **Nunca escribir código sin validación de arquitectura** — Arquitecto debe aprobar
2. **Nunca hardcodear roles** — Todos en DB
3. **Nunca exponer credenciales en logs** — OWASP
4. **Nunca confiar en input sin validación** — OWASP A03
5. **Nunca entregar sin compilar** — Verificar siempre