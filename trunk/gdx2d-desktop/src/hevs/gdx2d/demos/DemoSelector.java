package hevs.gdx2d.demos;

import hevs.gdx2d.lib.Version;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import com.badlogic.gdx.backends.lwjgl.LwjglPreferences;
import com.badlogic.gdx.files.FileHandle;

/**
 * A demo selector class, most of the code taken from Libgdx own demo selector
 * 
 * @author Pierre-Andre Mudry (mui)
 * @version 1.0
 */
@SuppressWarnings("serial")
public class DemoSelector extends JFrame {

	LinkedHashMap<String, String> tests = new LinkedHashMap<String, String>();

	/**
	 * Scans all classes accessible from the context class loader which belong
	 * to the given package and subpackages.
	 * 
	 * @param packageName
	 *            The base package
	 * @return The classes
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private static Class[] getClasses(String packageName) throws ClassNotFoundException, IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		assert classLoader != null;
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources = classLoader.getResources(path);
		List<File> dirs = new ArrayList<File>();

		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			dirs.add(new File(resource.getFile()));
		}

		ArrayList<Class> classes = new ArrayList<Class>();
		for (File directory : dirs) {
			classes.addAll(findClasses(directory, packageName));
		}

		return classes.toArray(new Class[classes.size()]);
	}

	/**
	 * Recursive method used to find all classes in a given directory and
	 * subdirs.
	 * 
	 * @param directory The base directory
	 * @param packageName The package name for classes found inside the base directory
	 * @return The classes
	 * @throws ClassNotFoundException
	 */
	private static List<Class> findClasses(File directory, String packageName)
			throws ClassNotFoundException {
		List<Class> classes = new ArrayList<Class>();
		if (!directory.exists()) {
			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file,
						packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				classes.add(Class.forName(packageName
						+ '.'
						+ file.getName().substring(0,
								file.getName().length() - 6)));
			}
		}
		return classes;
	}

	public DemoSelector() throws HeadlessException {
		super("GDX2D demos " + Version.version + " - mui, chn, mei 2013");

		tests.put("Simple shapes", "simple.DemoSimpleShapes");
		tests.put("Basic animation", "simple.DemoSimpleAnimation");
		tests.put("Image drawing", "image_drawing.DemoSimpleImage");
		tests.put("Rotating image", "image_drawing.DemoRotatingImage");
		tests.put("Music player", "music.DemoMusicPlay");
		tests.put("Scrolling", "scrolling.DemoScrolling");
		tests.put("Animation tweening", "tween.DemoTween");
		tests.put("Complex shapes", "complex_shapes.DemoComplexShapes");
		tests.put("Simple physics (dominoes)", "physics.DemoSimplePhysics");		
		tests.put("Physics soccer ball", "physics.DemoPhysicsBalls");
		tests.put("Physics mouse interactions", "physics.DemoPhysicsMouse");
		tests.put("Particles", "physics.particle.DemoParticlePhysics");		
		tests.put("Light", "lights.DemoLight");

		// Populate the window
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(new TestList());
		pack();		
		setSize(450, 400);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	class TestList extends JPanel {
		private static final long serialVersionUID = 1L;

		public TestList() {
			setLayout(new BorderLayout());
			final JButton button = new JButton("Run demo");
			String[] l = tests.keySet().toArray(new String[0]);
			final JList list = new JList(l);

			JScrollPane pane = new JScrollPane(list);

			DefaultListSelectionModel m = new DefaultListSelectionModel();
			m.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			m.setLeadAnchorNotificationEnabled(false);
			list.setSelectionModel(m);

			list.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent event) {
					if (event.getClickCount() == 2)
						button.doClick();
				}
			});

			list.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER)
						button.doClick();
				}
			});

			// Save the prefs if required
			final Preferences prefs = new LwjglPreferences(
					new FileHandle(
							new LwjglFiles().getExternalStoragePath()
							+ ".prefs/lwjgl-tests"));
			
			list.setSelectedValue(prefs.getString("last", null), true);

			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String testName = (String) list.getSelectedValue();
					prefs.putString("last", testName);
					prefs.flush();
					dispose();

					// Loads the class based on its name
					try {
						Class<?> clazz = Class.forName("hevs.gdx2d.demos."	+ tests.get(testName));
						Constructor<?> constructor = clazz.getConstructor(boolean.class);
						constructor.newInstance(false);						
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			});

			add(pane, BorderLayout.CENTER);
			add(button, BorderLayout.SOUTH);
		}
	}

	public static void main(String[] argv) throws Exception {		
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}		
		
//		Class[] c = DemoSelector.getClasses("hevs.gdx2d.demos");
//		
//		for (Class class1 : c) {
//			if (class1.getSimpleName().startsWith("Demo")) {
//				System.out.println(class1.getSimpleName());
//			}
//		}
		new DemoSelector();
	}
}