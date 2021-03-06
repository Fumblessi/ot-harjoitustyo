## Hahmogeneraattori-sovellus

### Sovelluksen tarkoitus
Sovelluksen on tarkoitus generoida, eli arpoa, käyttäjälleen satunnainen roolipelihahmo, joka pohjautuu hahmonluontiin
kaveriporukallamme pelaamassa roolipelissä. Roolipelin säännöt ovat pitkälti tutut Dungeons and Dragons 5th editionin säännöistä, mutta hieman peliporukkamme muokkaamat (ks. [käyttöohje](https://github.com/Fumblessi/ot-harjoitustyo/blob/master/dokumentointi/kaytto-ohje.md)).

Sovellus arpoo siis hahmolle hahmoluokan, rodun ja taustan. Näiden pohjalta sovellus edelleen arpoo hahmolle piirteet (Strength, Dexterity, Constitution, Intelligence, Wisdom ja Charisma) sekä hahmon taidot (esimerkiksi puhutut kielet tai asiat, missä hahmo on hyvä).

### Käyttäjät
Sovellukseen ei kuulu kirjautumista lainkaan. Jossain vaiheessa tämä toiminnallisuus voitaisiin lisätä, jos halutaan tallentaa käyttäjäkohtaisesti asetuksia.

### Käyttöliittymä

Käyttöliittymä jakautuu kolmeen päänäkymään; 1) Generointi-ikkunaan, 2) Asetukset-ikkunaan ja 3) Eri tietokantataulujen käsittely-ikkunaan. Edelleen pienempiä ponnahdusikkunoita liittyy tietokantataulujen muokkaamiseen.

### Toiminnallisuus
* Sovellus arpoo hahmoluokan, rodun, rotuominaisuudet ja taustan vastaavista tietokantatauluista, joihin eri vaihtoehdot on listattu
* Edelleen näiden pohjalta sovellus arpoo hahmolle piirteet (Strength, Dexterity, Constitution, Intelligence, Wisdom, Charisma) sekä sen omaamat taidot 
* Asetukset -välilehdeltä generointia pystyy rajoittamaan:
  * Voi määrittää arvottujen piirteiden määrän 
  * Voi määrittää arvottujen rotuominaisuuksien määrän 
  * Voi valita ottavansa mukaan satunnaiset +2 ja +1 piirteet hahmon rodusta 
  * Voi määrittää taustan antamien taitojen määrät ja eri tyyppien todennäköisyydet 
  * Voi määrittää arvottujen kielten määrän ja eri kielien harvinaisuuksien ja osaamistasojen todennäköisyydet 
  * Voi valita pakottavansa yhden äidinkielen kielten joukkoon 
* Asetukset -välilehdeltä voi lisäksi muokata tietokantojen sisältöä, voi esimerkiksi poistaa tai lisätä hahmoluokkia, rotuja tai taustoja, joiden joukosta generointi tehdään 

### Jatkokehitysideoita

* Asetukset -välilehdeltä pystyy valitsemaan hahmon aloitustason (eikä se oletuksena ole 1). Tällöin generoidun hahmon tietoihin on lisätty myös piirteet ja ominaisuudet, joita hahmo on saanut hahmotason kasvaessa
* Asetukset -välilehdeltä pystyy valitsemaan, että hahmolle saattaa arpoutua useampi hahmoluokka yhden sijaan (jos aloitustaso on suurempi kuin 1). Käyttäjä pystyy itse vaikuttamaan, kuinka monta hahmoluokkaa arpoutuu minimissään ja kuinka monta maksimissaan, ja mahdollisesti vaikuttamaan vielä näiden eri vaihtoehtojen todennäköisyyksiin
* Asetukset -välilehdeltä voi lisätä arvontaan esimerkiksi tavaroita, joiden kanssa hahmo aloittaa, tai jotain muita lisäpiirteitä liittyen hahmoon, kuten erityistaidon
* Sovellus luo generoidusta hahmosta kuvan, niin että pohjakuva valitaan rodun perusteella, ja sen vaatetus arvotun hahmoluokan perusteella
* Sovellus arpoo muutenkin hahmon ulkonäön, pituuden, painon, iän, silmien/hiusten värin yms.
* Background-tietokantataulun lisäksi myös Racial- ja Feat-tietokannat sisältävät kuvauksen ks. rotuominaisuudesta tai erityistaidosta, jotka sitten generoitaessa voi katsoa mouse over-tekstinä
* Tietokanta verkkoon, jotta peliporukan sisällä voidaan päivittää sitä ja synkronoida muutokset omiinkin tietokantoihin
