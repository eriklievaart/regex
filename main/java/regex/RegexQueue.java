package regex;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class RegexQueue extends Observable {

	private List<RegexReplace> queue = new ArrayList<>();

	public void add(String regex, String replace) {
		queue.add(new RegexReplace(regex, replace));
		setChanged();
		notifyObservers(queue.toArray(new RegexReplace[] {}));
	}

	public String process(String body) {
		String modified = body;

		for (RegexReplace mutation : queue) {
			modified = modified.replaceAll(mutation.getRegex(), mutation.getReplacement());
		}
		return modified;
	}
}
