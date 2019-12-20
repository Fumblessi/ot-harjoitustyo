# Testausdokumentti

Ohjelmaa on testattu sekä JUnitin yksikkötesteillä, että manuaalisesti käyttöliittymän kautta.

## Yksikkö- ja integraatiotestaus

Automatisoiduista testeistä suurin osa keskittyy tietokantatoiminnallisuuden testaamiseen, sillä se on laajin ja työläin osa projektia. Eri tietokantatauluja testaavat [GeneratorDaoTest](https://github.com/Fumblessi/ot-harjoitustyo/blob/master/Hahmogeneraattori/src/test/java/DaoTest/GeneratorDaoTest.java), [RaceDaoTest](https://github.com/Fumblessi/ot-harjoitustyo/blob/master/Hahmogeneraattori/src/test/java/DaoTest/RaceDaoTest.java), [RacialDaoTest](https://github.com/Fumblessi/ot-harjoitustyo/blob/master/Hahmogeneraattori/src/test/java/DaoTest/RacialDaoTest.java), [ProficiencyDaoTest](https://github.com/Fumblessi/ot-harjoitustyo/blob/master/Hahmogeneraattori/src/test/java/DaoTest/ProficiencyDaoTest.java), [ClassDaoTest](https://github.com/Fumblessi/ot-harjoitustyo/blob/master/Hahmogeneraattori/src/test/java/DaoTest/ClassDaoTest.java), [BackgroundDaoTest](https://github.com/Fumblessi/ot-harjoitustyo/blob/master/Hahmogeneraattori/src/test/java/DaoTest/BackgroundDaoTest.java) ja [FeatDaoTest](https://github.com/Fumblessi/ot-harjoitustyo/blob/master/Hahmogeneraattori/src/test/java/DaoTest/FeatDaoTest.java). Lisäksi tiedostoon tallentamista testaa [SettingsDaoTest](https://github.com/Fumblessi/ot-harjoitustyo/blob/master/Hahmogeneraattori/src/test/java/DaoTest/SettingsDaoTest.java).

Sekä tietokanta- että tiedostotallentamisen testit käyttävät feikkitiedostoa testatessaan toiminnallisuutta. 

## Testauskattavuus
