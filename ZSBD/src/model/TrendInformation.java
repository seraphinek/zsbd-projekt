package model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TrendInformation {
	private TrendFormation formation;
	private Date date;

	public TrendInformation(TrendFormation formation, Date date) {
		this.formation = formation;
		this.date = date;
	}

	@Override
	public String toString() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(date) + ": " + "Spotted " + formation.toString()
				+ ". Possible " + formation.getType().toString();
	}

	public TrendFormation getFormation() {
		return formation;
	}

	public void setFormation(TrendFormation formation) {
		this.formation = formation;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
