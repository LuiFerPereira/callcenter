package com.almundo.callcenter.dominio;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultEstrategiaAtenderLlamadasTest {

    private EstrategiaAtenderLlamadas estrategiaAtenderLlamadas;

    public DefaultEstrategiaAtenderLlamadasTest() {
        this.estrategiaAtenderLlamadas = new DefaultEstrategiaAtenderLlamadas();
    }

    @Test
    public void testAsignarAOperador() {
        Empleado operador = EmpleadoBuilder.buildOperador();
        Empleado supervisor = EmpleadoBuilder.buildSupervisor();
        Empleado director = EmpleadoBuilder.buildDirector();
        List<Empleado> empleadoList = Arrays.asList(operador, supervisor, director);

        Empleado empleado = this.estrategiaAtenderLlamadas.findEmpleado(empleadoList);

        assertNotNull(empleado);
        assertEquals(TipoEmpleado.OPERADOR, empleado.getTipoEmpleado());
    }

    @Test
    public void testAsignarASupervisor() {
        Empleado operador = mock(Empleado.class);
        when(operador.getEstadoEmpleado()).thenReturn(EstadoEmpleado.OCUPADO);
        Empleado supervisor = EmpleadoBuilder.buildSupervisor();
        Empleado director = EmpleadoBuilder.buildDirector();
        List<Empleado> empleadoList = Arrays.asList(operador, supervisor, director);

        Empleado empleado = this.estrategiaAtenderLlamadas.findEmpleado(empleadoList);

        assertNotNull(empleado);
        assertEquals(TipoEmpleado   .SUPERVISOR, empleado.getTipoEmpleado());
    }

    @Test
    public void testAsignarADirector() {
        Empleado operador = mockEmpleadoOcupado(TipoEmpleado.OPERADOR);
        Empleado supervisor = mockEmpleadoOcupado(TipoEmpleado.SUPERVISOR);
        Empleado director = EmpleadoBuilder.buildDirector();
        List<Empleado> empleadoList = Arrays.asList(operador, supervisor, director);

        Empleado empleado = this.estrategiaAtenderLlamadas.findEmpleado(empleadoList);

        assertNotNull(empleado);
        assertEquals(TipoEmpleado.DIRECTOR, empleado.getTipoEmpleado());
    }

    @Test
    public void testAsignarANadie() {
        Empleado operador = mockEmpleadoOcupado(TipoEmpleado.OPERADOR);
        Empleado supervisor = mockEmpleadoOcupado(TipoEmpleado.SUPERVISOR);
        Empleado director = mockEmpleadoOcupado(TipoEmpleado.DIRECTOR);
        List<Empleado> empleadoList = Arrays.asList(operador, supervisor, director);

        Empleado empleado = this.estrategiaAtenderLlamadas.findEmpleado(empleadoList);

        assertNull(empleado);
    }

    private static Empleado mockEmpleadoOcupado(TipoEmpleado empleadoType) {
        Empleado empleado = mock(Empleado.class);
        when(empleado.getTipoEmpleado()).thenReturn(empleadoType);
        when(empleado.getEstadoEmpleado()).thenReturn(EstadoEmpleado.OCUPADO);
        return empleado;
    }

}
