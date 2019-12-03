# Käyttöohje

## Asetukset

Tällä hetkellä Asetukset-välilehdeltä voi muokata hahmolle arvottavien piirteiden kokonaissummaa sekä jokaisen piirteen minimi-
ja maksimiarvoa. Parametrit tulisi asettaa niin, että kokonaissumma on suuruudeltaan 0-100, ja rajat ovat mahdolliset, eli
minimiarvo ei ole maksimiarvoa suurempi, ja että piirteiden kokonaissumma voidaan jakaa rajojen puitteissa kuuteen eri piirteeseen
(Strength, Dexterity, Constitution, Intelligence, Wisdom ja Charisma), ja ettei sitä jää yli. Ohjelma valittaa, mikäli asetetut
rajat ovat mahdottomat.

Halutessaan voi valita, että arvotaan samantien myös rodun pohjalta tulevat lisäpiirteet +2 ja +1 satunnaisiin piirteisiin.

Asetukset tallentuvat käyttökertojen välillä, mutta Asetukset-välilehdeltä voi palauttaa myös oletusasetukset (kokonaissumma on 70,
minimiarvo 8, maksimiarvo 18 ja rodun pohjalta tulevat piirrebonukset päällä).

### Uuden taidon lisääminen tietokantaan

Asetuksissa on nyt painike "Lisää tietokantaan", josta painamalla käyttäjä pääsee ikkunaan, jossa voi valita, mitä haluaa lisätä tietokantaan.
Tällä hetkellä ainoastaan painike "Proficiency" (taito) toimii, jota painamalla pääsee edelleen ikkunaan, jossa ohjelma pyytää kyseisen uuden taidon nimeä sekä tyyppiä (tyyppi on valittavissa muutamasta vaihtoehdosta). Painamalla tietojen täyttämisen jälkeen painiketta "Lisää", lisätään kyseinen uusi taito tietokantatauluun "Skill" (josta myöhemmin generaattori tuli arpomaan hahmolle taitoja).

### HUOM!

Tällä hetkellä ohjelma ei tarkista, syötetäänkö asetuksiin numeroita, vaan ei vaan pysty enää generoimaan, jos käyttäjä itse asettaa jotain muuta kuin kokonaislukuarvoisia parametreja asetuksiin. Korjautuu piakkoin.

## Generointi

Tällä hetkellä Generoi-painikkeella generaattori luo satunnaiset hahmon piirteet noudattaen Asetuksissa määriteltyjä rajoja.
