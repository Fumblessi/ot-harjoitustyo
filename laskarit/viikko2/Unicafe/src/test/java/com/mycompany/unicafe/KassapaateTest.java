/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.unicafe;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sampo
 */
public class KassapaateTest {
    
    Kassapaate kassa;
    Maksukortti kortti;
    Maksukortti koyhakortti;
    
    public KassapaateTest() {
    }
    
    @Before
    public void setUp() {
        kassa = new Kassapaate();
        kortti = new Maksukortti(500);
        koyhakortti = new Maksukortti(200);
    }
    
    @Test
    public void luodussaKassassaOikeaMaaraRahaa() {
        assertEquals(100000, kassa.kassassaRahaa());
    }
    
    @Test
    public void luodussaKassassaNollaMyytyaMaukasta() {
        assertEquals(0, kassa.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void luodussaKassassaNollaMyytyaEdullista() {
        assertEquals(0, kassa.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void edullisenOstoKunRahaaTarpeeksiSaldoKasvaa() {
        kassa.syoEdullisesti(240);
        assertEquals(100240, kassa.kassassaRahaa());
    }
    
    @Test
    public void edullisenOstoKunRahaaTarpeeksiOikeaVaihtoraha() {
        assertEquals(60, kassa.syoEdullisesti(300));
    }
    
    @Test
    public void edullisenOstoKunRahaaTarpeeksiMyytyjenMaaraKasvaa() {
        kassa.syoEdullisesti(240);
        assertEquals(1, kassa.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void edullisenOstoKunRahaaEiTarpeeksiPalautetaanRahat() {
        assertEquals(100, kassa.syoEdullisesti(100));
    }
    
    @Test
    public void edullisenOstoKunRahaaEiTarpeeksiMyydytEiMuutu() {
        kassa.syoEdullisesti(100);
        assertEquals(0, kassa.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void maukkaanOstoKunRahaaTarpeeksiSaldoKasvaa() {
        kassa.syoMaukkaasti(400);
        assertEquals(100400, kassa.kassassaRahaa());
    }
    
    @Test
    public void maukkaanOstoKunRahaaTarpeeksiOikeaVaihtoraha() {
        assertEquals(50, kassa.syoMaukkaasti(450));
    }
    
    @Test
    public void maukkaanOstoKunRahaaTarpeeksiMyytyjenMaaraKasvaa() {
        kassa.syoMaukkaasti(400);
        assertEquals(1, kassa.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void maukkaanOstoKunRahaaEiTarpeeksiPalautetaanRahat() {
        assertEquals(300, kassa.syoMaukkaasti(300));
    }
    
    @Test
    public void maukkaanOstoKunRahaaEiTarpeeksiMyydytEiMuutu() {
        kassa.syoMaukkaasti(300);
        assertEquals(0, kassa.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void edullisenOstoKortinSaldoVahenee() {
        kassa.syoEdullisesti(kortti);
        assertEquals("saldo: 2.60", kortti.toString());
    }
    
    @Test
    public void edullisenOstoKortillaPalauttaaTrue() {
        assertTrue(kassa.syoEdullisesti(kortti));
    }
    
    @Test
    public void edullisenOstoKortillaMyydytKasvaa() {
        kassa.syoEdullisesti(kortti);
        assertEquals(1, kassa.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void edullisenOstoKortillaKassanSaldoEiMuutu() {
        kassa.syoEdullisesti(kortti);
        assertEquals(100000, kassa.kassassaRahaa());
    }
    
    @Test
    public void edullisenOstoKortillaEiRahaaPalauttaaFalse() {
        assertTrue(!kassa.syoEdullisesti(koyhakortti));
    }
    
    @Test
    public void edullisenOstoKortillaEiRahaaEiMuutaSaldoa() {
        kassa.syoEdullisesti(koyhakortti);
        assertEquals("saldo: 2.0", koyhakortti.toString());
    }
    
    @Test
    public void edullisenOstoKortillaEiRahaaEiMuutaMyytyjenMaaraa() {
        kassa.syoEdullisesti(koyhakortti);
        assertEquals(0, kassa.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void maukkaanOstoKortinSaldoVahenee() {
        kassa.syoMaukkaasti(kortti);
        assertEquals("saldo: 1.0", kortti.toString());
    }
    
    @Test
    public void maukkaanOstoKortillaPalauttaaTrue() {
        assertTrue(kassa.syoMaukkaasti(kortti));
    }
    
    @Test
    public void maukkaanOstoKortillaMyydytKasvaa() {
        kassa.syoMaukkaasti(kortti);
        assertEquals(1, kassa.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void maukkaanOstoKortillaKassanSaldoEiMuutu() {
        kassa.syoMaukkaasti(kortti);
        assertEquals(100000, kassa.kassassaRahaa());
    }
    
    @Test
    public void maukkaanOstoKortillaEiRahaaPalauttaaFalse() {
        assertTrue(!kassa.syoMaukkaasti(koyhakortti));
    }
    
    @Test
    public void maukkaanOstoKortillaEiRahaaEiMuutaSaldoa() {
        kassa.syoMaukkaasti(koyhakortti);
        assertEquals("saldo: 2.0", koyhakortti.toString());
    }
    
    @Test
    public void maukkaanOstoKortillaEiRahaaEiMuutaMyytyjenMaaraa() {
        kassa.syoMaukkaasti(koyhakortti);
        assertEquals(0, kassa.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void kortinLatausLisaaKortilleRahat() {
        kassa.lataaRahaaKortille(kortti, 500);
        assertEquals("saldo: 10.0", kortti.toString());
    }
    
    @Test
    public void kortinLatausLisaaRahatKassaan() {
        kassa.lataaRahaaKortille(kortti, 500);
        assertEquals(100500, kassa.kassassaRahaa());
    }
    
    @Test
    public void negatiivisenSummanLataaminenKortilleSaldoEiMuutu() {
        kassa.lataaRahaaKortille(kortti, -100);
        assertEquals("saldo: 5.0", kortti.toString());
    }
    
    @Test
    public void negatiivisenSummanLataaminenKortilleKassaEiMuutu() {
        kassa.lataaRahaaKortille(kortti, -100);
        assertEquals(100000, kassa.kassassaRahaa());
    }
}
