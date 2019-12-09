# Hahmogeneraattori

Hahmogeneraattorin tarkoitus on arpoa satunnainen roolipelihahmo, sekä sille piirteet Strength, Dexterity, Constitution, Intelligence, Wisdon ja Charisma, sen rotu ja hahmoluokka, sekä näiden pohjalta sen osaamat taidot. Generaattorin asetuksista voi rajata arvontaa, sekä luoda uusia luokkia tai rotuja arvonnan pohjana olevaan tietokantaan.

# Releaset

[Viikko5](https://github.com/Fumblessi/ot-harjoitustyo/releases/tag/viikko5)

# Dokumentaatio

[Käyttöohje](https://github.com/Fumblessi/ot-harjoitustyo/blob/master/dokumentointi/kaytto-ohje.md)

[Alustava määrittelydokumentti](https://github.com/Fumblessi/ot-harjoitustyo/blob/master/dokumentointi/maarittelydokumentti.md)

[Arkkitehtuuri](https://github.com/Fumblessi/ot-harjoitustyo/blob/master/dokumentointi/arkkitehtuuri.md)

[Tuntikirjanpito](https://github.com/Fumblessi/ot-harjoitustyo/blob/master/dokumentointi/tyoaikakirjanpito.md)


# Työn alla tällä hetkellä:

* Testien luominen
* Varmistaminen, että ohjelman pystyy ajamaan laitoksen koneella komentoriviltä käsin
* Tietokantatoiminnallisuuden ja -kyselyiden edelleen kirjoittaminen
* Alustava pohja tietokanta olisi hyvä luoda, niin käyttäjä voi halutessaan palata jonkinlaiseen "default"-tilaan, mikäli on tietokannan sisältöön koskenut
* Käyttöliittymän ulkoasu kivemmaksi

# Komentorivi

Suoritus:

"mvn compile exec:java -Dexec.mainClass=hahmogeneraattori.ui.Main"

Testit:

"mvn test"

Testauskattavuus:

"mvn test jacoco:report"

Checkstyle:

"mvn jxr:jxr checkstyle:checkstyle"

JavaDoc*:

"mvn javadoc:javadoc"

*Luokat, joille tähän mennessä JavaDoc on tehty:
- hahmogeneraattori.dao.SQLDatabaseDao
- hahmogeneraattori.dao.FileSettingsDao
- hahmogeneraattori.domain.Settings
- hahmogeneraattori.domain.Generator
- hahmogeneraattori.domain.Stats
- hahmogeneraattori.domain.Proficiency
- hahmogeneraattori.domain.Racial
