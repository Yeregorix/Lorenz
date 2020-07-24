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

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;
import net.smoofyuniverse.lorenz.math.vector.Vector3f;
import net.smoofyuniverse.lorenz.util.Loop;
import net.smoofyuniverse.lorenz.util.Updatable;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import static com.jogamp.newt.event.KeyEvent.*;
import static com.jogamp.newt.event.MouseEvent.BUTTON1;
import static com.jogamp.opengl.util.gl2.GLUT.BITMAP_HELVETICA_12;

public final class Controller implements Updatable, KeyListener, MouseListener {
	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");
	private static final List<String> HELP = new ArrayList<>();

	private final Loop renderLoop;
	private final Camera camera;

	private boolean zoomIn, zoomOut, left, right, forward, backward, up, down, speedUp, slowDown;
	private float speed = 1;
	private double lastX, lastY;

	public boolean displayDebug, displayHelp;

	public Controller(Loop renderLoop, Camera camera) {
		if (renderLoop == null)
			throw new IllegalArgumentException("renderLoop");
		if (camera == null)
			throw new IllegalArgumentException("camera");

		this.renderLoop = renderLoop;
		this.camera = camera;
	}

	public void render(GL2 gl, GLUT glut) {
		if (this.displayDebug) {
			List<String> lines = new ArrayList<>();
			lines.add("IPS: " + (int) this.renderLoop.getCurrentFrequency() + " / " + (int) this.renderLoop.getPrefFrequency());
			lines.add("Champ de vision: " + format(this.camera.getFOV()) + "°");
			lines.add("Position: " + format(this.camera.getPosition()));
			lines.add("Vitesse: " + format(this.speed));
			renderString(gl, glut, lines, 0, 0, false);
		}

		if (this.displayHelp)
			renderString(gl, glut, HELP, this.camera.getWidth() / 2f, this.camera.getHeight() / 2f, true);
	}

	private static void renderString(GL2 gl, GLUT glut, List<String> lines, float x, float y, boolean center) {
		int max = 0;
		for (String s : lines) {
			int length = glut.glutBitmapLength(BITMAP_HELVETICA_12, s);
			if (length > max)
				max = length;
		}

		float width = max + 14, height = 17 * lines.size() + 14;

		if (center) {
			x -= width / 2;
			y -= height / 2;
		}

		gl.glColor4f(0.2f, 0.2f, 0.2f, 0.8f);
		gl.glRectf(x, y, x + width, y + height);

		x += 7;
		y += 4;
		gl.glColor3f(1, 1, 1);
		for (String s : lines) {
			y += 17;
			gl.glRasterPos2f(x, y);
			glut.glutBitmapString(BITMAP_HELVETICA_12, s);
		}
	}

	@Override
	public void update() {
		float z = 0;
		if (this.zoomIn)
			z--;
		if (this.zoomOut)
			z++;

		if (z != 0)
			this.camera.zoom(z);

		if (this.speedUp)
			this.speed += 0.1f;
		if (this.slowDown)
			this.speed -= 0.1f;

		if (this.speed > 10)
			this.speed = 10;
		else if (this.speed < 0.1f)
			this.speed = 0.1f;

		float dx = 0, dy = 0, dz = 0;
		if (this.right)
			dx++;
		if (this.left)
			dx--;
		if (this.forward)
			dz++;
		if (this.backward)
			dz--;
		if (this.up)
			dy++;
		if (this.down)
			dy--;

		float l = dx * dx + dy * dy + dz * dz;
		if (l != 0) {
			float f = 0.4f * getZoomFactor() * this.speed / (float) Math.sqrt(l);
			this.camera.move(dx * f, dy * f, dz * f);
		}
	}

	public float getSpeed() {
		return this.speed;
	}

	private float getZoomFactor() {
		return this.camera.getFOV() / Camera.DEFAULT_FOV;
	}

	private static String format(float value) {
		return DECIMAL_FORMAT.format(value);
	}

	private static String format(Vector3f value) {
		return "(" + format(value.x) + ", " + format(value.y) + ", " + format(value.z) + ")";
	}

