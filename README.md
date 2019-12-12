# Hahmogeneraattori

Hahmogeneraattorin tarkoitus on arpoa satunnainen roolipelihahmo, sekä sille piirteet Strength, Dexterity, Constitution, Intelligence, Wisdom ja Charisma, sen rotu ja hahmoluokka, sekä näiden pohjalta sen osaamat taidot. Generaattorin asetuksista voi rajata arvontaa, sekä luoda uusia luokkia tai rotuja arvonnan pohjana olevaan tietokantaan.

# Releaset

[Viikko5](https://github.com/Fumblessi/ot-harjoitustyo/releases/tag/viikko5)

[Viikko6](https://github.com/Fumblessi/ot-harjoitustyo/releases/tag/viikko6)

# Dokumentaatio

[Käyttöohje](https://github.com/Fumblessi/ot-harjoitustyo/blob/master/dokumentointi/kaytto-ohje.md)

[Alustava määrittelydokumentti](https://github.com/Fumblessi/ot-harjoitustyo/blob/master/dokumentointi/maarittelydokumentti.md)

[Arkkitehtuuri](https://github.com/Fumblessi/ot-harjoitustyo/blob/master/dokumentointi/arkkitehtuuri.md)

[Tuntikirjanpito](https://github.com/Fumblessi/ot-harjoitustyo/blob/master/dokumentointi/tyoaikakirjanpito.md)


# Työn alla tällä hetkellä:

* Tietokantatoiminnallisuuden rakentaminen uudestaan
* Testien korjaaminen uuden ohjelmarakenteen kanssa toimivaksi
* Generointi-toiminnallisuuden rakentaminen
* Alustava pohja tietokanta olisi hyvä luoda, niin käyttäjä voi halutessaan palata jonkinlaiseen "default"-tilaan, mikäli on tietokannan sisältöön koskenut
* Käyttöliittymän ulkoasu kivemmaksi

# Komentorivi

### Suoritus:

"mvn compile exec:java -Dexec.mainClass=hahmogeneraattori.ui.Main"

### Testit:

"mvn test"

### Testauskattavuus:

"mvn test jacoco:report"

### Checkstyle:

"mvn jxr:jxr checkstyle:checkstyle"

### JavaDoc*:

"mvn javadoc:javadoc"

*Luokat, joille tähän mennessä JavaDoc on tehty:
- hahmogeneraattori.dao.SQLDatabaseDao
- hahmogeneraattori.dao.SQLProficiencyDatabaseDao
- hahmogeneraattori.dao.SQLRacialDatabaseDao
- hahmogeneraattori.dao.FileSettingsDao
- hahmogeneraattori.domain.Settings
- hahmogeneraattori.domain.Generator
- hahmogeneraattori.domain.Stats
- hahmogeneraattori.domain.Proficiency
- hahmogeneraattori.domain.Racial
