package de.wbstraining.lotto.web.rest.service;

import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import de.wbstraining.lotto.business.lottospieler.KostenErmittelnLocal;
import de.wbstraining.lotto.dto.KostenDetailedDto;
import de.wbstraining.lotto.dto.KostenDto;

@Stateless
@Path("/kostendetaileddto")
public class KostenDetailedREST {

        @EJB
        private KostenErmittelnLocal kostenErmitteln;

//curl -X GET -H "accept: application/xml" http://localhost:8080/wbslotto/lottoapi/kostendetaileddto > kosten.xml
//curl -X GET -H "accept: application/json" http://localhost:8080/wbslotto/lottoapi/kostendetaileddto > kosten.json
        @GET
        @Produces({ "application/xml", "application/json" })
        public KostenDto kosten() {
                KostenDto dto = new KostenDto();
                dto.setAbgabeDatum(new Date());
                dto.setAnzahlTipps(10);
                dto.setLaufzeit(5);
                dto.setMittwoch(Boolean.TRUE);
                dto.setSamstag(Boolean.TRUE);
                dto.setSpiel77(Boolean.TRUE);
                dto.setSuper6(Boolean.FALSE);
                return dto;
        }

//curl -X GET -H "Content-type: application/xml" -d @kosten.xml http://localhost:8080/corejpa/lottoapi/kostendetaileddto > kostendetailed.xml
//curl -X GET -H "Content-type: application/json" -d @kosten.json http://localhost:8080/corejpa/lottoapi/kostendetaileddto > kostendetailed.json
        @GET
        @Consumes({ "application/xml", "application/json" }) // input: KostenDto
        @Produces({ "application/xml", "application/json" }) // output:
                                                                                                                                                                                                                                // KostenDetailedDto.xml
        public KostenDetailedDto detailedKosten(KostenDto kosten) {
                return kostenErmitteln.kostenErmittelnDetailed(kosten);
        }

////curl -X GET -H "Content-type: application/xml" -d @kostendetaileddto.xml http://localhost:8080/corejpa/lottoapi/kostendetaileddto
////curl -X GET -H "Content-type: application/json" -d @kostendetaileddto.json http://localhost:8080/corejpa/lottoapi/kostendetaileddto
//        @GET
//        @Consumes(MediaType.APPLICATION_XML) // input: dto
//        @Produces(MediaType.APPLICATION_XML) // output:
//        public KostenDetailedDto detailedKosten2(KostenDto kosten) {
//                return kostenErmitteln.kostenErmittelnDetailed(kosten);
//        }
//
//        @GET
//        @Consumes(MediaType.APPLICATION_JSON) // input: dto
//        @Produces(MediaType.APPLICATION_JSON) // output:
//        // @Produces("text/plain")
//        public Response detailedKostenJson(KostenDto kosten) {
//                return Response
//                        .ok(kostenErmitteln.kostenErmittelnDetailed(kosten),
//                                MediaType.APPLICATION_JSON)
//                        .build();
//        }
//
//        @POST
//        @Consumes(MediaType.APPLICATION_JSON) // input: dto
//        @Produces(MediaType.APPLICATION_JSON) // output:
//                                                                                                                                                                // KostenDetailedDto.xml
//        public KostenDetailedDto detailedKostenJsonPOST(KostenDto kosten) {
//                return detailedKosten(kosten);
//        }
//
//        @POST
//        @Consumes(MediaType.APPLICATION_XML) // input: dto
//        @Produces(MediaType.APPLICATION_XML) // output:
//                                                                                                                                                                // KostenDetailedDto.xml
//        public KostenDetailedDto detailedKostenXmlPOST(KostenDto kosten) {
//                return detailedKosten(kosten);
//        }
}
