package controler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.SwingUtilities;

import model.CompanyName;
import view.AboutWindow;
import view.MainWindow;

public class MainWindowController {
	private final MainWindow mainWindow;

	public MainWindowController() {
		this.mainWindow = new MainWindow(this);
		mainWindow.createView();
	}

	public void showAboutWindow() {
		new AboutWindow();
	}

	public void closeApplication() {
		System.exit(0);
	}

	private final ActionListener defaultParametersActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					Calendar c = Calendar.getInstance();
					c.set(2001, 8, 5);
					mainWindow.getDateFrom().setValue(c.getTime());
					c.set(2001, 8, 20);
					mainWindow.getDateTo().setValue(c.getTime());
				}
			});
		}
	};

	private final ActionListener executeTaskActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					// Date dateFrom = (Date)
					// mainWindow.getDateFrom().getValue();
					// Date dateTo = (Date) mainWindow.getDateTo().getValue();
					CompanyName companyName = (CompanyName) mainWindow
							.getCompanyName().getSelectedItem();
					new VisualizationTask(companyName);
				};

			});
		}
	};

	private final ActionListener aboutWindowActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					showAboutWindow();
				};

			});
		}
	};

	private final ActionListener closeApplicationActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					closeApplication();
				};

			});
		}
	};

	public ActionListener getExecuteTaskActionListener() {
		return executeTaskActionListener;
	}

	public ActionListener getDefaultParametersActionListener() {
		return defaultParametersActionListener;
	}

	public ActionListener getAboutWindowActionListener() {
		return aboutWindowActionListener;
	}

	public ActionListener getCloseApplicationActionListener() {
		return closeApplicationActionListener;
	}

}
