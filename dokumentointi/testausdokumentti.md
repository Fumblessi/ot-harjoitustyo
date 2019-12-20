# Testausdokumentti

Ohjelmaa on testattu sekä JUnitin yksikkötesteillä, että manuaalisesti käyttöliittymän kautta.

## Yksikkö- ja integraatiotestaus

Automatisoiduista testeistä suurin osa keskittyy tietokantatoiminnallisuuden testaamiseen, sillä se on laajin ja työläin osa projektia. Eri tietokantatauluja testaavat [GeneratorDaoTest](https://github.com/Fumblessi/ot-harjoitustyo/blob/master/Hahmogeneraattori/src/test/java/DaoTest/GeneratorDaoTest.java), [RaceDaoTest](https://github.com/Fumblessi/ot-harjoitustyo/blob/master/Hahmogeneraattori/src/test/java/DaoTest/RaceDaoTest.java), [RacialDaoTest](https://github.com/Fumblessi/ot-harjoitustyo/blob/master/Hahmogeneraattori/src/test/java/DaoTest/RacialDaoTest.java), [ProficiencyDaoTest](https://github.com/Fumblessi/ot-harjoitustyo/blob/master/Hahmogeneraattori/src/test/java/DaoTest/ProficiencyDaoTest.java), [ClassDaoTest](https://github.com/Fumblessi/ot-harjoitustyo/blob/master/Hahmogeneraattori/src/test/java/DaoTest/ClassDaoTest.java), [BackgroundDaoTest](https://github.com/Fumblessi/ot-harjoitustyo/blob/master/Hahmogeneraattori/src/test/java/DaoTest/BackgroundDaoTest.java) ja [FeatDaoTest](https://github.com/Fumblessi/ot-harjoitustyo/blob/master/Hahmogeneraattori/src/test/java/DaoTest/FeatDaoTest.java). Lisäksi tiedostoon tallentamista testaa [SettingsDaoTest](https://github.com/Fumblessi/ot-harjoitustyo/blob/master/Hahmogeneraattori/src/test/java/DaoTest/SettingsDaoTest.java).

Sekä tietokanta- että tiedostotallentamisen testit käyttävät feikkitiedostoa testatessaan toiminnallisuutta. 

## Testauskattavuus

Käyttöliittymäkerrosta lukuunottamatta testauksen rivikattavuus on 80% ja haaraumakattavuus 71%. Eniten kattavuus tippuu GeneratorService luokan metodien testaamattomuudesta, jotka muuttavat generaattorin generoimia olioita String-muotoisiksi, jotta ne voitaisiin näyttää käyttöliittymässä. Tämä kuitenkin tuli perinpohjaisesti testattua manuaalisesti.

<img src="https://raw.githubusercontent.com/Fumblessi/ot-harjoitustyo/master/dokumentointi/testauskattavuus.png" width="600">

## Järjestelmätestaus

Sovellusta on testattu sekä Linux- että Windows-ympäristössä niin, että sovelluksen kanssa samassa kansiossa on _config.properties_-, _settings.txt_- ja _generatordb.mv.db_-tiedostot. Testaamisessa on oletettu, ettei käyttäjä mene sotkemaan näiden tiedostojen sisältöä. 

## Toiminnallisuus

[Määrittelydokumentin](https://github.com/Fumblessi/ot-harjoitustyo/blob/master/dokumentointi/maarittelydokumentti.md) toiminnallisuus on toteutettu, ja käyttöliittymän sisältä asetuksiin ei voi laittaa virheitä aiheuttavia syötteitä.

## Sovellukseen jääneet laatuongelmat

* Testausta voisi vieläkin kattavuudeltaan laajentaa
* Randomizer-luokka jäi pahasti kaipaamaan refaktorointia, ihan koodin ulkomuotostandardien puolesta, mutta myös selkeyttääkseen sen toimintaa
