package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import utils.ReverseLineReader;

import com.espertech.esper.client.EPServiceProvider;

public class DataStreamer {

	private static final String START_DATE = "1990-09-05";
	private static final String END_DATE = "2013-09-20";

	private static final int FILE_COUNT = 12;
	private static final int MAX_ERRORS = 5;
	private static FileInfo fileInfos[] = new FileInfo[FILE_COUNT];
	private static DateFormat dateFormat = null;

	public void streamData(EPServiceProvider serviceProvider)
			throws IOException {
		Date startDate = null;
		Date endDate = null;
		int errorCount = 0;
		String[] splitResult = null;

		fileInfos[0] = new FileInfo("files/tableAPPLE_NASDAQ.csv", "Apple",
				"NASDAQ");
		fileInfos[1] = new FileInfo("files/tableCOCACOLA_NYSE.csv", "CocaCola",
				"NYSE");
		fileInfos[2] = new FileInfo("files/tableDISNEY_NYSE.csv", "Disney",
				"NYSE");
		fileInfos[3] = new FileInfo("files/tableFORD_NYSE.csv", "Ford", "NYSE");
		fileInfos[4] = new FileInfo("files/tableGOOGLE_NASDAQ.csv", "Google",
				"NASDAQ");
		fileInfos[5] = new FileInfo("files/tableHONDA_NYSE.csv", "Honda",
				"NYSE");
		fileInfos[6] = new FileInfo("files/tableIBM_NASDAQ.csv", "IBM",
				"NASDAQ");
		fileInfos[7] = new FileInfo("files/tableINTEL_NASDAQ.csv", "Intel",
				"NASDAQ");
		fileInfos[8] = new FileInfo("files/tableMICROSOFT_NASDAQ.csv",
				"Microsoft", "NASDAQ");
		fileInfos[9] = new FileInfo("files/tableORACLE_NASDAQ.csv", "Oracle",
				"NASDAQ");
		fileInfos[10] = new FileInfo("files/tablePEPSICO_NYSE.csv", "PepsiCo",
				"NYSE");
		fileInfos[11] = new FileInfo("files/tableYAHOO_NASDAQ.csv", "Yahoo",
				"NASDAQ");

		ReverseLineReader readers[] = new ReverseLineReader[FILE_COUNT];

		try {
			for (int i = 0; i < FILE_COUNT; i++) {
				readers[i] = new ReverseLineReader(new File(
						fileInfos[i].getNazwaPliku()), "UTF-8");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("Nie odnaleziono pliku!");
			System.exit(1);
		}

		try {
			dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			startDate = dateFormat.parse(START_DATE);
			endDate = dateFormat.parse(END_DATE);

		} catch (ParseException e) {
			e.printStackTrace();
			System.err
					.println("Nie uda³o siê wczytaæ podanych dat rozpoczêcia i zakoñczenia!");
			System.exit(1);
		}

		String linie[] = new String[FILE_COUNT];

		// Przesuniêcie do pierwszych notowañ z zakresu dat
		for (int i = 0; i < FILE_COUNT; i++) {
			while ((linie[i] = readers[i].readLine()) != null) {
				splitResult = linie[i].split(",");
				Date stockDate = null;
				try {
					stockDate = dateFormat.parse(splitResult[0]);
					if (stockDate.compareTo(startDate) >= 0) {
						break;
					}
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
		}

		Date dateIterator = startDate;
		StockPrice stockPrice;
		// G³ówna pêtla
		while ((dateIterator.compareTo(endDate) <= 0)
				&& (errorCount < MAX_ERRORS)) {

			for (int i = 0; i < FILE_COUNT; i++) {
				try {
					Date stockDate = extractDate(linie[i]);

					if (stockDate == null) {
						continue;
					}

					if ((stockDate.compareTo(dateIterator) == -1)) {
						// Data ostatnio wczytanego notowania wczeœniejsza ni¿
						// bie¿¹ca data - pobierz kolejne notowanie!
						if ((linie[i] = readers[i].readLine()) != null) {
							stockDate = extractDate(linie[i]);
						}
					} else if ((stockDate.compareTo(dateIterator) == 1)) {
						// Data ostatnio wczytanego notowania póŸniejsza ni¿
						// bie¿¹ca data - czekaj!
						continue;
					}

					if ((stockDate != null) && (stockDate.equals(dateIterator))) {
						// Tworzenie obiektu notowania
						splitResult = linie[i].split(",");
						stockPrice = new StockPrice(
								fileInfos[i].getNazwaSpolki(),
								fileInfos[i].getNazwaMarketu(), stockDate,
								Double.valueOf(splitResult[1].trim()),
								Double.valueOf(splitResult[2].trim()),
								Double.valueOf(splitResult[3].trim()),
								Double.valueOf(splitResult[4].trim()),
								Double.valueOf(splitResult[5].trim()));
						// System.out.println(stockPrice.toString());
						Thread.sleep(100);
						serviceProvider.getEPRuntime().sendEvent(stockPrice);
					}
				} catch (Exception e) {
					e.printStackTrace();
					errorCount++;
					System.err.println("B³¹d parsowania! [" + linie[i]
							+ "]. Po raz: " + errorCount);

					if (errorCount >= MAX_ERRORS) {
						System.err.println("Za du¿o b³êdów!");
						break;
					}
				}
			}

			// Inkrementacja daty
			dateIterator = incrementDate(dateIterator);
		}
	}

	// Metody pomocnicze
	private static Date incrementDate(Date data) {
		Calendar c = Calendar.getInstance();
		c.setTime(data);
		c.add(Calendar.DATE, 1);
		return c.getTime();
	}

	private static Date extractDate(String linia) {
		String[] splitResult = linia.split(",");
		if (!splitResult[0].equals("Date")) {
			try {
				return dateFormat.parse(splitResult[0]);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
