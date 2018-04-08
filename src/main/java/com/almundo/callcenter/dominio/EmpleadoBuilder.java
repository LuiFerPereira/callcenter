package com.almundo.callcenter.dominio;


import com.almundo.callcenter.dominio.Empleado;
import com.almundo.callcenter.dominio.TipoEmpleado;

/**
 * Clase con patrón Builder
 * Construye instancias de la clase Empleado por cada tipo
 */
public abstract class EmpleadoBuilder {

    public static Empleado buildOperador() {
        return new Empleado(TipoEmpleado.OPERADOR);
    }

    public static Empleado buildSupervisor() {
        return new Empleado(TipoEmpleado.SUPERVISOR);
    }

    public static Empleado buildDirector() {
        return new Empleado(TipoEmpleado.DIRECTOR);
    }
}
