package com.almundo.callcenter.dominio;

import java.util.Collection;

/**
 * Interface que modela las estrategias para encontrar un empleado que atienda la llamada
 */
public interface EstrategiaAtenderLlamadas {

    /**
     * Busca el empleado disponible para atender la llamada
     *
     * @param listaDeEmpleados Lista de empleados
     * @return Retorna al empleado que va atender la llamada
     */
    Empleado findEmpleado(Collection<Empleado> listaDeEmpleados);
}
