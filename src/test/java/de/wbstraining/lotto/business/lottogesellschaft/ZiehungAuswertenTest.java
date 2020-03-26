package de.wbstraining.lotto.business.lottogesellschaft;

/*
@RunWith(Arquillian.class)
public class ZiehungAuswertenTest {

	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap.create(WebArchive.class, "test.war")
				.addClasses(AdresseFacade.class, Adresse.class, AdresseFacadeLocal.class, AbstractFacade.class,
						Kunde.class, Bankverbindung.class, Lottoscheinziehung.class, Ziehung.class, Jackpot.class,
						Gewinnklasse.class, Gewinnklasseziehungquote.class, Spiel.class, Lottoscheinziehung6aus49.class,
						Lottoschein.class, PopulateDBCacheLocal.class, PopulateDBCache.class, KundeFacade.class,
						KundeFacadeLocal.class, ZiehungFacade.class, ZiehungFacadeLocal.class, LottoUtil.class,
						LottoDatumUtil.class, ByteLongConverter.class, Gebuehr.class, GebuehrenCache.class,
						GebuehrenCacheLocal.class, GebuehrFacade.class, GebuehrFacadeLocal.class,
						KostenErmittelnLocal.class, KostenErmitteln.class,
						Users.class, Groups.class, UsersFacade.class, UsersFacadeLocal.class,
						ZiehungAuswertenLocal.class, ZiehungAuswerten.class, LottoscheinFacadeLocal.class,
						LottoscheinFacade.class, GewinnklasseFacadeLocal.class, GewinnklasseFacade.class,
						SpielFacadeLocal.class, SpielFacade.class, LottoscheinziehungFacadeLocal.class,
						LottoscheinziehungFacade.class, Lottoscheinziehung6aus49FacadeLocal.class,
						Lottoscheinziehung6aus49Facade.class, GewinnklasseziehungquoteFacadeLocal.class,
						GewinnklasseziehungquoteFacade.class, JackpotFacadeLocal.class, JackpotFacade.class)
				.addAsResource("META-INF/persistence.xml").addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");

	}

	@EJB
	private ZiehungAuswertenLocal ziehungAuswerten;

	@EJB
	private ZiehungFacadeLocal ziehungFacade;

	@EJB
	private LottoscheinziehungFacadeLocal lottoscheinziehungFacade;

	@EJB
	private JackpotFacadeLocal jackpotFacade;

	@EJB
	private Lottoscheinziehung6aus49FacadeLocal lottoscheinziehung6aus49Facade;

	@EJB
	private GewinnklasseziehungquoteFacadeLocal gewinnklasseziehungquoteFacade;

	@Ignore
	@Test
	public void lottoScheineVerarbeiten_Test() {

		Ziehung ziehung1 = ziehungFacade.find(42L);
		assertTrue(ziehung1.getZiehungid() == 42L);
		ziehungAuswerten.ziehungAuswerten(ziehung1);

		for (long l = 43L; l < 55; l++) {
			Ziehung ziehung2 = ziehungFacade.find(l);
			assertTrue(ziehung2.getZiehungid() == l);
			ziehungAuswerten.ziehungAuswerten(ziehung2);
		}

		assertTrue(ziehung1.getEinsatzlotto().compareTo(BigInteger.ZERO) > 0);
		assertTrue(ziehung1.getEinsatzspiel77().compareTo(BigInteger.ZERO) > 0);
		assertTrue(ziehung1.getEinsatzsuper6().compareTo(BigInteger.ZERO) > 0);

		long spiel77_LottoscheinZiehung = lottoscheinziehungFacade.findAll().stream().filter(l -> {
			Gewinnklasse gkl = l.getGewinnklasseidspiel77();
			if (gkl != null)
				return gkl.getGewinnklasseid() > 9 && gkl.getGewinnklasseid() < 17;
			return false;
		}).count();

		long super6_LottoscheinZiehung = lottoscheinziehungFacade.findAll().stream().filter(l -> {
			Gewinnklasse gkl = l.getGewinnklasseidsuper6();
			if (gkl != null)
				return gkl.getGewinnklasseid() > 16 && gkl.getGewinnklasseid() < 23;
			return false;
		}).count();

		long lottoscheinZiehung6aus49 = lottoscheinziehung6aus49Facade.findAll().stream().count();

		assertTrue(spiel77_LottoscheinZiehung > 0 && super6_LottoscheinZiehung > 0 && lottoscheinZiehung6aus49 > 0);

		List<Jackpot> lJackpot = jackpotFacade.findAll();
		// ****1****************** GKL1_6aus49==0 && GKL1_Spiel77==0 && Ziehungid==42 &&
		// Ziehungid1==43 *************
		// && GKL1_6aus49==1 && GKL1_Spiel77==1 && Ziehungid==44

//		Jackpot jackpot1= lJackpot.stream().filter(l->l.getZiehungid().getZiehungid()==42L && l.getGewinnklasseid().getGewinnklasseid()==1L ).findFirst().get();
//		Jackpot jackpot2= lJackpot.stream().filter(l->l.getZiehungid().getZiehungid()==43L && l.getGewinnklasseid().getGewinnklasseid()==1L ).findFirst().get();
//		
//		assertTrue(jackpot1.getBetrag()+jackpot2.getBetrag()==jackpot2.getBetragkumuliert());
//		
//		 jackpot1= lJackpot.stream().filter(l->l.getZiehungid().getZiehungid()==42L && l.getGewinnklasseid().getGewinnklasseid()==10L ).findFirst().get();
//		 jackpot2= lJackpot.stream().filter(l->l.getZiehungid().getZiehungid()==43L && l.getGewinnklasseid().getGewinnklasseid()==10L ).findFirst().get();
//		
//		assertTrue(jackpot1.getBetrag()+jackpot2.getBetrag()==jackpot2.getBetragkumuliert());
//		
//		//kein jackpot mehr in Ziehungid==44
//		 jackpot1= lJackpot.stream().filter(l->l.getZiehungid().getZiehungid()==44L && l.getGewinnklasseid().getGewinnklasseid()==1L ).findFirst().get();
//		 jackpot2= lJackpot.stream().filter(l->l.getZiehungid().getZiehungid()==44L && l.getGewinnklasseid().getGewinnklasseid()==10L ).findFirst().get();
//
//		assertTrue(jackpot1.getBetrag()==0 && jackpot2.getBetrag()==0 );

		// ****1****

		// ****2****************** GKL1_6aus49==1 && GKL2_6aus49== 0 && Ziehungid1==43
		// **************
//		Jackpot jackpot1= lJackpot.stream().filter(l->l.getZiehungid().getZiehungid()==43L && l.getGewinnklasseid().getGewinnklasseid()==2L ).findFirst().get();
//		assertTrue(jackpot1.getBetrag()==0);         //kein jackpot
		// ****2****

		// ****3****************** GKL1_6aus49==0 && GKL2_6aus49== 0 && Ziehungid1==43
		// **************
//		Jackpot jackpot1= lJackpot.stream().filter(l->l.getZiehungid().getZiehungid()==43L && l.getGewinnklasseid().getGewinnklasseid()==1L ).findFirst().get();
//		Jackpot jackpot2= lJackpot.stream().filter(l->l.getZiehungid().getZiehungid()==43L && l.getGewinnklasseid().getGewinnklasseid()==2L ).findFirst().get();
//		assertTrue(jackpot1.getBetrag()>0 && jackpot2.getBetrag()>0 );         //2 jackpot
		// ****3****

		// ***4******************* GKL1_6aus49==0 von Ziehungid1==42 bis Ziehungid1==53
		// Sonderregel Gkl1: wenn 12 mal unbesetzt in nächste besetzte Gkl

		Jackpot jackpot1 = lJackpot.stream()
				.filter(l -> l.getZiehungid().getZiehungid() == 42L && l.getGewinnklasseid().getGewinnklasseid() == 1L)
				.findFirst().get();
		Jackpot jackpot2 = lJackpot.stream()
				.filter(l -> l.getZiehungid().getZiehungid() == 43L && l.getGewinnklasseid().getGewinnklasseid() == 1L)
				.findFirst().get();
		Jackpot jackpot3 = lJackpot.stream()
				.filter(l -> l.getZiehungid().getZiehungid() == 44L && l.getGewinnklasseid().getGewinnklasseid() == 1L)
				.findFirst().get();
		Jackpot jackpot4 = lJackpot.stream()
				.filter(l -> l.getZiehungid().getZiehungid() == 45L && l.getGewinnklasseid().getGewinnklasseid() == 1L)
				.findFirst().get();
		Jackpot jackpot5 = lJackpot.stream()
				.filter(l -> l.getZiehungid().getZiehungid() == 46L && l.getGewinnklasseid().getGewinnklasseid() == 1L)
				.findFirst().get();
		Jackpot jackpot6 = lJackpot.stream()
				.filter(l -> l.getZiehungid().getZiehungid() == 47L && l.getGewinnklasseid().getGewinnklasseid() == 1L)
				.findFirst().get();
		Jackpot jackpot7 = lJackpot.stream()
				.filter(l -> l.getZiehungid().getZiehungid() == 48L && l.getGewinnklasseid().getGewinnklasseid() == 1L)
				.findFirst().get();
		Jackpot jackpot8 = lJackpot.stream()
				.filter(l -> l.getZiehungid().getZiehungid() == 49L && l.getGewinnklasseid().getGewinnklasseid() == 1L)
				.findFirst().get();
		Jackpot jackpot9 = lJackpot.stream()
				.filter(l -> l.getZiehungid().getZiehungid() == 50L && l.getGewinnklasseid().getGewinnklasseid() == 1L)
				.findFirst().get();
		Jackpot jackpot10 = lJackpot.stream()
				.filter(l -> l.getZiehungid().getZiehungid() == 51L && l.getGewinnklasseid().getGewinnklasseid() == 1L)
				.findFirst().get();
		Jackpot jackpot11 = lJackpot.stream()
				.filter(l -> l.getZiehungid().getZiehungid() == 52L && l.getGewinnklasseid().getGewinnklasseid() == 1L)
				.findFirst().get();
		Jackpot jackpot12 = lJackpot.stream()
				.filter(l -> l.getZiehungid().getZiehungid() == 53L && l.getGewinnklasseid().getGewinnklasseid() == 1L)
				.findFirst().get();

		assertTrue(jackpot1.getBetrag() > 0 && jackpot2.getBetrag() > 0 && jackpot3.getBetrag() > 0
				&& jackpot4.getBetrag() > 0 && jackpot5.getBetrag() > 0 && jackpot6.getBetrag() > 0
				&& jackpot7.getBetrag() > 0 && jackpot8.getBetrag() > 0 && jackpot9.getBetrag() > 0
				&& jackpot10.getBetrag() > 0 && jackpot11.getBetrag() > 0);
		assertTrue(jackpot12.getBetrag() == 0);

		List<Gewinnklasseziehungquote> lGewinnklasseziehungquote = gewinnklasseziehungquoteFacade.findAll();

		Gewinnklasseziehungquote gewinnklasseziehungquote = lGewinnklasseziehungquote.stream()
				.filter(l -> l.getZiehungid().getZiehungid() == 53L && l.getGewinnklasseid().getGewinnklasseid() == 2L)
				.findFirst().get();
		assertTrue(gewinnklasseziehungquote.getQuote() >= jackpot11.getBetragkumuliert()
				/ gewinnklasseziehungquote.getAnzahlgewinner());

		// ***4*******************

//		long gewinnklasseziehungquoteCount = lGewinnklasseziehungquote.stream().count();
//		assertTrue(gewinnklasseziehungquoteCount>0);

	}

}
*/
