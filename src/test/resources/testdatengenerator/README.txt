/* PRECONDITION: die tabelle kunden (adresse, bankverbindung) muss bereits bestückt sein.

 * der generator ezeugt die gewünschte anzahl von treffern in den gewinnklassen
 * der einzelnen spiele für alle e c h t e n gewinnklassen.

 * der wert für die pseudogewinnklasse 0 im descriptor testdatengenerierung.xml
 * ist der minimalwert. der tatsächliche wert kann höher sein als der dort angegebene wert.
 * 
 * in der xml-konfiguration muss sichergestellt sein, dass die superzahl
 * und die endziffern von spiel77 und super6 alle verschieden sind.
 *
 * die spiel77- zahl einer ziehung darf nicht mit 0 enden.
 *
 * die super6- zahl einer ziehung darf nicht mit 0 enden.
 *
 * für die generierten daten gilt:
 * 
 * jeder schein gehört in höchstens einem spiel (6aus49, spiel77, super6)
 * in höchstens eine gewinnklasse.
 *
 * alle lottoscheine, die in derselben gewinnklasse in 6 aus 49 sind, haben dieselben tippzahlen.
 * 
 * wenn ein tipp in einer gewinnklasse von 6aus49 liegt, ist es immer der erste.
 * 
 * alle lottoscheine einer ziehung haben dieselbe anzahl von tipps.
 * 
 * alle lottoscheine, die in derselben gewinnklasse im spiel 77 sind, haben dieselbe losnummer.
 *
 * alle lottoscheine, die in derselben gewinnklasse im spiel super 6 sind, haben dieselbe losnummer.
 */
