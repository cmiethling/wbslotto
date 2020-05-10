package de.wbstraining.lotto.business.lottogesellschaft;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.util.List;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;

import de.wbstraining.lotto.persistence.model.Gewinnklasseziehungquote;
import de.wbstraining.lotto.persistence.model.Jackpot;

public class PdfReportGenerator {

	private static String DEST = "C:\\de.wbstraining.lotto.business.lottogesellschaft.ZiehungAuswertenTest.pdf";
	public static final String WBS = "../standalone/deployments/wbslotto.war/resources/images/wbs.png";
	
	// private static String DEST =
	// "C:/de.wbstraining.lotto.business.lottogesellschaft.ZiehungAuswertenTest.pdf";

	public static void createRepotPDF(List<Jackpot> jackpot,
			List<Gewinnklasseziehungquote> gewinnklasseziehungquoteList) {

		PdfWriter writer;

		try {
			writer = new PdfWriter(new FileOutputStream(DEST));
			PdfDocument pdf = new PdfDocument(writer);
			Document document = new Document(pdf);

			// add image
			try {
				document.add(createImageCell(WBS));
			} catch (MalformedURLException e) {
				e.getMessage();
			}

			document.add(new Paragraph("Report von Ziehung " + jackpot.get(0).getZiehung().getZiehungid())
					.addStyle(new Style().setFontSize(30)).setTextAlignment(TextAlignment.CENTER).setBold()
					.setFontColor(new DeviceRgb(143, 210, 247)));
			document.add(new Paragraph("Jackpot")
					.addStyle(new Style().setFontSize(20).setFontColor(new DeviceRgb(171, 88, 46))));
			document.add(createJackpotTable(jackpot));

			document.add(new Paragraph("Gewinnklasseziehungquote")
					.addStyle(new Style().setFontSize(20).setFontColor(new DeviceRgb(171, 88, 46))));
			document.add(createGewinnklasseziehungquoteTable(gewinnklasseziehungquoteList));

			document.add(newLine());
			document.add(newLine());

			document.add(new Paragraph("Die Gesamte Zahlung von Alle Spiele")
					.addStyle(new Style().setFontSize(20).setFontColor(new DeviceRgb(171, 88, 46))));

			document.add(gesamteZahlungTable(gewinnklasseziehungquoteList));

			document.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	private static Table createJackpotTable(List<Jackpot> jackpot) {

		Table table = new Table(new float[] { 100f, 100f, 100f, 100f, 100f, 100f });

		Style style;
		Style style1;

		style1 = new Style().setFontSize(12).setTextAlignment(TextAlignment.CENTER)
				.setFontColor(new DeviceRgb(148, 138, 90));
		style = new Style().setFontSize(12).setTextAlignment(TextAlignment.CENTER)
				.setFontColor(new DeviceRgb(207, 178, 52));
		Color color = new DeviceRgb(175, 205, 222); // 196, 252, 139
		Color color1 = new DeviceRgb(233, 240, 240);

		table.addCell(createTextCell("Jackpotid", 100, style1).setBorderRight(new SolidBorder(color, .1f))
				.setBackgroundColor(color1));
		table.addCell(createTextCell("Anzahlziehungen", 100, style1).setBorderRight(new SolidBorder(color, .1f))
				.setBackgroundColor(color1));
		table.addCell(createTextCell("Betrag", 100, style1).setBorderRight(new SolidBorder(color, .1f))
				.setBackgroundColor(color1));
		table.addCell(createTextCell("Gewinnklasseid", 100, style1).setBorderRight(new SolidBorder(color, .1f))
				.setBackgroundColor(color1));
		table.addCell(createTextCell("Spiel", 100, style1).setBorderRight(new SolidBorder(color, .1f))
				.setBackgroundColor(color1));
		table.addCell(createTextCell("Betragkumuliert", 100, style1).setBackgroundColor(color1));
		for (Jackpot j : jackpot) {

			String str;
			if (j.getGewinnklasse().getGewinnklasseid() == 10) {
				str = "Spiel77";
				color1 = new DeviceRgb(210, 238, 252);
			} else {
				str = "Spiel6aus49";
				color1 = new DeviceRgb(227, 242, 250);
			}
			table.addCell(createTextCell(String.valueOf(j.getJackpotid()), 100, style)
					.setBorderRight(new SolidBorder(color, .1f)).setBackgroundColor(color1));
			table.addCell(createTextCell(String.valueOf(j.getAnzahlziehungen()), 100, style)
					.setBorderRight(new SolidBorder(color, .1f)).setBackgroundColor(color1));
			table.addCell(createTextCell(String.valueOf(j.getBetrag()), 100, style)
					.setBorderRight(new SolidBorder(color, .1f)).setBackgroundColor(color1));
			table.addCell(createTextCell(String.valueOf(j.getGewinnklasse().getGewinnklasseid()), 100, style)
					.setBorderRight(new SolidBorder(color, .1f)).setBackgroundColor(color1));
			table.addCell(createTextCell(str, 100, style).setBorderRight(new SolidBorder(color, .1f))
					.setBackgroundColor(color1));

			table.addCell(
					createTextCell(String.valueOf(j.getBetragkumuliert()), 100, style).setBackgroundColor(color1));
		}

		return table;

	}

	private static Table createGewinnklasseziehungquoteTable(
			List<Gewinnklasseziehungquote> gewinnklasseziehungquoteList) {

		Table table = new Table(new float[] { 100f, 100f, 100f, 100f, 100f });
		Style style;
		Style style1;

		style1 = new Style().setFontSize(12).setTextAlignment(TextAlignment.CENTER)
				.setFontColor(new DeviceRgb(148, 138, 90));
		style = new Style().setFontSize(12).setTextAlignment(TextAlignment.CENTER)
				.setFontColor(new DeviceRgb(207, 178, 52));
		Color color = new DeviceRgb(175, 205, 222);
		Color color1 = new DeviceRgb(233, 240, 240);
		table.addCell(createTextCell("Gewinnklasseziehungquoteid", 100, style1)
				.setBorderRight(new SolidBorder(color, .1f)).setBackgroundColor(color1));
		table.addCell(createTextCell("Anzahlgewinner", 100, style1).setBorderRight(new SolidBorder(color, .1f))
				.setBackgroundColor(color1));
		table.addCell(createTextCell("Gewinnklasseid", 100, style1).setBorderRight(new SolidBorder(color, .1f))
				.setBackgroundColor(color1));
		table.addCell(createTextCell("Spiel", 100, style1).setBorderRight(new SolidBorder(color, .1f))
				.setBackgroundColor(color1));
		table.addCell(createTextCell("Quote", 100, style1).setBackgroundColor(color1));

		for (Gewinnklasseziehungquote g : gewinnklasseziehungquoteList) {
			String str;

			if (g.getGewinnklasse().getGewinnklasseid() < 10) {
				str = "Spiel6aus49";
				color1 = new DeviceRgb(210, 238, 252);
			} else if (g.getGewinnklasse().getGewinnklasseid() < 17
					&& g.getGewinnklasse().getGewinnklasseid() > 9) {
				str = "Spiel77";
				color1 = new DeviceRgb(227, 242, 250);
			} else {
				str = "Super6";
				color1 = new DeviceRgb(247, 249, 250);
			}

			if (g.getAnzahlgewinner() > 0) {
				table.addCell(createTextCell(String.valueOf(g.getGewinnklasseziehungquoteid()), 100, style)
						.setBorderRight(new SolidBorder(color, .1f)).setBackgroundColor(color1));
				table.addCell(createTextCell(String.valueOf(g.getAnzahlgewinner()), 100, style)
						.setBorderRight(new SolidBorder(color, .1f)).setBackgroundColor(color1));
				table.addCell(createTextCell(String.valueOf(g.getGewinnklasse().getGewinnklasseid()), 100, style)
						.setBorderRight(new SolidBorder(color, .1f)).setBackgroundColor(color1));
				table.addCell(createTextCell(str, 100, style).setBorderRight(new SolidBorder(color, .1f))
						.setBackgroundColor(color1));

				table.addCell(createTextCell(String.valueOf(g.getQuote()), 100, style).setBackgroundColor(color1));
			}
		}

		return table;

	}

	private static Table gesamteZahlungTable(List<Gewinnklasseziehungquote> gewinnklasseziehungquoteList) {

		Table table = new Table(new float[] { 150f, 150f });

		long gesamtSpiel6aus49 = 0;
		long gesamtSpiel77 = 0;
		long gesamtSuper6 = 0;

		Color color = new DeviceRgb(175, 205, 222);
		Color color1 = null;
		Style style;
		Style style1;

		style1 = new Style().setFontSize(12).setTextAlignment(TextAlignment.CENTER)
				.setFontColor(new DeviceRgb(148, 138, 90));
		style = new Style().setFontSize(12).setTextAlignment(TextAlignment.LEFT)
				.setFontColor(new DeviceRgb(207, 178, 52));

		for (Gewinnklasseziehungquote g : gewinnklasseziehungquoteList) {

			if (g.getGewinnklasse().getGewinnklasseid() < 10) {
				gesamtSpiel6aus49 += g.getAnzahlgewinner() * g.getQuote();
				color1 = new DeviceRgb(210, 238, 252);
			} else if (g.getGewinnklasse().getGewinnklasseid() < 17
					&& g.getGewinnklasse().getGewinnklasseid() > 9) {
				gesamtSpiel77 += g.getAnzahlgewinner() * g.getQuote();
				color1 = new DeviceRgb(227, 242, 250);
			} else {
				gesamtSuper6 += g.getAnzahlgewinner() * g.getQuote();
				color1 = new DeviceRgb(247, 249, 250);
			}
		}

		table.addCell(createTextCell("Spiele", 100, style1).setBorderRight(new SolidBorder(color, .1f))
				.setBackgroundColor(color1));
		table.addCell(createTextCell("Gesamte Gebüren", 100, style1).setBorderRight(new SolidBorder(color, .1f))
				.setBackgroundColor(color1));

		table.addCell(createTextCell("gesamtSpiel6aus49", 100, style).setBorderRight(new SolidBorder(color, .1f))
				.setBackgroundColor(color1));
		table.addCell(createTextCell(String.valueOf(gesamtSpiel6aus49), 100, style)
				.setBorderRight(new SolidBorder(color, .1f)).setBackgroundColor(color1));

		table.addCell(createTextCell("gesamtSpiel77", 100, style).setBorderRight(new SolidBorder(color, .1f))
				.setBackgroundColor(color1));
		table.addCell(createTextCell(String.valueOf(gesamtSpiel77), 100, style)
				.setBorderRight(new SolidBorder(color, .1f)).setBackgroundColor(color1));

		table.addCell(createTextCell("gesamtSuper6", 100, style).setBorderRight(new SolidBorder(color, .1f))
				.setBackgroundColor(color1));
		table.addCell(createTextCell(String.valueOf(gesamtSuper6), 100, style)
				.setBorderRight(new SolidBorder(color, .1f)).setBackgroundColor(color1));
		return table;
	}

	private static Cell createTextCell(String text, float width, Style style) {
		Cell cell = new Cell();
		Color color = new DeviceRgb(175, 205, 222);
		Paragraph p = new Paragraph(text);
		p.addStyle(style);
		cell.add(p).setVerticalAlignment(VerticalAlignment.MIDDLE);
		cell.setWidth(width);
		cell.setBorderBottom(new SolidBorder(color, .1f));
		cell.setBorder(Border.NO_BORDER);
		return cell;
	}

	private static Cell createImageCell(String path) throws MalformedURLException {
		Image img = new Image(ImageDataFactory.create(path));
		img.setWidth(UnitValue.createPercentValue(80));
		Cell cell = new Cell().add(img);
		cell.setBorder(null);
		return cell;
	}

	private static Paragraph newLine() {
		Paragraph paragraph = new Paragraph();
		paragraph.setFirstLineIndent(72);
		paragraph.addStyle(new Style().setTextAlignment(TextAlignment.JUSTIFIED));
		return paragraph;
	}
}
