/**
 * 
 */
package org.jboss.tools.smooks.gef.tree.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.smooks.configuration.editors.GraphicsConstants;
import org.jboss.tools.smooks.gef.tree.editpolicy.IShowHighlighFigure;
import org.jboss.tools.smooks.gef.tree.model.TreeContainerModel;

/**
 * @author DartPeng
 * 
 */
public class TreeContainerFigure extends Figure implements ISelectableFigure, IShowHighlighFigure {

	private IFigure headerFigure;

	private IFigure contentFigure;

	private Label label;

	private TreeContainerModel model;

	private Color headerColor = ColorConstants.button;

	protected Color headerSelectedColor = GraphicsConstants.TB_BG_CORLOR;

	private boolean focus;

	private boolean selected;

	private Color highlightColor = null;

	private boolean showHightlight = false;

	public TreeContainerFigure(TreeContainerModel model) {
		super();
		this.model = model;
		this.addChildrenFigures();
	}

	protected void addChildrenFigures() {
		headerFigure = new Figure() {

			@Override
			protected void paintFigure(Graphics graphics) {
				super.paintFigure(graphics);
				drawHeaderFigure(graphics);
			}

			@Override
			public Dimension getPreferredSize(int hint, int hint2) {
				Dimension size = super.getPreferredSize(hint, hint2);
				int width = Math.max(size.width, 100);
				return new Dimension(width, 25);
			}
		};
		label = new Label();
		headerFigure.add(label);
		ToolbarLayout layout = new ToolbarLayout();
		layout.setMinorAlignment(ToolbarLayout.ALIGN_CENTER);
		headerFigure.setLayoutManager(layout);
		headerFigure.setOpaque(true);

		contentFigure = new Figure();
		contentFigure.setLayoutManager(new ToolbarLayout());
		this.add(headerFigure);
		this.add(contentFigure);
		ToolbarLayout tl = new ToolbarLayout();
		tl.setMinorAlignment(ToolbarLayout.ALIGN_CENTER);
		this.setLayoutManager(tl);
	}

	protected void drawHeaderFigure(Graphics graphics) {
		try {
			graphics.pushState();
			Color currentColor = headerColor;
			if (isSelected() || isFocus()) {
				currentColor = headerSelectedColor;
			}
			if (showHightlight && highlightColor != null) {
				currentColor = highlightColor;
			}
			graphics.setForegroundColor(currentColor);
			graphics.setBackgroundColor(ColorConstants.white);
			graphics.fillGradient(headerFigure.getBounds().expand(30, 0), true);
			graphics.setForegroundColor(currentColor);
			graphics.drawLine(getBounds().getBottomLeft().translate(0, -1), getBounds().getBottomRight().translate(0,
					-1));
			graphics.popState();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the label
	 */
	public Label getLabel() {
		return label;
	}

	/**
	 * @param label
	 *            the label to set
	 */
	public void setLabel(Label label) {
		this.label = label;
	}

	public IFigure getContentFigure() {
		return contentFigure;
	}

	public Color getHeaderColor() {
		return headerColor;
	}

	public void setHeaderColor(Color headerColor) {
		this.headerColor = headerColor;
	}

	public Rectangle getBounds() {
		Rectangle rect = super.getBounds();
		if (getLayoutManager() != null) {
			Dimension d = getLayoutManager().getPreferredSize(this, -1, -1);
			rect.setSize(d);
		}
		return rect;
	}

	@Override
	protected void paintBorder(Graphics graphics) {
		try {
			graphics.setForegroundColor(ColorConstants.buttonDarkest);
			if (isSelected() || isFocus()) {
				graphics.setForegroundColor(GraphicsConstants.BORDER_CORLOR);
			}

			if (showHightlight && highlightColor != null) {
				graphics.setForegroundColor(FigureUtilities.darker(highlightColor));
			}
			Rectangle drawnRectangle = new Rectangle(getBounds().x, getBounds().y, getBounds().width - 1,
					getBounds().height - 1);
			graphics.drawRoundRectangle(drawnRectangle, 5, 5);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setText(String text) {
		if (label != null) {
			label.setText(text);
		}
	}

	public TreeContainerModel getModel() {
		return model;
	}

	public void setModel(TreeContainerModel model) {
		this.model = model;
	}

	@Override
	protected void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);
		graphics.pushState();
		graphics.setAlpha(190);
		graphics.setBackgroundColor(ColorConstants.white);
		graphics.fillRectangle(getBounds());
		graphics.popState();
	}

	public void setIcon(Image i) {
		if (label != null) {
			label.setIcon(i);
		}
	}

	/**
	 * @return the headerFigure
	 */
	public IFigure getHeaderFigure() {
		return headerFigure;
	}

	/**
	 * @param headerFigure
	 *            the headerFigure to set
	 */
	public void setHeaderFigure(IFigure headerFigure) {
		this.headerFigure = headerFigure;
	}

	/**
	 * @return the focus
	 */
	public boolean isFocus() {
		return focus;
	}

	/**
	 * @param focus
	 *            the focus to set
	 */
	public void setFocus(boolean focus) {
		this.focus = focus;
	}

	/**
	 * @return the selected
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * @param selected
	 *            the selected to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
		repaint();
	}

	/**
	 * @param contentFigure
	 *            the contentFigure to set
	 */
	public void setContentFigure(IFigure contentFigure) {
		this.contentFigure = contentFigure;
		repaint();
	}

	public void showbackOldbackgroundColor(Color color) {
		showHightlight = false;
		highlightColor = null;
		repaint();
	}

	public void showHighlightBackgroudColor(Color color) {
		showHightlight = true;
		highlightColor = color;
		repaint();
	}

}
