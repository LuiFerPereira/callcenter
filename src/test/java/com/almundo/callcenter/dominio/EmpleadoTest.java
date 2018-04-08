package com.almundo.callcenter.dominio;


import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class EmpleadoTest {

    @Test(expected = NullPointerException.class)
    public void testCreacionEmpleadoSinTipo() {
        new Empleado(null);
    }

    @Test
    public void testCreacionEmpleado() {
        Empleado empleado = EmpleadoBuilder.buildOperador();

        assertNotNull(empleado);
        assertEquals(TipoEmpleado.OPERADOR, empleado.getTipoEmpleado());
        assertEquals(EstadoEmpleado.DISPONIBLE, empleado.getEstadoEmpleado());
    }

    @Test
    public void testEmpleadoAtiendaLLamadaMientrasEstaDisponible() throws InterruptedException {
        Empleado empleado = EmpleadoBuilder.buildOperador();
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(empleado);
        empleado.atenderLlamada(Llamada.buildLlamada(0, 1));

        executorService.awaitTermination(5, TimeUnit.SECONDS);
        assertEquals(1, empleado.getLlamadasAtendidas().size());
    }

    @Test
    public void testEstadoEmpleadosMientrasAtiendeLlamada() throws InterruptedException {
        Empleado empleado = EmpleadoBuilder.buildOperador();
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(empleado);
        assertEquals(EstadoEmpleado.DISPONIBLE, empleado.getEstadoEmpleado());
        TimeUnit.SECONDS.sleep(1);
        empleado.atenderLlamada(Llamada.buildLlamada(2, 3));
        empleado.atenderLlamada(Llamada.buildLlamada(0, 1));
        TimeUnit.SECONDS.sleep(1);
        assertEquals(EstadoEmpleado.OCUPADO, empleado.getEstadoEmpleado());

        executorService.awaitTermination(5, TimeUnit.SECONDS);
        assertEquals(2, empleado.getLlamadasAtendidas().size());
    }

}
