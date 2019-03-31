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
import javafx.scene.paint.Color;
import net.smoofyuniverse.lorenz.math.Series;

import java.util.ArrayList;
import java.util.List;

import static com.jogamp.opengl.GL2.*;

public class ScatterChart {
	public final List<Series> data = new ArrayList<>();

	public void render(GL2 gl) {
		gl.glScalef(0.1f, 0.1f, 0.1f);

		gl.glLineWidth(2);
		gl.glBegin(GL_LINES);

		gl.glColor3f(1, 0, 0);
		gl.glVertex3f(0, 0, 0);
		gl.glVertex3f(1, 0, 0);

		gl.glColor4f(1, 0, 0, 0.4f);
		gl.glVertex3f(1, 0, 0);
		gl.glVertex3f(10, 0, 0);

		gl.glColor3f(0, 1, 0);
		gl.glVertex3f(0, 0, 0);
		gl.glVertex3f(0, 1, 0);

		gl.glColor4f(0, 1, 0, 0.4f);
		gl.glVertex3f(0, 1, 0);
		gl.glVertex3f(0, 10, 0);

		gl.glColor3f(0, 0, 1);
		gl.glVertex3f(0, 0, 0);
		gl.glVertex3f(0, 0, 1);

		gl.glColor4f(0, 0, 1, 0.4f);
		gl.glVertex3f(0, 0, 1);
		gl.glVertex3f(0, 0, 10);

		gl.glEnd();
		gl.glLineWidth(1);

		for (Series s : this.data) {
			Color c = s.getColor();
			gl.glColor4f((float) c.getRed(), (float) c.getGreen(), (float) c.getBlue(), (float) c.getOpacity());

			gl.glEnableClientState(GL_VERTEX_ARRAY);
			gl.glVertexPointer(3, GL_FLOAT, 0, s.getCachedBuffer());
			gl.glDrawArrays(s.connect ? GL_LINE_STRIP : GL_POINTS, 0, s.size());
			gl.glDisableClientState(GL_VERTEX_ARRAY);
		}
	}
}
