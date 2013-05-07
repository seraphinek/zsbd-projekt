package model;

public enum TrendFormation {
	EngulfingHossa(TrendType.Hossa, "Bullish Engulfing"), Hammer(
			TrendType.Hossa, "Hammer"), HaramiHossa(TrendType.Hossa,
			"Bullish Harami"), Piercing(TrendType.Hossa, "Piercing"), DojiHossa(
			TrendType.Hossa, "Bullish Doji"), DojiBessa(TrendType.Hossa,
			"Bearish Doji"), EngulfingBessa(TrendType.Bessa,
			"Bearish Engulfing"), ShootingStar(TrendType.Bessa, "Shooting Star"), HaramiBessa(
			TrendType.Bessa, "Bearish Harami"), DarkCloudCover(TrendType.Bessa,
			"Dark Cloud Cover"), Random(TrendType.Continuation, "random");

	private TrendType type;
	private String name;

	TrendFormation(TrendType type, String name) {
		this.type = type;
		this.name = name;
	}

	public TrendType getType() {
		return type;
	}

	@Override
	public String toString() {
		return name;
	}

}
