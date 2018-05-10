package br.edu.ufsm.menagedbeans;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlGithubValidator {

	private Pattern pattern;
	private Matcher matcher;

	private static final Pattern EMAIL_PATTERN = Pattern.compile("https://github.com/[_A-Za-z0-9-]/[_A-Za-z0-9-]", Pattern.MULTILINE)
		;

	/**
	 * Validate hex with regular expression
	 *
	 * @param hex
	 *            hex for validation
	 * @return true valid hex, false invalid hex
	 */
	public static boolean validate(String hex) {
		System.out.println(hex);
		System.out.println(EMAIL_PATTERN.pattern());
		return hex.matches(EMAIL_PATTERN.pattern());

	}
}