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

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLDrawableFactory;
import com.jogamp.opengl.GLOffscreenAutoDrawable;
import com.jogamp.opengl.GLProfile;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.transform.Rotate;
import net.smoofyuniverse.lorenz.util.Updatable;
import net.smoofyuniverse.lorenz.util.glfx.DefaultGLFXImageWriter;
import net.smoofyuniverse.lorenz.util.glfx.DirectGLFXImageWriter;
import net.smoofyuniverse.lorenz.util.glfx.GLFXImageWriter;

public class GLPane extends Region implements Updatable {
	public final GLOffscreenAutoDrawable offscreen;

	private final DefaultGLFXImageWriter defaultImageWriter;
	private final DirectGLFXImageWriter directImageWriter;
	private final ImageView imageView = new ImageView();
	private GLFXImageWriter imageWriter;
	private int prevWidth, prevHeight;
	private int renderMode;

	public GLPane(GLOffscreenAutoDrawable offscreen) {
		this.offscreen = offscreen;
		this.prevWidth = offscreen.getSurfaceWidth();
		this.prevHeight = offscreen.getSurfaceHeight();

		this.defaultImageWriter = new DefaultGLFXImageWriter(offscreen.getGLProfile());
		this.directImageWriter = new DirectGLFXImageWriter();
		this.imageWriter = this.defaultImageWriter;

		getChildren().add(this.imageView);
		this.imageView.setRotationAxis(Rotate.X_AXIS);
		this.imageView.setRotate(180);
	}

	public int getRenderMode() {
		return this.renderMode;
	}

	public void setRenderMode(int mode) {
		this.renderMode = mode % 3;
		switch (this.renderMode) {
			case 0:
				this.imageWriter = this.defaultImageWriter;
				break;
			case 1:
				this.directImageWriter.setUseImageBuffer(false);
				this.imageWriter = this.directImageWriter;
				break;
			case 2:
				this.directImageWriter.setUseImageBuffer(true);
				this.imageWriter = this.directImageWriter;
				break;
		}
	}

	@Override
	public void init() {
		this.offscreen.setExclusiveContextThread(Thread.currentThread());
	}

	@Override
	public void update() {
		int w = (int) getWidth(), h = (int) getHeight();
		if (w <= 0 || h <= 0)
			return;

		if (w != this.prevWidth || h != this.prevHeight) {
			this.defaultImageWriter.setDimensions(w, h);
			this.directImageWriter.setDimensions(w, h);
			this.offscreen.setSurfaceSize(w, h);
			this.prevWidth = w;
			this.prevHeight = h;
		}

		this.offscreen.display();

		this.imageWriter.doScreenshot(this.offscreen.getGL().getGL2());
		Image image = this.imageWriter.getImage();
		Platform.runLater(() -> this.imageView.setImage(image));
	}

	@Override
	public void dispose() {
		this.offscreen.getNativeSurface().unlockSurface();
		this.offscreen.destroy();
	}

	public static GLPane createDefault() {
		GLProfile profile = GLProfile.getDefault();
		GLCapabilities caps = new GLCapabilities(profile);
		caps.setDoubleBuffered(false);
		caps.setFBO(true);
		GLDrawableFactory factory = GLDrawableFactory.getFactory(profile);
		return new GLPane(factory.createOffscreenAutoDrawable(factory.getDefaultDevice(), caps, null, 1, 1));
	}
}
