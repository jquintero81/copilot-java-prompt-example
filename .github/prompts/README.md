# Prompts (agentes dinámicos) para GitHub Copilot

Formato recomendado por archivo `.prompt.md`:
- `name`: breve identificador.
- `purpose`: qué hace el prompt.
- `inputs`: placeholders que el usuario debe completar.
- `constraints`: restricciones (lenguaje, pruebas, clean code).
- `instructions`: paso a paso que Copilot debe seguir.
- `examples`: ejemplos de entrada/salida si aplica.

Uso:
- Copiar el contenido del prompt al chat de Copilot o insertarlo como "system message" si la herramienta lo permite.
- Reemplazar placeholders por el contexto real (paquete, entidades, requisitos).
- Pedir iteraciones: implementar -> ejecutar pruebas -> corregir -> refactorizar.

Estructura de carpetas:
- Prompts por categoría: `create-`, `add-`, `implement-`, `write-`, `refactor-`.