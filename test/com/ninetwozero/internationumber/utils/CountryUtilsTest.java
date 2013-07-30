package com.ninetwozero.internationumber.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CountryUtilsTest {
	
	private CountryUtils mCountryUtils = new CountryUtils(46, new String[] { "0" });

	@Test
	public void verifyThatWePrefixRandomStringsToo() {
		String convertedNumber = mCountryUtils.convert("ninetwozero");
		assertEquals("+46ninetwozero", convertedNumber);
	}
	
	@Test
	public void getTheInputBackWhenItIsAlreadyPrefixedWithPlus() {
		String number = "+46701234567";
		String convertedNumber = mCountryUtils.convert(number);
		assertEquals(number, convertedNumber);
	}
	
	@Test
	public void getNumberWhenWeHaveZeroLengthTrunkDigits() {
		CountryUtils countryUtils = new CountryUtils(46, new String[] { "" });
		String convertedNumber = countryUtils.convert("0701234567");
		assertEquals("+460701234567", convertedNumber);
	}

	@Test
	public void getNumberWhenWeHaveDoubleZerosInIt() {
		String number = "0850072123";
		String convertedNumber = mCountryUtils.convert(number);
		assertEquals("+46850072123", convertedNumber);
	}

	@Test
	public void getNumberWhenWeHaveDoubleZerosWithCountryPrefix(){
		String number = "0046850072123";
		String convertedNumber = mCountryUtils.convert(number);
		assertEquals("+46850072123", convertedNumber);
	}

	@Test
	public void getNumberWhenInputIsDomestic() {
		String number = "0701234567";
		String convertedNumber = mCountryUtils.convert(number);
		assertEquals("+46701234567", convertedNumber);
	}
	
	@Test
	public void getNumberWhenCountryDoesntHaveTrunkDigits() {
		CountryUtils countryUtils = new CountryUtils(47, new String[] {});
		String number = "71234567";
		String convertedNumber = countryUtils.convert(number);
		assertEquals("+4771234567", convertedNumber);
	}
	
	@Test
	public void getNumberWhenCountryHasMoreThanOneTrunkDigit() {
		// Only Lithuania
		CountryUtils countryUtils = new CountryUtils(370, new String[] {"0", "8"});
		String number1 = "01234567";
		String number2 = "81234567";
		
		String convertedNumber1 = countryUtils.convert(number1);
		String convertedNumber2 = countryUtils.convert(number2);

		assertEquals("+3701234567", convertedNumber1);
		assertEquals("+3701234567", convertedNumber2);
	}
	
	@Test
	public void getNumberWhenIsCertainAsterisk() {
		String number = "#31#1234567";
		String convertedNumber = mCountryUtils.convert(number);
		assertEquals("#31#+461234567", convertedNumber);
	}
	
	@Test
	public void getNumberWhenCountryHasTrunkDigitsButNumberDoesnt() {
		String number = "71234567";
		String convertedNumber = mCountryUtils.convert(number);
		assertEquals("+4671234567", convertedNumber);
	}
	
	@Test
	public void getNumberWhenInputHasAsteriskCommands() {
		String number = "*33*0701234567";
		String convertedNumber = mCountryUtils.convert(number);
		assertEquals("*33*+46701234567", convertedNumber);
	}
	
	@Test
	public void getNumberWhenInputHasAsteriskCommandsEndingWithHashtag() {
		String number = "*33*0701234567#";
		String convertedNumber = mCountryUtils.convert(number);
		assertEquals("*33*+46701234567#", convertedNumber);
	}
	
	@Test
	public void getNumberWhenInputHasHashCommands() {
		String number = "#33#701234567";
		String convertedNumber = mCountryUtils.convert(number);
		assertEquals("#33#+46701234567", convertedNumber);
	}
	
	@Test
	public void getNumberWhenInputIsInternationalAndHasAsteriskCommands() {
		String number = "*33*+46701234567";
		String convertedNumber = mCountryUtils.convert(number);
		assertEquals("*33*+46701234567", convertedNumber);
	}
	
	@Test
	public void getNumberWhenInputIsInternationalAndHasHashCommands() {
		String number = "#33#+46701234567";
		String convertedNumber = mCountryUtils.convert(number);
		assertEquals("#33#+46701234567", convertedNumber);
	}
	
	@Test
	public void getNumberWhenInputHasCountryCodeButWithDoubleZero() {
		String number = "0046701234567";
		String convertedNumber = mCountryUtils.convert(number);
		assertEquals("+46701234567", convertedNumber);
	}
	
	@Test
	public void getNumberWhenInputHasCountryCodeWithoutDoubleZeroOrPlus() {
		String number = "46701234567";
		String convertedNumber = mCountryUtils.convert(number);
		assertEquals("+46701234567", convertedNumber);
	}
	
	@Test
	public void getNumberWhenInputIsOnlyCommand() throws Exception {
		String number = "*120#";
		String convertedNumber = mCountryUtils.convert(number);
		assertEquals("*120#", convertedNumber);
	}
}
