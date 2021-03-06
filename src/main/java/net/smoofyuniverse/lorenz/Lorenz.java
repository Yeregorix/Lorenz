/*
 * Copyright (c) 2019-2021 Hugo Dupanloup (Yeregorix)
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

package net.smoofyuniverse.lorenz;

import net.smoofyuniverse.common.app.Application;
import net.smoofyuniverse.common.app.Arguments;
import net.smoofyuniverse.common.app.OperatingSystem;
import net.smoofyuniverse.common.environment.ApplicationUpdater;
import net.smoofyuniverse.common.environment.DependencyInfo;
import net.smoofyuniverse.common.environment.DependencyManager;
import net.smoofyuniverse.common.environment.source.GithubReleaseSource;

import java.util.ArrayList;
import java.util.List;

public class Lorenz extends Application {

	public Lorenz(Arguments args) {
		super(args, "Lorenz", "1.0.10");
	}

	@Override
	public void init() throws Exception {
		requireGUI();
		initServices();

		if (!this.devEnvironment) {
			List<DependencyInfo> list = new ArrayList<>();
			Libraries.get(OperatingSystem.CURRENT, list);
			new DependencyManager(this, list).setup();
		}

		Manager manager = new Manager(this);
		manager.start();

		runLater(() -> {
			initStage(700, 600, "favicon.png");
			setScene(manager.createUI()).show();
		});

		new ApplicationUpdater(this, new GithubReleaseSource("Yeregorix", "Lorenz", null, "Lorenz", getConnectionConfig())).run();
	}

	public static void main(String[] args) {
		new Lorenz(Arguments.parse(args)).launch();
	}
}
