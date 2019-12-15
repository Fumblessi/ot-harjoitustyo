# Käyttöohje

-----------------------------------------------------------------------------------------------------------------------------

## Dungeons and Dragons ja termistö

Dungeons and Dragons on suosittu roolipeli, johon generaattorin on tarkoitus tehdä satunnaisia hahmoja. Normaalisti pelatessa jokaisella pelaajallaan on oma hahmo, jolle he ovat valinneet hahmon rodun (Orc, Dwarf tai Human yms.), hahmon luokan (esim. Fighter tai Wizard yms.), ja hahmon taustan (Noble, Soldier tai Sage yms.). Tämän jälkeen pelaajat saavat hahmollemaan piirteet (usein jakamalla tietyn pistemäärän eri piirteiden kesken tai heittämällä nopilla) Strength (voima), Dexterity (ketteryys), Constitution (kestävyys), Intelligence (älykkyys), Wisdom (viisaus) ja Charisma (karisma).

Koska hahmonluontiin kuluu mittavasti aikaa, peliporukassamme on aika ajoin esiintynyt tarve pystyä nopeasti laittamaan peli kasaan ja pelaamaan yhden päivän/illan mittainen rento sessio, johon hahmogeneraattorille on ollut kysyntää. Tämä on motivaatio projektityöni taustalla, ja luultavasti jatkan projektin kehittämistä Ohjelmistotekniikka-kurssin jälkeenkin.

## Termipankki

