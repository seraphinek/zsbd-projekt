package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import model.CompanyName;
import controler.MainWindowController;

public class MainWindow extends JFrame {

	private static final int FIELD_HEIGHT = 20;
	private static final long serialVersionUID = 3172688540921699213L;

	private JButton defaultButton;
	private JButton startChartButton;
	private JButton exitButton;
	private JButton aboutButton;

	private final MainWindowController controller;

	private JComboBox<CompanyName> companyName;
	private JSpinner dateFrom;
	private JSpinner dateTo;

	public MainWindow(MainWindowController mainWindowController) {
		this.controller = mainWindowController;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Candle Stick Chart Visualizer");
	}

	public void createView() {
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setResizable(false);
		add(createTitlePanel());
		add(createParametersPanel());
		add(createButtonsPanel());
		setVisible(true);
		pack();
	}

	private JPanel createButtonsPanel() {
		JPanel buttonsPanel = new JPanel();
		defaultButton = new JButton("Default values");
		defaultButton.addActionListener(controller
				.getDefaultParametersActionListener());
		startChartButton = new JButton("Start visualization");
		startChartButton.addActionListener(controller
				.getExecuteTaskActionListener());
		aboutButton = new JButton("About...");
		aboutButton
				.addActionListener(controller.getAboutWindowActionListener());
		exitButton = new JButton("Exit");
		exitButton.addActionListener(controller
				.getCloseApplicationActionListener());
		buttonsPanel.add(defaultButton);
		buttonsPanel.add(startChartButton);
		buttonsPanel.add(exitButton);
		buttonsPanel.add(aboutButton);
		return buttonsPanel;
	}

	private JPanel createParametersPanel() {
		JPanel parametersPanel = new JPanel();
		parametersPanel.setLayout(new GridLayout(3, 2));
		prepareParametersFields(parametersPanel);
		return parametersPanel;
	}

	private void prepareParametersFields(JPanel paramsPanel) {
		createCompanyNameFields(paramsPanel);
		// createDateFromFields(paramsPanel);
		// createDateToFields(paramsPanel);
	}

	// private void createDateFromFields(JPanel paramsPanel) {
	// JLabel dateFromLabel = new JLabel("Date from:");
	// dateFromLabel.setLabelFor(dateFrom);
	// dateFromLabel.setPreferredSize(new Dimension(100, FIELD_HEIGHT));
	// paramsPanel.add(dateFromLabel);
	// dateFrom = new JSpinner(new SpinnerDateModel());
	// JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(dateFrom,
	// "yyyy-MM-dd");
	// dateFrom.setEditor(timeEditor);
	// dateFrom.setValue(new Date());
	// paramsPanel.add(dateFrom);
	// }
	//
	// private void createDateToFields(JPanel paramsPanel) {
	// JLabel dateToLabel = new JLabel("Date to:");
	// dateToLabel.setLabelFor(dateTo);
	// dateToLabel.setPreferredSize(new Dimension(100, FIELD_HEIGHT));
	// paramsPanel.add(dateToLabel);
	// dateTo = new JSpinner(new SpinnerDateModel());
	// JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(dateTo,
	// ComponentUtils.DATE_FORMAT);
	// dateTo.setEditor(timeEditor);
	// dateTo.setValue(new Date());
	// paramsPanel.add(dateTo);
	// }

	private void createCompanyNameFields(JPanel paramsPanel) {
		JPanel panel = new JPanel();
		JLabel companyNameLabel = new JLabel("Company name:");
		companyNameLabel.setLabelFor(companyName);
		companyNameLabel.setPreferredSize(new Dimension(100, FIELD_HEIGHT));
		panel.add(companyNameLabel);
		companyName = new JComboBox<CompanyName>(CompanyName.values());
		companyName.setPreferredSize(new Dimension(180, FIELD_HEIGHT));
		panel.add(companyName);
		paramsPanel.add(panel);
	}

	private JPanel createTitlePanel() {
		JPanel titlePanel = new JPanel(new BorderLayout());
		JLabel titleLabel = new JLabel("Candle Stick Chart Visualizer");
		titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 26));
		titlePanel.add(titleLabel);
		return titlePanel;
	}

	public JComboBox<CompanyName> getCompanyName() {
		return companyName;
	}

	public JSpinner getDateFrom() {
		return dateFrom;
	}

	public JSpinner getDateTo() {
		return dateTo;
	}

}
