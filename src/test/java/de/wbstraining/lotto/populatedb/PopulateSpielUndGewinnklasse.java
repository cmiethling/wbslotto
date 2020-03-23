/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wbstraining.lotto.populatedb;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import de.wbstraining.lotto.persistence.dao.GewinnklasseFacadeLocal;
import de.wbstraining.lotto.persistence.dao.SpielFacadeLocal;
import de.wbstraining.lotto.persistence.model.Gewinnklasse;
import de.wbstraining.lotto.persistence.model.Spiel;
/**
 *
 * @author gz1
 */
@Stateless
public class PopulateSpielUndGewinnklasse implements PopulateSpielUndGewinnklasseLocal {
    @EJB
    private SpielFacadeLocal spielFacade;
    @EJB
    private GewinnklasseFacadeLocal gewinnklasseFacade;
    
    private void populateGewinnklassen(Date date, Date gueltigBis, Spiel spiel, String[] gewinnKlassen) {

        Gewinnklasse gewinnklasse;
        String[] items;

        for (String gkl : gewinnKlassen) {

            items = gkl.split(",");

            gewinnklasse = new Gewinnklasse();

            gewinnklasse.setSpielid(spiel);
            // das müssen wir noch automatisch machen
            gewinnklasse.setCreated(date);
             // das müssen wir noch automatisch machen
            gewinnklasse.setLastmodified(date);
            gewinnklasse.setBeschreibung(items[0]);
            gewinnklasse.setGewinnklassenr(Integer.parseInt(items[1]));
            gewinnklasse.setBezeichnunglatein(items[2]);
            gewinnklasse.setBetrag(BigInteger.valueOf(Long.parseLong(items[3])));
            gewinnklasse.setIsabsolut(Boolean.valueOf(items[4]));
            gewinnklasse.setGueltigab(date);
            gewinnklasse.setGueltigbis(gueltigBis);

            gewinnklasseFacade.create(gewinnklasse);
        }
    }

    @Override
    public void populateSpielUndGewinnklasse() {
        // beschreibung
        // gewinnklasseNr
        // bezeichnungLatein
        // betrag
        // isAbsolut
        String[] gewinnklassen6Aus49 = {
            "6 Richtige + SZ,1,I,1280,false",
            "6 Richtige,2,II,1000,false",
            "5 Richtige + SZ,3,III,500,false",
            "5 Richtige,4,IV,1500,false",
            "4 Richtige + SZ,5,V,500,false",
            "4 Richtige,6,VI,1000,false",
            "3 Richtige + SZ,7,VII,1000,false",
            "3 Richtige,8,VIII,4500,false",
            "2 Richtige + SZ,9,IX,500,true"
        };
         // beschreibung
         // gewinnklasseNr
         // bezeichnungLatein
         // betrag
         // isAbsolut
        String[] gewinnklassenSpiel77 = {
            "richtige Gewinnzahl,1,I,0,true",
            "6 richtige Endziffern,2,II,7777700,true",
            "5 richtige Endziffern,3,III,777700,true",
            "4 richtige Endziffern,4,IV,77700,true",
            "3 richtige Endziffern,5,V,7700,true",
            "2 richtige Endziffern,6,VI,1700,true",
            "1 richtige Endziffer,7,VII,500,true"
        };
        // beschreibung
        // gewinnklasseNr
        // bezeichnungLatein
        // betrag
        // isAbsolut
        String[] gewinnklassenSuper6 = {
            "6 richtige Endziffern,1,I,10000000,true",
            "5 richtige Endziffern,2,II,666600,true",
            "4 richtige Endziffern,3,III,66600,true",
            "3 richtige Endziffern,4,IV,6600,true",
            "2 richtige Endziffern,5,V,600,true",
            "1 richtige Endziffer,6,VI,250,true"
        };

        Date date = new Date();
        Date gueltigBis = new GregorianCalendar(2020, Calendar.DECEMBER, 31).getTime();
        
        Spiel sechsAus49 = new Spiel();
        Spiel super6 = new Spiel();
        Spiel spiel77 = new Spiel();
        
        sechsAus49.setName("6 aus 49");
        sechsAus49.setBeschreibung("Das klassische Lotto. 6 aus 49 auswaehlen");
        sechsAus49.setPfadanleitung("anleitungen/sechsAus49");
        sechsAus49.setCreated(date);
        sechsAus49.setLastmodified(date);
       
        super6.setName("Super 6");
        super6.setBeschreibung("beschreibung super6...");
        super6.setPfadanleitung("anleitungen/super6");
        super6.setCreated(date);
        super6.setLastmodified(date);
        
        spiel77.setName("Spiel 77");
        spiel77.setBeschreibung("beschreibung spiel77...");
        spiel77.setPfadanleitung("anleitungen/spiel77");
        spiel77.setCreated(date);
        spiel77.setLastmodified(date);
        
        spielFacade.create(sechsAus49);
        spielFacade.create(super6);
        spielFacade.create(spiel77);
       
        populateGewinnklassen(date, gueltigBis, sechsAus49, gewinnklassen6Aus49);
        populateGewinnklassen(date, gueltigBis, spiel77, gewinnklassenSpiel77);
        populateGewinnklassen(date, gueltigBis, super6, gewinnklassenSuper6);
    }
}
