package com.ninetwozero.internationumber.utils;

import com.ninetwozero.internationumber.database.dao.Country;

public class NumberConverter {
    private final Country country;
    private final String[] prefixes;

    public NumberConverter(final Country country) {
        this.country = country;
        this.prefixes = country.getPrefixes().split(Country.DELIMITER);
    }

    public Country getCountry() {
        return country;
    }

    public String convert(final String number) {
        if (number == null) {
            throw new IllegalArgumentException("The input number is null.");
        }

        // If the number starts with +, we return the same number as it's already international
        if (number.charAt(0) == '+') {
            return number;
        }

        // Is this a command number?
        if (number.charAt(0) == '*') {
            // TODO: Handle command numbers
        }

        // TODO: Handle emergency numbers and so on (more specifically - numbers with a certain length)
        final StringBuilder builder = new StringBuilder(number.length());
        builder.append("+").append(country.getCode());
        if (prefixes.length > 0) {
            for (String prefix : prefixes) {
                if (number.startsWith(prefix)) {
                    builder.append(number.substring(prefix.length()));
                    break;
                }
            }
        } else {
            builder.append(number);
        }
        return builder.toString();
    }
}
