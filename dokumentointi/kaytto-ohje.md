# Käyttöohje

## Asetukset

Tällä hetkellä Asetukset-välilehdeltä voi muokata hahmolle arvottavien piirteiden kokonaissummaa sekä jokaisen piirteen minimi-
ja maksimiarvoa. Parametrit tulisi asettaa niin, että kokonaissumma on suuruudeltaan 0-100, ja rajat ovat mahdolliset, eli
minimiarvo ei ole maksimiarvoa suurempi, ja että piirteiden kokonaissumma voidaan jakaa rajojen puitteissa kuuteen eri piirteeseen
(Strength, Dexterity, Constitution, Intelligence, Wisdom ja Charisma), ja ettei sitä jää yli. Ohjelma valittaa, mikäli asetetut
rajat ovat mahdottomat.

Asetukset tallentuvat käyttökertojen välillä, mutta Asetukset-välilehdeltä voi palauttaa myös oletusasetukset (kokonaissumma on 70,
minimiarvo 8 ja maksimiarvo 18).

### HUOM!

Tällä hetkellä ohjelma ei tarkista, syötetäänkö asetuksiin numeroita, vaan ei vaan pysty enää generoimaan, jos käyttäjä ei itse aseta kokonaislukuarvoisia parametreja asetuksiin. Korjautuu piakkoin.

## Generointi

Tällä hetkellä Generoi-painikkeella generaattori luo satunnaiset hahmon piirteet noudattaen Asetuksissa määriteltyjä rajoja.
