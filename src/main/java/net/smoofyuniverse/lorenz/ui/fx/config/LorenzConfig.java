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

package net.smoofyuniverse.lorenz.ui.fx.config;

import net.smoofyuniverse.common.fx.task.ObservableProgressListener;
import net.smoofyuniverse.common.task.IncrementalListener;
import net.smoofyuniverse.lorenz.math.Function;
import net.smoofyuniverse.lorenz.math.RungeKutta4;
import net.smoofyuniverse.lorenz.math.Series;
import net.smoofyuniverse.lorenz.math.vector.Vector3d;
import net.smoofyuniverse.lorenz.util.Updatable;

public final class LorenzConfig implements Updatable {
	public double sigma = Function.DEFAULT_SIGMA, rho = Function.DEFAULT_RHO, beta = Function.DEFAULT_BETA, x0 = 1, y0 = 1, z0 = 1, h = 0.001;
	public int points = 100000, speed = 100;

	public final ObservableProgressListener progressListener = new ObservableProgressListener();
	public final Series series = new Series();

	private IncrementalListener listener;
	private Updatable solver;

	public void start() {
		stop();

		this.progressListener.setCancelled(false);
		this.listener = this.progressListener.limit(this.points);
		this.series.ensureCapacity(this.points, true);
		this.solver = new RungeKutta4(new Vector3d(this.x0, this.y0, this.z0), this.h, Function.lorenz(this.sigma, this.rho, this.beta), this.series, this.listener, this.speed);
		this.solver.init();
	}

	public void stop() {
		if (this.listener != null) {
			this.listener.cancel();
			this.listener = null;
		}
		if (this.solver != null) {
			this.solver.dispose();
			this.solver = null;
		}
		this.series.clear();
	}

	@Override
	public void update() {
		if (this.solver != null)
			this.solver.update();
	}

	@Override
	public void dispose() {
		stop();
	}
}