Erilaisia D&D-peliin liittyviä termejä, joita ohjelmassani ja dokumenteissani käytän, olen listannut ja suomentanut [tänne](https://github.com/Fumblessi/ot-harjoitustyo/blob/master/dokumentointi/termisto.md).

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

### Race-tietokantataulun muokkaus

Tietokanta-palkista valitsemalla Race, voi tarkastella tietokantataulun 'Race' sisältöä, eli generoinnin pohjana olevia vaihtoehtoisia rotuja, ja edelleen lisätä tietokantaan uusia rotuja tai halutessaan muokata tai poistaa siellä jo entuudestaan olevia.

<img src="https://raw.githubusercontent.com/Fumblessi/ot-harjoitustyo/master/dokumentointi/raceLisays.png" width="250">

Roduista tietokantaan tallennetaan tällä hetkellä vain niiden nimi. Myöhemmin saatan lisätä rotuun liittyviin tietoihin ulkonäköön liittyviä seikkoja, kuten hahmon pituuden, painon ja iän arpomisen, mihin rotu vaikuttaa.

### Proficiency-tietokantataulun muokkaus

Tietokanta-palkista valitsemalla Proficiency, voi tarkastella tietokantataulun 'Proficiency' sisältöä, eli generoinnin pohjana olevia vaihtoehtoisia hahmon taitoja, ja edelleen lisätä tietokaan uusia taitoja tai halutessaan muokata tai poistaa siellä jo entuudestaan olevia taitoja.

<img src="https://raw.githubusercontent.com/Fumblessi/ot-harjoitustyo/master/dokumentointi/proflisays.png" width="350">

1. Uuden taidon nimi.
2. Uuden taidon tyyppi.
3. Riippuen taidon tyypistä, taidolla saattaa olla myös alityyppejä.
4. "Lisää" lisää uuden taidon tietokantaan, "Takaisin" palaa proficiency-tietokantanäkymään.

### Racial-tietokantataulun muokkaus

Tietokanta-palkista valitsemalla Proficiency, voi tarkastella tietokantataulun 'Racial' sisältöä, eli generoinnin pohjana olevia vaihtoehtoisia hahmon rotuominaisuuksia, ja edelleen lisätä tietokantaan uusia ominaisuuksia tai halutessaan muokata tai poistaa siellä jo entuudestaan olevia.

<img src="https://raw.githubusercontent.com/Fumblessi/ot-harjoitustyo/master/dokumentointi/racialLisays.png" width="600">

1. Uuden rotuominaisuuden nimi.
2. Jos rotuominaisuus vaikuttaa hahmon piirteisiin, tähän kirjataan määrä. Voi olla negatiivinen tai positiivinen kokonaisluku.
3. Jos rotuominaisuus antaa varmasti hahmolle joitain taitoja, valitse ne ensimmäisestä listasta
4. Jos rotuominaisuus normaalisti antaisi pelaajan valita tietyistä vaihtoehtoisista taidosta tietyt, kirjaa kuinka monta pelaaja saisi valita ja valitse listasta, mistä vaihtoehdoista. Generaattori osaa sitten "arpoa" tämän valitsemin.
5. Jos rotuominaisuus antaa hahmolle uusia kieliä, kirjaa tähän määrä.
6. Jos rotuominaisuus antaa valinnaisia tietystä kategoriasta, kirjaa tähän montako, ja valitse alta mistä kategoriasta.
7. Jos rotuominaisuus antaa hahmolle erityistaidon (feat), ruksaa tämä boksi.
8. Painike "Lisää" lisää uuden rotuominaisuuden tietokantaan, painike "Takaisin" palaa Racial-tietokantanäkymään.

### Class-tietokantataulun muokkaus

Tietokanta-palkista valitsemalla Class, voi tarkastella tietokantataulun 'Class' sisältöä, eli generoinnin pohjana olevia vaihtoehtoisia hahmon hahmoluokkia, ja edelleen lisätä tietokantaan uusia hahmoluokkia tai halutessaan muokata tai poistaa siellä jo entuudestaan olevia.

<img src="https://raw.githubusercontent.com/Fumblessi/ot-harjoitustyo/master/dokumentointi/classLisays.png" width="600">

1. ks. Racial-tietokannan muokkaus (hahmoluokan taitojen antaminen toimii samalla tavalla, mutta hahmoluokka ei voi antaa hahmolle erityistaitoa tai vaikuttaa hahmon piirteisiin)
2. Hahmoluokkaan liittyy erilaisia aliluokkia, jotka listataan tähän.

### Background-tietokantataulun muokkaus

Tietokanta-palkista valitsemalla Background, voi tarkastella tietokantataulun 'Background' sisältöä, eli generoinnin pohjana olevia vaihtoehtoisia hahmon taustoja, ja edelleen lisätä tietokantaan uusia taustoja tai halutessaan muokata tai poistaa siellä jo entuudestaan olevia.

<img src="https://raw.githubusercontent.com/Fumblessi/ot-harjoitustyo/master/dokumentointi/bgLisays.png" width="600">

ks. Racial-tietokantataulun muokkaus (taustan taitojen antaminen toimii samalla tavalla, mutta tausta ei voi antaa hahmolle erityistaitoa tai vaikuttaa hahmon piirteisiin)

### Feat-tietokantataulun muokkaus

Tietokanta-palkista valitsemalla Feat, voi tarkastella tietokantataulun 'Feat' sisältöä, eli generoinnissa mukana olevia vaihtoehtoisia hahmojen erityistaitoja (joita hahmolla voi olla, muttei välttämättä ole), ja edelleen lisätä tietokantaan uusia erityistaitoja tai halutessaan muokata tai poistaa siellä jo entuudestaan olevia.

<img src="https://raw.githubusercontent.com/Fumblessi/ot-harjoitustyo/master/dokumentointi/featLisays.png" width="600">

ks. Racial-tietokantataulun muokkaus, minkä lisäksi:
1. Erityistaito saattaa antaa hahmolle +1 johonkin piirteeseen. Valitse ruksaamalla ne piirteet, joihin tuo +1 voisi tulla.

### Default-tietokanta

Projektin juuressa on kansio "default-database", johon on rakennettu varmuuskopio alustavasta perustietokannasta.

-----------------------------------------------------------------------------------------------------------------------------

## Usein kysytyt kysymykset

### 1. Miksi käyttäjän tarvitsee pystyä muokkaamaan tietokantojen sisältöä?

Dungeons and Dragons peli päivittyy koko ajan, tulee uusia rotuja, uusia taustoja ja uusia taitoja hahmoille. Hahmogeneraattori kykenee nyt sellaiseen seuraamaan näitä muutoksia, sillä käyttäjä voi lisätä tietokantaan uutta tavaraa sitä mukaa kun sitä tulee. Tietokannan muokkaus mahdollistaa myös omien vaihtoehtojen keksimisen ja lisäämisen generaattoriin, joten sen tavoite on mukautua peliporukan tarpeisiin.

Esimerkiksi 'Lucky'-niminen feat (erityistaito) on yleisesti koettu verrattaen todella hyväksi, joten moni peliporukka saattaisi haluta sen ottaa pois tietokannasta, vaikka se on sinänsä täysin sääntöjen mukainen.

### 2. Generaattorisi puhuu englantia ja suomea sekaisin! Päätä jo kumpi!

Tätä olen paljon pyöritellyt, että kieltämättä olisi mukavaa puhua suoraan vain kokonaan jompaa kumpaa kieltä. Olemme halunneet peliporukassamme siirtyä mahdollisimman paljolti suomen käyttämiseen, mutta silti on selkeämpää viitata tiettyihin asioihin yksiselitteisesti samoilla termeillä kun D&D-pelin säännöissä, joten osa termistöstä ikävä kyllä säilynee englanninkielisenä, vaikka käyttöliittymä pitkälti onkin suomea. Pahoittelut.
