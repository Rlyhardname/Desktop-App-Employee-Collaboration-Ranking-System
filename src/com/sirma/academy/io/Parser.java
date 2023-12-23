package com.sirma.academy.io;

import com.sirma.academy.exception.UnsupportedLocalDateFormatException;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class Parser {

    public static Long parseLong(String item) {
        try {
            return Long.parseLong(item);
        } catch (NumberFormatException e) {
            return null;
        }

    }

    public static LocalDate parseDate(String item, DateTimeFormatter formatter) {
        try {
            return LocalDate.parse(item,formatter);
        } catch (DateTimeException e) {
            return null;
        }

    }

    public static DateTimeFormatter formatterUsed(String date) {
        String[] commonPatterns = {
                "d.M.yyyy", "M.d.yyyy", "yyyy.M.d", "dd.MM.yyyy", "MM.dd.yyyy", "yyyy.MM.dd",
                "yyyy-MM-dd", "MM/dd/yyyy", "dd-MMM-yyyy", "yyyy/MM/dd", "dd/MM/yyyy", "MM-dd-yyyy",
                "yyyyMMdd", "ddMMyyyy", "MMyyyydd", "yyMMdd", "ddMMMyyyy", "MMMddyyyy"
        };
        for (String pattern : commonPatterns) {
            try {
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern, Locale.ENGLISH);
                LocalDate dt = LocalDate.parse(date, dateTimeFormatter);
                return dateTimeFormatter;
            } catch (DateTimeParseException e) {

            }
        }

        throw new UnsupportedLocalDateFormatException();
    }
}