# Hahmogeneraattori

Hahmogeneraattorin tarkoitus on arpoa satunnainen roolipelihahmo, sekä sille piirteet Strength, Dexterity, Constitution, Intelligence, Wisdom ja Charisma, sen rotu ja hahmoluokka, sekä näiden pohjalta sen osaamat taidot. Generaattorin asetuksista voi rajata arvontaa, sekä luoda uusia luokkia tai rotuja arvonnan pohjana olevaan tietokantaan.

# Releaset

[Loppupalautus](https://github.com/Fumblessi/ot-harjoitustyo/releases/tag/Loppupalautus)

[Viikko6](https://github.com/Fumblessi/ot-harjoitustyo/releases/tag/viikko6)

[Viikko5](https://github.com/Fumblessi/ot-harjoitustyo/releases/tag/viikko5)

# Dokumentaatio

[Käyttöohje](https://github.com/Fumblessi/ot-harjoitustyo/blob/master/dokumentointi/kaytto-ohje.md)

[Määrittelydokumentti](https://github.com/Fumblessi/ot-harjoitustyo/blob/master/dokumentointi/maarittelydokumentti.md)

[Arkkitehtuuri](https://github.com/Fumblessi/ot-harjoitustyo/blob/master/dokumentointi/arkkitehtuuri.md)

[Testausdokumentti](https://github.com/Fumblessi/ot-harjoitustyo/blob/master/dokumentointi/testausdokumentti.md)

[Tuntikirjanpito](https://github.com/Fumblessi/ot-harjoitustyo/blob/master/dokumentointi/tyoaikakirjanpito.md)


# Työn alla tällä hetkellä:

* 

# Komentorivi

### Suoritus:

"mvn compile exec:java -Dexec.mainClass=hahmogeneraattori.ui.Main"

tai .jar-tiedoston suoritus:

"java -jar Hahmogeneraattori.jar"

### Jar-tiedoston pakkaus:

"mvn package"

### Testit:

"mvn test"

### Testauskattavuus:

"mvn test jacoco:report"

### Checkstyle:

"mvn jxr:jxr checkstyle:checkstyle"

### JavaDoc:

"mvn javadoc:javadoc"
