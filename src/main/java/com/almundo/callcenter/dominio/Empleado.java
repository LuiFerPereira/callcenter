package com.almundo.callcenter.dominio;


import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

/**
 * Cada instancia de la clase Empleado, es un hilo de ejecución
 *
 * Cada empleado tiene un estado (Disponible/Ocupadado)
 * Cada empleado tiene un tipo (Operador/Supervisor/Director)
 */
public class Empleado implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Empleado.class);

    private TipoEmpleado tipoEmpleado;

    private EstadoEmpleado estadoEmpleado;

    private ConcurrentLinkedDeque<Llamada> llamadasEntrantes;

    private ConcurrentLinkedDeque<Llamada> llamadasAtendidas;

    public Empleado(TipoEmpleado tipoEmpleado) {
        Validate.notNull(tipoEmpleado);
        this.tipoEmpleado = tipoEmpleado;
        this.estadoEmpleado = EstadoEmpleado.DISPONIBLE;
        this.llamadasEntrantes = new ConcurrentLinkedDeque<>();
        this.llamadasAtendidas = new ConcurrentLinkedDeque<>();
    }

    public TipoEmpleado getTipoEmpleado() {
        return tipoEmpleado;
    }

    public synchronized EstadoEmpleado getEstadoEmpleado() {
        return estadoEmpleado;
    }

    private synchronized void setEstadoEmpleado(EstadoEmpleado estadoEmpleado) {
        logger.info("Empleado " + Thread.currentThread().getName() + " cambia a estado: " + estadoEmpleado);
        this.estadoEmpleado = estadoEmpleado;
    }

    public synchronized List<Llamada> getLlamadasAtendidas() {
        return new ArrayList<>(llamadasAtendidas);
    }

    /**
     * Cola de Llamadas a ser atendidas por el Empleado
     *
     * @param Llamada Llamada por ser atendida
     */
    public synchronized void atenderLlamada(Llamada Llamada) {
        logger.info("Empleado " + Thread.currentThread().getName() + " encola llamada de " + Llamada.getDuracionEnSegundos() + " segundos");
        this.llamadasEntrantes.add(Llamada);
    }



    /**
     * Este es el método que se ejecuta en el hilo.
     * Si la cola de llamadas entrantes no está vacía, cambia su estado de DISPONIBLE a OCUPADO y atiende la llamada
     * y cuando termina, cambia su estado de OCUPADO a DISPONIBLE.
     */
    @Override
    public void run() {
        logger.info("Empleado " + Thread.currentThread().getName() + " empieza a trabajar");
        while (true) {
            if (!this.llamadasEntrantes.isEmpty()) {
                Llamada Llamada = this.llamadasEntrantes.poll();
                this.setEstadoEmpleado(EstadoEmpleado.OCUPADO);
                logger.info("Empleado " + Thread.currentThread().getName() + " empieza trabajando en Llamada de " + Llamada.getDuracionEnSegundos() + " segundos");
                try {
                    TimeUnit.SECONDS.sleep(Llamada.getDuracionEnSegundos());
                } catch (InterruptedException e) {
                    logger.error("Empleado " + Thread.currentThread().getName() + " fue interrumpido y no pudo terminar la Llamada de " + Llamada.getDuracionEnSegundos() + " segundos");
                } finally {
                    this.setEstadoEmpleado(EstadoEmpleado.DISPONIBLE);
                }
                this.llamadasAtendidas.add(Llamada);
                logger.info("Empleado " + Thread.currentThread().getName() + " finaliza Llamada de " + Llamada.getDuracionEnSegundos() + " segundos");
            }
        }
    }

}
