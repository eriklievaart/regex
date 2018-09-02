package toolkit;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.Action;

public class ConfigurationManager {
	private static ConfigurationManager manager;
	private HashMap<String, HashMap> map = new HashMap<String, HashMap>();

	private static ConfigurationManager getInstance() {
		if (manager == null) {
			manager = new ConfigurationManager();
			load();
		}
		return manager;
	}

	public static Action getOptionFrameAction() {
		class OptionAction extends AbstractAction {
			public OptionAction() {
				super("Options");
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				HashMap<String, HashMap> configurables = getInstance().map;
				Object[] keys = configurables.keySet().toArray();
				String[] keyStrings = new String[keys.length];
				for (int i = 0; i < keys.length; i++) {
					keyStrings[i] = keys[i].toString();
				}
				Option[][] settings = new Option[keys.length][0];
				for (int o = 0; o < keys.length; o++) {
					HashMap optionMap = configurables.get(keys[o]);
					Object[] optionObjects = optionMap.keySet().toArray();
					Option[] options = new Option[optionObjects.length];
					for (int i = 0; i < options.length; i++) {
						options[i] = ((Option) optionObjects[i]).cloneOption();
					}
					settings[o] = options;
				}
				new OptionFrame(keyStrings, settings);
			}
		}
		return new OptionAction();
	}

	static void updateOptions(String[] classes, Option[][] options) {
		if (classes.length != options.length) {
			throw new IllegalArgumentException("classes <-> options mismatch");
		}
		HashMap<String, HashMap> configurables = getInstance().map;
		for (int o = 0; o < classes.length; o++) {
			HashMap optionMap = configurables.get(classes[o]);
			assert optionMap != null : "Illegal State";
			for (int i = 0; i < options[o].length; i++) {
				OptionAction action = (OptionAction) optionMap.get(options[o][i]);
				optionMap.remove(options[o][i]);
				optionMap.put(options[o][i], action);
				try {
					action.actionPerformed(options[o][i].getValue());
				} catch (Exception e) {
				}
			}
		}
		save();
	}

	public static void registerConfigurable(Object owner) {
		HashMap<String, HashMap> configurables = getInstance().map;
		if (configurables.get(owner.getClass().getName()) == null) {
			configurables.put(owner.getClass().getName(), new HashMap<Option, OptionAction>());
		}
	}

	public static void registerOption(Object owner, Option o, OptionAction changeAction) {
		HashMap<String, HashMap> map = getInstance().map;
		HashMap settings = map.get(owner.getClass().getName());
		assert settings != null : "Register the owner before registering options; owner-" + owner + " option:"
				+ o.getName();
		if (!settings.containsKey(o)) {
			settings.put(o, changeAction);
			save();
		}
		try {
			Iterator keys = settings.keySet().iterator();
			while (keys.hasNext()) {
				Option opt = (Option) keys.next();
				if (opt.equals(o)) {
					if (settings.get(opt) == null) {
						settings.put(opt, changeAction);
					}
					((OptionAction) settings.get(opt)).actionPerformed(opt.getValue());
					return;
				}
			}
			throw new IllegalStateException("Key not found");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void updateOption(Object owner, Option o) {
		HashMap<String, HashMap> map = getInstance().map;
		HashMap settings = map.get(owner.getClass().getName());
		assert settings != null : "register owner before updating options";
		Object[] options = settings.keySet().toArray();
		Option saved = null;
		for (int i = 0; i < options.length; i++) {
			if (((Option) options[i]).equals(o)) {
				saved = (Option) options[i];
			}
		}
		assert saved != null : "Register options before updating them";
		saved.setValue(o.getValue());
		save();
		try {
			((OptionAction) settings.get(o)).actionPerformed(o.getValue());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void save() {
		StringWriter writer = new StringWriter("ConfigurationManager.ini");
		HashMap<String, HashMap> map = getInstance().map;
		Object[] keys = map.keySet().toArray();
		for (int i = 0; i < keys.length; i++) {
			HashMap settings = map.get(keys[i]);
			Object[] options = settings.keySet().toArray();
			Option[] options2 = new Option[options.length];
			for (int o = 0; o < options.length; o++) {
				options2[o] = (Option) options[o];
			}
			writer.printlns(toFile(keys[i].toString(), options2));
		}
	}

	private static String[] toFile(String owner, Option[] options) {
		String[] out = new String[options.length * 2 + 1];
		out[0] = '[' + owner + ']';
		for (int o = 0; o < options.length; o++) {
			out[o * 2 + 1] = escape(options[o].getName()) + "=" + escape(options[o].getValue());
			out[o * 2 + 2] = escape(options[o].getName()) + ".regex=" + escape(options[o].getRegex());
		}
		return out;
	}

	private static String escape(String in) {
		return in.replaceAll("\\\\", "\\\\b").replaceAll("=", "\\\\e").replaceAll("\\.", "\\\\d")
				.replaceAll("\\[", "\\\\o").replaceAll("\\]", "\\\\c");
	}

	private static void load() {
		StringReader reader = new StringReader("ConfigurationManager.ini");
		String[] elements = reader.readlns();
		if (elements == null || elements.length == 0) {
			return;
		}

		int start = 0;
		int stop = 1;
		while (stop < elements.length) {
			for (int i = stop; i < elements.length && elements[i].matches("[^\\[].*+"); i++) {
				stop = i;
			}

			String[] configurable = new String[stop - start + 1];
			for (int i = 0; i < configurable.length; i++) {
				configurable[i] = elements[start + i];
			}
			fromFile(configurable);

			start = ++stop;
			++stop;
		}
	}

	private static void fromFile(String[] elements) {
		String cname = "";
		if (elements[0].matches("\\[(.*)\\]")) {
			cname = elements[0].substring(1, elements[0].length() - 1);
		} else {
			throw new IllegalArgumentException("First element should be class");
		}

		assert elements.length % 2 == 1 : "Incorrect element count:" + elements.length;
		HashMap<Option, Object> out = new HashMap<Option, Object>();
		for (int i = 0; i < elements.length / 2; i++) {
			String[] nv = elements[i * 2 + 1].split("=");
			String name = unescape(nv[0]);
			String value = nv.length == 2 ? unescape(nv[1]) : "";
			String regex = unescape(elements[i * 2 + 2].split("=")[1]);
			assert elements[i * 2 + 2].startsWith(name + ".regex") : "Illegal state:" + name + "-" + regex;
			out.put(new Option(name, regex, value), null);
		}
		getInstance().map.put(cname, out);
	}

	private static String unescape(String in) {
		return in.replaceAll("\\\\e", "=").replaceAll("\\\\d", ".").replaceAll("\\\\o", "[").replaceAll("\\\\c", "]")
				.replaceAll("\\\\b", "\\\\");
	}

	private static void dump() {
		Iterator it = getInstance().map.keySet().iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}
}
