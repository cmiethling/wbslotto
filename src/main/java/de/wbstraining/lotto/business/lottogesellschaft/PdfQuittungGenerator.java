package de.wbstraining.lotto.business.lottogesellschaft;

import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;

import de.wbstraining.lotto.dto.KostenDetailedDto;
import de.wbstraining.lotto.dto.KostenDetailedDto.Item;
import de.wbstraining.lotto.persistence.model.Adresse;
import de.wbstraining.lotto.persistence.model.Gebuehr;

public class PdfQuittungGenerator {

	public static final String unterschrift = "../standalone/deployments/wbslotto.war/resources/images/unterschrift.png";
	public static final String WBS = "../standalone/deployments/wbslotto.war/resources/images/wbs.png";
	public static final String bezahlt = "../standalone/deployments/wbslotto.war/resources/images/bezahlt.png";

	public static byte[] createPDFAsByteArray(Auftrag auftrag, AuftragKunde kunde,
		KostenDetailedDto kostenDetailedDto) {

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		PdfWriter writer = new PdfWriter(os);
		PdfDocument pdf = new PdfDocument(writer);
		Document document = new Document(pdf);

		// Color color = new DeviceRgb(175, 205, 222);
		Color color = Color.LIGHT_GRAY;
		// add image
		try {
			document.add(createImageCell(WBS));
		} catch (MalformedURLException e) {
			e.getMessage();
		}

		// add Adresse
		document.add(createTableAdresse(kunde));
		document.add(newLine());
		document.add(newLine());
		// add Line Separator
		SolidLine lineDrawer = new SolidLine(1f);
		lineDrawer.setColor(color);
		document.add(createLineSeparator(color));
		// add Betreff
		document.add(new Paragraph("Betreff : Rechnung")
			.addStyle(new Style().setFontSize(20)));
		document.add(newLine());
		document.add(newLine());
		// add message
		document.add(new Paragraph("Sehr geehrte Damen und Herren,\r\n"
			+ "hiermit stelle ich Ihnen die folgende Positionen in Rechnung."));
		document.add(newLine());
		// add Add Rechnung
		document.add(createTableRechnung(auftrag));

		document.add(newLine());

		// add Einzelheiten der Zahlung
		Style style = new Style().setFontSize(22)
			.setBold()
			.setTextAlignment(TextAlignment.LEFT);
		Paragraph p = new Paragraph("Einzelheiten der Zahlung");
		p.addStyle(style);
		document.add(p);
		document.add(newLine());
		DecimalFormat myFormatter = new DecimalFormat("#,##0.00");

		document.add(new Paragraph("Grundgebühr: \t"
			+ myFormatter.format(kostenDetailedDto.getGrundgebuehr() / 100.0)
			+ "\t €"));
		document.add(newLine());
		pdf.addNewPage();

		document.add(createTableZahlungseinzelheiten(kostenDetailedDto));

		// add Ende
		document.add(newLine());
		document.add(newLine());
		document.add(new Paragraph("Die Rechnung wurde bereits bezahlt."));
		document.add(newLine());
		try {
			document.add(createImageUnterschrift(unterschrift));
		} catch (MalformedURLException e) {
		}
		// add image
		try {
			document.add(createImageCell(bezahlt));
		} catch (MalformedURLException e) {
			e.getMessage();
		}
		document.close();
		return os.toByteArray();
	}

	public static byte[] createPDFAsByteArray(Auftrag auftrag, AuftragKunde kunde,
		Map<LocalDate, Gebuehr> gebueren) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		PdfWriter writer = new PdfWriter(os);
		PdfDocument pdf = new PdfDocument(writer);
		Document document = new Document(pdf);

		// Color color = new DeviceRgb(175, 205, 222);
		Color color = Color.LIGHT_GRAY;
		// add image
		try {
			document.add(createImageCell(WBS));
		} catch (MalformedURLException e) {
			e.getMessage();
		}

		// add Adresse
		document.add(createTableAdresse(kunde));
		document.add(newLine());
		document.add(newLine());
		// add Line Separator
		SolidLine lineDrawer = new SolidLine(1f);
		lineDrawer.setColor(color);
		document.add(createLineSeparator(color));
		// add Betreff
		document.add(new Paragraph("Betreff : Rechnung")
			.addStyle(new Style().setFontSize(20)));
		document.add(newLine());
		document.add(newLine());
		// add message
		document.add(new Paragraph("Sehr geehrte Damen und Herren,\r\n"
			+ "hiermit stelle ich Ihnen die folgende Positionen in Rechnung."));
		document.add(newLine());
		// add Add Rechnung
		document.add(createTableRechnung(auftrag));

