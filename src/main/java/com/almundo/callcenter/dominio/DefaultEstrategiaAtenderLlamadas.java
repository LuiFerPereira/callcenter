package com.almundo.callcenter.dominio;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Estrategia por defecto para atender llamadas.
 *
 * Se basa en estrategia: El proceso de la atención de una llamada telefónica en primera
 * instancia debe ser atendida por un operador, si no hay ninguno libre debe
 * ser atendida por un supervisor, y de no haber tampoco supervisores libres
 * debe ser atendida por un director.
 *
 */
public class DefaultEstrategiaAtenderLlamadas implements EstrategiaAtenderLlamadas {

    private static final Logger logger = LoggerFactory.getLogger(DefaultEstrategiaAtenderLlamadas.class);

    /**
     * Busca el empleado disponible de acuerdo a la estrategia:
     * primero atienden los operadores disponibles, luego los supervisores disponibles y luego el director.
     * @param EmpleadoList: Lista de empleados del CallCenter
     * @return retorna empleado disponible. Si no hay disponible, retorna Null
     */
    @Override
    public Empleado findEmpleado(Collection<Empleado> EmpleadoList) {
        Validate.notNull(EmpleadoList);
        List<Empleado> empleadosDisponibles = EmpleadoList.stream().filter(e -> e.getEstadoEmpleado() == EstadoEmpleado.DISPONIBLE).collect(Collectors.toList());
        logger.info("Empleados Disponibles: " + empleadosDisponibles.size());
        Optional<Empleado> Empleado = empleadosDisponibles.stream().filter(e -> e.getTipoEmpleado() == TipoEmpleado.OPERADOR).findAny();
        if (!Empleado.isPresent()) {
            logger.info("No operadores disponibles encontrados");
            Empleado = empleadosDisponibles.stream().filter(e -> e.getTipoEmpleado() == TipoEmpleado.SUPERVISOR).findAny();
            if (!Empleado.isPresent()) {
                logger.info("No supervisors disponibles encontrados");
                Empleado = empleadosDisponibles.stream().filter(e -> e.getTipoEmpleado() == TipoEmpleado.DIRECTOR).findAny();
                if (!Empleado.isPresent()) {
                    logger.info("No directores disponibles encontrados");
                    return null;
                }
            }
        }
        logger.info("Empleado de tipo " + Empleado.get().getTipoEmpleado() + " encontrados");
        return Empleado.get();
    }
}
