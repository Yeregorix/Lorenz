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

import net.smoofyuniverse.common.app.OperatingSystem;
import net.smoofyuniverse.common.environment.DependencyInfo;

import java.util.Collection;

public class Libraries {
	public static final DependencyInfo GLUEGEN_RT = new DependencyInfo("org.jogamp.gluegen:gluegen-rt:2.3.2", "https://repo1.maven.org/maven2/org/jogamp/gluegen/gluegen-rt/2.3.2/gluegen-rt-2.3.2.jar", 345605, "edc35ccfc13d4a4ad02c50d580874c18bf48bbef", "sha1"),
			GLUEGEN_RT_LINUX_AMD64 = new DependencyInfo("org.jogamp.gluegen:gluegen-rt:2.3.2:natives-linux-amd64", "https://repo1.maven.org/maven2/org/jogamp/gluegen/gluegen-rt/2.3.2/gluegen-rt-2.3.2-natives-linux-amd64.jar", 4149, "477a6bc4f9256d8c9935b75d55e2b7b01314440b", "sha1"),
			GLUEGEN_RT_LINUX_ARMV6 = new DependencyInfo("org.jogamp.gluegen:gluegen-rt:2.3.2:natives-linux-armv6", "https://repo1.maven.org/maven2/org/jogamp/gluegen/gluegen-rt/2.3.2/gluegen-rt-2.3.2-natives-linux-armv6.jar", 3042, "c55cd566d98d4fcd004b9aaa2c502c8c2dcec192", "sha1"),
			GLUEGEN_RT_LINUX_ARMV6HF = new DependencyInfo("org.jogamp.gluegen:gluegen-rt:2.3.2:natives-linux-armv6hf", "https://repo1.maven.org/maven2/org/jogamp/gluegen/gluegen-rt/2.3.2/gluegen-rt-2.3.2-natives-linux-armv6hf.jar", 3050, "f6d3587b061d7ae6e68932fc62317e3500d2112d", "sha1"),
			GLUEGEN_RT_LINUX_I586 = new DependencyInfo("org.jogamp.gluegen:gluegen-rt:2.3.2:natives-linux-i586", "https://repo1.maven.org/maven2/org/jogamp/gluegen/gluegen-rt/2.3.2/gluegen-rt-2.3.2-natives-linux-i586.jar", 4130, "da68aff010edd35a9ee89b1801dce6e284c7e673", "sha1"),
			GLUEGEN_RT_MACOSX_UNIVERSAL = new DependencyInfo("org.jogamp.gluegen:gluegen-rt:2.3.2:natives-macosx-universal", "https://repo1.maven.org/maven2/org/jogamp/gluegen/gluegen-rt/2.3.2/gluegen-rt-2.3.2-natives-macosx-universal.jar", 5076, "7227ae9e480354909d80f4df92b30bbb965387be", "sha1"),
			GLUEGEN_RT_WINDOWS_AMD64 = new DependencyInfo("org.jogamp.gluegen:gluegen-rt:2.3.2:natives-windows-amd64", "https://repo1.maven.org/maven2/org/jogamp/gluegen/gluegen-rt/2.3.2/gluegen-rt-2.3.2-natives-windows-amd64.jar", 8159, "85213c97a5a50d8dd37de4c35ef32cd4b0ac2ef0", "sha1"),
			GLUEGEN_RT_WINDOWS_I586 = new DependencyInfo("org.jogamp.gluegen:gluegen-rt:2.3.2:natives-windows-i586", "https://repo1.maven.org/maven2/org/jogamp/gluegen/gluegen-rt/2.3.2/gluegen-rt-2.3.2-natives-windows-i586.jar", 7577, "f9b848384a343f39f454e59cead435c2c0cbf542", "sha1");