	@Override
	public void keyPressed(KeyEvent e) {
		short symbol = e.getKeySymbol();
		switch (symbol) {
			case VK_F3:
				this.displayDebug = !this.displayDebug;
				break;
			case VK_H:
				this.displayHelp = !this.displayHelp;
				break;
			case VK_ADD:
				int maxFps = (int) this.renderLoop.getPrefFrequency();
				if (maxFps < 200)
					this.renderLoop.setPrefFrequency(maxFps + 1);
				break;
			case VK_SUBTRACT:
				maxFps = (int) this.renderLoop.getPrefFrequency();
				if (maxFps > 20)
					this.renderLoop.setPrefFrequency(maxFps - 1);
				break;
			case VK_NUMPAD1:
				this.camera.resetFOV();
				break;
			case VK_NUMPAD2:
				this.camera.resetPosition();
				break;
			case VK_NUMPAD3:
				this.camera.resetOrientation();
				break;
			case VK_NUMPAD4:
				this.speed = 1;
				break;
			case VK_UP:
				this.zoomIn = true;
				break;
			case VK_DOWN:
				this.zoomOut = true;
				break;
			case VK_RIGHT:
				this.speedUp = true;
				break;
			case VK_LEFT:
				this.slowDown = true;
				break;
			case VK_C:
				this.right = true;
				break;
			case VK_W:
				this.left = true;
				break;
			case VK_D:
				this.forward = true;
				break;
			case VK_X:
				this.backward = true;
				break;
			case VK_SPACE:
				this.up = true;
				break;
			case VK_SHIFT:
				this.down = true;
				break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.isAutoRepeat())
			return;

		short symbol = e.getKeySymbol();
		switch (symbol) {
			case VK_UP:
				this.zoomIn = false;
				break;
			case VK_DOWN:
				this.zoomOut = false;
				break;
			case VK_RIGHT:
				this.speedUp = false;
				break;
			case VK_LEFT:
				this.slowDown = false;
				break;
			case VK_C:
				this.right = false;
				break;
			case VK_W:
				this.left = false;
				break;
			case VK_D:
				this.forward = false;
				break;
			case VK_X:
				this.backward = false;
				break;
			case VK_SPACE:
				this.up = false;
				break;
			case VK_SHIFT:
				this.down = false;
				break;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == BUTTON1)
			this.lastX = this.lastY = -1;
	}

	@Override
	public void mouseMoved(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (e.getButton() != BUTTON1)
			return;

		double lastX = this.lastX, lastY = this.lastY;
		this.lastX = e.getX();
		this.lastY = e.getY();

		if (lastX == -1 || lastY == -1)
			return;

		double dx = e.getX() - lastX, dy = lastY - e.getY();
		if (dx * dx + dy * dy > 1000)
			return;

		this.camera.pitch((float) dy * -0.003f * getZoomFactor());
		this.camera.yaw((float) dx * 0.003f * getZoomFactor());
	}

	@Override
	public void mouseWheelMoved(MouseEvent e) {
		this.camera.roll(e.getRotation()[1] * -0.05f);
	}

	static {
		DecimalFormatSymbols dfs = DECIMAL_FORMAT.getDecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		DECIMAL_FORMAT.setDecimalFormatSymbols(dfs);

		HELP.add("H: Afficher ou cacher cette aide.");
		HELP.add("F3: Afficher ou cacher les détails.");
		HELP.add("+: Augmenter la limite d'IPS.");
		HELP.add("-: Diminuer la limite d'IPS.");

		HELP.add("W: Aller à gauche.");
		HELP.add("C: Aller à droite.");
		HELP.add("D: Avancer.");
		HELP.add("X: Reculer.");
		HELP.add("Espace: Monter.");
		HELP.add("Shift: Descendre.");

		HELP.add("Flèche avant: Zoomer en avant.");
		HELP.add("Flèche arrière: Zoomer en arrière.");
		HELP.add("Flèche droite: Augmenter la vitesse.");
		HELP.add("Flèche gauche: Diminuer la vitesse.");

		HELP.add("1: Réinitialiser le champ de vision.");
		HELP.add("2: Réinitialiser la position.");
		HELP.add("3: Réinitialiser l'orientation.");
		HELP.add("4: Réinitialiser la vitesse.");
	}
}
