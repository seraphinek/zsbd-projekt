package model;

public class TrendAnalyzer {
	public static TrendInformation predictTrend(StockPrice firstCandle,
			StockPrice secondCandle) {

		if (Math.abs(secondCandle.getClose() - secondCandle.getOpen()) < 0.125) {
			if (secondCandle.isBlack()) {
				return new TrendInformation(TrendFormation.DojiHossa,
						secondCandle.getDate());
			} else {
				return new TrendInformation(TrendFormation.DojiBessa,
						secondCandle.getDate());
			}
		}

		if (firstCandle.isBlack() && !secondCandle.isBlack()) {
			return bullishPrediction(firstCandle, secondCandle);
		}

		if (!firstCandle.isBlack() && secondCandle.isBlack()) {
			return bearishPrediction(firstCandle, secondCandle);
		}

		return new TrendInformation(TrendFormation.Random,
				secondCandle.getDate());
	}

	private static TrendInformation bearishPrediction(StockPrice firstCandle,
			StockPrice secondCandle) {

		if (firstCandle.getClose() < secondCandle.getOpen()
				&& firstCandle.getOpen() > secondCandle.getClose()) {
			return new TrendInformation(TrendFormation.EngulfingBessa,
					secondCandle.getDate());
		}

		if (firstCandle.getClose() > secondCandle.getOpen()
				&& firstCandle.getOpen() < secondCandle.getClose()) {
			return new TrendInformation(TrendFormation.HaramiBessa,
					secondCandle.getDate());
		}

		if (firstCandle.getClose() > secondCandle.getClose()
				&& ((firstCandle.getOpen() + firstCandle.getClose()) / 2) < secondCandle
						.getClose()
				&& firstCandle.getClose() > secondCandle.getOpen()) {
			return new TrendInformation(TrendFormation.DarkCloudCover,
					secondCandle.getDate());
		}

		if (secondCandle.getLow() == secondCandle.getClose()
				&& secondCandle.getHigh() > secondCandle.getOpen()) {
			return new TrendInformation(TrendFormation.ShootingStar,
					secondCandle.getDate());
		}

		return new TrendInformation(TrendFormation.Random,
				secondCandle.getDate());
	}

	private static TrendInformation bullishPrediction(StockPrice firstCandle,
			StockPrice secondCandle) {
		if (firstCandle.getOpen() < secondCandle.getClose()
				&& firstCandle.getClose() > secondCandle.getOpen()) {
			return new TrendInformation(TrendFormation.EngulfingHossa,
					secondCandle.getDate());
		}

		if (firstCandle.getOpen() > secondCandle.getClose()
				&& firstCandle.getClose() < secondCandle.getOpen()) {
			return new TrendInformation(TrendFormation.HaramiHossa,
					secondCandle.getDate());
		}

		if (firstCandle.getOpen() > secondCandle.getClose()
				&& ((firstCandle.getOpen() + firstCandle.getClose()) / 2) < secondCandle
						.getClose()
				&& firstCandle.getClose() > secondCandle.getOpen()) {
			return new TrendInformation(TrendFormation.Piercing,
					secondCandle.getDate());
		}

		if (secondCandle.getHigh() == secondCandle.getClose()
				&& secondCandle.getLow() < secondCandle.getOpen()) {
			return new TrendInformation(TrendFormation.Hammer,
					secondCandle.getDate());
		}

		return new TrendInformation(TrendFormation.Random,
				secondCandle.getDate());
	}
}
