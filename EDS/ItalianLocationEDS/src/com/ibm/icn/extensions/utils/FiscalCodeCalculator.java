package com.ibm.icn.extensions.utils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

/**
 * Utility class for calculating Italian Fiscal Code (Codice Fiscale).
 * 
 * The Italian fiscal code is a 16-character alphanumeric code that uniquely identifies
 * Italian citizens and residents for tax and administrative purposes.
 * 
 * Format: RSSMRA85T10A562S
 * - 3 chars: Surname consonants
 * - 3 chars: First name consonants
 * - 2 digits: Year of birth
 * - 1 char: Month of birth (A-T)
 * - 2 digits: Day of birth (+ 40 for females)
 * - 4 chars: Municipality/Country code (Belfiore code)
 * - 1 char: Check digit
 */
public class FiscalCodeCalculator {
    
    // Month codes for fiscal code
    private static final char[] MONTH_CODES = {
        'A', 'B', 'C', 'D', 'E', 'H', 'L', 'M', 'P', 'R', 'S', 'T'
    };
    
    // Odd position character values for check digit calculation
    private static final Map<Character, Integer> ODD_VALUES = new HashMap<>();
    
    // Even position character values for check digit calculation
    private static final Map<Character, Integer> EVEN_VALUES = new HashMap<>();
    
