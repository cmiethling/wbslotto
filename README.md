## Code-Beiträge von mir

- [business.ZiehungAuswerten.java](https://github.com/cmiethli/wbslotto/blob/3f818fcc3cc653640cb02c00aea4e04f137b384c/src/main/java/de/wbstraining/lotto/business/lottogesellschaft/ZiehungAuswerten.java#L47)
  - **Herzstück des Projektes**: Hier wird eine Ziehung ausgewertet und sowohl die Einnahmen der Lottogesellschaft, als auch die Gewinne der Spieler berechnet und persistiert.
  - in Zusammenarbeit mit Dimitri
    - Dimitris Beitrag: [getJackpotParam(...)](https://github.com/cmiethli/wbslotto/blob/3f818fcc3cc653640cb02c00aea4e04f137b384c/src/main/java/de/wbstraining/lotto/business/lottogesellschaft/ZiehungAuswerten.java#L639)
    - Mein Beitrag: *Rest* >> kleine Highlights: [getUpdatedVersion() und getMapKeyAsGewinnklassenr()](https://github.com/cmiethli/wbslotto/blob/3f818fcc3cc653640cb02c00aea4e04f137b384c/src/main/java/de/wbstraining/lotto/business/lottogesellschaft/ZiehungAuswerten.java#L962)<br> <br>

- [business.KostenErmitteln.java](https://github.com/cmiethli/wbslotto/blob/3f818fcc3cc653640cb02c00aea4e04f137b384c/src/main/java/de/wbstraining/lotto/business/lottospieler/KostenErmitteln.java#L28) (inkl. [dto.KostenDetailedDto.java](https://github.com/cmiethli/wbslotto/blob/3f818fcc3cc653640cb02c00aea4e04f137b384c/src/main/java/de/wbstraining/lotto/dto/KostenDetailedDto.java#L13), [rest.service.KostenDetailedREST.java](https://github.com/cmiethli/wbslotto/blob/3f818fcc3cc653640cb02c00aea4e04f137b384c/src/main/java/de/wbstraining/lotto/web/rest/service/KostenDetailedREST.java#L39))
  - Hier werden die Kosten für einen Lottoschein berechnet und mithilfe der Methode [kostenErmittelnDetailed()](https://github.com/cmiethli/wbslotto/blob/3f818fcc3cc653640cb02c00aea4e04f137b384c/src/main/java/de/wbstraining/lotto/business/lottospieler/KostenErmitteln.java#L125) in einem DTO abgespeichert. Dieses kann dann über die REST-Schnittstelle weiter verwendet werden, z.B. für die Erstellung einer Rechnung im pdf-Format.
  - in Zusammenarbeit mit Martin und Dimitri
    - Dimitris Beiträge: [putEinsaetzeInKstnDtlDtoObj()](https://github.com/cmiethli/wbslotto/blob/3f818fcc3cc653640cb02c00aea4e04f137b384c/src/main/java/de/wbstraining/lotto/business/lottospieler/KostenErmitteln.java#L229), [Zeilen 169-210](https://github.com/cmiethli/wbslotto/blob/3f818fcc3cc653640cb02c00aea4e04f137b384c/src/main/java/de/wbstraining/lotto/business/lottospieler/KostenErmitteln.java#L168)
    - Mein Beitrag: *Rest*
