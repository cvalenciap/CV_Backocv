package pe.com.ocv.util;

import org.jfree.data.category.CategoryDataset;
import java.awt.Color;
import java.awt.Paint;
import org.jfree.chart.renderer.category.BarRenderer;

public class CustomBarRenderer extends BarRenderer {

	private static final long serialVersionUID = 1L;

	public CustomBarRenderer() {
		super();
	}

	@Override
	public Paint getItemPaint(final int row, final int column) {
		final CategoryDataset cd = this.getPlot().getDataset();
		if (cd == null) {
			return null;
		}
		if (row == cd.getRowCount() - 1) {
			return Color.BLACK;
		}
		return Color.GRAY;
	}

}