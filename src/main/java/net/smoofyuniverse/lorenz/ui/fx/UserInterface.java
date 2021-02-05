/*
 * Copyright (c) 2019-2021 Hugo Dupanloup (Yeregorix)
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

package net.smoofyuniverse.lorenz.ui.fx;

import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import net.smoofyuniverse.common.util.GridUtil;
import net.smoofyuniverse.lorenz.ui.fx.config.LorenzConfig;
import net.smoofyuniverse.lorenz.ui.fx.config.LorenzConfigList;
import net.smoofyuniverse.lorenz.ui.gl.ScatterChart;
import net.smoofyuniverse.lorenz.util.Loop;

public class UserInterface extends GridPane {

	public UserInterface(Loop processingLoop, ScatterChart chart) {
		if (processingLoop == null)
			throw new IllegalArgumentException("processingLoop");
		if (chart == null)
			throw new IllegalArgumentException("chart");

		Button add = new Button("Ajouter"), clear = new Button("Vider"), calculate = new Button("Calculer");
		LorenzConfigList list = new LorenzConfigList();

		list.getItems().addListener((ListChangeListener<LorenzConfig>) c -> {
			while (c.next()) {
				for (LorenzConfig cfg : c.getRemoved()) {
					cfg.stop();
					processingLoop.updatables.remove(cfg);
					chart.data.remove(cfg.series);
				}

				for (LorenzConfig cfg : c.getAddedSubList()) {
					chart.data.add(cfg.series);
					processingLoop.updatables.add(cfg);
				}
			}
		});

		add.setOnAction(e -> list.getItems().add(new LorenzConfig()));
		clear.setOnAction(e -> list.getItems().clear());
		calculate.setOnAction(e -> {
			for (LorenzConfig cfg : list.getItems())
				cfg.start();
		});

		add.setMaxWidth(Double.MAX_VALUE);
		clear.setMaxWidth(Double.MAX_VALUE);
		calculate.setMaxWidth(Double.MAX_VALUE);

		add(list, 0, 0, 3, 1);
		addRow(1, add, clear, calculate);

		setVgap(5);
		setHgap(5);
		setPadding(new Insets(8));

		getColumnConstraints().addAll(GridUtil.createColumn(33), GridUtil.createColumn(33), GridUtil.createColumn(33));
		getRowConstraints().addAll(GridUtil.createRow(Priority.ALWAYS), GridUtil.createRow());
	}
}