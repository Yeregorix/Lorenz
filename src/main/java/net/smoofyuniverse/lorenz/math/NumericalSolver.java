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

import net.smoofyuniverse.common.task.IncrementalListener;
import net.smoofyuniverse.lorenz.math.vector.Vector3d;

public class NumericalSolver {

	public static Vector3d applyRungeKutta4(Vector3d v0, double h, Function function, Series series, IncrementalListener listener) {
		double h2 = h / 2D, h6 = h / 6D;
		Vector3d v = v0.copy(), k1 = new Vector3d(), k2 = new Vector3d(), k3 = new Vector3d(), k4 = new Vector3d();
		listener.increment(1);
		series.add(v);

		while (!listener.isCancelled()) {
			function.apply(v, k1);
			function.apply(v.x + h2 * k1.x, v.y + h2 * k1.y, v.z + h2 * k1.z, k2);
			function.apply(v.x + h2 * k2.x, v.y + h2 * k2.y, v.z + h2 * k2.z, k3);
			function.apply(v.x + h * k3.x, v.y + h * k3.y, v.z + h * k3.z, k4);

			v.x += h6 * (k1.x + 2 * k2.x + 2 * k3.x + k4.x);
			v.y += h6 * (k1.y + 2 * k2.y + 2 * k3.y + k4.y);
			v.z += h6 * (k1.z + 2 * k2.z + 2 * k3.z + k4.z);

			listener.increment(1);
			series.add(v);
		}

		return v;
	}
}
