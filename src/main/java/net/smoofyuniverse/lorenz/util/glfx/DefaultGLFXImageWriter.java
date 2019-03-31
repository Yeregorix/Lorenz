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

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.awt.AWTGLReadBufferUtil;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public final class DefaultGLFXImageWriter implements GLFXImageWriter {
	private final AWTGLReadBufferUtil util;
	private WritableImage image;
	private int width, height;

	public DefaultGLFXImageWriter(GLProfile profile) {
		this.util = new AWTGLReadBufferUtil(profile, true);
	}

	@Override
	public void setDimensions(int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public void doScreenshot(GL2 gl) {
		this.image = SwingFXUtils.toFXImage(this.util.readPixelsToBufferedImage(gl, 0, 0, this.width, this.height, false), this.image);
	}

	@Override
	public Image getImage() {
		return this.image;
	}
}
