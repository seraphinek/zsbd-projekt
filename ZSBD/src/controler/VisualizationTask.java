package controler;

import model.CompanyName;
import model.ExecutionContext;
import view.CandleStickChartView;

import com.espertech.esper.client.EPStatement;

public class VisualizationTask {

	private final CandleStickChartView chartView;

	public VisualizationTask(CompanyName companyName) {
		chartView = new CandleStickChartView(companyName.name());
		EPStatement statement = prepareStatement(companyName);
		statement.addListener(chartView);
	}

	public EPStatement prepareStatement(CompanyName companyName) {
		String query = "select istream * from model.StockPrice(company=\""
				+ companyName.name() + "\").win:length(3) ";
		System.out.println(query);
		return ExecutionContext.getInstance().getAdministator()
				.createEPL(query);
	}
}
