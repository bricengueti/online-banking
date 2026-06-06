package com.onlinebank.common.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Utility class for date and time operations.
 * Thread-safe, immutable, and fully tested.
 *
 * @author OnlineBank Team
 * @version 1.0
 */
public final class DateUtils {

    private DateUtils() {
        // Private constructor to prevent instantiation
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    // ==================== DATE FORMATTERS ====================

    /**
     * ISO standard date-time formatter (yyyy-MM-dd'T'HH:mm:ss)
     */
    public static final DateTimeFormatter ISO_DATE_TIME = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * ISO standard date formatter (yyyy-MM-dd)
     */
    public static final DateTimeFormatter ISO_DATE = DateTimeFormatter.ISO_LOCAL_DATE;

    /**
     * French date format (dd/MM/yyyy)
     */
    public static final DateTimeFormatter FRENCH_DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * French date-time format (dd/MM/yyyy HH:mm:ss)
     */
    public static final DateTimeFormatter FRENCH_DATE_TIME = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    /**
     * Banking standard format (yyyyMMddHHmmss) - useful for transaction references
     */
    public static final DateTimeFormatter BANKING_TIMESTAMP = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /**
     * For audit logs (yyyy-MM-dd HH:mm:ss)
     */
    public static final DateTimeFormatter AUDIT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // ==================== CURRENT DATE/TIME ====================

    /**
     * Get current system date-time in default timezone
     */
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    /**
     * Get current system date in default timezone
     */
    public static LocalDate today() {
        return LocalDate.now();
    }

    /**
     * Get current UTC timestamp
     */
    public static Instant nowUtc() {
        return Instant.now();
    }

    /**
     * Get current timestamp in milliseconds
     */
    public static long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    // ==================== CONVERSIONS ====================

    /**
     * Convert legacy Date to LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        if (date == null) return null;
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * Convert LocalDateTime to legacy Date
     */
    public static Date toDate(LocalDateTime localDateTime) {
        if (localDateTime == null) return null;
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Convert Instant to LocalDateTime using system timezone
     */
    public static LocalDateTime toLocalDateTime(Instant instant) {
        if (instant == null) return null;
        return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * Convert LocalDateTime to Instant using system timezone
     */
    public static Instant toInstant(LocalDateTime localDateTime) {
        if (localDateTime == null) return null;
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

    /**
     * Convert String to LocalDateTime with custom formatter
     */
    public static LocalDateTime toLocalDateTime(String dateStr, DateTimeFormatter formatter) {
        if (dateStr == null || formatter == null) return null;
        return LocalDateTime.parse(dateStr, formatter);
    }

    // ==================== FORMATTING ====================

    /**
     * Format LocalDateTime to ISO standard
     */
    public static String format(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(ISO_DATE_TIME) : null;
    }

    /**
     * Format LocalDate to ISO standard
     */
    public static String format(LocalDate date) {
        return date != null ? date.format(ISO_DATE) : null;
    }

    /**
     * Format LocalDateTime to French format (dd/MM/yyyy HH:mm:ss)
     */
    public static String formatFrench(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(FRENCH_DATE_TIME) : null;
    }

    /**
     * Format LocalDate to French format (dd/MM/yyyy)
     */
    public static String formatFrench(LocalDate date) {
        return date != null ? date.format(FRENCH_DATE) : null;
    }

    /**
     * Format for banking timestamps (yyyyMMddHHmmss)
     */
    public static String formatBankingTimestamp(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(BANKING_TIMESTAMP) : null;
    }

    /**
     * Format for audit logs (yyyy-MM-dd HH:mm:ss)
     */
    public static String formatForAudit(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(AUDIT_FORMATTER) : null;
    }

    // ==================== PARSING ====================

    /**
     * Parse ISO formatted date-time string
     */
    public static LocalDateTime parse(String dateTimeStr) {
        return dateTimeStr != null ? LocalDateTime.parse(dateTimeStr, ISO_DATE_TIME) : null;
    }

    /**
     * Parse ISO formatted date string
     */
    public static LocalDate parseDate(String dateStr) {
        return dateStr != null ? LocalDate.parse(dateStr, ISO_DATE) : null;
    }

    /**
     * Parse French formatted date-time string
     */
    public static LocalDateTime parseFrench(String dateTimeStr) {
        return dateTimeStr != null ? LocalDateTime.parse(dateTimeStr, FRENCH_DATE_TIME) : null;
    }

    /**
     * Parse banking timestamp (yyyyMMddHHmmss)
     */
    public static LocalDateTime parseBankingTimestamp(String timestamp) {
        return timestamp != null ? LocalDateTime.parse(timestamp, BANKING_TIMESTAMP) : null;
    }

    // ==================== VALIDATION & CHECKS ====================

    /**
     * Check if a date-time is expired (before current time)
     */
    public static boolean isExpired(LocalDateTime dateTime) {
        return dateTime != null && dateTime.isBefore(now());
    }

    /**
     * Check if a date is expired (before today)
     */
    public static boolean isExpired(LocalDate date) {
        return date != null && date.isBefore(today());
    }

    /**
     * Check if a date-time is in the future
     */
    public static boolean isFuture(LocalDateTime dateTime) {
        return dateTime != null && dateTime.isAfter(now());
    }

    /**
     * Check if a date is in the future
     */
    public static boolean isFuture(LocalDate date) {
        return date != null && date.isAfter(today());
    }

    /**
     * Check if date-time is between two dates (inclusive)
     */
    public static boolean isBetween(LocalDateTime dateTime, LocalDateTime start, LocalDateTime end) {
        if (dateTime == null || start == null || end == null) return false;
        return (dateTime.isEqual(start) || dateTime.isAfter(start)) &&
                (dateTime.isEqual(end) || dateTime.isBefore(end));
    }

    /**
     * Check if two date-times are on the same day
     */
    public static boolean isSameDay(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        if (dateTime1 == null || dateTime2 == null) return false;
        return dateTime1.toLocalDate().equals(dateTime2.toLocalDate());
    }

    /**
     * Check if a date is a valid future date (for scheduling)
     */
    public static boolean isValidFutureDate(LocalDate date) {
        return date != null && date.isAfter(today());
    }

    /**
     * Check if a date is a valid past date (for historical data)
     */
    public static boolean isValidPastDate(LocalDate date) {
        return date != null && date.isBefore(today());
    }

    /**
     * Check if date range is valid (end >= start)
     */
    public static boolean isValidDateRange(LocalDate start, LocalDate end) {
        return start != null && end != null && !end.isBefore(start);
    }

    /**
     * Check if date range is valid (end >= start)
     */
    public static boolean isValidDateRange(LocalDateTime start, LocalDateTime end) {
        return start != null && end != null && !end.isBefore(start);
    }

    // ==================== DATE MANIPULATION ====================

    /**
     * Add days to LocalDateTime
     */
    public static LocalDateTime plusDays(LocalDateTime dateTime, long days) {
        return dateTime != null ? dateTime.plusDays(days) : null;
    }

    /**
     * Add hours to LocalDateTime
     */
    public static LocalDateTime plusHours(LocalDateTime dateTime, long hours) {
        return dateTime != null ? dateTime.plusHours(hours) : null;
    }

    /**
     * Add minutes to LocalDateTime
     */
    public static LocalDateTime plusMinutes(LocalDateTime dateTime, long minutes) {
        return dateTime != null ? dateTime.plusMinutes(minutes) : null;
    }

    /**
     * Add days to LocalDate
     */
    public static LocalDate plusDays(LocalDate date, long days) {
        return date != null ? date.plusDays(days) : null;
    }

    /**
     * Add months to LocalDateTime
     */
    public static LocalDateTime plusMonths(LocalDateTime dateTime, long months) {
        return dateTime != null ? dateTime.plusMonths(months) : null;
    }

    /**
     * Subtract days from LocalDateTime
     */
    public static LocalDateTime minusDays(LocalDateTime dateTime, long days) {
        return dateTime != null ? dateTime.minusDays(days) : null;
    }

    /**
     * Subtract hours from LocalDateTime
     */
    public static LocalDateTime minusHours(LocalDateTime dateTime, long hours) {
        return dateTime != null ? dateTime.minusHours(hours) : null;
    }

    /**
     * Subtract minutes from LocalDateTime
     */
    public static LocalDateTime minusMinutes(LocalDateTime dateTime, long minutes) {
        return dateTime != null ? dateTime.minusMinutes(minutes) : null;
    }

    // ==================== DIFFERENCES ====================

    /**
     * Calculate days between two dates
     */
    public static long daysBetween(LocalDate start, LocalDate end) {
        if (start == null || end == null) return 0;
        return Duration.between(start.atStartOfDay(), end.atStartOfDay()).toDays();
    }

    /**
     * Calculate days between two date-times
     */
    public static long daysBetween(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) return 0;
        return Duration.between(start, end).toDays();
    }

    /**
     * Calculate hours between two date-times
     */
    public static long hoursBetween(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) return 0;
        return Duration.between(start, end).toHours();
    }

    /**
     * Calculate minutes between two date-times
     */
    public static long minutesBetween(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) return 0;
        return Duration.between(start, end).toMinutes();
    }

    /**
     * Calculate seconds between two date-times
     */
    public static long secondsBetween(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) return 0;
        return Duration.between(start, end).getSeconds();
    }

