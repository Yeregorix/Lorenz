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

import javafx.scene.Scene;
import javafx.stage.Stage;
import net.smoofyuniverse.common.app.Application;
import net.smoofyuniverse.common.environment.source.GitHubReleaseSource;

public class Lorenz extends Application {

	@Override
	public void run() throws Exception {
		Manager manager = new Manager(getManager());
		manager.start();

		runLater(() -> {
			Stage stage = createStage(700, 600, "favicon.png");
			setStage(stage);

			stage.setScene(new Scene(manager.createUI()));
			stage.show();
		});

		getManager().runUpdater(new GitHubReleaseSource("Yeregorix", "Lorenz", null, "Lorenz", getManager().getConnectionConfig()));
	}
}
