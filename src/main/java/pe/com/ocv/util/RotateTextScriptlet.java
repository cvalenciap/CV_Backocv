package pe.com.ocv.util;

import java.util.List;
import java.awt.FontMetrics;
import java.util.ArrayList;
import java.awt.RenderingHints;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.awt.Color;
import java.awt.Font;
import net.sf.jasperreports.engine.JRDefaultScriptlet;

public class RotateTextScriptlet extends JRDefaultScriptlet {
	public RotateTextScriptlet() {
		super();
	}

	public Image rotateText(String text, Font font, int width, int height, int textAngle, Color textColor) {
		if (text == null) {
			throw new IllegalArgumentException("text must be not-null ");
		}
		if (text.length() == 0) {
			throw new IllegalArgumentException("text is empty string");
		}
		if (text.trim().length() == 0) {
			throw new IllegalArgumentException("text must contain at least one character that is not space");
		}
		BufferedImage image = new BufferedImage(width, height, 2);
		for (int i = image.getWidth() - 1; i > -1; --i) {
			for (int j = image.getHeight() - 1; j > -1; --j) {
				if (image.getRGB(i, j) == new Color(220, 220, 220).getRGB()) {
					image.setRGB(i, j, new Color(0, 0, 0, 0).getRGB());
				}
			}
		}
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
		g.setFont(font);
		FontMetrics fm = g.getFontMetrics();
		double maxLineWidth = width;
		double completeWidth = fm.stringWidth(text);
		double caLinesCountD = completeWidth / maxLineWidth;
		int caLinesCount = -1;
		if (caLinesCountD % 1.0 > 0.0) {
			caLinesCount = (int) caLinesCountD + 1;
		} else {
			caLinesCount = (int) caLinesCountD;
		}
		List<String> lines = new ArrayList<String>(caLinesCount);
		if (caLinesCount == 1) {
			lines.add(text);
		} else {
			int caSepIdx = text.length() / caLinesCount;
			int k = 0;
			while (k < text.length()) {
				int nextCaSep = k + caSepIdx;
				if (text.length() <= nextCaSep) {
					lines.add(text.substring(k));
					break;
				}
				int sepIdx = text.substring(k, nextCaSep + 1).lastIndexOf(32);
				if (sepIdx == 0) {
					++k;
				} else if (sepIdx == -1) {
					lines.add(text.substring(k, nextCaSep));
					k += nextCaSep;
				} else {
					lines.add(text.substring(k, k + sepIdx));
					k += sepIdx + 1;
				}
			}
		}
		g.rotate(0.017453292519943295 * textAngle, width / 2, height / 2);
		int textHeight = fm.getMaxAscent();
		int lineY = height / 2;
		int halfLines = lines.size() / 2;
		double shiftLines = (lines.size() % 2 == 0) ? (halfLines - 1) : (halfLines - 0.5);
		shiftLines += 0.2;
		lineY -= (int) (textHeight * shiftLines);
		for (int lineIdx = 0; lineIdx < lines.size(); ++lineIdx) {
			String line = lines.get(lineIdx);
			int lineWidth = fm.stringWidth(line);
			int lineX = width / 2 - lineWidth / 2;
			g.setColor(textColor);
			g.drawChars(line.toCharArray(), 0, line.length(), lineX, lineY);
			lineY += textHeight;
		}
		return image;
	}
}