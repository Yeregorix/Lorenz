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
import net.smoofyuniverse.common.fx.field.DoubleField;
import net.smoofyuniverse.common.fx.field.IntegerField;
import net.smoofyuniverse.common.util.GridUtil;
import net.smoofyuniverse.lorenz.math.Function;
import net.smoofyuniverse.lorenz.math.Series;

public final class LorenzConfigList extends ListView<LorenzConfig> {

	public LorenzConfigList() {
		setCellFactory(l -> new LorenzConfigCell());
	}

	private class LorenzConfigCell extends ListCell<LorenzConfig> {
		private final ColorPicker color = new ColorPicker();
		private final DoubleField sigma = new DoubleField(-1000, 1000, Function.DEFAULT_SIGMA), rho = new DoubleField(-1000, 1000, Function.DEFAULT_RHO), beta = new DoubleField(-1000, 1000, Function.DEFAULT_BETA),
				x0 = new DoubleField(-1000, 1000, 0), y0 = new DoubleField(-1000, 1000, 0), z0 = new DoubleField(-1000, 1000, 0), h = new DoubleField(0, 10, 0.001);
		private final IntegerField points = new IntegerField(0, 10_000_000), speed = new IntegerField(1, 10000);
		private final Button connect = new Button();

		private final GridPane pane = new GridPane();

		public LorenzConfigCell() {
			setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

			this.color.valueProperty().addListener((v, oldV, newV) -> getItem().series.setColor(newV));

			this.sigma.valueProperty().addListener((v, oldV, newV) -> getItem().sigma = newV.doubleValue());
			this.rho.valueProperty().addListener((v, oldV, newV) -> getItem().rho = newV.doubleValue());
			this.beta.valueProperty().addListener((v, oldV, newV) -> getItem().beta = newV.doubleValue());
			this.x0.valueProperty().addListener((v, oldV, newV) -> getItem().x0 = newV.doubleValue());
			this.y0.valueProperty().addListener((v, oldV, newV) -> getItem().y0 = newV.doubleValue());
			this.z0.valueProperty().addListener((v, oldV, newV) -> getItem().z0 = newV.doubleValue());
			this.h.valueProperty().addListener((v, oldV, newV) -> getItem().h = newV.doubleValue());

			this.points.valueProperty().addListener((v, oldV, newV) -> getItem().points = newV.intValue());
			this.speed.valueProperty().addListener((v, oldV, newV) -> getItem().speed = newV.intValue());

			this.connect.setOnAction(e -> {
				Series s = getItem().series;
				if (s.connect) {
					s.connect = false;
					this.connect.setText("Connecter");
				} else {
					s.connect = true;
					this.connect.setText("Déconnecter");
				}
			});

			Button remove = new Button("Retirer");
			remove.setOnAction(e -> getItems().remove(getIndex()));

			remove.setMaxWidth(Double.MAX_VALUE);
			remove.setPrefWidth(100);
			this.connect.setMaxWidth(Double.MAX_VALUE);
			this.connect.setPrefWidth(100);
			this.color.setMaxWidth(Double.MAX_VALUE);
			this.color.setPrefWidth(100);

			this.pane.add(new Label("Couleur:"), 0, 0);
			this.pane.add(this.color, 1, 0);
			this.pane.add(this.connect, 3, 0);
			this.pane.add(remove, 5, 0);

			this.pane.add(new Label("Pas:"), 0, 1);
			this.pane.add(this.h, 1, 1);
			this.pane.add(new Label("Points:"), 2, 1);
			this.pane.add(this.points, 3, 1);
			this.pane.add(new Label("Vitesse:"), 4, 1);
			this.pane.add(this.speed, 5, 1);

			this.pane.add(new Label("σ:"), 0, 2);
			this.pane.add(this.sigma, 1, 2);
			this.pane.add(new Label("ρ:"), 2, 2);
			this.pane.add(this.rho, 3, 2);
			this.pane.add(new Label("β:"), 4, 2);
			this.pane.add(this.beta, 5, 2);

			this.pane.add(new Label("x0"), 0, 3);
			this.pane.add(this.x0, 1, 3);
			this.pane.add(new Label("y0:"), 2, 3);
			this.pane.add(this.y0, 3, 3);
			this.pane.add(new Label("z0:"), 4, 3);
			this.pane.add(this.z0, 5, 3);

			this.pane.setVgap(5);
			this.pane.setHgap(5);

			this.pane.getColumnConstraints().addAll(GridUtil.createColumn(10), GridUtil.createColumn(30), GridUtil.createColumn(10), GridUtil.createColumn(30), GridUtil.createColumn(10), GridUtil.createColumn(30));
			this.pane.getRowConstraints().addAll(GridUtil.createRow(), GridUtil.createRow(), GridUtil.createRow(), GridUtil.createRow());
		}

		@Override
		public void updateIndex(int index) {
			super.updateIndex(index);
			setGraphic(index == -1 || isEmpty() ? null : updateContent());
		}

		private Node updateContent() {
			LorenzConfig item = getItem();
			this.connect.setText(item.series.connect ? "Déconnecter" : "Connecter");
			this.color.setValue(item.series.getColor());
			this.sigma.setValue(item.sigma);
			this.rho.setValue(item.rho);
			this.beta.setValue(item.beta);
			this.x0.setValue(item.x0);
			this.y0.setValue(item.y0);
			this.z0.setValue(item.z0);
			this.h.setValue(item.h);
			this.points.setValue(item.points);
			this.speed.setValue(item.speed);
			return this.pane;
		}

		@Override
		protected void updateItem(LorenzConfig item, boolean empty) {
			super.updateItem(item, empty);
			setGraphic(getIndex() == -1 || empty ? null : updateContent());
		}
	}
}
