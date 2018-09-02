package regex;

public class RegexReplace {

	private final String regex;
	private final String replacement;

	public RegexReplace(String regex, String replacement) {
		this.regex = regex;
		this.replacement = replacement;
	}

	public String getRegex() {
		return regex;
	}

	public String getReplacement() {
		return replacement;
	}

	@Override
	public String toString() {
		return regex + " => " + replacement;
	}
}
