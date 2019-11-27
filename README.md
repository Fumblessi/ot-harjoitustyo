# Hahmogeneraattori

Hahmogeneraattorin tarkoitus on arpoa satunnainen roolipelihahmo, sekä sille piirteet Strength, Dexterity, Constitution, Intelligence, Wisdon ja Charisma, sen rotu ja hahmoluokka, sekä näiden pohjalta sen osaamat taidot. Generaattorin asetuksista voi rajata arvontaa, sekä luoda uusia luokkia tai rotuja arvonnan pohjana olevaan tietokantaan.

# Javafx

Testattuani etätyöpöydällä laitoksen cubbli linuxilla, täällä olevalla konfiguraatiolla pitäisi pystyä ajamaan ohjelma laitoksen koneelta. Käyttöliittymän ulkoasu oli jostain syystä hieman eri näköinen kuin omalla koneella ajettaessa (vaikka tässäkin on linux sekä Java 8). Ongelmia voi aiheuttaa lisäämäni spring-sovelluskehyksen dependencyt, mitä en kerinnyt vielä laitoksen koneella testata.

# Dokumentaatio

[Käyttöohje](https://github.com/Fumblessi/ot-harjoitustyo/blob/master/dokumentointi/kaytto-ohje.md)

[Alustava määrittelydokumentti](https://github.com/Fumblessi/ot-harjoitustyo/blob/master/dokumentointi/maarittelydokumentti.md)

[Tämänhetkinen pakkauskaavio](https://github.com/Fumblessi/ot-harjoitustyo/blob/master/dokumentointi/arkkitehtuuri.md)

[Tuntikirjanpito](https://github.com/Fumblessi/ot-harjoitustyo/blob/master/dokumentointi/tyoaikakirjanpito.md)

# Työn alla tällä hetkellä:

* Asetukset-välilehden tekstikentät hyväksyvät vain kokonaisluku-muotoisia syötteitä
* Pakkauskaavion laajentaminen koko ohjelman suunnitelmaa vastaavaksi
* Tietokantojen alustaminen ja taulujen rakentaminen, sekä tietokantakyselyiden kirjoittaminen

# Komentorivi

Suoritus:

"mvn compile exec:java -Dexec.mainClass=hahmogeneraattori.ui.Main"

Testit:

"mvn test"

Testauskattavuus:

"mvn test jacoco:report"

Checkstyle:

"mvn jxr:jxr checkstyle:checkstyle"
