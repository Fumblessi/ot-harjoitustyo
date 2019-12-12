# Pakkauskaavio

Päivitetty pakkauskaavio näyttää, mihin muotoon haluaisin ohjelmani refaktoroida. Ainoa asetustiedostoa käyttävä luokka olisi Settings, ja ainoa tietokantaa (kaavio alla) käyttävä luokka olisi Generator, jonka kautta generoitaisiin sattumanvaraisesti tarvittavia muita olioita käyttäen kaaviossa näkyviä olioluokkia.

<img src="https://raw.githubusercontent.com/Fumblessi/ot-harjoitustyo/master/dokumentointi/pakkauskaavio.png" width="600">

# Tietokantakaavio:

Valmiin ohjelman tietokantakaavio. Havaitsin, että aiempi tietokanta-arkkitehtuurini oli naiivisti aivan liian yksinkertainen kattamaan D&D-roolipelin tarjoama varianssi, ja muistamaan kaikki oleellinen tieto. Nyt kun huomaan, kuinka monimutkainen prosessi oikeasti hahmon proficiencyjen arpominen on, saa nähdä kerkeänkö toteuttamaan tätä kaikkea toiminnallisuutta kurssin puitteissa.

<img src="https://raw.githubusercontent.com/Fumblessi/ot-harjoitustyo/master/dokumentointi/tietokantakaavio.png" width="600">

# Sekvenssikaavio(t):

1. Sekvenssikaavio, kun käyttöliittymän kautta lisätään uusi taito taitoja sisältävään "Skill"-tietokantaan. Roolipelitermistöön liittyvistä syistä käytän ohjelmassani kuitenkin termiä Proficiency (tietokantataulun nimi on sekvenssikaavion luonnin jälkeen muutettukin).

<img src="https://raw.githubusercontent.com/Fumblessi/ot-harjoitustyo/master/dokumentointi/ProficiencynLisaaminenSekvenssi.png" width="600">