	public static final DependencyInfo JOGL_ALL = new DependencyInfo("org.jogamp.jogl:jogl-all:2.3.2", "https://repo1.maven.org/maven2/org/jogamp/jogl/jogl-all/2.3.2/jogl-all-2.3.2.jar", 3414448, "99e0f64bb8882f054825ae4a8a527a17b544a0b5", "sha1"),
			JOGL_ALL_LINUX_AMD64 = new DependencyInfo("org.jogamp.jogl:jogl-all:2.3.2:natives-linux-amd64", "https://repo1.maven.org/maven2/org/jogamp/jogl/jogl-all/2.3.2/jogl-all-2.3.2-natives-linux-amd64.jar", 224010, "ffd1c6bd5f6fb088df6cad25e25721eb55ad2228", "sha1"),
			JOGL_ALL_LINUX_ARMV6 = new DependencyInfo("org.jogamp.jogl:jogl-all:2.3.2:natives-linux-armv6", "https://repo1.maven.org/maven2/org/jogamp/jogl/jogl-all/2.3.2/jogl-all-2.3.2-natives-linux-armv6.jar", 161199, "6d14f46336be9e2dc5f85d5df17490cee5d09a90", "sha1"),
			JOGL_ALL_LINUX_ARMV6HF = new DependencyInfo("org.jogamp.jogl:jogl-all:2.3.2:natives-linux-armv6hf", "https://repo1.maven.org/maven2/org/jogamp/jogl/jogl-all/2.3.2/jogl-all-2.3.2-natives-linux-armv6hf.jar", 164852, "2c434c560dbd821b0ed569e44d3d441727491dac", "sha1"),
			JOGL_ALL_LINUX_I586 = new DependencyInfo("org.jogamp.jogl:jogl-all:2.3.2:natives-linux-i586", "https://repo1.maven.org/maven2/org/jogamp/jogl/jogl-all/2.3.2/jogl-all-2.3.2-natives-linux-i586.jar", 217274, "59b7a336c0ee54161e33662d2ca8ac9d8f6c476d", "sha1"),
			JOGL_ALL_MACOSX_UNIVERSAL = new DependencyInfo("org.jogamp.jogl:jogl-all:2.3.2:natives-macosx-universal", "https://repo1.maven.org/maven2/org/jogamp/jogl/jogl-all/2.3.2/jogl-all-2.3.2-natives-macosx-universal.jar", 443876, "2da2215dbe1091c54cb83cf2484f8a54a4d0cf7a", "sha1"),
			JOGL_ALL_WINDOWS_AMD64 = new DependencyInfo("org.jogamp.jogl:jogl-all:2.3.2:natives-windows-amd64", "https://repo1.maven.org/maven2/org/jogamp/jogl/jogl-all/2.3.2/jogl-all-2.3.2-natives-windows-amd64.jar", 240721, "becd519b1f741176561d93645abd90971ca0dca0", "sha1"),
			JOGL_ALL_WINDOWS_I586 = new DependencyInfo("org.jogamp.jogl:jogl-all:2.3.2:natives-windows-i586", "https://repo1.maven.org/maven2/org/jogamp/jogl/jogl-all/2.3.2/jogl-all-2.3.2-natives-windows-i586.jar", 209445, "5db8db5eca6b4445db6cd14f68938736630ae976", "sha1");

	public static void get(OperatingSystem os, Collection<DependencyInfo> col) {
		switch (os) {
			case WINDOWS:
				col.add(GLUEGEN_RT_WINDOWS_AMD64);
				col.add(JOGL_ALL_WINDOWS_AMD64);
				col.add(GLUEGEN_RT_WINDOWS_I586);
				col.add(JOGL_ALL_WINDOWS_I586);
				break;
			case MACOS:
				col.add(GLUEGEN_RT_MACOSX_UNIVERSAL);
				col.add(JOGL_ALL_MACOSX_UNIVERSAL);
				break;
			case LINUX:
				col.add(GLUEGEN_RT_LINUX_AMD64);
				col.add(JOGL_ALL_LINUX_AMD64);
				col.add(GLUEGEN_RT_LINUX_ARMV6);
				col.add(JOGL_ALL_LINUX_ARMV6);
				col.add(GLUEGEN_RT_LINUX_ARMV6HF);
				col.add(JOGL_ALL_LINUX_ARMV6HF);
				col.add(GLUEGEN_RT_LINUX_I586);
				col.add(JOGL_ALL_LINUX_I586);
				break;
			default:
				throw new UnsupportedOperationException("Unsupported OS");
		}
		col.add(GLUEGEN_RT);
		col.add(JOGL_ALL);
	}
}