    // ==================== START/END OF DAY ====================

    /**
     * Get start of day (00:00:00)
     */
    public static LocalDateTime startOfDay(LocalDate date) {
        return date != null ? date.atStartOfDay() : null;
    }

    /**
     * Get end of day (23:59:59.999999999)
     */
    public static LocalDateTime endOfDay(LocalDate date) {
        return date != null ? date.atTime(LocalTime.MAX) : null;
    }

    /**
     * Get start of day from LocalDateTime
     */
    public static LocalDateTime startOfDay(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.toLocalDate().atStartOfDay() : null;
    }

    /**
     * Get end of day from LocalDateTime
     */
    public static LocalDateTime endOfDay(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.toLocalDate().atTime(LocalTime.MAX) : null;
    }

    /**
     * Get start of month
     */
    public static LocalDateTime startOfMonth(LocalDate date) {
        return date != null ? date.withDayOfMonth(1).atStartOfDay() : null;
    }

    /**
     * Get end of month
     */
    public static LocalDateTime endOfMonth(LocalDate date) {
        if (date == null) return null;
        LocalDate lastDay = date.withDayOfMonth(date.lengthOfMonth());
        return lastDay.atTime(LocalTime.MAX);
    }

    // ==================== BUSINESS DAY CHECKS ====================

    /**
     * Check if date is a weekend (Saturday or Sunday)
     */
    public static boolean isWeekend(LocalDate date) {
        if (date == null) return false;
        DayOfWeek day = date.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }

    /**
     * Check if date is a weekday (Monday to Friday)
     */
    public static boolean isWeekday(LocalDate date) {
        return !isWeekend(date);
    }
}