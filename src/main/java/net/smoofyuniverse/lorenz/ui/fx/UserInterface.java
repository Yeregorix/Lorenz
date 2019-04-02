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

package net.smoofyuniverse.lorenz.ui.fx;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import net.smoofyuniverse.common.task.impl.SimpleIncrementalListener;
import net.smoofyuniverse.lorenz.math.Function;
import net.smoofyuniverse.lorenz.math.NumericalSolver;
import net.smoofyuniverse.lorenz.math.Series;
import net.smoofyuniverse.lorenz.math.vector.Vector3d;
import net.smoofyuniverse.lorenz.math.vector.Vector3f;
import net.smoofyuniverse.lorenz.ui.gl.Camera;
import net.smoofyuniverse.lorenz.ui.gl.Renderer;
import net.smoofyuniverse.lorenz.ui.gl.ScatterChart;
import net.smoofyuniverse.lorenz.util.Loop;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class UserInterface extends BorderPane {
	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");

	public final ScatterChart chart = new ScatterChart();
	public final Camera camera = new Camera();

	private final Controller controller = new Controller(this.camera);
	private final GLPane glPane = GLPane.createDefault();
	private final Loop renderLoop = new Loop(), controlLoop = new Loop();
	private final StackPane center = new StackPane(this.glPane);
	private final Label details = new Label();
	private int maxFps;
	private BooleanProperty showDetails = new SimpleBooleanProperty(false);

	public UserInterface() {
		this.glPane.offscreen.addGLEventListener(new Renderer(this.camera, this.chart));
		this.glPane.setRenderMode(2);
		this.controller.installMouseListeners(this.glPane);

		this.showDetails.addListener((v, oldV, newV) -> {
			if (newV)
				this.center.getChildren().add(this.details);
			else
				this.center.getChildren().remove(this.details);
		});

		this.details.setTextFill(Color.WHITE);
		this.details.setBackground(new Background(new BackgroundFill(Color.gray(0.3, 0.8), new CornerRadii(10), new Insets(-7))));
		StackPane.setAlignment(this.details, Pos.TOP_LEFT);
		StackPane.setMargin(this.details, new Insets(10));

		setCenter(this.center);

		setOnKeyPressed(this::onKeyPressed);

		this.renderLoop.updatables.add(this.glPane);
		setMaxFPS(60);
		this.renderLoop.start();

		this.controlLoop.updatables.add(this.controller);
		this.controlLoop.updatables.add(() -> {
			Platform.runLater(() -> this.details.setText(
					"IPS: " + (int) this.renderLoop.getCurrentFrequency() + " / " + this.maxFps
							+ "\nMode de rendu: " + this.glPane.getRenderMode()
							+ "\nChamp de vision: " + format(this.camera.getFOV()) + "°"
							+ "\nPosition: " + format(this.camera.getPosition())
							+ "\nVitesse: " + format(this.controller.getSpeed())
			));
		});
		this.controlLoop.setPrefFrequency(30);
		this.controlLoop.start();

		Series series = new Series();
		this.chart.data.add(series);
		NumericalSolver.applyRungeKutta4(new Vector3d(1, 1, 1), 0.001, Function.lorenz(), series, new SimpleIncrementalListener(1000000));
	}

	private void setMaxFPS(int value) {
		this.maxFps = value;
		this.renderLoop.setPrefFrequency(value);
	}

	public void onKeyPressed(KeyEvent e) {
		switch (e.getCode()) {
			case F3:
				this.showDetails.set(!this.showDetails.get());
				break;
			case MULTIPLY:
				this.glPane.setRenderMode(this.glPane.getRenderMode() + 1);
				break;
			case ADD:
				if (this.maxFps < 100)
					setMaxFPS(this.maxFps + 1);
				break;
			case SUBTRACT:
				if (this.maxFps > 20)
					setMaxFPS(this.maxFps - 1);
				break;
		}
	}

	private static String format(float value) {
		return DECIMAL_FORMAT.format(value);
	}

	private static String format(Vector3f value) {
		return "(" + format(value.x) + ", " + format(value.y) + ", " + format(value.z) + ")";
	}

	static {
		DecimalFormatSymbols dfs = DECIMAL_FORMAT.getDecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		DECIMAL_FORMAT.setDecimalFormatSymbols(dfs);
	}
}