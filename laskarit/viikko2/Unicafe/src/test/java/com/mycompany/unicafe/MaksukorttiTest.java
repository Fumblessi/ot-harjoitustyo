package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MaksukorttiTest {

    Maksukortti kortti;

    @Before
    public void setUp() {
        kortti = new Maksukortti(10);
    }

    @Test
    public void luotuKorttiOlemassa() {
        assertTrue(kortti!=null);      
    }
    
    @Test
    public void saldoAlussaOikein() {
        assertEquals("saldo: 0.10", kortti.toString());
    }
    
    @Test
    public void rahanLatausToimii() {
        kortti.lataaRahaa(200);
        assertEquals("saldo: 2.10", kortti.toString());
    }
    
    @Test
    public void otaRahaaToimiiKunRahaaTarpeeksi() {
        Maksukortti uusikortti = new Maksukortti(100);
        uusikortti.otaRahaa(50);
        assertEquals("saldo: 0.50", uusikortti.toString());
    }
    
    @Test
    public void otaRahaaToimiiKunRahaaEiTarpeeksi() {
        kortti.otaRahaa(100);
        assertEquals("saldo: 0.10", kortti.toString());
    }
    
    @Test
    public void otaRahaaPalauttaaTrue() {
        Maksukortti uusikortti = new Maksukortti(100);
        assertTrue(uusikortti.otaRahaa(5));
    }
    
    @Test
    public void otaRahaaPalauttaaFalse() {
        assertTrue(!kortti.otaRahaa(100));
    }
}
