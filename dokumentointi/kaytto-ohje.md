# Käyttöohje

-----------------------------------------------------------------------------------------------------------------------------

## Dungeons and Dragons ja termistö

Dungeons and Dragons on suosittu roolipeli, johon generaattorin on tarkoitus tehdä satunnaisia hahmoja. Normaalisti pelatessa jokaisella pelaajallaan on oma hahmo, jolle he ovat valinneet hahmon rodun (Orc, Dwarf tai Human yms.), hahmon luokan (esim. Fighter tai Wizard yms.), ja hahmon taustan (Noble, Soldier tai Sage yms.). Tämän jälkeen pelaajat saavat hahmollemaan piirteet (usein jakamalla tietyn pistemäärän eri piirteiden kesken tai heittämällä nopilla) Strength (voima), Dexterity (ketteryys), Constitution (kestävyys), Intelligence (älykkyys), Wisdom (viisaus) ja Charisma (karisma).

Koska hahmonluontiin kuluu mittavasti aikaa, peliporukassamme on aika ajoin esiintynyt tarve pystyä nopeasti laittamaan peli kasaan ja pelaamaan yhden päivän/illan mittainen rento sessio, johon hahmogeneraattorille on ollut kysyntää. Tämä on motivaatio projektityöni taustalla, ja luultavasti jatkan projektin kehittämistä Ohjelmistotekniikka-kurssin jälkeenkin.

### Termipankki

