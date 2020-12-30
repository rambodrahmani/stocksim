package it.unipi.lsmsdb.stocksim.client.charting;

import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.chart.ui.UIUtils;
import org.jfree.data.general.AbstractDataset;

import javax.swing.*;

public abstract class Chart {
	
	// title of the chart
	protected String title;
	protected ApplicationFrame applicationFrame;
	protected abstract AbstractDataset createDataset();
	protected abstract JPanel createPanel();
	
	/**
	 * Spawn new window
	 */
	public void showChart(){
		applicationFrame.setSize(600, 400);
		UIUtils.centerFrameOnScreen(applicationFrame);
		applicationFrame.setContentPane(createPanel());
		applicationFrame.setVisible(true);
	}
}
