package model;

import java.text.SimpleDateFormat;
import java.util.Date;

import utils.ComponentUtils;

public class StockPrice {

	private final String company;
	private final String market;
	private final Date date;

	private final Double open;
	private final Double high;
	private final Double low;
	private final Double close;
	private final Double volume;
	private final boolean black;

	private static SimpleDateFormat dateFormat = new SimpleDateFormat(
			ComponentUtils.DATE_FORMAT);

	public StockPrice(String company, String market, Date date, Double open,
			Double high, Double low, Double close, Double volume) {
		this.company = company;
		this.market = market;
		this.date = date;
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
		this.volume = volume;
		this.black = (open > close);
	}

	@Override
	public String toString() {
		return "StockPrice [company=" + company + ",\tmarket=" + market
				+ ",\tdate=" + dateFormat.format(date) + ",\topen=" + open
				+ ",\thigh=" + high + ",\tlow=" + low + ",\tclose=" + close
				+ ",\tvolume=" + volume + ",\tisBlack=" + black + "]";
	}

	public String getCompany() {
		return company;
	}

	public String getMarket() {
		return market;
	}

	public Date getDate() {
		return date;
	}

	public Double getOpen() {
		return open;
	}

	public Double getHigh() {
		return high;
	}

	public Double getLow() {
		return low;
	}

	public Double getClose() {
		return close;
	}

	public Double getVolume() {
		return volume;
	}

	public boolean isBlack() {
		return black;
	}

}