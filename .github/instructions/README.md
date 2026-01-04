# GitHub Copilot - Instructions

Propósito:
- Proveer instrucciones estáticas para guiar a GitHub Copilot y Copilot Chat.
- Mantener reglas globales de estilo, convenciones y objetivos del proyecto.

Estructura:
- instructions/: archivos markdown con pautas generales (clean code, convenciones de commit, arquitectura).
- prompts/: plantillas de prompts (agentes dinámicos) para generar y modificar código.

Cómo usar:
1. Abrir Copilot Chat en VS Code o usar Copilot en el editor.
2. Para tareas de alto nivel (políticas, reglas, convenciones) leer `instructions/`.
3. Para acciones concretas (crear proyecto, endpoints, tests) usar los archivos en `prompts/` como plantilla.
4. Al usar un prompt, sustituir los placeholders y proveer contexto (clases existentes, modelos de datos, requisitos del negocio).

Recomendaciones:
- Mantener las instrucciones en `instructions/` y actualizarlas cuando cambie la política del proyecto.
- Crear un prompt nuevo en `prompts/` para tareas repetibles y nombrarlo con un prefijo claro (`create-`, `add-`, `implement-`, `refactor-`).