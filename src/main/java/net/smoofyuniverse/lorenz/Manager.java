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

package net.smoofyuniverse.lorenz;

import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import net.smoofyuniverse.common.app.Application;
import net.smoofyuniverse.lorenz.ui.fx.UserInterface;
import net.smoofyuniverse.lorenz.ui.gl.*;
import net.smoofyuniverse.lorenz.util.Loop;

public class Manager {
	private final Loop renderLoop = new Loop(), controlLoop = new Loop(), processingLoop = new Loop();
	private final Camera camera = new Camera();
	private final ScatterChart chart = new ScatterChart();
	private final Controller controller = new Controller(this.renderLoop, this.camera);
	private final GLWindow window;

	public Manager(Application app) {
		GLProfile glp = GLProfile.getDefault();
		GLCapabilities caps = new GLCapabilities(glp);
		caps.setDoubleBuffered(false);

		this.window = GLWindow.create(caps);

		this.window.addKeyListener(this.controller);
		this.window.addMouseListener(this.controller);
		this.window.addGLEventListener(new Renderer(this.camera, this.chart, this.controller));

		this.window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowDestroyNotify(WindowEvent e) {
				app.shutdown();
			}
		});

		this.window.setSize(1000, 800);
		this.window.setTitle(app.getTitle() + " " + app.getVersion());

		this.renderLoop.setPrefFrequency(60);
		this.controlLoop.setPrefFrequency(30);
		this.processingLoop.setPrefFrequency(30);

		this.renderLoop.updatables.add(new Animator(this.window));
		this.controlLoop.updatables.add(this.controller);
	}

	public void start() {
		this.window.setVisible(true);

		this.renderLoop.start();
		this.controlLoop.start();
		this.processingLoop.start();
	}

	public UserInterface createUI() {
		return new UserInterface(this.processingLoop, this.chart);
	}
}
