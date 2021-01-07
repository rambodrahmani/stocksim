package it.unipi.lsmsdb.stocksim.client.charting;

import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.chart.ui.UIUtils;
import org.jfree.data.general.AbstractDataset;

import javax.swing.*;

/**
 * Abstract Chart class. All charting classes must extend this.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public abstract class Chart {
	// title of the chart window
	protected String title;

	// application frame where to show the chart
	protected ApplicationFrame applicationFrame;


	protected abstract JPanel createPanel();
	
	/**
	 * Spawn new window and show the chart.
	 */
	public void showChart() {
		applicationFrame.setSize(600, 400);
		UIUtils.centerFrameOnScreen(applicationFrame);
		applicationFrame.setContentPane(createPanel());
		applicationFrame.setVisible(true);
	}
}