Erilaisia D&D-peliin liittyviä termejä, joita ohjelmassani ja dokumenteissani käytän, olen listannut ja suomentanut [tänne](https://github.com/Fumblessi/ot-harjoitustyo/blob/master/dokumentointi/termisto.md).

### Sääntömuunnokset verrattuna perinteiseen Dungeons and Dragons -roolipeliin

Peliporukassamme olemme muokanneet joitakin sääntöjä, ja hahmogeneraattorin olen tehnyt seuraamaan näitä sääntömuutoksia. Normaalisti esimerkiksi hahmon rotu määrittää sen rotuominaisuuden (Racial), mutta olemme tässä mielessä roolipeliporukassamme tasa-arvoistaneet eri rotuja, sillä yksilöinä hahmot kuitenkin voivat venyä vaikka mihin, ja pelaajat saavat itse valita rotuominaisuutensa kaikista eri rotujen tarjoamista vaihtoehdoista, riippumatta hahmon rodusta. Siksi generaattorikin arpoo hahmolle rotuominaisuudet kaikista vaihtoehdoista tietokantataulussa 'Racial'.

Kielitaito pelissämme on hieman monimutkaistettu perinteisestä D&D-pelistä. Siinä tavallisesti hahmo joko osaa tai ei osaa kieltä, mutta meillä osaaminen on jaettu kolmeen eri osaamistasoon:
* I: Osaa jotain alkeita, saattaa tunnistaa tiettyjä sanoja. Kommunikointi työlästä ja kömpelöä.
* II: Osaa puhua ja lukea kieltä kohtuullisen hyvin, kommunikointi onnistuu, mutta näkyy läpi, ettei ole natiivi puhuja.
* III: Puhuu kieltä moitteettomasti ja sujuvasti, kuin se olisi äidinkieli.

Jos jollakulla muulla tulisi generaattorille kysyntää, saattaisin tehdä näistä sääntömuutoksista valinnaisia, ja asetusten kautta säädettäviä.

-----------------------------------------------------------------------------------------------------------------------------

## Generointi

<img src="https://raw.githubusercontent.com/Fumblessi/ot-harjoitustyo/master/dokumentointi/generointi.png" width="450">

1. Painike "Generoi" luo uuden satunnaisen hahmon, ja painikkeesta "Asetukset" voi siirtyä määrittämään generoinnin parametreja tai tietokantojen sisältöä
2. Generoidun hahmon piirteet tulevan näkymään tähän
3. Generoidun hahmon ominaisuudet, eli rotu, aliluokka & luokka, tausta, "order/morality" (eli arvot siitä, kuinka järjestelmällinen/lakia noudatta ja hyvän-/pahantahtoinen hahmo on) sekä arvotut rotuominaisuudet tulevat näkymään tähän (viemällä hiiren "backgroundin" kohdalle, voi lukea taustan antaman lisäominaisuuden)
4. Hahmolle kohdan 3 pohjalta generoidut taidot tulevat näkymään tähän
5. Painikkeesta "kopioi leikepöydälle" voi kopioida generoinnin sisällön, jos haluaa ottaa hahmon tiedot esimerkiksi talteen peliä varten

-----------------------------------------------------------------------------------------------------------------------------

## Asetukset

Valitsemalla alkunäkymästä painikkeen Asetukset, pääsee asetusnäkymään, josta voi säätää generoinnin parametreja.

<img src="https://raw.githubusercontent.com/Fumblessi/ot-harjoitustyo/master/dokumentointi/asetukset1.png" width="450">
<img src="https://raw.githubusercontent.com/Fumblessi/ot-harjoitustyo/master/dokumentointi/asetukset2.png" width="450">

1. "Tallenna ja palaa"-painikkeella palataan takaisin generointiruutuun, ja tallennetaan asetusten muutokset. "Palauta alkuperäiset"-painike muuttaa asetukset oletusasetuksiksi, ja "Tietokanta"-palkista pääsee tarkastelemaan tietokannan sisältöä.
2. Tästä voi säätää hahmon piirteiden arpomisen parametreja. Piirteiden summa on piirteiden yhteenlaskettu määrä. Jos haluaa, että arvotut hahmot saavat keskenään saman verran piirteitä, varianssin voi laittaa nollaan, mutta jos haluaa satunnaisuutta, voi asettaa piirteiden summalle satunnaista vaihtelua. Piirteiden minimi- ja maksimiarvo kuvaa sitä, kuinka paljon vähintään tai korkeintaan jokaiseen yksittäiseen piirteeseen tulee pisteitä.
3. Halutessaan voi laittaa generaattorin sijoittamaan normaalisti rodun puolesta saadut +2 ja +1 johonkin piirteeseen satunnaiseti. Voit myös valita arvottujen rotuominaisuuksien määrän.
4. Voit vaikuttaa siihen, montako skilliä tai muuta taitoa background hahmolle antaa. Näistä muista proficiencyistä voit edelleen valita painotuksen, että millä todennäköisyydellä arvottu muu taito on jonkin esineen käyttöön liittyvä taito vai osattu kieli. Edelleen arvotuista esine-taidoista voit valita todennäköisyyden, millä ne ovat työkaluja tai muita välineitä, vai soittimia tai pelejä.
5. Voit valita, kuinka monta kieltä hahmolle pohjimmiltaan arvotaan (tähän päälle voi tulla kielitaitoja taustasta tai muista generoiduista ominaisuuksista).
6. Kielien arpomisen todennäköisyyksiä voi itse säätää. Meidän oletusasetuksemme näkyvät ruudussa, eli yleisen tason kielet ovat yleisimpiä, ja niitä myös todennäköisemmin satunnainen hahmo osaa puhua paremmin.
7. Halutessaan voi pakottaa hahmolle äidinkielen (mikä yleensä on järkevää), eli ensimmäinen kieli joka hahmolle arpoutuisikin on automaattisesti tason II-III kieli (todennäköisyyden tälle voi myös itse määrittää), ja voi valita muutamasta vaihtoehdosta, mistä tämä kieli valitaan (Argan on meidän pelin puhutuin kieli, joten se on tässä vaihtoehtona, joskin jossain vaiheessa muutan tämän asetuksen niin, että haluamansa kielen voi määrittää itse).

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

<img src="https://raw.githubusercontent.com/Fumblessi/ot-harjoitustyo/master/dokumentointi/bgLisays.png" width="300">

1. Uuden taustan nimi.
2. Taustan antama ominaisuus ja sen kuvaus.
3. Painike "Lisää" lisää uuden taustan tietokantaan, painike "Takaisin" palaa Background-tietokantanäkymään.

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
