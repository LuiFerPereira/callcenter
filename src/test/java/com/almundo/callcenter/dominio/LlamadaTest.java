package com.almundo.callcenter.dominio;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class LlamadaTest {

    @Test(expected = IllegalArgumentException.class)
    public void testCreacionLlamadaConParametrosInvalidados() {
        new Llamada(-1);
    }

    @Test(expected = NullPointerException.class)
    public void testCreacionLlamadaParametroNull() {
        new Llamada(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreacionLlamadaConPrimerParametroInvalido() {
        Llamada.buildLlamada(-1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreacionLlamadaConSegundoParametroInvalido() {
        Llamada.buildLlamada(1, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreacionLlamadaConParametrosEnDesorden() {
        Llamada.buildLlamada(2, 1);
    }

    @Test
    public void testCreacionLlamadaConParametrosValidos() {
        Integer min = 5;
        Integer max = 10;
        Llamada call = Llamada.buildLlamada(min, max);

        assertNotNull(call);
        assertTrue(min <= call.getDuracionEnSegundos());
        assertTrue(call.getDuracionEnSegundos() <= max);
    }

}
