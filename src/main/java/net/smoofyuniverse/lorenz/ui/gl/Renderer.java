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

package net.smoofyuniverse.lorenz.ui.gl;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2ES1.*;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH;

public class Renderer implements GLEventListener {
	private final Camera camera;
	private final ScatterChart chart;
	private final Controller controller;

	private final GLU glu = new GLU();
	private final GLUT glut = new GLUT();

	public Renderer(Camera camera, ScatterChart chart, Controller controller) {
		if (camera == null)
			throw new IllegalArgumentException("camera");
		if (chart == null)
			throw new IllegalArgumentException("chart");
		if (controller == null)
			throw new IllegalArgumentException("controller");

		this.camera = camera;
		this.chart = chart;
		this.controller = controller;
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glShadeModel(GL_SMOOTH);
		gl.glClearColor(0, 0, 0, 0);
		gl.glClearDepth(1);

		gl.glEnable(GL_DEPTH_TEST);
		gl.glDepthFunc(GL_LEQUAL);
		gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);

		gl.glEnable(GL_LINE_SMOOTH);
		gl.glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);

		gl.glEnable(GL_POINT_SMOOTH);
		gl.glHint(GL_POINT_SMOOTH_HINT, GL_NICEST);

		gl.glEnable(GL_BLEND);
		gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		// 3D Setup
		gl.glMatrixMode(GL_PROJECTION);
		gl.glLoadIdentity();
		this.camera.load3D(this.glu);

		// 3D Render
		gl.glMatrixMode(GL_MODELVIEW);
		gl.glLoadIdentity();
		this.chart.render(gl);

		// 2D Setup
		gl.glMatrixMode(GL_PROJECTION);
		gl.glLoadIdentity();
		this.camera.load2D(gl);

		// 2D Render
		gl.glMatrixMode(GL_MODELVIEW);
		gl.glLoadIdentity();
		this.controller.render(gl, this.glut);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glViewport(0, 0, width, height);
		this.camera.setWidth(width);
		this.camera.setHeight(height);
	}
}
