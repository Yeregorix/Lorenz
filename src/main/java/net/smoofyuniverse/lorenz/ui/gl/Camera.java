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
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.math.FloatUtil;
import net.smoofyuniverse.lorenz.math.vector.Vector3f;

public final class Camera {
	public static final float MIN_FOV = 1, MAX_FOV = 80, DEFAULT_FOV = 50;

	private int width = 1, height = 1;
	private float fov = DEFAULT_FOV;

	private float posX, posY, posZ;
	private float[] left = {-1, 0, 0}, up = {0, 1, 0}, forward = {0, 0, 1};

	public void load3D(GLU glu) {
		glu.gluPerspective(this.fov, this.width / (float) this.height, 0.01f, 10000f);
		glu.gluLookAt(this.posX, this.posY, this.posZ, this.posX + this.forward[0], this.posY + this.forward[1], this.posZ + this.forward[2], this.up[0], this.up[1], this.up[2]);
	}

	public void load2D(GL2 gl) {
		gl.glOrtho(0, this.width, this.height, 0, -1, 1);
	}

	public int getWidth() {
		return this.width;
	}

	public void setWidth(int width) {
		this.width = width > 0 ? width : 1;
	}

	public int getHeight() {
		return this.height;
	}

	public void setHeight(int height) {
		this.height = height > 0 ? height : 1;
	}

	public float getFOV() {
		return this.fov;
	}

	public void setFOV(float value) {
		if (value < MIN_FOV)
			value = MIN_FOV;
		else if (value > MAX_FOV)
			value = MAX_FOV;
		this.fov = value;
	}

	public void resetFOV() {
		this.fov = DEFAULT_FOV;
	}

	public void zoom(float dv) {
		setFOV(this.fov + dv);
	}

	public Vector3f getPosition() {
		return new Vector3f(this.posX, this.posY, this.posZ);
	}

	public void setPosition(Vector3f value) {
		this.posX = value.x;
		this.posY = value.y;
		this.posZ = value.z;
	}

	public void setPosition(float x, float y, float z) {
		this.posX = x;
		this.posY = y;
		this.posZ = z;
	}

	public void resetPosition() {
		this.posX = this.posY = this.posZ = 0;
	}

	public void translate(float x, float y, float z) {
		this.posX += x;
		this.posY += y;
		this.posZ += z;
	}

	public void move(float l, float u, float f) {
		this.posX += this.left[0] * l + this.up[0] * u + this.forward[0] * f;
		this.posY += this.left[1] * l + this.up[1] * u + this.forward[1] * f;
		this.posZ += this.left[2] * l + this.up[2] * u + this.forward[2] * f;
	}

	public void resetOrientation() {
		this.left[0] = -1;
		this.left[1] = 0;
		this.left[2] = 0;

		this.up[0] = 0;
		this.up[1] = 1;
		this.up[2] = 0;

		this.forward[0] = 0;
		this.forward[1] = 0;
		this.forward[2] = 1;
	}

	public void pitch(float angle) {
		rotate(this.left, angle, this.up, this.forward);
		normalize(this.up);
		normalize(this.forward);
	}

	private static void rotate(float[] axis, float angle, float[]... vectors) {
		rotate(axis[0], axis[1], axis[2], angle, vectors);
	}

	private static void normalize(float[] vec) {
		float l2 = vec[0] * vec[0] + vec[1] * vec[1] + vec[2] * vec[2];
		float l = (float) Math.sqrt(l2);
		if (l > FloatUtil.EPSILON) {
			vec[0] /= l;
			vec[1] /= l;
			vec[2] /= l;
		}
	}

	private static void rotate(float x, float y, float z, float w, float[]... vectors) {
		float x2 = x * x, y2 = y * y, z2 = z * z, w2 = w * w;

		for (float[] vec : vectors) {
			float vx = -vec[0], vy = -vec[1], vz = -vec[2];
			vec[0] = w2 * vx + x2 * vx - z2 * vx - y2 * vx + 2f * (y * w * vz - z * w * vy + y * x * vy + z * x * vz);
			vec[1] = y2 * vy - z2 * vy + w2 * vy - x2 * vy + 2f * (x * y * vx + z * y * vz + w * z * vx - x * w * vz);
			vec[2] = z2 * vz - y2 * vz - x2 * vz + w2 * vz + 2f * (x * z * vx + y * z * vy - w * y * vx + w * x * vy);
		}
	}

	public void yaw(float angle) {
		rotate(this.up, angle, this.left, this.forward);
		normalize(this.left);
		normalize(this.forward);
	}

	public void roll(float angle) {
		rotate(this.forward, angle, this.left, this.up);
		normalize(this.left);
		normalize(this.up);
	}
}
