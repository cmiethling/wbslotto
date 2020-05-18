package de.wbstraining.lotto.business.lottogesellschaft;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

public class PDFGewinnerGenerator {

	private static String DEST = "C:\\GewinerPDF\\";
	public static final String GRATULATION = "../standalone/deployments/wbslotto.war/resources/images/GRATULATION.jpg";
	// Table : getBelegnummer , getLosnummer , Betrag : getGewinnspiel77 oder
	// getGewinnsuper6

	public static synchronized void createPDFAsByteArray(
			AuftragGewinner auftrag) {// byte[]

//		ByteArrayOutputStream os = new ByteArrayOutputStream();
//		PdfWriter writer = new PdfWriter(os);
//		PdfDocument pdf = new PdfDocument(writer);
//		Document document = new Document(pdf);

		String spiel = null;
		double betrag = 0;

		if (auftrag.getGewinnspiel77() > 0 && auftrag.getGewinnsuper6() > 0) {
			spiel = "Spiel77 und Super6";
			betrag = auftrag.getGewinnspiel77() + auftrag.getGewinnsuper6();
			betrag = betrag / 100;
		} else if (auftrag.getGewinnspiel77() > 0) {
			spiel = "Spiel77";
			betrag = auftrag.getGewinnspiel77();
			betrag = betrag / 100;
		} else if (auftrag.getGewinnsuper6() > 0) {
			spiel = "Super6";
			betrag = auftrag.getGewinnsuper6();
			betrag = betrag / 100;
		} else {
			spiel = "kein Gewinn!";
		}
		try (
				PdfWriter writer = new PdfWriter(
						new FileOutputStream(DEST + auftrag.getName() + ".pdf"));
				PdfDocument pdf = new PdfDocument(writer);
				Document document = new Document(pdf);) {

			// add image
			try {
				document.add(createImageCell(GRATULATION))
						.setWidth(1024);
			} catch (MalformedURLException e) {
				e.getMessage();
			}
			// add betreff
			document.add(new Paragraph("Gewinne im Lotto Spiel!")
					.addStyle(new Style().setFontSize(30))
					.setTextAlignment(TextAlignment.CENTER)
					.setBold());
			// add message
			document.add(new Paragraph("Sehr geehrte Frau / geehrter Herr : "
					+ auftrag.getName() + " " + auftrag.getVorname() + ",\r\n"
					+ "Sie haben " + String.valueOf(betrag) + " € an der Ziehung von "
					+ auftrag.getZiehungDatum() + " mit Belegnummer: "
					+ auftrag.getBelegnummer() + " und Losnummer: "
					+ auftrag.getLosnummer() + " gewonnen im Spiel " + spiel + "."));
			document.add(newLine());
			document.add(newLine());
			document.add(newLine());
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}

		// return os.toByteArray();
	}

	private static Paragraph newLine() {
		Paragraph paragraph = new Paragraph();
		paragraph.setFirstLineIndent(72);
		paragraph.addStyle(new Style().setTextAlignment(TextAlignment.JUSTIFIED));
		return paragraph;
	}

	private static Cell createImageCell(String path)
			throws MalformedURLException {
		Image img = new Image(ImageDataFactory.create(path));
		img.setWidth(UnitValue.createPercentValue(80));
		Cell cell = new Cell().add(img);
		cell.setBorder(null);
		return cell;
	}
}
