package com.almundo.callcenter.dominio;

import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Representa una llamada con una duración dada en segundos
 */
public class Llamada {

    private Integer duracionEnSegundos;

    /**
     * Crea una llamada con una duracion dada en segundos
     *
     * @param duracionEnSegundos duracion de llamada.
     */
    public Llamada(Integer duracionEnSegundos) {
        Validate.notNull(duracionEnSegundos);
        Validate.isTrue(duracionEnSegundos >= 0);
        this.duracionEnSegundos = duracionEnSegundos;
    }

    public Integer getDuracionEnSegundos() {
        return duracionEnSegundos;
    }

    /**
     * Crea una nueva llamada con una duracion aleatoria
     *
     * @param minDuracionEnSegundos duracion minima
     * @param maxDuracionEnSegundos duracion maxima
     * @return una nueva llamada con una duracion aleatoria entre el minimo y maximo
     */
    public static Llamada buildLlamada(Integer minDuracionEnSegundos, Integer maxDuracionEnSegundos) {
        Validate.isTrue(maxDuracionEnSegundos >= minDuracionEnSegundos && minDuracionEnSegundos >= 0);
        return new Llamada(ThreadLocalRandom.current().nextInt(minDuracionEnSegundos, maxDuracionEnSegundos + 1));
    }

    /**
     *
     * Crea una lista de llamadas aleatoria
     *
     * @param tamanioLista             tamanio de la lista de llamadas por crear
     * @param minDuracionEnSegundos duracion minima
     * @param maxDuracionEnSegundos duracion maxima
     * @return una lista de llamadas aleatorias
     */
        public static List<Llamada> buildListaDeLlamadas(Integer tamanioLista, Integer minDuracionEnSegundos, Integer maxDuracionEnSegundos) {
        Validate.isTrue(tamanioLista >= 0);
        List<Llamada> listaDellamdas = new ArrayList<>();
        for (int i = 0; i < tamanioLista; i++) {
            listaDellamdas.add(buildLlamada(minDuracionEnSegundos, maxDuracionEnSegundos));
        }
        return listaDellamdas;
    }
}
