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

import javafx.scene.Node;
import javafx.scene.input.*;
import net.smoofyuniverse.lorenz.ui.gl.Camera;
import net.smoofyuniverse.lorenz.util.Updatable;

public final class Controller implements Updatable {
	private final Camera camera;
	private Node installedNode;

	private boolean zoomIn, zoomOut, left, right, forward, backward, up, down, speedUp, slowDown;
	private float speed = 1;
	private double lastX, lastY;

	public Controller(Camera camera) {
		if (camera == null)
			throw new IllegalArgumentException("camera");
		this.camera = camera;
	}

	public void installMouseListeners(Node node) {
		if (this.installedNode != null)
			throw new IllegalStateException();

		node.setOnKeyPressed(this::onKeyPressed);
		node.setOnKeyReleased(this::onKeyReleased);
		node.setOnScroll(this::onScroll);
		node.setOnMousePressed(this::onMousePressed);
		node.setOnMouseReleased(this::onMouseReleased);
		node.setOnMouseDragged(this::onMouseDragged);

		this.installedNode = node;
	}

	public void onScroll(ScrollEvent e) {
		this.camera.roll((float) e.getDeltaY() * -0.002f);
	}

	public void onMousePressed(MouseEvent e) {
		if (this.installedNode != null)
			this.installedNode.requestFocus();
	}

	public void onMouseReleased(MouseEvent e) {
		if (e.getButton() == MouseButton.PRIMARY)
			this.lastX = this.lastY = -1;
	}

	public void onMouseDragged(MouseEvent e) {
		if (e.getButton() != MouseButton.PRIMARY)
			return;

		double lastX = this.lastX, lastY = this.lastY;
		this.lastX = e.getX();
		this.lastY = e.getY();

		if (lastX == -1 || lastY == -1)
			return;

		double dx = e.getX() - lastX, dy = lastY - e.getY();
		if (dx * dx + dy * dy > 1000)
			return;

		this.camera.pitch((float) dy * -0.002f * getZoomFactor());
		this.camera.yaw((float) dx * 0.002f * getZoomFactor());
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

	public void onKeyPressed(KeyEvent e) {
		KeyCode code = e.getCode();
		switch (code) {
			case NUMPAD1:
				this.camera.resetFOV();
				break;
			case NUMPAD2:
				this.camera.resetPosition();
				break;
			case NUMPAD3:
				this.camera.resetOrientation();
				break;
			case NUMPAD4:
				this.speed = 1;
				break;
			case UP:
				this.zoomIn = true;
				break;
			case DOWN:
				this.zoomOut = true;
				break;
			case RIGHT:
				this.speedUp = true;
				break;
			case LEFT:
				this.slowDown = true;
				break;
			case C:
				this.right = true;
				break;
			case W:
				this.left = true;
				break;
			case D:
				this.forward = true;
				break;
			case X:
				this.backward = true;
				break;
			case SPACE:
				this.up = true;
				break;
			case SHIFT:
				this.down = true;
				break;
		}
	}

	public void onKeyReleased(KeyEvent e) {
		KeyCode code = e.getCode();
		switch (code) {
			case UP:
				this.zoomIn = false;
				break;
			case DOWN:
				this.zoomOut = false;
				break;
			case RIGHT:
				this.speedUp = false;
				break;
			case LEFT:
				this.slowDown = false;
				break;
			case C:
				this.right = false;
				break;
			case W:
				this.left = false;
				break;
			case D:
				this.forward = false;
				break;
			case X:
				this.backward = false;
				break;
			case SPACE:
				this.up = false;
				break;
			case SHIFT:
				this.down = false;
				break;
		}
	}
}
