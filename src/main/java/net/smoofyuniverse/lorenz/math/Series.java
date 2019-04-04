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

package net.smoofyuniverse.lorenz.math;

import com.jogamp.common.nio.Buffers;
import javafx.scene.paint.Color;
import net.smoofyuniverse.lorenz.math.vector.Vector3d;
import net.smoofyuniverse.lorenz.math.vector.Vector3f;

import java.nio.FloatBuffer;
import java.util.Arrays;

public final class Series {
	public static final Color DEFAULT_COLOR = Color.color(1, 1, 1, 0.8);

	public boolean connect = true;
	private Color color = DEFAULT_COLOR;
	private float[] points = new float[81];
	private int size = 0;
	private FloatBuffer cachedBuffer;

	public Color getColor() {
		return this.color;
	}

	public void setColor(Color color) {
		this.color = color == null ? Color.WHITE : color;
	}

	public void add(Vector3d v) {
		add((float) v.x, (float) v.y, (float) v.z);
	}

	public void add(float x, float y, float z) {
		ensureCapacity(this.size + 1, false);

		int pos = this.size * 3;
		this.points[pos] = x;
		this.points[pos + 1] = y;
		this.points[pos + 2] = z;

		this.size++;
	}

	public void ensureCapacity(int capacity, boolean exact) {
		int prevCapacity = this.points.length / 3;
		if (capacity <= prevCapacity)
			return;

		if (!exact) {
			if (capacity < prevCapacity * 2)
				capacity = prevCapacity * 2;
		}

		this.points = Arrays.copyOf(this.points, capacity * 3);
	}

	public void clear() {
		this.size = 0;
	}

	public int size() {
		return this.size;
	}

	public void forEach(Vector3f.Consumer consumer) {
		for (int i = 0; i < this.size; i++) {
			int pos = i * 3;
			consumer.accept(this.points[pos], this.points[pos + 1], this.points[pos + 2]);
		}
	}

	public FloatBuffer getCachedBuffer() {
		if (this.cachedBuffer == null || this.cachedBuffer.capacity() != this.size * 3)
			this.cachedBuffer = Buffers.newDirectFloatBuffer(this.points, 0, this.size * 3);
		return this.cachedBuffer;
	}
}
