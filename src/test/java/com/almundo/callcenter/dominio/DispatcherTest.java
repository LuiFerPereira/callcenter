package com.almundo.callcenter.dominio;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class DispatcherTest {

    private static final int CANTIDAD_LLAMADAS = 15;

    private static final int MIN_DURACION_LLAMADA = 5;

    private static final int MAX_DURACION_LLAMADA = 10;

    /**
     * Valida la instancia del Dispatcher con una lista de empleados !=null
     */
    @Test(expected = NullPointerException.class)
    public void testDispatcherCreacionConEmpleadosNull() {
        new Dispatcher(null);
    }

    /**
     * Valida la instanccia del Dispatcher con una estrategia para atender llamadas definidas
     */
    @Test(expected = NullPointerException.class)
    public void testDispatcherCreacionConEstrategiaNull() {
        new Dispatcher(new ArrayList<>(), null);
    }


    /**
     * Test principal del Dispatcher
     *
     * Construye una cantidad de llamadas determinadas, con una duración aleatoria entre 5 y 10 segundos.
     * La lista de llamadas empieza a ser atendidas por los empleados según la estrategia definida:
     *  primero atienden los operadores disponibles, luego los supervisores disponibles y luego el director.
     * Si llegan más llamadas que la capacidad instalada, quedan en cola y se atienden en el orden de llegada, tan pronto
     * haya un empleado disponible.
     */
    @Test
    public void testDispatchLlamadasAEmpleados() throws InterruptedException {
        List<Empleado> employeeList = buildListaDeEmpleados();
        Dispatcher dispatcher = new Dispatcher(employeeList);
        dispatcher.start();
        TimeUnit.SECONDS.sleep(1);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(dispatcher);
        TimeUnit.SECONDS.sleep(1);

        buildListaDeLlamadas().stream().forEach(call -> {
            dispatcher.dispatchCall(call);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                fail();
            }
        });

        executorService.awaitTermination(MAX_DURACION_LLAMADA * 2, TimeUnit.SECONDS);
        assertEquals(CANTIDAD_LLAMADAS, employeeList.stream().mapToInt(employee -> employee.getLlamadasAtendidas().size()).sum());
    }

    /**
     * Construye una lista de empleados (6 operadores, 3 supervidores y 1 un director
     * @return una lista de empleados
     */
    private static List<Empleado> buildListaDeEmpleados() {
        Empleado operator1 = EmpleadoBuilder.buildOperador();
        Empleado operator2 = EmpleadoBuilder.buildOperador();
        Empleado operator3 = EmpleadoBuilder.buildOperador();
        Empleado operator4 = EmpleadoBuilder.buildOperador();
        Empleado operator5 = EmpleadoBuilder.buildOperador();
        Empleado operator6 = EmpleadoBuilder.buildOperador();
        Empleado supervisor1 = EmpleadoBuilder.buildSupervisor();
        Empleado supervisor2 = EmpleadoBuilder.buildSupervisor();
        Empleado supervisor3 = EmpleadoBuilder.buildSupervisor();
        Empleado director = EmpleadoBuilder.buildDirector();
        return Arrays.asList(operator1, operator2, operator3, operator4, operator5, operator6,
                supervisor1, supervisor2, supervisor3, director);
    }

    /**
     * Construye una lista de llamadas
     *
     * @return una lista de llamadas con cantidad, duracion minima y maxima seteadas
     */
    private static List<Llamada> buildListaDeLlamadas() {
        return Llamada.buildListaDeLlamadas(CANTIDAD_LLAMADAS, MIN_DURACION_LLAMADA, MAX_DURACION_LLAMADA);
    }

}
