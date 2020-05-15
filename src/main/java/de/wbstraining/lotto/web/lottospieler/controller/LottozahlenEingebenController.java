/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wbstraining.lotto.web.lottospieler.controller;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author gz1
 */
@SessionScoped
@Named
public class LottozahlenEingebenController implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String RADIO_BUTTON = "radioButton";
	private static final String BUTTON_GRAY = "buttonGray";
	private static final String BUTTON_YELLOW = "buttonYellow";
	private static final String RADIO_BUTTON_SELECTED = "radioButtonSelected";

	private int zahl;
	private String zahlen;

	private int tipp;
	private int tipps;

	private String tipp1;
	private String tipp2;
	private String tipp3;
	private String tipp4;
	private String tipp5;
	private String tipp6;
	private String tipp7;
	private String tipp8;
	private String tipp9;
	private String tipp10;
	private String tipp11;
	private String tipp12;

	private String radioBtn1Class = RADIO_BUTTON;
	private String radioBtn2Class = RADIO_BUTTON;
	private String radioBtn3Class = RADIO_BUTTON;
	private String radioBtn4Class = RADIO_BUTTON;
	private String radioBtn5Class = RADIO_BUTTON;
	private String radioBtn6Class = RADIO_BUTTON;
	private String radioBtn7Class = RADIO_BUTTON;
	private String radioBtn8Class = RADIO_BUTTON;
	private String radioBtn9Class = RADIO_BUTTON;
	private String radioBtn10Class = RADIO_BUTTON;
	private String radioBtn11Class = RADIO_BUTTON;
	private String radioBtn12Class = RADIO_BUTTON;

	private int isValid1;
	private int isValid2;
	private int isValid3;
	private int isValid4;
	private int isValid5;
	private int isValid6;
	private int isValid7;
	private int isValid8;
	private int isValid9;
	private int isValid10;
	private int isValid11;
	private int isValid12;

	private String valid1Class;
	private String valid2Class;
	private String valid3Class;
	private String valid4Class;
	private String valid5Class;
	private String valid6Class;
	private String valid7Class;
	private String valid8Class;
	private String valid9Class;
	private String valid10Class;
	private String valid11Class;
	private String valid12Class;

	private String btn1Class = BUTTON_GRAY;
	private String btn2Class = BUTTON_GRAY;
	private String btn3Class = BUTTON_GRAY;
	private String btn4Class = BUTTON_GRAY;
	private String btn5Class = BUTTON_GRAY;
	private String btn6Class = BUTTON_GRAY;
	private String btn7Class = BUTTON_GRAY;
	private String btn8Class = BUTTON_GRAY;
	private String btn9Class = BUTTON_GRAY;
	private String btn10Class = BUTTON_GRAY;
	private String btn11Class = BUTTON_GRAY;
	private String btn12Class = BUTTON_GRAY;
	private String btn13Class = BUTTON_GRAY;
	private String btn14Class = BUTTON_GRAY;
	private String btn15Class = BUTTON_GRAY;
	private String btn16Class = BUTTON_GRAY;
	private String btn17Class = BUTTON_GRAY;
	private String btn18Class = BUTTON_GRAY;
	private String btn19Class = BUTTON_GRAY;
	private String btn20Class = BUTTON_GRAY;
	private String btn21Class = BUTTON_GRAY;
	private String btn22Class = BUTTON_GRAY;
	private String btn23Class = BUTTON_GRAY;
	private String btn24Class = BUTTON_GRAY;
	private String btn25Class = BUTTON_GRAY;
	private String btn26Class = BUTTON_GRAY;
	private String btn27Class = BUTTON_GRAY;
	private String btn28Class = BUTTON_GRAY;
	private String btn29Class = BUTTON_GRAY;
	private String btn30Class = BUTTON_GRAY;
	private String btn31Class = BUTTON_GRAY;
	private String btn32Class = BUTTON_GRAY;
	private String btn33Class = BUTTON_GRAY;
	private String btn34Class = BUTTON_GRAY;
	private String btn35Class = BUTTON_GRAY;
	private String btn36Class = BUTTON_GRAY;
	private String btn37Class = BUTTON_GRAY;
	private String btn38Class = BUTTON_GRAY;
	private String btn39Class = BUTTON_GRAY;
	private String btn40Class = BUTTON_GRAY;
	private String btn41Class = BUTTON_GRAY;
	private String btn42Class = BUTTON_GRAY;
	private String btn43Class = BUTTON_GRAY;
	private String btn44Class = BUTTON_GRAY;
	private String btn45Class = BUTTON_GRAY;
	private String btn46Class = BUTTON_GRAY;
	private String btn47Class = BUTTON_GRAY;
	private String btn48Class = BUTTON_GRAY;
	private String btn49Class = BUTTON_GRAY;

	private int[] btn = new int[50];

	public String next() {
		return "ok, next";
	}

	public String delete(int i) {
		return "ok2";
	}

	public void setZahlen() {
		btn[1] = 0;
		btn1Class = BUTTON_GRAY;
		btn[2] = 0;
		btn2Class = BUTTON_GRAY;
		btn[3] = 0;
		btn3Class = BUTTON_GRAY;
		btn[4] = 0;
		btn4Class = BUTTON_GRAY;
		btn[5] = 0;
		btn5Class = BUTTON_GRAY;
		btn[6] = 0;
		btn6Class = BUTTON_GRAY;
		btn[7] = 0;
		btn7Class = BUTTON_GRAY;
		btn[8] = 0;
		btn8Class = BUTTON_GRAY;
		btn[9] = 0;
		btn9Class = BUTTON_GRAY;
		btn[10] = 0;
		btn10Class = BUTTON_GRAY;
		btn[11] = 0;
		btn11Class = BUTTON_GRAY;
		btn[12] = 0;
		btn12Class = BUTTON_GRAY;
		btn[13] = 0;
		btn13Class = BUTTON_GRAY;
		btn[14] = 0;
		btn14Class = BUTTON_GRAY;
		btn[15] = 0;
		btn15Class = BUTTON_GRAY;
		btn[16] = 0;
		btn16Class = BUTTON_GRAY;
		btn[17] = 0;
		btn17Class = BUTTON_GRAY;
		btn[18] = 0;
		btn18Class = BUTTON_GRAY;
		btn[19] = 0;
		btn19Class = BUTTON_GRAY;
		btn[20] = 0;
		btn20Class = BUTTON_GRAY;
		btn[21] = 0;
		btn21Class = BUTTON_GRAY;
		btn[22] = 0;
		btn22Class = BUTTON_GRAY;
		btn[23] = 0;
		btn23Class = BUTTON_GRAY;
		btn[24] = 0;
		btn24Class = BUTTON_GRAY;
		btn[25] = 0;
		btn25Class = BUTTON_GRAY;
		btn[26] = 0;
		btn26Class = BUTTON_GRAY;
		btn[27] = 0;
		btn27Class = BUTTON_GRAY;
		btn[28] = 0;
		btn28Class = BUTTON_GRAY;
		btn[29] = 0;
		btn29Class = BUTTON_GRAY;
		btn[30] = 0;
		btn30Class = BUTTON_GRAY;
		btn[31] = 0;
		btn31Class = BUTTON_GRAY;
		btn[32] = 0;
		btn32Class = BUTTON_GRAY;
		btn[33] = 0;
		btn33Class = BUTTON_GRAY;
		btn[34] = 0;
		btn34Class = BUTTON_GRAY;
		btn[35] = 0;
		btn35Class = BUTTON_GRAY;
		btn[36] = 0;
		btn36Class = BUTTON_GRAY;
		btn[37] = 0;
		btn37Class = BUTTON_GRAY;
		btn[38] = 0;
		btn38Class = BUTTON_GRAY;
		btn[39] = 0;
		btn39Class = BUTTON_GRAY;
		btn[40] = 0;
		btn40Class = BUTTON_GRAY;
		btn[41] = 0;
		btn41Class = BUTTON_GRAY;
		btn[42] = 0;
		btn42Class = BUTTON_GRAY;
		btn[43] = 0;
		btn43Class = BUTTON_GRAY;
		btn[44] = 0;
		btn44Class = BUTTON_GRAY;
		btn[45] = 0;
		btn45Class = BUTTON_GRAY;
		btn[46] = 0;
		btn46Class = BUTTON_GRAY;
		btn[47] = 0;
		btn47Class = BUTTON_GRAY;
		btn[48] = 0;
		btn48Class = BUTTON_GRAY;
		btn[49] = 0;
		btn49Class = BUTTON_GRAY;

		int i;
		int count = 0;
		String[] items = zahlen.split(" ");

		for (String s : items) {
			try {
				if (!s.equals("")) {
					i = Integer.parseInt(s);
					if ((i > 0) && (i < 50)) {
						btn[i] = 1;
						count++;
					}
				}
			} catch (Exception e) {
			}

			if (count == 6)
				break;
		}
	}

	public String select(int i) {
		if (tipp > 0) {
			radioBtn1Class = RADIO_BUTTON;
			radioBtn2Class = RADIO_BUTTON;
			radioBtn3Class = RADIO_BUTTON;
			radioBtn4Class = RADIO_BUTTON;
			radioBtn5Class = RADIO_BUTTON;
			radioBtn6Class = RADIO_BUTTON;
			radioBtn7Class = RADIO_BUTTON;
			radioBtn8Class = RADIO_BUTTON;
			radioBtn9Class = RADIO_BUTTON;
			radioBtn10Class = RADIO_BUTTON;
			radioBtn11Class = RADIO_BUTTON;
			radioBtn12Class = RADIO_BUTTON;
			switch (tipp) {
			case 1:
				zahlen = tipp1;
				break;
			case 2:
				zahlen = tipp2;
				break;
			case 3:
				zahlen = tipp3;
				break;
			case 4:
				zahlen = tipp4;
				break;
			case 5:
				zahlen = tipp5;
				break;
			case 6:
				zahlen = tipp6;
				break;
			case 7:
				zahlen = tipp7;
				break;
			case 8:
				zahlen = tipp8;
				break;
			case 9:
				zahlen = tipp9;
				break;
			case 10:
				zahlen = tipp10;
				break;
			case 11:
				zahlen = tipp11;
				break;
			case 12:
				zahlen = tipp12;
				break;
			default:
				assert false;
			}
			if (zahlen == null)
				zahlen = "";
			setZahlen();
			checkZahlen();
		}

		tipp = i;
		switch (tipp) {
		case 1:
			zahlen = tipp1;
			radioBtn1Class = RADIO_BUTTON_SELECTED;
			break;
		case 2:
			zahlen = tipp2;
			radioBtn2Class = RADIO_BUTTON_SELECTED;
			break;
		case 3:
			zahlen = tipp3;
			radioBtn3Class = RADIO_BUTTON_SELECTED;
			break;
		case 4:
			zahlen = tipp4;
			radioBtn4Class = RADIO_BUTTON_SELECTED;
			break;
		case 5:
			zahlen = tipp5;
			radioBtn5Class = RADIO_BUTTON_SELECTED;
			break;
		case 6:
			zahlen = tipp6;
			radioBtn6Class = RADIO_BUTTON_SELECTED;
			break;
		case 7:
			zahlen = tipp7;
			radioBtn7Class = RADIO_BUTTON_SELECTED;
			break;
		case 8:
			zahlen = tipp8;
			radioBtn8Class = RADIO_BUTTON_SELECTED;
			break;
		case 9:
			zahlen = tipp9;
			radioBtn9Class = RADIO_BUTTON_SELECTED;
			break;
		case 10:
			zahlen = tipp10;
			radioBtn10Class = RADIO_BUTTON_SELECTED;
			break;
		case 11:
			zahlen = tipp11;
			radioBtn11Class = RADIO_BUTTON_SELECTED;
			break;
		case 12:
			zahlen = tipp12;
			radioBtn12Class = RADIO_BUTTON_SELECTED;
			break;
		default:
			assert false;
		}

		if (zahlen == null)
			zahlen = "";
		setZahlen();
		checkZahlen();
		return "ok2";
	}

	public int checkTipps() {
		tipps = 0;
		if (isValid1 == 2)
			tipps++;
		if (isValid2 == 2)
			tipps++;
		if (isValid3 == 2)
			tipps++;
		if (isValid4 == 2)
			tipps++;
		if (isValid5 == 2)
			tipps++;
		if (isValid6 == 2)
			tipps++;
		if (isValid7 == 2)
			tipps++;
		if (isValid8 == 2)
			tipps++;
		if (isValid9 == 2)
			tipps++;
		if (isValid10 == 2)
			tipps++;
		if (isValid11 == 2)
			tipps++;
		if (isValid12 == 2)
			tipps++;
		return tipps;
	}

	public void checkZahlen() {
		int isValid;
		String validClass;

		zahlen = "";
		for (int i = 1; i < 50; i++) {
			switch (i) {
			case 1:
				if (btn[i] == 1) {
					zahlen = zahlen + i + " ";
					btn1Class = BUTTON_YELLOW;
				}
				break;
			case 2:
				if (btn[i] == 1) {
					zahlen = zahlen + i + " ";
					btn2Class = BUTTON_YELLOW;
				}
				break;
			case 3:
				if (btn[i] == 1) {
					zahlen = zahlen + i + " ";
					btn3Class = BUTTON_YELLOW;
				}
				break;
			case 4:
				if (btn[i] == 1) {
					zahlen = zahlen + i + " ";
					btn4Class = BUTTON_YELLOW;
				}
				break;
			case 5:
				if (btn[i] == 1) {
					zahlen = zahlen + i + " ";
					btn5Class = BUTTON_YELLOW;
				}
				break;
			case 6:
				if (btn[i] == 1) {
					zahlen = zahlen + i + " ";
					btn6Class = BUTTON_YELLOW;
				}
				break;
			case 7:
				if (btn[i] == 1) {
					zahlen = zahlen + i + " ";
					btn7Class = BUTTON_YELLOW;
				}
				break;
			case 8:
				if (btn[i] == 1) {
					zahlen = zahlen + i + " ";
					btn8Class = BUTTON_YELLOW;
				}
				break;
			case 9:
				if (btn[i] == 1) {
					zahlen = zahlen + i + " ";
					btn9Class = BUTTON_YELLOW;
				}
				break;
			case 10:
				if (btn[i] == 1) {
					zahlen = zahlen + i + " ";
					btn10Class = BUTTON_YELLOW;
				}
				break;
			case 11:
				if (btn[i] == 1) {
					zahlen = zahlen + i + " ";
					btn11Class = BUTTON_YELLOW;
				}
				break;
			case 12:
				if (btn[i] == 1) {
					zahlen = zahlen + i + " ";
					btn12Class = BUTTON_YELLOW;
				}
				break;
			case 13:
				if (btn[i] == 1) {
					zahlen = zahlen + i + " ";
					btn13Class = BUTTON_YELLOW;
				}
				break;
			case 14:
				if (btn[i] == 1) {
					zahlen = zahlen + i + " ";
					btn14Class = BUTTON_YELLOW;
				}
				break;
			case 15:
				if (btn[i] == 1) {
					zahlen = zahlen + i + " ";
					btn15Class = BUTTON_YELLOW;
				}
				break;
			case 16:
				if (btn[i] == 1) {
					zahlen = zahlen + i + " ";
					btn16Class = BUTTON_YELLOW;
				}
				break;
			case 17:
				if (btn[i] == 1) {
					zahlen = zahlen + i + " ";
					btn17Class = BUTTON_YELLOW;
				}
				break;
			case 18:
				if (btn[i] == 1) {
					zahlen = zahlen + i + " ";
					btn18Class = BUTTON_YELLOW;
				}
				break;
			case 19:
				if (btn[i] == 1) {
					zahlen = zahlen + i + " ";
					btn19Class = BUTTON_YELLOW;
				}
				break;
			case 20:
				if (btn[i] == 1) {
					zahlen = zahlen + i + " ";
					btn20Class = BUTTON_YELLOW;
				}
				break;
			case 21:
				if (btn[i] == 1) {
					zahlen = zahlen + i + " ";
					btn21Class = BUTTON_YELLOW;
				}
				break;
			case 22:
				if (btn[i] == 1) {
					zahlen = zahlen + i + " ";
					btn22Class = BUTTON_YELLOW;
				}
				break;
			case 23:
				if (btn[i] == 1) {
					zahlen = zahlen + i + " ";
					btn23Class = BUTTON_YELLOW;
				}
				break;
			case 24:
				if (btn[i] == 1) {
					zahlen = zahlen + i + " ";
					btn24Class = BUTTON_YELLOW;
				}
				break;
			case 25:
				if (btn[i] == 1) {
					zahlen = zahlen + i + " ";
					btn25Class = BUTTON_YELLOW;
				}
				break;
			case 26:
				if (btn[i] == 1) {
					zahlen = zahlen + i + " ";
					btn26Class = BUTTON_YELLOW;
				}
				break;
			case 27:
				if (btn[i] == 1) {
					zahlen = zahlen + i + " ";
					btn27Class = BUTTON_YELLOW;
				}
				break;
			case 28:
				if (btn[i] == 1) {
					zahlen = zahlen + i + " ";
					btn28Class = BUTTON_YELLOW;
				}
				break;
			case 29:
				if (btn[i] == 1) {
					zahlen = zahlen + i + " ";
					btn29Class = BUTTON_YELLOW;
				}
				break;
			case 30:
				if (btn[i] == 1) {
					zahlen = zahlen + i + " ";
					btn30Class = BUTTON_YELLOW;
				}
				break;
			case 31:
				if (btn[i] == 1) {
					zahlen = zahlen + i + " ";
					btn31Class = BUTTON_YELLOW;
				}
				break;
			case 32:
				if (btn[i] == 1) {
					zahlen = zahlen + i + " ";
					btn32Class = BUTTON_YELLOW;
				}
				break;
			case 33:
				if (btn[i] == 1) {
					zahlen = zahlen + i + " ";
					btn33Class = BUTTON_YELLOW;
				}
				break;
			case 34:
				if (btn[i] == 1) {
					zahlen = zahlen + i + " ";
					btn34Class = BUTTON_YELLOW;
				}
				break;
			case 35:
				if (btn[i] == 1) {
					zahlen = zahlen + i + " ";
					btn35Class = BUTTON_YELLOW;
				}
				break;
			case 36:
				if (btn[i] == 1) {
					zahlen = zahlen + i + " ";
					btn36Class = BUTTON_YELLOW;
				}
				break;
			case 37:
				if (btn[i] == 1) {
					zahlen = zahlen + i + " ";
					btn37Class = BUTTON_YELLOW;
				}
				break;
			case 38:
				if (btn[i] == 1) {
					zahlen = zahlen + i + " ";
					btn38Class = BUTTON_YELLOW;
				}
				break;
			case 39:
				if (btn[i] == 1) {
					zahlen = zahlen + i + " ";
					btn39Class = BUTTON_YELLOW;
				}
				break;
			case 40:
				if (btn[i] == 1) {
					zahlen = zahlen + i + " ";
					btn40Class = BUTTON_YELLOW;
				}
				break;
			case 41:
				if (btn[i] == 1) {
					zahlen = zahlen + i + " ";
					btn41Class = BUTTON_YELLOW;
				}
				break;
			case 42:
				if (btn[i] == 1) {
					zahlen = zahlen + i + " ";
					btn42Class = BUTTON_YELLOW;
				}
				break;
			case 43:
				if (btn[i] == 1) {
					zahlen = zahlen + i + " ";
					btn43Class = BUTTON_YELLOW;
				}
				break;
			case 44:
				if (btn[i] == 1) {
					zahlen = zahlen + i + " ";
					btn44Class = BUTTON_YELLOW;
				}
				break;
			case 45:
				if (btn[i] == 1) {
					zahlen = zahlen + i + " ";
					btn45Class = BUTTON_YELLOW;
				}
				break;
			case 46:
				if (btn[i] == 1) {
					zahlen = zahlen + i + " ";
					btn46Class = BUTTON_YELLOW;
				}
				break;
			case 47:
				if (btn[i] == 1) {
					zahlen = zahlen + i + " ";
					btn47Class = BUTTON_YELLOW;
				}
				break;
			case 48:
				if (btn[i] == 1) {
					zahlen = zahlen + i + " ";
					btn48Class = BUTTON_YELLOW;
				}
				break;
			case 49:
				if (btn[i] == 1) {
					zahlen = zahlen + i + " ";
					btn49Class = BUTTON_YELLOW;
				}
				break;
			default:
				assert false;
			}
		}

		String[] items = zahlen.split(" ");
		if (zahlen.isEmpty()) {
			isValid = 0;
			validClass = "";
		} else if (items.length == 6) {

			isValid = 2;
			validClass = "labelGreen";
		} else {
			isValid = 1;
			validClass = "labelRed";
		}

		switch (tipp) {
		case 1:
			tipp1 = zahlen;
			isValid1 = isValid;
			valid1Class = validClass;
			break;
		case 2:
			tipp2 = zahlen;
			isValid2 = isValid;
			valid2Class = validClass;
			break;
		case 3:
			tipp3 = zahlen;
			isValid3 = isValid;
			valid3Class = validClass;
			break;
		case 4:
			tipp4 = zahlen;
			isValid4 = isValid;
			valid4Class = validClass;
			break;
		case 5:
			tipp5 = zahlen;
			isValid5 = isValid;
			valid5Class = validClass;
			break;
		case 6:
			tipp6 = zahlen;
			isValid6 = isValid;
			valid6Class = validClass;
			break;
		case 7:
			tipp7 = zahlen;
			isValid7 = isValid;
			valid7Class = validClass;
			break;
		case 8:
			tipp8 = zahlen;
			isValid8 = isValid;
			valid8Class = validClass;
			break;
		case 9:
			tipp9 = zahlen;
			isValid9 = isValid;
			valid9Class = validClass;
			break;
		case 10:
			tipp10 = zahlen;
			isValid10 = isValid;
			valid10Class = validClass;
			break;
		case 11:
			tipp11 = zahlen;
			isValid11 = isValid;
			valid11Class = validClass;
			break;
		case 12:
			tipp12 = zahlen;
			isValid12 = isValid;
			valid12Class = validClass;
			break;
		default:
			assert false;
		}

	}

	public String buttonSenden(int i) {
		boolean found = false;
		zahl = i;
		if (tipp == 0) {
			select(1);
			radioBtn1Class = RADIO_BUTTON_SELECTED;
		}
		switch (tipp) {
		case 1:
			zahlen = tipp1;
			break;
		case 2:
			zahlen = tipp2;
			break;
		case 3:
			zahlen = tipp3;
			break;
		case 4:
			zahlen = tipp4;
			break;
		case 5:
			zahlen = tipp5;
			break;
		case 6:
			zahlen = tipp6;
			break;
		case 7:
			zahlen = tipp7;
			break;
		case 8:
			zahlen = tipp8;
			break;
		case 9:
			zahlen = tipp9;
			break;
		case 10:
			zahlen = tipp10;
			break;
		case 11:
			zahlen = tipp11;
			break;
		case 12:
			zahlen = tipp12;
			break;
		default:
			assert false;
		}

		setZahlen();

		Set<String> set = new HashSet<String>();
		if (zahlen == null)
			zahlen = "";
		else {

			String[] items = zahlen.split(" ");
			if (items.length > 1) {
				for (int j = 0; j < items.length; j++) {
					set.add(items[j]);
					try {
						if (Integer.parseInt(items[j]) == i)
							found = true;
					} catch (Exception e) {
					}
				}
			}

			if ((set.size() < 6) || (found == true)) {
				btn[i] = (btn[i] + 1) % 2;
			}
		}
		checkZahlen();
		checkTipps();

		return "ok2";
	}

	public int getZahl() {
		return zahl;
	}

	public void setZahl(int zahl) {
		this.zahl = zahl;
	}

	public String getZahlen() {
		return zahlen;
	}

	public void setZahlen(String zahlen) {
		this.zahlen = zahlen;
	}

	public int getTipp() {
		return tipp;
	}

	public void setTipp(int tipp) {
		this.tipp = tipp;
	}

	public String getTipp1() {
		return tipp1;
	}

	public void setTipp1(String tipp1) {
		this.tipp1 = tipp1;
	}

	public String getTipp2() {
		return tipp2;
	}

	public void setTipp2(String tipp2) {
		this.tipp2 = tipp2;
	}

	public String getTipp3() {
		return tipp3;
	}

	public void setTipp3(String tipp3) {
		this.tipp3 = tipp3;
	}

	public String getTipp4() {
		return tipp4;
	}

	public void setTipp4(String tipp4) {
		this.tipp4 = tipp4;
	}

	public String getTipp5() {
		return tipp5;
	}

	public void setTipp5(String tipp5) {
		this.tipp5 = tipp5;
	}

	public String getTipp6() {
		return tipp6;
	}

	public void setTipp6(String tipp6) {
		this.tipp6 = tipp6;
	}

	public String getTipp7() {
		return tipp7;
	}

	public void setTipp7(String tipp7) {
		this.tipp7 = tipp7;
	}

	public String getTipp8() {
		return tipp8;
	}

	public void setTipp8(String tipp8) {
		this.tipp8 = tipp8;
	}

	public String getTipp9() {
		return tipp9;
	}

	public void setTipp9(String tipp9) {
		this.tipp9 = tipp9;
	}

	public String getTipp10() {
		return tipp10;
	}

	public void setTipp10(String tipp10) {
		this.tipp10 = tipp10;
	}

	public String getTipp11() {
		return tipp11;
	}

	public void setTipp11(String tipp11) {
		this.tipp11 = tipp11;
	}

	public String getTipp12() {
		return tipp12;
	}

	public void setTipp12(String tipp12) {
		this.tipp12 = tipp12;
	}

	public String getRadioBtn1Class() {
		return radioBtn1Class;
	}

	public void setRadioBtn1Class(String radioBtn1Class) {
		this.radioBtn1Class = radioBtn1Class;
	}

	public String getRadioBtn2Class() {
		return radioBtn2Class;
	}

	public void setRadioBtn2Class(String radioBtn2Class) {
		this.radioBtn2Class = radioBtn2Class;
	}

	public String getRadioBtn3Class() {
		return radioBtn3Class;
	}

	public void setRadioBtn3Class(String radioBtn3Class) {
		this.radioBtn3Class = radioBtn3Class;
	}

	public String getRadioBtn4Class() {
		return radioBtn4Class;
	}

	public void setRadioBtn4Class(String radioBtn4Class) {
		this.radioBtn4Class = radioBtn4Class;
	}

	public String getRadioBtn5Class() {
		return radioBtn5Class;
	}

	public void setRadioBtn5Class(String radioBtn5Class) {
		this.radioBtn5Class = radioBtn5Class;
	}

	public String getRadioBtn6Class() {
		return radioBtn6Class;
	}

	public void setRadioBtn6Class(String radioBtn6Class) {
		this.radioBtn6Class = radioBtn6Class;
	}

	public String getRadioBtn7Class() {
		return radioBtn7Class;
	}

	public void setRadioBtn7Class(String radioBtn7Class) {
		this.radioBtn7Class = radioBtn7Class;
	}

	public String getRadioBtn8Class() {
		return radioBtn8Class;
	}

	public void setRadioBtn8Class(String radioBtn8Class) {
		this.radioBtn8Class = radioBtn8Class;
	}

	public String getRadioBtn9Class() {
		return radioBtn9Class;
	}

	public void setRadioBtn9Class(String radioBtn9Class) {
		this.radioBtn9Class = radioBtn9Class;
	}

	public String getRadioBtn10Class() {
		return radioBtn10Class;
	}

	public void setRadioBtn10Class(String radioBtn10Class) {
		this.radioBtn10Class = radioBtn10Class;
	}

	public String getRadioBtn11Class() {
		return radioBtn11Class;
	}

	public void setRadioBtn11Class(String radioBtn11Class) {
		this.radioBtn11Class = radioBtn11Class;
	}

	public String getRadioBtn12Class() {
		return radioBtn12Class;
	}

	public void setRadioBtn12Class(String radioBtn12Class) {
		this.radioBtn12Class = radioBtn12Class;
	}

	public int getIsValid1() {
		return isValid1;
	}

	public void setIsValid1(int isValid1) {
		this.isValid1 = isValid1;
	}

	public int getIsValid2() {
		return isValid2;
	}

	public void setIsValid2(int isValid2) {
		this.isValid2 = isValid2;
	}

	public int getIsValid3() {
		return isValid3;
	}

	public void setIsValid3(int isValid3) {
		this.isValid3 = isValid3;
	}

	public int getIsValid4() {
		return isValid4;
	}

	public void setIsValid4(int isValid4) {
		this.isValid4 = isValid4;
	}

	public int getIsValid5() {
		return isValid5;
	}

	public void setIsValid5(int isValid5) {
		this.isValid5 = isValid5;
	}

	public int getIsValid6() {
		return isValid6;
	}

	public void setIsValid6(int isValid6) {
		this.isValid6 = isValid6;
	}

	public int getIsValid7() {
		return isValid7;
	}

	public void setIsValid7(int isValid7) {
		this.isValid7 = isValid7;
	}

	public int getIsValid8() {
		return isValid8;
	}

	public void setIsValid8(int isValid8) {
		this.isValid8 = isValid8;
	}

	public int getIsValid9() {
		return isValid9;
	}

	public void setIsValid9(int isValid9) {
		this.isValid9 = isValid9;
	}

	public int getIsValid10() {
		return isValid10;
	}

	public void setIsValid10(int isValid10) {
		this.isValid10 = isValid10;
	}

	public int getIsValid11() {
		return isValid11;
	}

	public void setIsValid11(int isValid11) {
		this.isValid11 = isValid11;
	}

	public int getIsValid12() {
		return isValid12;
	}

	public void setIsValid12(int isValid12) {
		this.isValid12 = isValid12;
	}

	public String getValid1Class() {
		return valid1Class;
	}

	public void setValid1Class(String valid1Class) {
		this.valid1Class = valid1Class;
	}

	public String getValid2Class() {
		return valid2Class;
	}

	public void setValid2Class(String valid2Class) {
		this.valid2Class = valid2Class;
	}

	public String getValid3Class() {
		return valid3Class;
	}

	public void setValid3Class(String valid3Class) {
		this.valid3Class = valid3Class;
	}

	public String getValid4Class() {
		return valid4Class;
	}

	public void setValid4Class(String valid4Class) {
		this.valid4Class = valid4Class;
	}

	public String getValid5Class() {
		return valid5Class;
	}

	public void setValid5Class(String valid5Class) {
		this.valid5Class = valid5Class;
	}

	public String getValid6Class() {
		return valid6Class;
	}

	public void setValid6Class(String valid6Class) {
		this.valid6Class = valid6Class;
	}

	public String getValid7Class() {
		return valid7Class;
	}

	public void setValid7Class(String valid7Class) {
		this.valid7Class = valid7Class;
	}

	public String getValid8Class() {
		return valid8Class;
	}

	public void setValid8Class(String valid8Class) {
		this.valid8Class = valid8Class;
	}

	public String getValid9Class() {
		return valid9Class;
	}

	public void setValid9Class(String valid9Class) {
		this.valid9Class = valid9Class;
	}

	public String getValid10Class() {
		return valid10Class;
	}

	public void setValid10Class(String valid10Class) {
		this.valid10Class = valid10Class;
	}

	public String getValid11Class() {
		return valid11Class;
	}

	public void setValid11Class(String valid11Class) {
		this.valid11Class = valid11Class;
	}

	public String getValid12Class() {
		return valid12Class;
	}

	public void setValid12Class(String valid12Class) {
		this.valid12Class = valid12Class;
	}

	public String getBtn1Class() {
		return btn1Class;
	}

	public void setBtn1Class(String btn1Class) {
		this.btn1Class = btn1Class;
	}

	public String getBtn2Class() {
		return btn2Class;
	}

	public void setBtn2Class(String btn2Class) {
		this.btn2Class = btn2Class;
	}

	public String getBtn3Class() {
		return btn3Class;
	}

	public void setBtn3Class(String btn3Class) {
		this.btn3Class = btn3Class;
	}

	public String getBtn4Class() {
		return btn4Class;
	}

	public void setBtn4Class(String btn4Class) {
		this.btn4Class = btn4Class;
	}

	public String getBtn5Class() {
		return btn5Class;
	}

	public void setBtn5Class(String btn5Class) {
		this.btn5Class = btn5Class;
	}

	public String getBtn6Class() {
		return btn6Class;
	}

	public void setBtn6Class(String btn6Class) {
		this.btn6Class = btn6Class;
	}

	public String getBtn7Class() {
		return btn7Class;
	}

	public void setBtn7Class(String btn7Class) {
		this.btn7Class = btn7Class;
	}

	public String getBtn8Class() {
		return btn8Class;
	}

	public void setBtn8Class(String btn8Class) {
		this.btn8Class = btn8Class;
	}

	public String getBtn9Class() {
		return btn9Class;
	}

	public void setBtn9Class(String btn9Class) {
		this.btn9Class = btn9Class;
	}

	public String getBtn10Class() {
		return btn10Class;
	}

	public void setBtn10Class(String btn10Class) {
		this.btn10Class = btn10Class;
	}

	public String getBtn11Class() {
		return btn11Class;
	}

	public void setBtn11Class(String btn11Class) {
		this.btn11Class = btn11Class;
	}

	public String getBtn12Class() {
		return btn12Class;
	}

	public void setBtn12Class(String btn12Class) {
		this.btn12Class = btn12Class;
	}

	public String getBtn13Class() {
		return btn13Class;
	}

	public void setBtn13Class(String btn13Class) {
		this.btn13Class = btn13Class;
	}

	public String getBtn14Class() {
		return btn14Class;
	}

	public void setBtn14Class(String btn14Class) {
		this.btn14Class = btn14Class;
	}

	public String getBtn15Class() {
		return btn15Class;
	}

	public void setBtn15Class(String btn15Class) {
		this.btn15Class = btn15Class;
	}

	public String getBtn16Class() {
		return btn16Class;
	}

	public void setBtn16Class(String btn16Class) {
		this.btn16Class = btn16Class;
	}

	public String getBtn17Class() {
		return btn17Class;
	}

	public void setBtn17Class(String btn17Class) {
		this.btn17Class = btn17Class;
	}

	public String getBtn18Class() {
		return btn18Class;
	}

	public void setBtn18Class(String btn18Class) {
		this.btn18Class = btn18Class;
	}

	public String getBtn19Class() {
		return btn19Class;
	}

	public void setBtn19Class(String btn19Class) {
		this.btn19Class = btn19Class;
	}

	public String getBtn20Class() {
		return btn20Class;
	}

	public void setBtn20Class(String btn20Class) {
		this.btn20Class = btn20Class;
	}

	public String getBtn21Class() {
		return btn21Class;
	}

	public void setBtn21Class(String btn21Class) {
		this.btn21Class = btn21Class;
	}

	public String getBtn22Class() {
		return btn22Class;
	}

	public void setBtn22Class(String btn22Class) {
		this.btn22Class = btn22Class;
	}

	public String getBtn23Class() {
		return btn23Class;
	}

	public void setBtn23Class(String btn23Class) {
		this.btn23Class = btn23Class;
	}

	public String getBtn24Class() {
		return btn24Class;
	}

	public void setBtn24Class(String btn24Class) {
		this.btn24Class = btn24Class;
	}

	public String getBtn25Class() {
		return btn25Class;
	}

	public void setBtn25Class(String btn25Class) {
		this.btn25Class = btn25Class;
	}

	public String getBtn26Class() {
		return btn26Class;
	}

	public void setBtn26Class(String btn26Class) {
		this.btn26Class = btn26Class;
	}

	public String getBtn27Class() {
		return btn27Class;
	}

	public void setBtn27Class(String btn27Class) {
		this.btn27Class = btn27Class;
	}

	public String getBtn28Class() {
		return btn28Class;
	}

	public void setBtn28Class(String btn28Class) {
		this.btn28Class = btn28Class;
	}

	public String getBtn29Class() {
		return btn29Class;
	}

	public void setBtn29Class(String btn29Class) {
		this.btn29Class = btn29Class;
	}

	public String getBtn30Class() {
		return btn30Class;
	}

	public void setBtn30Class(String btn30Class) {
		this.btn30Class = btn30Class;
	}

	public String getBtn31Class() {
		return btn31Class;
	}

	public void setBtn31Class(String btn31Class) {
		this.btn31Class = btn31Class;
	}

	public String getBtn32Class() {
		return btn32Class;
	}

	public void setBtn32Class(String btn32Class) {
		this.btn32Class = btn32Class;
	}

	public String getBtn33Class() {
		return btn33Class;
	}

	public void setBtn33Class(String btn33Class) {
		this.btn33Class = btn33Class;
	}

	public String getBtn34Class() {
		return btn34Class;
	}

	public void setBtn34Class(String btn34Class) {
		this.btn34Class = btn34Class;
	}

	public String getBtn35Class() {
		return btn35Class;
	}

	public void setBtn35Class(String btn35Class) {
		this.btn35Class = btn35Class;
	}

	public String getBtn36Class() {
		return btn36Class;
	}

	public void setBtn36Class(String btn36Class) {
		this.btn36Class = btn36Class;
	}

	public String getBtn37Class() {
		return btn37Class;
	}

	public void setBtn37Class(String btn37Class) {
		this.btn37Class = btn37Class;
	}

	public String getBtn38Class() {
		return btn38Class;
	}

	public void setBtn38Class(String btn38Class) {
		this.btn38Class = btn38Class;
	}

	public String getBtn39Class() {
		return btn39Class;
	}

	public void setBtn39Class(String btn39Class) {
		this.btn39Class = btn39Class;
	}

	public String getBtn40Class() {
		return btn40Class;
	}

	public void setBtn40Class(String btn40Class) {
		this.btn40Class = btn40Class;
	}

	public String getBtn41Class() {
		return btn41Class;
	}

	public void setBtn41Class(String btn41Class) {
		this.btn41Class = btn41Class;
	}

	public String getBtn42Class() {
		return btn42Class;
	}

	public void setBtn42Class(String btn42Class) {
		this.btn42Class = btn42Class;
	}

	public String getBtn43Class() {
		return btn43Class;
	}

	public void setBtn43Class(String btn43Class) {
		this.btn43Class = btn43Class;
	}

	public String getBtn44Class() {
		return btn44Class;
	}

	public void setBtn44Class(String btn44Class) {
		this.btn44Class = btn44Class;
	}

	public String getBtn45Class() {
		return btn45Class;
	}

	public void setBtn45Class(String btn45Class) {
		this.btn45Class = btn45Class;
	}

	public String getBtn46Class() {
		return btn46Class;
	}

	public void setBtn46Class(String btn46Class) {
		this.btn46Class = btn46Class;
	}

	public String getBtn47Class() {
		return btn47Class;
	}

	public void setBtn47Class(String btn47Class) {
		this.btn47Class = btn47Class;
	}

	public String getBtn48Class() {
		return btn48Class;
	}

	public void setBtn48Class(String btn48Class) {
		this.btn48Class = btn48Class;
	}

	public String getBtn49Class() {
		return btn49Class;
	}

	public void setBtn49Class(String btn49Class) {
		this.btn49Class = btn49Class;
	}

	public int[] getBtn() {
		return btn;
	}

	public void setBtn(int[] btn) {
		this.btn = btn;
	}

	public int getTipps() {
		return tipps;
	}

	public void setTipps(int tipps) {
		this.tipps = tipps;
	}

}