package it.unipi.lsmsdb.stocksim.client.charting;

import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.chart.ui.UIUtils;

import javax.swing.*;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

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

	// JPanel for the chart
	protected abstract JPanel createPanel();
	
	/**
	 * Spawn new window and show the chart.
	 */
	public void showChart() {
		applicationFrame.setSize(800, 600);
		applicationFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		UIUtils.centerFrameOnScreen(applicationFrame);
		applicationFrame.setContentPane(createPanel());
		applicationFrame.setVisible(true);
	}
}
