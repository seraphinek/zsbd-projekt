package model;

public class FileInfo {

	private final String fileName;
	private final String companyName;
	private final String marketName;

	public FileInfo(String fileName, String companyName, String marketName) {
		this.fileName = fileName;
		this.companyName = companyName;
		this.marketName = marketName;
	}

	public String getNazwaPliku() {
		return fileName;
	}

	public String getNazwaSpolki() {
		return companyName;
	}

	public String getNazwaMarketu() {
		return marketName;
	}

}
