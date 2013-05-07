package main;

import java.io.IOException;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import model.ExecutionContext;
import model.DataStreamer;
import controler.MainWindowController;

public class Main {

	public static void main(String[] args) throws IOException {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

					@Override
					protected Void doInBackground() throws Exception {

						DataStreamer inputStream = new DataStreamer();
						try {
							inputStream.streamData(ExecutionContext
									.getInstance().getServiceProvider());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return null;
					};

				};
				worker.execute();

				new MainWindowController();
			};
		});

	}

}
