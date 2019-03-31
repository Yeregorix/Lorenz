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

package net.smoofyuniverse.lorenz.util.glfx;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLDrawable;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;

import java.nio.Buffer;
import java.nio.IntBuffer;

import static com.jogamp.opengl.GL.GL_BGRA;
import static com.jogamp.opengl.GL.GL_UNSIGNED_BYTE;

public final class DirectGLFXImageWriter implements GLFXImageWriter {
	private boolean useImageBuffer;

	private Buffer buffer;
	private WritableImage image;
	private int width, height;

	@Override
	public void setDimensions(int width, int height) {
		this.width = width;
		this.height = height;

		this.image = null;
		this.buffer = null;
	}

	@Override
	public void doScreenshot(GL2 gl) {
		int w = this.width, h = this.height;

		if (this.image == null) {
			this.image = new WritableImage(w, h);
			if (this.useImageBuffer)
				this.buffer = ((com.sun.prism.Image) this.image.impl_getPlatformImage()).getPixelBuffer();
			else
				this.buffer = Buffers.newDirectIntBuffer(w * h);
		}

		GLDrawable drawable = gl.getContext().getGLReadDrawable();
		if (drawable.getSurfaceWidth() < w)
			w = drawable.getSurfaceWidth();
		if (drawable.getSurfaceHeight() < h)
			h = drawable.getSurfaceHeight();

		gl.glReadPixels(0, 0, w, h, GL_BGRA, GL_UNSIGNED_BYTE, this.buffer);

		if (this.image != null) {
			if (this.useImageBuffer)
				this.image.getPixelWriter().setArgb(0, 0, 0);
			else
				this.image.getPixelWriter().setPixels(0, 0, w, h, PixelFormat.getIntArgbInstance(), (IntBuffer) this.buffer, w);
		}
	}

	@Override
	public Image getImage() {
		return this.image;
	}

	public void setUseImageBuffer(boolean value) {
		if (this.useImageBuffer == value)
			return;

		this.useImageBuffer = value;
		this.image = null;
	}
}
