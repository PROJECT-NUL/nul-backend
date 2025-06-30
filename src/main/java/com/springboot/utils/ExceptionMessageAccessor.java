package com.springboot.utils;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Objects;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * Created on Ağustos, 2020
 *
 * @author Faruk
 */
@Service
public class ExceptionMessageAccessor {

	private final MessageSource messageSource;

	ExceptionMessageAccessor(@Qualifier("exceptionMessageSource") MessageSource messageSource) {
		this.messageSource = messageSource;
	}

        public String getMessage(Locale locale, String key, Object... parameter) {

                Locale resolved = Objects.nonNull(locale) ? locale : LocaleContextHolder.getLocale();

                if (Objects.isNull(resolved)) {
                        resolved = ProjectConstants.DEFAULT_LOCALE;
                }

                return messageSource.getMessage(key, parameter, resolved);
        }

}
