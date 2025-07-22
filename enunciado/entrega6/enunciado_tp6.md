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

Frente a ellos, líneas de código flotaban como corrientes vivas de energía. Y entre esas líneas, un flujo en tiempo real les mostraba cada evento, cada decisión, cada acción ocurrida en su mundo anterior. Veían combates, alianzas, traiciones... todo actualizado segundo a segundo, como si la realidad misma estuviera siendo retransmitida para su juicio.

Y entonces lo notaron: una tabla flotante, repleta de nombres, puntuaciones y victorias. Un ranking.

Ellos ya no eran simples programadores. Eran parte de algo más grande. Y esa tabla... esa tabla dictaba su posición en un nuevo orden.

Pero entonces surgió una nueva pregunta, inevitable:

¿Podrían controlar este nuevo poder? ¿Serían capaces de moldear la realidad a su voluntad?

Eso... aún estaba por verse.

## Funcionalidades

### Dominio de espíritus
A partir de ahora:
- Tendrán un número del 1 al 100 que represente la vida que tienen, que debe comenzar en 100 al momento de crear un espíritu. Si se intenta realizar una acción con una vida de 0, deberá arrojarse la excepción `EspirituMuertoException`. De más está decir que la vida no puede ser negativa.
- Podrán moverse entre ubicaciones sin depender de un medium para ello. Si un espíritu se intenta mover a una ubicación estando conectado a un medium, deberá arrojarse la excepción `EspirituConectadoException`. 
- Podrán combatir con otro espíritu. Para esto tener en cuenta que un espíritu debe tener un número que represente el nivel de ataque y otro de defensa, los cuales al sumarlos el resultado no debe ser mayor a 100. En caso contario, lanzar una excepción. 
- Los angeles deberán tener un ataque base de 5 y una defensa base de 10, mientras que demonios deberán tener un ataque base de 10 y una defensa base de 5.
- El daño de un ataque debe resultar de a los puntos de ataque restarle la defensa del rival en caso de que el ataque sea superior a la defensa. En caso contrario, el espíritu que lanza el ataque debe recibir de daño el resultante de restarle a su propia defensa la mitad de la defensa del rival.  
- Tendrán un número de victorias y de derrotes por combate.

## Servicios
Se deberán modificar los siguientes servicios:

### EspirituService
- `void desplazar(Long espirituId, Long ubicacionId)` - Deberá mover al espíritu a la ubicación indicada, a una coordenada cualquiera que pertenezca a dicha ubicación.
- `void combatir(Long atacanteId, Long receptorId)` - Los espíritus dados deberán entrar en combate según las indicaciones dadas previamente.

## Se pide:
* Hacer un front que permita el combate individual entre un espíritu y otro, y que además muestre distintos rankings. Uno de los rankings debe ser de los espíritus con más victorias, otro de los espíritus con más derrotas, y otro con los espíritus con más combates realizados. Este front debe aprovechar las ventajas realtime de Firebase Firestore para mostrar los datos necesarios en tiempo real.
* Persistir los nuevos datos que requieran sincronización inmediata en Firebase Firestore.
* Modificar el CRUD de Espíritu para que cumpla los nuevos requisitos de persistencia.
