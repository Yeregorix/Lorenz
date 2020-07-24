/*
 * Copyright (c) 2019-2020 Hugo Dupanloup (Yeregorix)
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

import net.smoofyuniverse.lorenz.math.vector.Vector3d;

public interface Function {
	double DEFAULT_SIGMA = 10, DEFAULT_RHO = 28, DEFAULT_BETA = 8 / 3D;

	default void apply(Vector3d input, Vector3d output) {
		apply(input.x, input.y, input.z, output);
	}

	void apply(double x, double y, double z, Vector3d output);

	static Function lorenz() {
		return lorenz(DEFAULT_SIGMA, DEFAULT_RHO, DEFAULT_BETA);
	}

	static Function lorenz(double sigma, double rho, double beta) {
		return (x, y, z, output) -> {
			output.x = sigma * (y - x);
			output.y = rho * x - y - x * z;
			output.z = x * y - beta * z;
		};
	}
}
