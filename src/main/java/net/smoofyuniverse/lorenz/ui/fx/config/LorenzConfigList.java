/*
 * Copyright (c) 2019 Hugo Dupanloup (Yeregorix)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.smoofyuniverse.lorenz.ui.fx.config;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import net.smoofyuniverse.common.fxui.field.DoubleField;
import net.smoofyuniverse.common.fxui.field.IntegerField;
import net.smoofyuniverse.common.util.GridUtil;
import net.smoofyuniverse.lorenz.math.Function;

public final class LorenzConfigList extends ListView<LorenzConfig> {

	public LorenzConfigList() {
		setCellFactory(l -> new LorenzConfigCell());
	}

	private class LorenzConfigCell extends ListCell<LorenzConfig> {
		private final ColorPicker color = new ColorPicker();
		private final DoubleField sigma = new DoubleField(-1000, 1000, Function.DEFAULT_SIGMA), rho = new DoubleField(-1000, 1000, Function.DEFAULT_RHO), beta = new DoubleField(-1000, 1000, Function.DEFAULT_BETA),
				x0 = new DoubleField(-1000, 1000, 0), y0 = new DoubleField(-1000, 1000, 0), z0 = new DoubleField(-1000, 1000, 0), h = new DoubleField(0, 10, 0.001);
		private final IntegerField points = new IntegerField(0, 10_000_000), speed = new IntegerField(1, 10000);

		private final GridPane pane = new GridPane();
		private LorenzConfig currentConfig;

		public LorenzConfigCell() {
			setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

			Button remove = new Button("Retirer");
			remove.setOnAction(e -> getItems().remove(getIndex()));

			remove.setMaxWidth(Double.MAX_VALUE);
			remove.setPrefWidth(100);
			this.color.setMaxWidth(Double.MAX_VALUE);
			this.color.setPrefWidth(100);

			this.pane.add(remove, 0, 0, 2, 1);
			this.pane.add(this.color, 2, 0);

			this.pane.add(new Label("Pas:"), 0, 1);
			this.pane.add(this.h, 1, 1, 2, 1);
			this.pane.add(new Label("Points:"), 3, 1);
			this.pane.add(this.points, 4, 1);
			this.pane.add(new Label("Vitesse:"), 5, 1);
			this.pane.add(this.speed, 6, 1);

			this.pane.add(new Label("σ:"), 0, 2);
			this.pane.add(this.sigma, 1, 2, 2, 1);
			this.pane.add(new Label("ρ:"), 3, 2);
			this.pane.add(this.rho, 4, 2);
			this.pane.add(new Label("β:"), 5, 2);
			this.pane.add(this.beta, 6, 2);

			this.pane.add(new Label("x0"), 0, 3);
			this.pane.add(this.x0, 1, 3, 2, 1);
			this.pane.add(new Label("y0:"), 3, 3);
			this.pane.add(this.y0, 4, 3);
			this.pane.add(new Label("z0:"), 5, 3);
			this.pane.add(this.z0, 6, 3);

			this.pane.setVgap(5);
			this.pane.setHgap(5);

			this.pane.getColumnConstraints().addAll(GridUtil.createColumn(10), GridUtil.createColumn(10), GridUtil.createColumn(20), GridUtil.createColumn(10), GridUtil.createColumn(30), GridUtil.createColumn(10), GridUtil.createColumn(30));
			this.pane.getRowConstraints().addAll(GridUtil.createRow(), GridUtil.createRow(), GridUtil.createRow(), GridUtil.createRow());
		}

		@Override
		public void updateIndex(int index) {
			super.updateIndex(index);
			unbindContent();
			setGraphic(index == -1 || isEmpty() ? null : updateContent());
		}

		private void unbindContent() {
			if (this.currentConfig != null) {
				this.color.valueProperty().unbindBidirectional(this.currentConfig.color);
				this.sigma.valueProperty().unbindBidirectional(this.currentConfig.sigma);
				this.rho.valueProperty().unbindBidirectional(this.currentConfig.rho);
				this.beta.valueProperty().unbindBidirectional(this.currentConfig.beta);
				this.x0.valueProperty().unbindBidirectional(this.currentConfig.x0);
				this.y0.valueProperty().unbindBidirectional(this.currentConfig.y0);
				this.z0.valueProperty().unbindBidirectional(this.currentConfig.z0);
				this.h.valueProperty().unbindBidirectional(this.currentConfig.h);
				this.points.valueProperty().unbindBidirectional(this.currentConfig.points);
				this.speed.valueProperty().unbindBidirectional(this.currentConfig.speed);
				this.currentConfig = null;
			}
		}

		private Node updateContent() {
			LorenzConfig item = getItem();
			this.color.valueProperty().bindBidirectional(item.color);
			this.sigma.valueProperty().bindBidirectional(item.sigma);
			this.rho.valueProperty().bindBidirectional(item.rho);
			this.beta.valueProperty().bindBidirectional(item.beta);
			this.x0.valueProperty().bindBidirectional(item.x0);
			this.y0.valueProperty().bindBidirectional(item.y0);
			this.z0.valueProperty().bindBidirectional(item.z0);
			this.h.valueProperty().bindBidirectional(item.h);
			this.points.valueProperty().bindBidirectional(item.points);
			this.speed.valueProperty().bindBidirectional(item.speed);
			this.currentConfig = item;
			return this.pane;
		}

		@Override
		protected void updateItem(LorenzConfig item, boolean empty) {
			super.updateItem(item, empty);
			unbindContent();
			setGraphic(getIndex() == -1 || empty ? null : updateContent());
		}
	}
}