		document.add(newLine());
		document.add(newLine());

		// add Einzelheiten der Zahlung
		Style style = new Style().setFontSize(22)
			.setBold()
			.setTextAlignment(TextAlignment.LEFT);
		Paragraph p = new Paragraph("Einzelheiten der Zahlung");
		p.addStyle(style);
		document.add(p);
		document.add(newLine());

		LocalDate ersteDatum = gebueren.keySet()
			.stream()
			.limit(1)
			.findFirst()
			.get();
		Gebuehr ersteGebuehr = gebueren.remove(ersteDatum);

		DecimalFormat myFormatter = new DecimalFormat("#,##0.00");

		document.add(new Paragraph("Grundgebühr: \t"
			+ myFormatter.format(ersteGebuehr.getGrundgebuehr()) + "\t €"));
		document.add(newLine());

		document.add(createTableZahlungseinzelheiten(gebueren));
		// add Ende
		document.add(newLine());
		document.add(newLine());
		document.add(new Paragraph("Die Rechnung wurde bereits bezahlt."));
		document.add(newLine());
		try {
			document.add(createImageUnterschrift(unterschrift));
		} catch (MalformedURLException e) {
		}
		// add image
		try {
			document.add(createImageCell(bezahlt));
		} catch (MalformedURLException e) {
			e.getMessage();
		}
		document.close();
		return os.toByteArray();
	}

	private static Cell createImageCell(String path)
		throws MalformedURLException {
		Image img = new Image(ImageDataFactory.create(path));
		img.setWidth(UnitValue.createPercentValue(80));
		Cell cell = new Cell().add(img);
		cell.setBorder(null);
		return cell;
	}

	private static Cell createImageUnterschrift(String path)
		throws MalformedURLException {
		Image img = new Image(ImageDataFactory.create(path));
		img.setWidth(UnitValue.createPercentValue(20));
		Cell cell = new Cell().add(img);
		cell.setBorder(null);
		return cell;
	}

	private static Cell createTextCell(String text, float width, float height,
		Style style, VerticalAlignment verticalAlignment) {
		Cell cell = new Cell();
		Paragraph p = new Paragraph(text);
		p.addStyle(style);
		cell.add(p)
			.setVerticalAlignment(verticalAlignment);
		cell.setWidth(width);
		cell.setHeight(height);
		cell.setBorder(Border.NO_BORDER);
		return cell;
	}

	private static Cell createLeftCellOfTableAdresse(AuftragKunde kunde) {
		String name = kunde.getName();
		String vorname = kunde.getVorname();

		Adresse adresse = kunde.getAdresseList()
			.get(0);

		String strasse = adresse.getStrasse();
		String hausnummer = adresse.getHausnummer();
		String plz = adresse.getPlz();
		String ort = adresse.getOrt();

		Cell cell = new Cell();
		Table table = new Table(1);
		Style style;
		String str;

		style = new Style().setFontSize(8)
			.setUnderline()
			.setTextAlignment(TextAlignment.LEFT);
		str = "WBS Lotto - Lockwitzer Str. 23 - 01219 Dresden";
		table.addCell(createTextCell(str, 400, 40, style, VerticalAlignment.TOP));

		style = new Style().setFontSize(12)
			.setTextAlignment(TextAlignment.LEFT);
		str = name + " " + vorname + "\n" + strasse + "  " + hausnummer + "\n" + plz
			+ "  " + ort;
		table.addCell(createTextCell(str, 400, 100, style, VerticalAlignment.TOP));

		style = new Style().setFontSize(22)
			.setBold()
			.setTextAlignment(TextAlignment.LEFT);
		str = "Rechnung";
		table
			.addCell(createTextCell(str, 400, 70, style, VerticalAlignment.MIDDLE));

		cell.add(table)
			.setVerticalAlignment(VerticalAlignment.TOP);
		cell.setWidth(400);
		cell.setBorder(Border.NO_BORDER);
		return cell;
	}

	private static Cell createRightCellOfTableAdresse(AuftragKunde kunde) {
		long kundeId = kunde.getKundeid();

		Cell cell = new Cell();
		Table table = new Table(1);
		Style style;
		String str;
		LocalDate date = LocalDate.now();
//                DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

		style = new Style().setFontSize(12)
			.setTextAlignment(TextAlignment.RIGHT);
		str = "WBS Lotto \r" + "Lockwitzer Str. 23 \r" + "01219 Dresden";
		table.addCell(createTextCell(str, 400, 60, style, VerticalAlignment.TOP));

		str = "Tel.: 0525 12345 67 \r" + "E-Mail: kundenservice@wbs.de \r"
			+ "Internet: www.wbs.de";
		table.addCell(createTextCell(str, 400, 80, style, VerticalAlignment.TOP));

		str = "Rechnungdatum: " + date.format(formatter) + "\n" + "Rechnungnummer: "
			+ String.valueOf(ThreadLocalRandom.current()
				.nextInt(10_000_000, 99_999_999))
			+ "\n" + "Kundenummer: " + String.valueOf(kundeId) + "\n";
		table
			.addCell(createTextCell(str, 400, 70, style, VerticalAlignment.BOTTOM));

		cell.add(table)
			.setVerticalAlignment(VerticalAlignment.TOP);
		cell.setWidth(400);
		cell.setBorder(Border.NO_BORDER);
		return cell;
	}

	private static Table createTableAdresse(AuftragKunde kunde) {

		Table table = new Table(2);

		table.addCell(createLeftCellOfTableAdresse(kunde));

		table.addCell(createRightCellOfTableAdresse(kunde));

		return table;
	}

	private static Table createTableRechnung(Auftrag auftrag) {

		String artikel;
		// Color color = new DeviceRgb(175, 205, 222);
		Color color = Color.LIGHT_GRAY;
		String AnzahlTipps = "AnzahlTipps \t" + auftrag.getAnzahlTipps();
		String einsatSpiel77 = auftrag.getIsSpiel77() ? "Spiel77 \t Ja"
			: "Spiel77 \t Nein";
		String einsatSuper6 = auftrag.getIsSuper6() ? "Super6 \t Ja"
			: "Super6 \t Nein";
		String mittwoch = auftrag.getIsMittwoch() ? "Mittwoch \t Ja"
			: "Mittwoch \t Nein";
		String samstag = auftrag.getIsSamstag() ? "Samstag \t Ja"
			: "Samstag \t Nein";

		String woche = " Woche";
		if (auftrag.getLaufzeit() != 1) {
			woche += "n";
		}

		String laufzeit = "Laufzeit \t" + auftrag.getLaufzeit() + woche;

		artikel = "Lottoschein 6 aus 49\n" + AnzahlTipps + "\n" + einsatSpiel77
			+ "\n" + einsatSuper6 + "\n" + mittwoch + "\n" + samstag + "\n"
			+ laufzeit;

		Table table = new Table(4);

		DecimalFormat myFormatter = new DecimalFormat("#,##0.00");

		table.addCell(createTextCell("Pos", 50, TextAlignment.JUSTIFIED)
			.setBorderBottom(new SolidBorder(color, .1f))
			.setBorderRight(new SolidBorder(color, .1f)));
		table.addCell(createTextCell("Artikel", 300, TextAlignment.JUSTIFIED)
			.setBorderBottom(new SolidBorder(color, .1f))
			.setBorderRight(new SolidBorder(color, .1f)));
		table.addCell(createTextCell("Anzahl", 50, TextAlignment.JUSTIFIED)
			.setBorderBottom(new SolidBorder(color, .1f))
			.setBorderRight(new SolidBorder(color, .1f)));
		table.addCell(createTextCell("Preis", 100, TextAlignment.JUSTIFIED)
			.setBorderBottom(new SolidBorder(color, .1f)));

		table.addCell(createTextCell("1", 30, TextAlignment.JUSTIFIED)
			.setBorderBottom(new SolidBorder(color, .1f))
			.setBorderRight(new SolidBorder(color, .1f)));
		table.addCell(createTextCell(artikel, 30, TextAlignment.JUSTIFIED)
			.setBorderBottom(new SolidBorder(color, .1f))
			.setBorderRight(new SolidBorder(color, .1f)));
		table.addCell(createTextCell("1", 30, TextAlignment.JUSTIFIED)
			.setBorderBottom(new SolidBorder(color, .1f))
			.setBorderRight(new SolidBorder(color, .1f)));
		table.addCell(
			createTextCell(myFormatter.format(auftrag.getKosten() / 100.0) + " €", 30,
				TextAlignment.JUSTIFIED).setBorderBottom(new SolidBorder(color, .1f)));
		return table;
	}

	private static Table createTableZahlungseinzelheiten(
		KostenDetailedDto kostenDetailedDto) {

		Table table = new Table(5);

		Color color = Color.LIGHT_GRAY;

		table
			.addCell(createTextCell("Gültig ab Datum", 100, TextAlignment.JUSTIFIED)
				.setBorderBottom(new SolidBorder(color, .1f))
				.setBorderRight(new SolidBorder(color, .1f)));
		table
			.addCell(createTextCell("Anzahl Ziehungen", 100, TextAlignment.JUSTIFIED)
				.setBorderBottom(new SolidBorder(color, .1f))
				.setBorderRight(new SolidBorder(color, .1f)));
		table
			.addCell(createTextCell("Einsatz pro Tipp", 100, TextAlignment.JUSTIFIED)
				.setBorderBottom(new SolidBorder(color, .1f))
				.setBorderRight(new SolidBorder(color, .1f)));
		table
			.addCell(createTextCell("Einsatz Spiel 77", 100, TextAlignment.JUSTIFIED)
				.setBorderBottom(new SolidBorder(color, .1f))
				.setBorderRight(new SolidBorder(color, .1f)));
		table
			.addCell(createTextCell("Einsatz Super 6", 100, TextAlignment.JUSTIFIED)
				.setBorderBottom(new SolidBorder(color, .1f))
				.setBorderRight(new SolidBorder(color, .1f)));

		Map<LocalDate, Item> tipps = kostenDetailedDto.getEinsatzTipps();
		Map<LocalDate, Item> sp77 = kostenDetailedDto.getEinsatzSpiel77();
		Map<LocalDate, Item> sup6 = kostenDetailedDto.getEinsatzSuper6();
		LocalDate abgabeDatum = kostenDetailedDto.getAbgabeDatum()
			.toLocalDate();

		Set<LocalDate> dates = tipps.keySet();

		DecimalFormat myFormatter = new DecimalFormat("#,##0.00");

		Map<LocalDate, String[]> gebuehrmap = new TreeMap<>();
		for (LocalDate date : dates) {
			String[] gebuehrzeile = new String[4];
			gebuehrzeile[0] = String.format("%03d", tipps.get(date)
				.getAnzahlZiehungen()); // Neu Anzahl Ziehungen
			gebuehrzeile[1] = myFormatter.format(tipps.get(date)
				.getGrundEinsatz() / 100.0) + "\t €";
			gebuehrzeile[2] = myFormatter.format(sp77.get(date)
				.getGrundEinsatz() / 100.0) + "\t €";
			gebuehrzeile[3] = myFormatter.format(sup6.get(date)
				.getGrundEinsatz() / 100.0) + "\t €";
			if (abgabeDatum.isAfter(date)) {
				date = abgabeDatum;
			}
			gebuehrmap.put(date, gebuehrzeile);
		}

		// wir verwenden zeile[1] bis [3] für Geld und die zeile[0] für Anzahl
		// Ziehungen
		gebuehrmap.forEach((date, zeile) -> {
			table.addCell(
				createTextCell(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
					100, TextAlignment.JUSTIFIED)
						.setBorderBottom(new SolidBorder(color, .1f))
						.setBorderRight(new SolidBorder(color, .1f)));
			// diese Zeile "Grundgebühr" wird ersetzt durch Anzahl Ziehungen
			table.addCell(createTextCell(zeile[0], 100, TextAlignment.CENTER)
				.setBorderBottom(new SolidBorder(color, .1f))
				.setBorderRight(new SolidBorder(color, .1f)));
			table.addCell(createTextCell(zeile[1], 100, TextAlignment.RIGHT)
				.setBorderBottom(new SolidBorder(color, .1f))
				.setBorderRight(new SolidBorder(color, .1f)));
			table.addCell(createTextCell(zeile[2], 100, TextAlignment.RIGHT)
				.setBorderBottom(new SolidBorder(color, .1f))
				.setBorderRight(new SolidBorder(color, .1f)));
			table.addCell(createTextCell(zeile[3], 100, TextAlignment.RIGHT)
				.setBorderBottom(new SolidBorder(color, .1f))
				.setBorderRight(new SolidBorder(color, .1f)));
		});

		// Map<LocalDate, Gebuehr> gebuehrmap = new TreeMap<>();
//                for (LocalDate date : dates) {
//                        Gebuehr gebuehrzeile = new Gebuehr();
//                        
//                        gebuehrzeile.set(kostenDetailedDto.getGrundgebuehr()); // Platzhalter für Grundgebühr
//                        gebuehrzeile.setEinsatzprotipp(tipps.get(date).getGrundEinsatz());
//                        gebuehrzeile.setEinsatzspiel77(sp77.get(date).getGrundEinsatz());
//                        gebuehrzeile.setEinsatzsuper6(sup6.get(date).getGrundEinsatz());
//                        gebuehrmap.put(date, gebuehrzeile);
//                }

//                gebuehrmap.forEach((date, gebuehr) -> {
//                        table.addCell(
//                                        createTextCell(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), 100, TextAlignment.JUSTIFIED)
//                                                        .setBorderBottom(new SolidBorder(color, .1f)).setBorderRight(new SolidBorder(color, .1f)));
//                        table.addCell(createTextCell(myFormatter.format(gebuehr.getEinsatzprotipp()) + "\t €", 100,
//                                        TextAlignment.JUSTIFIED).setBorderBottom(new SolidBorder(color, .1f))
//                                                        .setBorderRight(new SolidBorder(color, .1f)));
//                        table.addCell(createTextCell(myFormatter.format(gebuehr.getEinsatzspiel77()) + "\t €", 100,
//                                        TextAlignment.JUSTIFIED).setBorderBottom(new SolidBorder(color, .1f))
//                                                        .setBorderRight(new SolidBorder(color, .1f)));
//                        table.addCell(createTextCell(myFormatter.format(gebuehr.getEinsatzsuper6()) + "\t €", 100,
//                                        TextAlignment.JUSTIFIED).setBorderBottom(new SolidBorder(color, .1f))
//                                                        .setBorderRight(new SolidBorder(color, .1f)));
//                });

		return table;
	}

	private static Table createTableZahlungseinzelheiten(
		Map<LocalDate, Gebuehr> gebueren) {

		Table table = new Table(4);
		Color color = new DeviceRgb(175, 205, 222);

		table.addCell(createTextCell("ZiehungsDatum", 100, TextAlignment.JUSTIFIED)
			.setBorderBottom(new SolidBorder(color, .1f))
			.setBorderRight(new SolidBorder(color, .1f)));
//                table.addCell(createTextCell("Grundgebühr", 100, TextAlignment.JUSTIFIED)
//                                .setBorderBottom(new SolidBorder(color, .1f)).setBorderRight(new SolidBorder(color, .1f)));
		table
			.addCell(createTextCell("Einsatz pro Tipp", 100, TextAlignment.JUSTIFIED)
				.setBorderBottom(new SolidBorder(color, .1f))
				.setBorderRight(new SolidBorder(color, .1f)));
		table.addCell(createTextCell("EinsatzSpiel77", 100, TextAlignment.JUSTIFIED)
			.setBorderBottom(new SolidBorder(color, .1f))
			.setBorderRight(new SolidBorder(color, .1f)));
		table.addCell(createTextCell("EinsatzSuper6", 100, TextAlignment.JUSTIFIED)
			.setBorderBottom(new SolidBorder(color, .1f))
			.setBorderRight(new SolidBorder(color, .1f)));

		gebueren.forEach((date, gebuehr) -> {
			table.addCell(
				createTextCell(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
					100, TextAlignment.JUSTIFIED)
						.setBorderBottom(new SolidBorder(color, .1f))
						.setBorderRight(new SolidBorder(color, .1f)));
			table.addCell(
				createTextCell(String.valueOf(gebuehr.getEinsatzprotipp()), 100,
					TextAlignment.JUSTIFIED).setBorderBottom(new SolidBorder(color, .1f))
						.setBorderRight(new SolidBorder(color, .1f)));
			table.addCell(
				createTextCell(String.valueOf(gebuehr.getEinsatzspiel77()), 100,
					TextAlignment.JUSTIFIED).setBorderBottom(new SolidBorder(color, .1f))
						.setBorderRight(new SolidBorder(color, .1f)));
			table
				.addCell(createTextCell(String.valueOf(gebuehr.getEinsatzsuper6()), 100,
					TextAlignment.JUSTIFIED).setBorderBottom(new SolidBorder(color, .1f))
						.setBorderRight(new SolidBorder(color, .1f)));
		});

		return table;
	}

	private static Paragraph newLine() {
		Paragraph paragraph = new Paragraph();
		paragraph.setFirstLineIndent(72);
		paragraph.addStyle(new Style().setTextAlignment(TextAlignment.JUSTIFIED));
		return paragraph;
	}

	private static LineSeparator createLineSeparator(Color color) {
		SolidLine lineDrawer = new SolidLine(2F);
		lineDrawer.setLineWidth(3F);
		lineDrawer.setColor(color);
		LineSeparator lineSeparator = new LineSeparator(lineDrawer);
		return lineSeparator;
	}

	private static Cell createTextCell(String text, float width,
		TextAlignment alignment) {
		Cell cell = new Cell();
		Paragraph p = new Paragraph(text);
		p.setTextAlignment(alignment);
		cell.add(p)
			.setVerticalAlignment(VerticalAlignment.BOTTOM);
		cell.setWidth(width);
		// cell.setMaxHeight(100);
		cell.setBorder(Border.NO_BORDER);
		return cell;
	}
}