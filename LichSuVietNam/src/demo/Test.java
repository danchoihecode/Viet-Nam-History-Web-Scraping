package demo;

public class Test {

	public static String capitalizeFirstLetter(String input) {
	    StringBuilder output = new StringBuilder();
	    boolean capitalizeNext = true;

	    for (char c : input.toCharArray()) {
	        if (Character.isWhitespace(c)) {
	            capitalizeNext = true;
	        } else if (capitalizeNext) {
	            output.append(Character.toUpperCase(c));
	            capitalizeNext = false;
	        } else {
	            output.append(Character.toLowerCase(c));
	        }
	    }

	    return output.toString();
	}

	public static void main(String[] args) {
		String input = "hello world";
		String output = capitalizeFirstLetter(input);
		System.out.println(output);

	}

}
