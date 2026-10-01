# Informe — Fogón, app de recetas

**Parcial 1 · Aplicaciones Móviles · ACN4B**
Alumno: Mateo Teggi

## Objetivo de la aplicación

Fogón es una app de recetas caseras con foco en la cocina argentina. Permite descubrir recetas, adaptarlas a la cantidad de comensales, armar la lista de compras y cocinar paso a paso sin perder el hilo.

## Mockups

Los mockups de todas las pantallas, con sus flujos, se hicieron en un canvas de diseño:
[Fogón — pantallas y flujos](https://claude.ai/artifact/4v6WoghhRseoGLTBdTs9eA)

## Pantallas y flujo de uso

### 1. Inicio
- Saludo y buscador de recetas o ingredientes.
- Filtro por categoría: Todas, Saladas, Dulces, Veggie.
- Receta destacada de la semana y listado de recetas.
- **Flujo:** tocar una receta abre su **Detalle**. El botón **+** abre **Nueva receta**. **Compras** abre la **Lista de compras**.

### 2. Detalle de receta (pantalla desarrollada)
- Foto de la receta, botón para volver y botón de favorito.
- Categoría, nombre, descripción y tarjetas con tiempo, dificultad y calorías.
- Selector de porciones (– / +).
- Lista de ingredientes con casillas para tildar lo que ya se tiene.
- Pasos de preparación numerados.
- Barra inferior con **Agregar a compras** y **Empezar a cocinar**.
- **Flujo:** cambiar las porciones recalcula todas las cantidades. Tildar un ingrediente lo tacha. *Agregar a compras* suma los ingredientes que faltan a la lista. *Empezar a cocinar* resalta los pasos de a uno hasta terminar.

### 3. Modo cocina
- Un paso por pantalla con letra grande, barra de progreso y temporizador en los pasos con tiempo.
- **Flujo:** *Anterior* / *Siguiente* recorren los pasos. *Terminar* vuelve al detalle.

### 4. Nueva receta
- Foto, nombre, categoría, minutos y porciones.
- Campo de ingrediente + botón **Agregar** que suma el ingrediente a la lista (se puede quitar).
- **Flujo:** *Guardar receta* confirma y vuelve al inicio.

### 5. Lista de compras
- Ítems agrupados por comercio (verdulería, carnicería, almacén) con contador de comprados.
- **Flujo:** tildar marca el ítem como comprado. Se pueden sumar ítems sueltos.

## Pantalla desarrollada: Detalle de receta

| Requisito | Dónde se cumple |
|---|---|
| ConstraintLayout | Raíz de `activity_main.xml` y el hero con foto y botones |
| LinearLayout vertical / horizontal | Cuerpo de la receta (vertical); tarjetas de info, selector de porciones y barra inferior (horizontal) |
| Button | Porciones – / +, Agregar a compras, Empezar a cocinar |
| TextView | Título, descripción, tarjetas, contador de porciones, pasos |
| Imágenes | Foto de la receta en el hero, íconos vectoriales de volver y favorito |
| Eventos | `android:onClick` en todos los botones y listener en cada ingrediente |
| Elementos creados en Java | Ingredientes (`CheckBox`) y pasos (`LinearLayout` + `TextView`) se crean en `MainActivity` y se agregan con `addView` |
| Recursos organizados | `strings.xml`, `ingredients.xml` (plurales), `dimens.xml`, `colors.xml` |

### Comportamiento dinámico
1. **Porciones:** – / + recalcula la cantidad de cada ingrediente (redondeando a ½ y usando singular o plural).
2. **Ingredientes:** al tildarlos se tachan y no cuentan para la lista de compras.
3. **Agregar a compras:** informa cuántos ingredientes faltan y cambia el estado del botón. Un segundo toque los quita.
4. **Favorito:** alterna el ícono del corazón y avisa con un mensaje.
5. **Empezar a cocinar:** resalta el paso actual, desplaza la pantalla hasta él y muestra el progreso en el botón.

## Paleta y diseño

| Color | Uso |
|---|---|
| `#2F5D3A` verde hierba | Acciones primarias, porciones, checks |
| `#C4491F` tomate | Llamado principal (Empezar a cocinar), favorito, paso activo |
| `#FAFAF7` fondo | Fondo general |
| `#E6EEE6` verde suave | Tarjetas de información |
| `#1D1F1C` tinta | Texto principal |

## Convención de commits

Se usa [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/): `feat:`, `refactor:`, `docs:`, `chore:`.
