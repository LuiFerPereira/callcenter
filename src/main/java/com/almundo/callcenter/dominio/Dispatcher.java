package com.almundo.callcenter.dominio;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Clase que operada el Call Center
 *
 * Inicia los hilos de ejecución de cada empleado, asigna las llamadas para atender.
 *
 * Mantiene una lista de llamadas entrantes, mientras no haya un empleado disponible para atender la llamada
 */
public class Dispatcher implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Dispatcher.class);

    /**
     * Capacidad del Call Center para atender llamadas
     */
    public static final Integer CAPACIDAD_CALL_CENTER = 10;

    private Boolean activo;

    private ExecutorService executorService;

    private ConcurrentLinkedDeque<Empleado> Empleados;

    private ConcurrentLinkedDeque<Llamada> llamadasEntrantes;

    private EstrategiaAtenderLlamadas estrategiaAtenderLlamadas;

    public Dispatcher(List<Empleado> Empleados) {
        this(Empleados, new DefaultEstrategiaAtenderLlamadas());
    }

    public Dispatcher(List<Empleado> Empleados, EstrategiaAtenderLlamadas estrategiaAtenderLlamadas) {
        Validate.notNull(Empleados);
        Validate.notNull(estrategiaAtenderLlamadas);
        this.Empleados = new ConcurrentLinkedDeque(Empleados);
        this.estrategiaAtenderLlamadas = estrategiaAtenderLlamadas;
        this.llamadasEntrantes = new ConcurrentLinkedDeque<>();
        this.executorService = Executors.newFixedThreadPool(CAPACIDAD_CALL_CENTER);
    }

    public synchronized void dispatchCall(Llamada llamada) {
        logger.info("Dispatch una llamada de " + llamada.getDuracionEnSegundos() + " segundos");
        this.llamadasEntrantes.add(llamada);
    }

    /**
     * Inicia el hilo Empleado y permite ejecutar el Dispatcher
     */
    public synchronized void start() {
        this.activo = true;
        for (Empleado Empleado : this.Empleados) {
            this.executorService.execute(Empleado);
        }
    }

    /**
     * Detiene el hilo (thread) Empleado y el Dispatcher inmediatamente
     */
    public synchronized void stop() {
        this.activo = false;
        this.executorService.shutdown();
    }

    public synchronized Boolean getActivo() {
        return activo;
    }

    /**
     * Este es el método que se ejecuta el hilo Dispatcher.
     * Si la cola de llamadas entrantes no está vacía, busca Empleado disponible para tomar la llamada.
     * Las llamadas se pondrán en cola hasta que algunos trabajadores estén disponibles
     */
    @Override
    public void run() {
        while (getActivo()) {
            if (this.llamadasEntrantes.isEmpty()) {
                continue;
            } else {
                Empleado Empleado = this.estrategiaAtenderLlamadas.findEmpleado(this.Empleados);
                if (Empleado == null) {
                    continue;
                }
                Llamada call = this.llamadasEntrantes.poll();
                try {
                    Empleado.atenderLlamada(call);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    this.llamadasEntrantes.addFirst(call);
                }
            }
        }
    }

}

