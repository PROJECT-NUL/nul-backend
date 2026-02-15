package com.springboot.utils;

import java.util.Locale;

/**
 * Created on Ağustos, 2020
 *
 * @author Faruk
 */
public final class ProjectConstants {

	// FIXME : Customize project constants for your application.

	public static final String DEFAULT_ENCODING = "UTF-8";

        public static final Locale TURKISH_LOCALE = new Locale.Builder().setLanguage("tr").setRegion("TR").build();

        public static final Locale KOREAN_LOCALE = new Locale.Builder().setLanguage("ko").setRegion("KR").build();

        public static final Locale DEFAULT_LOCALE = KOREAN_LOCALE;

	private ProjectConstants() {

		throw new UnsupportedOperationException();
	}

}
