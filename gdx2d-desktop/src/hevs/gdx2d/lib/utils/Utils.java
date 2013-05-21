package hevs.gdx2d.lib.utils;

/**
 * Various utils for checking things
 * @author Nils Chatton (chn)
 * @date 2013
 * @version 1.0
 */
public class Utils {

	public static void callCheck(String className, String method) {
		boolean callFromWrongLocation = true;

		StackTraceElement[] callStack = Thread.currentThread().getStackTrace();

		for (StackTraceElement elem : callStack) {
			if (elem.getClassName().equals(className)
					&& elem.getMethodName().equals(method)) {
				callFromWrongLocation = false;
				break;
			}
		}

		if (callFromWrongLocation) {
			try {
				throw new Exception(
						"This new class shall be created in the onInit() method from the class implementing PortableApplication");
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}

	public static void callCheckExcludeGraphicRender() {

		String className = "hevs.gdx2d.components.Game";
		String method = "render";

		boolean callFromWrongLocation = false;

		StackTraceElement[] callStack = Thread.currentThread().getStackTrace();

		for (StackTraceElement elem : callStack) {
			if (elem.getClassName().equals(className)
					&& elem.getMethodName().equals(method)) {
				callFromWrongLocation = true;
				break;
			}
		}

		if (callFromWrongLocation) {
			try {
				throw new Exception(
						"For performance issue, this new class shall not be created in the onGraphicRender() method from the class implementing PortableApplication.");
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}

	}
}