    // Check digit characters
    private static final char[] CHECK_CHARS = {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
        'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    };
    
    static {
        // Initialize odd position values
        ODD_VALUES.put('0', 1); ODD_VALUES.put('1', 0); ODD_VALUES.put('2', 5);
        ODD_VALUES.put('3', 7); ODD_VALUES.put('4', 9); ODD_VALUES.put('5', 13);
        ODD_VALUES.put('6', 15); ODD_VALUES.put('7', 17); ODD_VALUES.put('8', 19);
        ODD_VALUES.put('9', 21);
        ODD_VALUES.put('A', 1); ODD_VALUES.put('B', 0); ODD_VALUES.put('C', 5);
        ODD_VALUES.put('D', 7); ODD_VALUES.put('E', 9); ODD_VALUES.put('F', 13);
        ODD_VALUES.put('G', 15); ODD_VALUES.put('H', 17); ODD_VALUES.put('I', 19);
        ODD_VALUES.put('J', 21); ODD_VALUES.put('K', 2); ODD_VALUES.put('L', 4);
        ODD_VALUES.put('M', 18); ODD_VALUES.put('N', 20); ODD_VALUES.put('O', 11);
        ODD_VALUES.put('P', 3); ODD_VALUES.put('Q', 6); ODD_VALUES.put('R', 8);
        ODD_VALUES.put('S', 12); ODD_VALUES.put('T', 14); ODD_VALUES.put('U', 16);
        ODD_VALUES.put('V', 10); ODD_VALUES.put('W', 22); ODD_VALUES.put('X', 25);
        ODD_VALUES.put('Y', 24); ODD_VALUES.put('Z', 23);
        
        // Initialize even position values
        for (int i = 0; i <= 9; i++) {
            EVEN_VALUES.put((char)('0' + i), i);
        }
        for (int i = 0; i < 26; i++) {
            EVEN_VALUES.put((char)('A' + i), i);
        }
    }
    
    /**
     * Calculates the Italian fiscal code based on personal information.
     * 
     * @param lastName Last name
     * @param firstName First name
     * @param birthDate Birth date in format dd/MM/yyyy
     * @param gender Gender (M/F)
     * @param birthPlace Birth place (municipality name or foreign country)
     * @param birthPlaceCode Belfiore code for birth place
     * @return The calculated fiscal code, or null if calculation fails
     */
    public static String calculate(String lastName, String firstName, String birthDate,
                                   String gender, String birthPlace, String birthPlaceCode) {
        try {
            System.out.println("FiscalCodeCalculator: Starting calculation");
            System.out.println("  lastName=" + lastName);
            System.out.println("  firstName=" + firstName);
            System.out.println("  birthDate=" + birthDate);
            System.out.println("  gender=" + gender);
            System.out.println("  birthPlace=" + birthPlace);
            System.out.println("  birthPlaceCode=" + birthPlaceCode);
            
            // Validate inputs
            if (lastName == null || lastName.trim().isEmpty() ||
                firstName == null || firstName.trim().isEmpty() ||
                birthDate == null || birthDate.trim().isEmpty() ||
                gender == null || gender.trim().isEmpty() ||
                birthPlaceCode == null || birthPlaceCode.trim().isEmpty()) {
                System.out.println("FiscalCodeCalculator: Missing required fields");
                return null;
            }
            
            // Normalize inputs
            lastName = lastName.trim().toUpperCase();
            firstName = firstName.trim().toUpperCase();
            gender = gender.trim().toUpperCase();
            birthPlaceCode = birthPlaceCode.trim().toUpperCase();
            
            // Build fiscal code
            StringBuilder fiscalCode = new StringBuilder();
            
            // 1. Surname code (3 chars)
            fiscalCode.append(encodeSurname(lastName));
            
            // 2. First name code (3 chars)
            fiscalCode.append(encodeFirstName(firstName));
            
            // 3. Birth date and gender (5 chars: YY + M + DD)
            fiscalCode.append(encodeBirthDateAndGender(birthDate, gender));
            
            // 4. Birth place code (4 chars)
            fiscalCode.append(birthPlaceCode);
            
            // 5. Check digit (1 char)
            fiscalCode.append(calculateCheckDigit(fiscalCode.toString()));
            
            String result = fiscalCode.toString();
            System.out.println("FiscalCodeCalculator: Calculated fiscal code=" + result);
            
            return result;
            
        } catch (Exception e) {
            System.err.println("FiscalCodeCalculator: Error calculating fiscal code: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Encodes the surname into 3 characters.
     * Uses consonants first, then vowels, padding with X if needed.
     */
    private static String encodeSurname(String surname) {
        String consonants = extractConsonants(surname);
        String vowels = extractVowels(surname);
        
        StringBuilder code = new StringBuilder();
        code.append(consonants);
        code.append(vowels);
        
        // Pad with X if needed
        while (code.length() < 3) {
            code.append('X');
        }
        
        return code.substring(0, 3);
    }
    
    /**
     * Encodes the first name into 3 characters.
     * Special rule: if there are 4+ consonants, use 1st, 3rd, and 4th.
     * Otherwise, use consonants then vowels, padding with X if needed.
     */
    private static String encodeFirstName(String firstName) {
        String consonants = extractConsonants(firstName);
        String vowels = extractVowels(firstName);
        
        StringBuilder code = new StringBuilder();
        
        if (consonants.length() >= 4) {
            // Special rule: use 1st, 3rd, and 4th consonant
            code.append(consonants.charAt(0));
            code.append(consonants.charAt(2));
            code.append(consonants.charAt(3));
        } else {
            code.append(consonants);
            code.append(vowels);
            
            // Pad with X if needed
            while (code.length() < 3) {
                code.append('X');
            }
        }
        
        return code.substring(0, 3);
    }
    
    /**
     * Encodes birth date and gender into 5 characters (YY + M + DD).
     * For females, add 40 to the day.
     */
    private static String encodeBirthDateAndGender(String birthDate, String gender) throws Exception {
        // ICN serializes xs:date fields by converting the user's local midnight to UTC.
        // e.g. 8 April 1970 entered in a browser at CEST (UTC+1 DST) becomes
        // "1970-04-07T23:00:00.000Z" — the date part alone gives the wrong day.
        //
        // The correct approach: a birth date has no timezone — it is always the date
        // the user typed. ICN encodes "local midnight" as UTC, so:
        //   UTC hour >= 12 → browser is east of UTC  → real day = UTC date + 1
        //   UTC hour == 0  → browser is at UTC        → real day = UTC date
        //   UTC hour < 12  → browser is west of UTC  → real day = UTC date (hours lost going west)
        //
        // This works for all timezones UTC-12 to UTC+14 because no timezone shift
        // moves midnight more than 14 hours in either direction.
        String normalizedDate = birthDate.trim();

        int year, month, day;

        if (normalizedDate.contains("/")) {
            // Format: dd/MM/yyyy — no timezone component, parse directly
            String[] parts = normalizedDate.split("/");
            day   = Integer.parseInt(parts[0]);
            month = Integer.parseInt(parts[1]);
            year  = Integer.parseInt(parts[2]) % 100;
        } else if (normalizedDate.length() > 10) {
            // Full ISO 8601 with time and Z suffix
            // Format: yyyy-MM-ddTHH:mm:ss.SSSZ or yyyy-MM-ddTHH:mm:ssZ
            String[] dateTimeParts = normalizedDate.split("T");
            String[] dateParts = dateTimeParts[0].split("-");
            String timePart = dateTimeParts[1].replaceAll("Z$", "").replaceAll("\\.\\d+$", "");
            String[] timeParts = timePart.split(":");

            int utcYear  = Integer.parseInt(dateParts[0]);
            int utcMonth = Integer.parseInt(dateParts[1]); // 1-based
            int utcDay   = Integer.parseInt(dateParts[2]);
            int utcHour  = Integer.parseInt(timeParts[0]);

            // If UTC hour >= 12, the user's local midnight was on the next UTC day,
            // meaning the real date is utcDay + 1. Use Calendar to handle month rollover.
            if (utcHour >= 12) {
                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                cal.set(utcYear, utcMonth - 1, utcDay, utcHour, 0, 0);
                cal.add(Calendar.DAY_OF_MONTH, 1);
                year  = cal.get(Calendar.YEAR) % 100;
                month = cal.get(Calendar.MONTH) + 1;
                day   = cal.get(Calendar.DAY_OF_MONTH);
            } else {
                year  = utcYear % 100;
                month = utcMonth;
                day   = utcDay;
            }
        } else {
            // Format: yyyy-MM-dd — no time component, parse directly
            String[] parts = normalizedDate.split("-");
            year  = Integer.parseInt(parts[0]) % 100;
            month = Integer.parseInt(parts[1]);
            day   = Integer.parseInt(parts[2]);
        }
        
        // For females, add 40 to day
        if (gender.equals("F") || gender.equals("FEMMINA") || gender.equals("FEMALE")) {
            day += 40;
        }
        
        // Build code
        StringBuilder code = new StringBuilder();
        code.append(String.format("%02d", year));
        code.append(MONTH_CODES[month - 1]);
        code.append(String.format("%02d", day));
        
        return code.toString();
    }
    
    /**
     * Calculates the check digit (last character of fiscal code).
     */
    private static char calculateCheckDigit(String fiscalCode) {
        int sum = 0;
        
        for (int i = 0; i < fiscalCode.length(); i++) {
            char c = fiscalCode.charAt(i);
            
            if (i % 2 == 0) {
                // Odd position (1-based indexing)
                sum += ODD_VALUES.get(c);
            } else {
                // Even position (1-based indexing)
                sum += EVEN_VALUES.get(c);
            }
        }
        
        return CHECK_CHARS[sum % 26];
    }
    
    /**
     * Extracts consonants from a string.
     */
    private static String extractConsonants(String str) {
        StringBuilder consonants = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (Character.isLetter(c) && !isVowel(c)) {
                consonants.append(c);
            }
        }
        return consonants.toString();
    }
    
    /**
     * Extracts vowels from a string.
     */
    private static String extractVowels(String str) {
        StringBuilder vowels = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (isVowel(c)) {
                vowels.append(c);
            }
        }
        return vowels.toString();
    }
    
    /**
     * Checks if a character is a vowel.
     */
    private static boolean isVowel(char c) {
        c = Character.toUpperCase(c);
        return c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U';
    }
    
    /**
     * Finds the Belfiore code for a municipality.
     * 
     * @param municipalityName Name of the municipality
     * @param allComuni JSONArray of all municipalities
     * @return The Belfiore code, or null if not found
     */
    public static String findMunicipalityBelfioreCode(String municipalityName, JSONArray allComuni) {
        try {
            if (municipalityName == null || municipalityName.trim().isEmpty()) {
                return null;
            }
            
            String normalizedName = municipalityName.trim();
            
            for (int i = 0; i < allComuni.size(); i++) {
                JSONObject comune = (JSONObject) allComuni.get(i);
                String denominazione = comune.get("denominazione_ita").toString();
                
                if (denominazione.equals(normalizedName)) {
                    return comune.get("codice_belfiore").toString();
                }
            }
            
            System.err.println("FiscalCodeCalculator: Municipality not found: " + municipalityName);
            return null;
            
        } catch (Exception e) {
            System.err.println("FiscalCodeCalculator: Error finding municipality code: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Finds the tax code for a foreign country.
     * 
     * @param countryCode ISO country code (e.g., "USA", "FRA")
     * @param allStati JSONArray of all countries
     * @return The tax code, or null if not found
     */
    public static String findCountryTaxCode(String countryCode, JSONArray allStati) {
        try {
            if (countryCode == null || countryCode.trim().isEmpty()) {
                return null;
            }
            
            String normalizedCode = countryCode.trim().toUpperCase();
            
            // Italy doesn't have a tax code (birth place is municipality)
            if (normalizedCode.equals("ITA") || normalizedCode.equals("ITALIA")) {
                return null;
            }
            
            for (int i = 0; i < allStati.size(); i++) {
                JSONObject stato = (JSONObject) allStati.get(i);
                String value = stato.get("value").toString();
                
                if (value.equals(normalizedCode)) {
                    Object taxCode = stato.get("taxcode_country_code");
                    if (taxCode != null && !taxCode.toString().isEmpty()) {
                        return taxCode.toString();
                    }
                }
            }
            
            System.err.println("FiscalCodeCalculator: Country tax code not found for: " + countryCode);
            return null;
            
        } catch (Exception e) {
            System.err.println("FiscalCodeCalculator: Error finding country tax code: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}

// Made with Bob
