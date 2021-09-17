package toolkit;

public class Option {

	String name;
	String regex;
	String value;

	public Option(String n, String r, String v) {
		if (n == null || n.equals("")) {
			throw new IllegalArgumentException("n cannot be null or \"\"");
		}
		if (!v.matches(r)) {
			throw new IllegalArgumentException("v doesn't match the specified regex; value:" + v + " regex:" + r);
		}

		name = n;
		regex = r;
		value = v;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public String getRegex() {
		return regex;
	}

	public void setValue(String v) {
		if (!v.matches(regex)) {
			throw new IllegalArgumentException("n doesn't match the specified regex");
		}
		value = v;
	}

	public Option cloneOption() {
		return new Option(name, regex, value);
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Option)) {
			return false;
		}
		Option other = (Option) o;
		return other.name.equals(name);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public String toString() {
		return "option:" + name + "[" + value + "]-regex:" + regex;
	}
}
