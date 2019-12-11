# Käyttöohje

-----------------------------------------------------------------------------------------------------------------------------

## Dungeons and Dragons ja termistö

Dungeons and Dragons on suosittu roolipeli, johon generaattorin on tarkoitus tehdä satunnaisia hahmoja. Normaalisti pelatessa jokaisella pelaajallaan on oma hahmo, jolle he ovat valinneet hahmon rodun (Orc, Dwarf tai Human yms.), hahmon luokan (esim. Fighter tai Wizard yms.), ja hahmon taustan (Noble, Soldier tai Sage yms.). Tämän jälkeen pelaajat saavat hahmollemaan piirteet (usein jakamalla tietyn pistemäärän eri piirteiden kesken tai heittämällä nopilla) Strength (voima), Dexterity (ketteryys), Constitution (kestävyys), Intelligence (älykkyys), Wisdom (viisaus) ja Charisma (karisma).

Koska hahmonluontiin kuluu mittavasti aikaa, peliporukassamme on aika ajoin esiintynyt tarve pystyä nopeasti laittamaan peli kasaan ja pelaamaan yhden päivän/illan mittainen rento sessio, johon hahmogeneraattorille on ollut kysyntää. Tämä on motivaatio projektityöni taustalla, ja luultavasti jatkan projektin kehittämistä Ohjelmistotekniikka-kurssin jälkeenkin.

## Termipankki

Erilaisia termejä joita ohjelmassani ja dokumenteissani käytän olen listannut ja suomentanut [tänne](https://github.com/Fumblessi/ot-harjoitustyo/blob/master/dokumentointi/termisto.md).

### Sääntömuunnokset verrattuna perinteiseen Dungeons and Dragons -roolipeliin

Peliporukassamme olemme muokanneet joitakin sääntöjä, ja hahmogeneraattorin olen tehnyt seuraamaan näitä sääntömuutoksia. Normaalisti esimerkiksi hahmon rotu määrittää sen rotuominaisuuden (Racial), mutta olemme tässä mielessä roolipeliporukassamme tasa-arvoistaneet eri rotuja, sillä yksilöinä hahmot kuitenkin voivat venyä vaikka mihin, ja pelaajat saavat itse valita rotuominaisuutensa kaikista eri rotujen tarjoamista vaihtoehdoista, riippumatta hahmon rodusta. Siksi generaattorikin arpoo hahmolle rotuominaisuudet kaikista vaihtoehdoista tietokantataulussa 'Racial'.

Jos jollakulla muulla tulisi generaattorille kysyntää, saattaisin tehdä näistä sääntömuutoksista valinnaisia, ja asetusten kautta säädettäviä.

-----------------------------------------------------------------------------------------------------------------------------

## Generointi

Tällä hetkellä Generoi-painikkeella generaattori luo satunnaiset hahmon piirteet noudattaen Asetuksissa määriteltyjä rajoja, eikä vielä arvo hahmolle taitoja/luokkaa/taustaa yms.

-----------------------------------------------------------------------------------------------------------------------------

## Asetukset

Tällä hetkellä Asetukset-välilehdeltä voi muokata hahmolle arvottavien piirteiden kokonaissummaa sekä jokaisen piirteen minimi-
ja maksimiarvoa. Parametrit tulisi asettaa niin, että kokonaissumma on suuruudeltaan 0-100, ja rajat ovat mahdolliset, eli
minimiarvo ei ole maksimiarvoa suurempi, ja että piirteiden kokonaissumma voidaan jakaa rajojen puitteissa kuuteen eri piirteeseen
(Strength, Dexterity, Constitution, Intelligence, Wisdom ja Charisma), ja ettei sitä jää yli. Ohjelma valittaa, mikäli asetetut
rajat ovat mahdottomat.

Halutessaan voi valita, että arvotaan samantien myös rodun pohjalta tulevat lisäpiirteet +2 ja +1 satunnaisiin piirteisiin.

Asetukset tallentuvat käyttökertojen välillä, mutta Asetukset-välilehdeltä voi palauttaa myös oletusasetukset (kokonaissumma on 70,
minimiarvo 8, maksimiarvo 18 ja rodun pohjalta tulevat piirrebonukset päällä).

Asetuksissa on nyt yläpalkissa menu "Tietokanta", josta painamalla näkee vaihtoehtoiset eri tietokantataulut. Tällä hetkellä ainoastaan Proficiency ja Racial ovat muokattavissa. Tietokantaa ei ole vielä täytetty, mutta joitakin esimerkkejä siellä on.

### Proficiency-tietokantataulun muokkaus

Tietokanta-palkista valitsemalla Proficiency, voi tarkastella tietokantataulun 'Proficiency' sisältöä, eli generoinnin pohjana olevat vaihtoehtoiset hahmon taidot, ja edelleen lisätä tietokaan uusia taitoja tai halutessaan muokata tai poistaa siellä jo entuudestaan olevia taitoja.

### Racial-tietokantataulun muokkaus

Tietokanta-palkista valitsemalla Racial, voi tarkastella tietokantataulun 'Racial' sisältöä, eli generoinnin pohjana olevia hahmon ominaisuuksia, jotka perustuvat sen rotuun (esim. örkki, haltija, ihminen yms). Halutessaan sieltä voi lisätä tietokantaan uusia ominaisuuksia, tai muokata ja poistaa siellä jo entuudestaan olevia ominaisuuksia.

### Class-tietokantataulun muokkaus

Ei vielä tehty

### Background-tietokantataulun muokkaus

Ei vielä tehty

### Feat-tietokantataulun muokkaus

Ei vielä tehty

-----------------------------------------------------------------------------------------------------------------------------

## Usein kysytyt kysymykset

### 1. Miksi käyttäjän tarvitsee pystyä muokkaamaan tietokantojen sisältöä?

Dungeons and Dragons peli päivittyy koko ajan, tulee uusia rotuja, uusia taustoja ja uusia taitoja hahmoille. Hahmogeneraattori kykenee nyt sellaiseen seuraamaan näitä muutoksia, sillä käyttäjä voi lisätä tietokantaan uutta tavaraa sitä mukaa kun sitä tulee. Tietokannan muokkaus mahdollistaa myös omien vaihtoehtojen keksimisen ja lisäämisen generaattoriin, joten sen tavoite on mukautua peliporukan tarpeisiin.

Esimerkiksi 'Lucky'-niminen feat (erityistaito) on yleisesti koettu verrattaen todella hyväksi, joten moni peliporukka saattaisi haluta sen ottaa pois tietokannasta, vaikka se on sinänsä täysin sääntöjen mukainen.

### 2. Generaattorisi puhuu englantia ja suomea sekaisin! Päätä jo kumpi!

Tätä olen paljon pyöritellyt, että kieltämättä olisi mukavaa puhua suoraan vain kokonaan jompaa kumpaa kieltä. Olemme halunneet peliporukassamme siirtyä mahdollisimman paljolti suomen käyttämiseen, mutta silti on selkeämpää viitata tiettyihin asioihin yksiselitteisesti samoilla termeillä kun D&D-pelin säännöissä, joten osa termistöstä ikävä kyllä säilynee englanninkielisenä, vaikka käyttöliittymä pitkälti onkin suomea. Pahoittelut.
