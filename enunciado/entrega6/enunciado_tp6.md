# TP 6 - NoSQL - Firebase Firestore

## Intro

Los cinco programadores, atónitos por lo que acababan de presenciar, cruzaron miradas entre sí. Sin mediar palabra, avanzaron hacia el portal mórbido que acababa de abrirse frente a ellos.

Al atravesarlo, emergieron en lo que parecía ser una dimensión paralela. El entorno era extrañamente similar al suyo, pero envuelto en una oscuridad casi total y cubierto por una niebla densa y asfixiante.

Durante algunos segundos, observaron en silencio. Entonces, una sensación de vacío comenzó a invadirlos, lenta pero implacable. Algo en ese lugar parecía drenar su esencia misma, como si su existencia estuviera siendo deshilachada. Sentían cómo el desvanecimiento los desgarraba desde dentro, hasta reducirlos... al vacío.

Silencio.

Oscuridad absoluta.

No era simplemente la incapacidad de comprender lo que sucedía. No había nada que comprender. No existía un "alrededor". No existía nada. Solo vacío. Y silencio.

De pronto: luces.

Sonidos inentendibles comenzaron a colarse en sus conciencias, como ecos de un lenguaje desconocido. Poco a poco, el mundo se volvió perceptible otra vez, aunque ya no estaban donde habían estado antes.

—¿Cuánto tiempo ha pasado? —se preguntó uno de ellos, sin recibir respuesta.

—¿Dónde estamos...? —murmuró otro, perdido entre pensamientos difusos.

Una eternidad diminuta parecía haber transcurrido. No había arriba ni abajo. La gravedad no los tocaba. Una nube espesa los envolvía. Podían escucharse entre ellos, pero no lograban verse.

Pasaron algunos segundos —¿o fueron horas?— hasta que la neblina comenzó a disiparse. Finalmente, pudieron ver.

Lo que descubrieron los dejó sin aliento.

Desde una altura imposible, contemplaban todo aquello que alguna vez recorrieron. Las marcas lejanas que veían parecían ahora señaladas por símbolos nuevos, extraños... y sin embargo, los comprendían. Como si un nuevo poder se hubiera despertado en su interior, los cinco programadores se habían convertido en observadores omnipotentes, capaces de verlo todo desde las alturas.

Pero entonces surgió una nueva pregunta, inevitable:

¿Podrían controlar este nuevo poder?
¿Serían capaces de moldear la realidad a su voluntad?

Eso... aún estaba por verse.

## Funcionalidades

### Dominio de espíritus
A partir de ahora:
- Tendrán un número del 1 al 100 que represente la energía que tienen. Si se intenta realizar una acción sin energía, deberá arrojarse la excepción `EspirituSinEnergiaException`.
- Podrán moverse entre ubicaciones sin depender de un medium para ello.
- Podrán atacar a otro espíritu. (dar reglas)
- Tendrán un número de victorias y de derrotes por combate.
- Tendrán un número que represente el poder de ataque, y otro que represente el poder de defensa. Solamente aplican para el nuevo esquema de duelo.


## Servicios
Se deberán modificar los siguientes servicios:

### EspirituService
- `void desplazarse(Ubicacion ubicacion)` - Deberá mover al espíritu a la ubicación indicada, en una coordenada cualquiera. Si el espíritu está conectado a un medium, deberá arrojarse la excepción `EspirituConectadoException`.
- `void combatir(Espiritu espiritu)` - Deberá atacar al espíritu indicado según las indicaciones dadas previamente.
- 
### EstadisticaService
- `List<Ubicacion> ubicacionesMasPopuladas` - Retorna una lista ordenada de mayor a menor con las ubicaciones que tienen más espíritus 

### Bonus
- `List<Espiritu> espiritusMasVictoriosos` - Retorna una lista ordenada de mayor a menor con los espíritus que tienen más victorias
