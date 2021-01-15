package it.unipi.lsmsdb.stocksim.client.charting;

import org.jfree.chart.ui.UIUtils;

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
	protected JFrame applicationFrame;

	// JPanel for the chart
	protected abstract JPanel createPanel();
	
	/**
	 * Spawn new window and show the chart.
	 */
	public void showChart() {
		applicationFrame.setSize(800, 600);
		applicationFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		applicationFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		UIUtils.centerFrameOnScreen(applicationFrame);
		applicationFrame.setContentPane(createPanel());
		applicationFrame.setVisible(true);
	}
}
