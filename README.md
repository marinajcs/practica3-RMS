# Práctica 3 de RMS: Implementación de un servicio telemático basado en webcam.
## Requisitos
- Instalar el JDK de JAVA
- Una API de manejo de webcams en JAVA (`com.github.sarxos.webcam.Webcam`)
- Un IDE para desarrollar la aplicación (`Apache NetBeans`)
## Objetivos
* Desarrollar una aplicación en JAVA que transmita una imagen capturada por una webcam desde un PC a otro PC remoto.
* Manejar los sockets para enviar y recibir información a través de los protocolos TCP y UDP.
## Tareas
La realización de esta práctica consiste en desarrollar una aplicación en JAVA que permita enviar una señal multimedia desde 
un PC a otro a través de la red. La información multimedia a transmitir consistirá en una imagen capturada por una webcam 
a través de la aplicación. Dicha aplicación seguirá el modelo cliente-servidor para la comunicación entre los dos PCs.

Las tareas realizadas incluyen:
- Un cliente (`TCP_Client.java`) y servidor (`TCP_Server.java`) que permiten el envío de una imagen capturada por webcam mediante el uso de sockets TCP o de tipo _Stream_.
- Repetir la tarea anterior pero esta vez mediante el uso de sockets UDP o de tipo _datagrama_ (`UDP_Client.java` y `UDP_Server.java`).
- Convertir el servidor TCP en un servidor concurrente (`SCTCP.java`) mediante el uso de hilos (_threads_) de forma que el servidor lance un nuevo hilo de ejecución para gestionar la conexión de cada cliente nuevo que se conecta.
