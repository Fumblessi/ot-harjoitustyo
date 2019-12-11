## Hahmogeneraattori-sovellus

### Sovelluksen tarkoitus
Sovelluksen on tarkoitus generoida, eli arpoa, käyttäjälleen satunnainen roolipelihahmo, joka pohjautuu hahmonluontiin
kaveriporukallamme pelaamassa roolipelissä. Roolipelin säännöt ovat pitkälti tutut Dungeons and Dragons 5th editionin säännöistä,
mutta hieman peliporukkamme muokkaamat.

Sovellus arpoo siis hahmolle hahmoluokan, rodun ja taustan. Näiden pohjalta sovellus edelleen arpoo hahmolle piirteet (Strength,
Dexterity, Constitution, Intelligence, Wisdom ja Charisma) sekä hahmon taidot (esimerkiksi puhutut kielet tai asiat, missä hahmo
on hyvä).

### Käyttäjät
Alkuvaiheessa sovellukseen ei tule kuulumaan käyttäjiä tai kirjautumista ollenkaan. Jossain vaiheessa käyttäjätoiminnallisuus
voitaisiin lisätä, jotta sovellus muistaisi käyttäjäkohtaiset asetukset, jolloin ohjelma sisältäisi yhden käyttäjäroolin. Tämä
ei kuitenkaan välttämättä ole oleellista vielä tämän kurssin puitteissa.

### Käyttöliittymäluonnos
Käyttöliittymästä ei ole hahmoteltua luonnosta, mutta käyttöliittymä tulee pohjautumaan kahteen eri näkymään; Perusnäkymään, jossa
on painikkeet "Generoi" sekä "Asetukset", joista ensimmäinen arpoo näkymään generoidun hahmon tiedot, ja joista toinen vie Asetukset-
näkymään, josta generoinnin parametreja voi muokata.

Tämän lisäksi Asetukset -näkymästä voidaan avata kolmas näkymä, josta voi muokata tietokannan sisältöä.

### Toiminnallisuus
* Sovellus arpoo hahmoluokan, rodun ja taustan vastaavista tietokantatauluista, joihin eri vaihtoehdot on listattu
* Edelleen näiden pohjalta sovellus arpoo hahmolle piirteet (Strength, Dexterity, Constitution, Intelligence, Wisdom, Charisma) **TEHTY** sekä sen omaamat taidot
* Asetukset -välilehdeltä generointia pystyy rajoittamaan:
  * Voi valita arvontaan vain tietyt luokat
  * Voi rajata piirteiden määrää arvonnassa, ja lisäksi valita ottavansa mukaan satunnaiset +2 ja +1 piirteet hahmon rodusta **TEHTY**
* Asetukset -välilehdeltä voi lisäksi muokata tietokantojen sisältöä, voi esimerkiksi poistaa tai lisätä hahmoluokkia, rotuja tai
taustoja, joiden joukosta generointi tehdään **TEHTY Kokonaan Proficiency-tietokantataulun osalta sekä melkein Racial-tietokantataulun osalta**

### Jatkokehitysideoita
Luultavasti yllälistatussa toiminnallisuudessa on jo tarpeeksi tekemistä tämän kurssin puitteisiin, mutta jos aikaa jää, koitetaan
toteuttaa näitäkin.

* Asetukset -välilehdeltä pystyy valitsemaan hahmon aloitustason (eikä se oletuksena ole 1). Tällöin generoidun hahmon tietoihin
on lisätty myös piirteet ja ominaisuudet, joita hahmo on saanut hahmotason kasvaessa
* Asetukset -välilehdeltä pystyy valitsemaan, että hahmolle saattaa arpoutua useampi hahmoluokka yhden sijaan (jos aloitustaso on
suurempi kuin 1). Käyttäjä pystyy itse vaikuttamaan, kuinka monta hahmoluokkaa arpoutuu minimissään ja kuinka monta maksimissaan, ja
mahdollisesti vaikuttamaan vielä näiden eri vaihtoehtojen todennäköisyyksiin
* Asetukset -välilehdeltä voi lisätä arvontaan esimerkiksi tavaroita, joiden kanssa hahmo aloittaa, tai jotain muita lisäpiirteitä
liittyen hahmoon
* Sovellus luo generoidusta hahmosta kuvan, niin että pohjakuva valitaan rodun perusteella, ja sen vaatetus arvotun hahmoluokan perusteella
* Sovellus arpoo muutenkin hahmon ulkonäön, pituuden, painon, iän, silmien/hiusten värin yms.
