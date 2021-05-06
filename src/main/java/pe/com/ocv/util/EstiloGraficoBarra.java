package pe.com.ocv.util;

import org.jfree.data.category.CategoryDataset;
import java.awt.Color;
import org.jfree.chart.renderer.category.BarRenderer;
import net.sf.jasperreports.engine.JRChart;
import org.jfree.chart.JFreeChart;
import net.sf.jasperreports.engine.JRChartCustomizer;

public class EstiloGraficoBarra implements JRChartCustomizer {
	public EstiloGraficoBarra() {
		super();
	}

	@Override
	public void customize(JFreeChart chart, JRChart jasperChart) {
		BarRenderer br = (BarRenderer) chart.getCategoryPlot().getRenderer();
		br.setMaximumBarWidth(0.35);
		CategoryDataset catDS = chart.getCategoryPlot().getDataset();
		for (int i = 0; i < catDS.getRowCount(); ++i) {
			Color seriesColor;
			if (i == catDS.getRowCount() - 1) {
				seriesColor = Color.black;
			} else {
				seriesColor = Color.gray;
			}
			br.setSeriesPaint(catDS.getRowIndex(catDS.getRowKey(i)), seriesColor);
		}
	}
}