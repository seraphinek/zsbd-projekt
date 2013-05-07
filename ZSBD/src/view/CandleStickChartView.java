package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import model.DefaultHighLowDataset;
import model.StockPrice;
import model.TrendAnalyzer;
import model.TrendFormation;
import model.TrendInformation;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.SegmentedTimeline;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.ui.ApplicationFrame;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

public class CandleStickChartView extends ApplicationFrame implements
		UpdateListener {

	private static final long serialVersionUID = 601722211608199551L;

	DefaultHighLowDataset dataset = null;

	private final ChartPanel chartPanel;

	private final JFreeChart chart;

	private StockPrice previousElement;

	private final JList<String> possibleTrendsList;

	private final DefaultListModel<String> predictedTrends;

	private final JLabel possibleTrendLabel;

	public CandleStickChartView(String title) {
		super(title);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		DateAxis domainAxis = new DateAxis("Date");
		NumberAxis rangeAxis = new NumberAxis("Price");
		CandlestickRenderer renderer = new CandlestickRenderer();
		dataset = new DefaultHighLowDataset("X");
		predictedTrends = new DefaultListModel<String>();
		XYPlot mainPlot = new XYPlot(dataset, domainAxis, rangeAxis, renderer);

		renderer.setSeriesPaint(0, Color.BLACK);
		renderer.setDrawVolume(false);
		rangeAxis.setAutoRangeIncludesZero(false);
		domainAxis.setTimeline(SegmentedTimeline
				.newMondayThroughFridayTimeline());

		chart = new JFreeChart("", null, mainPlot, false);
		chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(900, 500));
		setLayout(new BorderLayout());
		this.add(chartPanel, BorderLayout.CENTER);
		possibleTrendLabel = new JLabel("Possible trends:");
		possibleTrendsList = new JList<String>();
		possibleTrendsList.setPreferredSize(new Dimension(350, 450));
		possibleTrendsList.setBorder(BorderFactory
				.createLineBorder(Color.BLACK));
		possibleTrendsList.setModel(predictedTrends);
		JPanel trendPanel = new JPanel(new BorderLayout());
		trendPanel.add(possibleTrendLabel, BorderLayout.NORTH);
		trendPanel.add(possibleTrendsList, BorderLayout.CENTER);
		this.add(trendPanel, BorderLayout.EAST);
		this.pack();
		this.setVisible(true);
	}

	public void updateData(EventBean[] newEvents) {
		for (EventBean eventBean : newEvents) {
			StockPrice stockPrice = (StockPrice) eventBean.getUnderlying();
			System.out.println(stockPrice.toString());
			if (dataset.getItemCount(0) > 75) {
				dataset.removeFirst();
			}
			dataset.add(stockPrice.getDate(), stockPrice.getHigh(),
					stockPrice.getLow(), stockPrice.getOpen(),
					stockPrice.getClose(), stockPrice.getVolume());
			if (previousElement != null) {
				TrendInformation prediction = TrendAnalyzer.predictTrend(
						previousElement, stockPrice);
				if (!TrendFormation.Random.equals(prediction.getFormation())) {
					predictedTrends.add(0, prediction.toString());
				}
			}
			previousElement = stockPrice;
		}
	}

	@Override
	public void update(final EventBean[] newEvents, EventBean[] oldEvents) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				updateData(newEvents);
			}
		});

	}

}
