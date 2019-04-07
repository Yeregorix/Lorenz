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

package net.smoofyuniverse.lorenz;

import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import net.smoofyuniverse.common.app.App;
import net.smoofyuniverse.common.app.Application;
import net.smoofyuniverse.common.app.Arguments;
import net.smoofyuniverse.lorenz.ui.fx.UserInterface;
import net.smoofyuniverse.lorenz.ui.gl.*;
import net.smoofyuniverse.lorenz.util.Loop;

import java.util.concurrent.Executors;

public class Lorenz extends Application {
	private Loop renderLoop, controlLoop, processingLoop;
	private Camera camera;
	private ScatterChart chart;
	private Controller controller;

	public Lorenz(Arguments args) {
		super(args, "Lorenz", "1.0.3");
	}

	@Override
	public void init() throws Exception {
		requireUI();
		initServices(Executors.newCachedThreadPool());

		this.renderLoop = new Loop();
		this.controlLoop = new Loop();
		this.processingLoop = new Loop();
		this.camera = new Camera();
		this.chart = new ScatterChart();
		this.controller = new Controller(this.renderLoop, this.camera);

		GLProfile glp = GLProfile.getDefault();
		GLCapabilities caps = new GLCapabilities(glp);
		caps.setDoubleBuffered(false);
		GLWindow window = GLWindow.create(caps);

		window.addKeyListener(this.controller);
		window.addMouseListener(this.controller);
		window.addGLEventListener(new Renderer(this.camera, this.chart, this.controller));

		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowDestroyNotify(WindowEvent e) {
				shutdown();
			}
		});

		window.setSize(1000, 800);
		window.setTitle(this.title + " " + this.version);
		window.setVisible(true);

		this.renderLoop.setPrefFrequency(60);
		this.controlLoop.setPrefFrequency(30);
		this.processingLoop.setPrefFrequency(30);

		this.renderLoop.updatables.add(new Animator(window));
		this.controlLoop.updatables.add(this.controller);

		this.renderLoop.start();
		this.controlLoop.start();
		this.processingLoop.start();

		App.runLater(() -> {
			initStage(700, 600, true, "favicon.png");
			setScene(new UserInterface(this.processingLoop, this.chart)).show();
		});

		checkForUpdate();
	}

	public static void main(String[] args) {
		new Lorenz(Arguments.parse(args)).launch();
	}

	public static Lorenz get() {
		return (Lorenz) Application.get();
	}
}
