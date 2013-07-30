/*
	This file is part of Internationumber

    Internationumber is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Internationumber is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
 */

package com.ninetwozero.internationumber.utils;

final public class CountryUtils {
	private final int mCountryCode;
	private final String[] mTrunkDigits;

	public CountryUtils(int countryCode, String[] trunkDigits) {
		mCountryCode = countryCode;
		mTrunkDigits = trunkDigits;
	}
	
	public boolean hasCountry() {
		return mCountryCode > 0 && mTrunkDigits != null;
	}
	
	public String convert(String inputNumber) {
		String number = normalize(inputNumber);
		if( isDomestic(number) ) {
			if( hasCommandPrefix(number) ) {
				number = getNumberWithCommandPrefix(number);
			} else if( hasCountryCodeButNoPlusCharacter(number) ) {
				number = "+" + number;
			} else if( hasTrunkDigits(inputNumber) ) {
				number = getNumberWithoutTrunkDigits(number);				
			} else {
				number = buildRegularNumber(number);
			}
		}
		return number;
	}
	
	private boolean hasTrunkDigit(String number, String trunkDigit) {
		return (!trunkDigit.equals("") && number.startsWith(trunkDigit));
	}

	private boolean hasTrunkDigits(String number) {
		for( String trunkDigit : mTrunkDigits) {
			if( hasTrunkDigit(number, trunkDigit) ) {
				return true;
			}
		}
		return false;
	}

	private boolean isDomestic(String number) {
		return number.indexOf('+') == -1;
	}

	private boolean hasCommandPrefix(String number) {
		return number.charAt(0) == '*' || number.charAt(0) == '#';
	}

	private boolean hasCountryCodeButNoPlusCharacter(String number) {
		return number.startsWith(String.valueOf(mCountryCode));
	}

	private String getNumberWithCommandPrefix(String inputNumber) {
		final String number = inputNumber;
		final char character = number.charAt(0);
		final int positionOfClosingCharacter = number.indexOf(character, 1);
		
		if( positionOfClosingCharacter > -1 ) {
			final String command = number.substring(0, positionOfClosingCharacter+1);
			final String rawNumber = number.substring(positionOfClosingCharacter+1);
			return buildNumberWithCommandPrefix(command, rawNumber);
		}
		return number;
	}

	private String buildNumberWithCommandPrefix(String command, String number) {
		if( hasTrunkDigits(number) ) {
			return command + getNumberWithoutTrunkDigits(number);
		} else {
			return command + buildRegularNumber(number);
		}
	}

	private String getNumberWithoutTrunkDigits(String inputNumber) {
		String number = inputNumber;
		final int numberOfTrunkDigits = mTrunkDigits.length;
		if( numberOfTrunkDigits > 0 ) {
			if( numberOfTrunkDigits == 1 ) {
				number = buildNumberWithoutTrunkDigits(number, mTrunkDigits[0]);
			} else {
				number = removeTrunkDigit(number);
			}
		}
		if( number.charAt(0) != '+' ){
			number = buildRegularNumber(number);
		}
		return number;
	}

	private String removeTrunkDigit(String inputNumber) {
		String number = inputNumber;
		for( String trunkDigit : mTrunkDigits ) {
			if( hasTrunkDigit(number, trunkDigit) ) {
				number = number.substring(1);
			}
		}
		return number;
	}
	
	private String buildNumberWithoutTrunkDigits(String inputNumber, String prefix) {
		String number = inputNumber;
		if( hasTrunkDigits(inputNumber) ){
			if( trunkDigitsAreMoreThanOne(prefix) ) {
				number = buildRegularNumber(number.substring(prefix.length()));
			} else if( number.charAt(0) == prefix.charAt(0) ) {
				number = buildRegularNumber(number.substring(1));
			} else if( number.charAt(0) != prefix.charAt(0) ) {
				number = buildRegularNumber(number);
			} else {
				number = "";
			}
		}
		System.out.println("");
		return number;
	}

	private boolean trunkDigitsAreMoreThanOne(String prefix) {
		return prefix.length() > 1;
	}	
	
	private String buildRegularNumber(String number) {
		return '+' + String.valueOf(mCountryCode) + number;
	}
	
	private String normalize(String number) {
		String normalizedNumber = number.replaceAll("[ -]+", "");
		if( normalizedNumber.startsWith("00") ) {
			normalizedNumber = normalizedNumber.replaceFirst("00", "+");
		}
		return normalizedNumber;
	}
}
