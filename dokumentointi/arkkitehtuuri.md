# Pakkauskaavio

Päivitetty pakkauskaavio näyttää, mihin muotoon haluaisin ohjelmani refaktoroida. Ainoa asetustiedostoa käyttävä luokka olisi Settings, ja ainoa tietokantaa (kaavio alla) käyttävä luokka olisi Generator, jonka kautta generoitaisiin sattumanvaraisesti tarvittavia muita olioita käyttäen kaaviossa näkyviä olioluokkia. Vanhan pakkauskaavion näkee [täältä](https://github.com/Fumblessi/ot-harjoitustyo/blob/master/dokumentointi/pakkauskaavio_vanha.png).

<img src="https://raw.githubusercontent.com/Fumblessi/ot-harjoitustyo/master/dokumentointi/pakkauskaavio.png" width="600">

# Tietokantakaavio:

Siinä missä pakkauskaavio vielä tässä vaiheessa vastaa tämänhetkistä tilannetta, tietokantakaavio vastaa suunnitelmaani koko
ohjelman tietokannan toteutuksesta. Feat- ja FeatProficiency-tietokantataulut samoin kuin Featin arpomistoiminnallisuus tosin toteutunee
vasta kurssin jälkeen.

<img src="https://raw.githubusercontent.com/Fumblessi/ot-harjoitustyo/master/dokumentointi/tietokantakaavio.png" width="600">

# Sekvenssikaavio(t):

1. Sekvenssikaavio, kun käyttöliittymän kautta lisätään uusi taito taitoja sisältävään "Skill"-tietokantaan. Roolipelitermistöön liittyvistä syistä käytän ohjelmassani kuitenkin termiä Proficiency (tietokantataulun nimi on sekvenssikaavion luonnin jälkeen muutettukin).

<img src="https://raw.githubusercontent.com/Fumblessi/ot-harjoitustyo/master/dokumentointi/ProficiencynLisaaminenSekvenssi.png" width="600">
