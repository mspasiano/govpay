/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2022 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package it.govpay.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import it.govpay.core.exceptions.ValidationException;

/**
 * Funzioni di utilita' per la gestione del parsing e della formattazione delle date.
 * 
 * @author Pintori Giuliano (pintori@link.it)
 *
 */
public class SimpleDateFormatUtils {
	
	public static SimpleDateFormatUtils getInstance() {
		return new SimpleDateFormatUtils();
	}
	private static final String PATTERN_DATA_JSON_YYYY_MM_DD_T_HH_MM_SS_SSS_Z = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	private static final String PATTERN_DATA_JSON_YYYY_MM_DD_T_HH_MM_SS_SSS = "yyyy-MM-dd'T'HH:mm:ss.SSS";
	private static final String PATTERN_DATA_JSON_YYYY_MM_DD_T_HH_MM = "yyyy-MM-dd'T'HH:mm";
	private static final String PATTERN_DATA_JSON_YYYY_MM_DD_T_HH_MM_SS = "yyyy-MM-dd'T'HH:mm:ss";
	private static final String PATTERN_DATA_JSON_YYYY_MM_DD = "yyyy-MM-dd";
	private static final String PATTERN_DATA_JSON_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
	private static final String PATTERN_DATA_DD_MM_YYYY_HH_MM_SS_SSS = "ddMMyyyyHHmmSSsss";
	private static final String PATTERN_DATA_YYYY = "yyyy";
	private static final String PATTERN_DATA_YYYY_MM_DD_SENZA_SPAZI = "yyyyMMdd";
	private static final String PATTERN_DATA_YYYY_MM_DD_HH_MM_SENZA_SPAZI = "yyyyMMdd_HHmm";
	private static final String PATTERN_DATA_GG_MM_AAAA = "dd/MM/yyyy";
	
	public static List<String> datePatterns = null;
	static {
		datePatterns = new ArrayList<>();
		datePatterns.add(DateFormatUtils.ISO_DATE_FORMAT.getPattern());
		datePatterns.add(DateFormatUtils.ISO_DATETIME_FORMAT.getPattern());
		datePatterns.add(DateFormatUtils.ISO_DATE_TIME_ZONE_FORMAT.getPattern());
		datePatterns.add(DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.getPattern());
	}
	
	public static List<String> datePatternsRest = null;
	static {
		datePatternsRest = new ArrayList<>();
		datePatternsRest.addAll(datePatterns);
		datePatternsRest.add(PATTERN_DATA_JSON_YYYY_MM_DD_T_HH_MM_SS_SSS_Z);
		datePatternsRest.add(PATTERN_DATA_JSON_YYYY_MM_DD_T_HH_MM_SS_SSS);
		datePatternsRest.add(PATTERN_DATA_JSON_YYYY_MM_DD_T_HH_MM);
		datePatternsRest.add(PATTERN_DATA_JSON_YYYY_MM_DD_T_HH_MM_SS);
		datePatternsRest.add(PATTERN_DATA_JSON_YYYY_MM_DD);
		datePatternsRest.add(PATTERN_DATA_DD_MM_YYYY_HH_MM_SS_SSS);
		datePatternsRest.add(PATTERN_DATA_YYYY);
	}
	
	public static List<String> onlyDatePatternsRest = null;
	static {
		onlyDatePatternsRest = new ArrayList<>();
		onlyDatePatternsRest.add(DateFormatUtils.ISO_DATE_FORMAT.getPattern());
		onlyDatePatternsRest.add(PATTERN_DATA_JSON_YYYY_MM_DD);
	}
	
	public static SimpleDateFormat newSimpleDateFormat() {
		return newSimpleDateFormat(SimpleDateFormatUtils.PATTERN_DATA_JSON_YYYY_MM_DD_T_HH_MM_SS_SSS);
	}
	
	public static SimpleDateFormat newSimpleDateFormatDataOreMinuti() {
		return newSimpleDateFormat(SimpleDateFormatUtils.PATTERN_DATA_JSON_YYYY_MM_DD_T_HH_MM);
	}
	
	public static SimpleDateFormat newSimpleDateFormatDataOreMinutiSecondi() {
		return newSimpleDateFormat(SimpleDateFormatUtils.PATTERN_DATA_JSON_YYYY_MM_DD_T_HH_MM_SS);
	}
	
	public static SimpleDateFormat newSimpleDateFormatSoloData() {
		return newSimpleDateFormat(SimpleDateFormatUtils.PATTERN_DATA_JSON_YYYY_MM_DD);
	}
	
	public static SimpleDateFormat newSimpleDateFormatDataOra() {
		return newSimpleDateFormat(SimpleDateFormatUtils.PATTERN_DATA_JSON_YYYY_MM_DD_HH_MM);
	}
	
	public static SimpleDateFormat newSimpleDateFormatSoloDataSenzaSpazi() {
		return newSimpleDateFormat(SimpleDateFormatUtils.PATTERN_DATA_YYYY_MM_DD_SENZA_SPAZI);
	}
	
	public static SimpleDateFormat newSimpleDateFormatDataOraMinutiSenzaSpazi() {
		return newSimpleDateFormat(SimpleDateFormatUtils.PATTERN_DATA_YYYY_MM_DD_HH_MM_SENZA_SPAZI);
	}
	
	public static SimpleDateFormat newSimpleDateFormatIuvUtils() {
		return newSimpleDateFormat(SimpleDateFormatUtils.PATTERN_DATA_DD_MM_YYYY_HH_MM_SS_SSS);
	}
	
	public static SimpleDateFormat newSimpleDateFormatSoloAnno() {
		return newSimpleDateFormat(SimpleDateFormatUtils.PATTERN_DATA_YYYY);
	}
	
	public static SimpleDateFormat newSimpleDateFormatGGMMAAAA() {
		return newSimpleDateFormat(SimpleDateFormatUtils.PATTERN_DATA_GG_MM_AAAA);
	}
	
	public static SimpleDateFormat newSimpleDateFormat(String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		sdf.setTimeZone(TimeZone.getTimeZone("Europe/Rome"));
		sdf.setLenient(false);
		return sdf;
	}
	
	public static Date getDataDaConTimestamp(String dataInput, String dataInputName) throws ValidationException{
		return getDataDaConTimestamp(dataInput, dataInputName, false, true);
	}
	
	public static Date getDataDaConTimestamp(String dataInput, String dataInputName, boolean azzeraOraMinutiTimestamp) throws ValidationException{
		return getDataDaConTimestamp(dataInput, dataInputName, azzeraOraMinutiTimestamp, true);
	}
	
	public static Date getDataDaConTimestamp(String dataInput, String dataInputName, boolean azzeraOraMinutiTimestamp, boolean throwException) throws ValidationException{
		Date dataOutput = null;
		
		try {
			dataOutput = DateUtils.parseDate(dataInput, SimpleDateFormatUtils.onlyDatePatternsRest.toArray(new String[0]));
			Calendar c = Calendar.getInstance();
			c.setTime(dataOutput);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			dataOutput = c.getTime();
		}catch(ParseException e) {
			try {
				dataOutput = DateUtils.parseDate(dataInput, SimpleDateFormatUtils.datePatternsRest.toArray(new String[0]));
				if(azzeraOraMinutiTimestamp) {
					Calendar c = Calendar.getInstance();
					c.setTime(dataOutput);
					c.set(Calendar.HOUR_OF_DAY, 0);
					c.set(Calendar.MINUTE, 0);
					c.set(Calendar.SECOND, 0);
					c.set(Calendar.MILLISECOND, 0);
					dataOutput = c.getTime();
				}
			} catch (ParseException e1) {
				if(throwException)
					throw new ValidationException("Il formato della data indicata ["+dataInput+"] per il parametro ["+dataInputName+"] non e' valido.");
			}
		}
		return dataOutput;
	}
	
	public static Date getDataAConTimestamp(String dataInput, String dataInputName) throws ValidationException{
		return getDataAConTimestamp(dataInput, dataInputName, false, true);
	}
	
	public static Date getDataAConTimestamp(String dataInput, String dataInputName, boolean azzeraOraMinutiTimestamp) throws ValidationException{
		return getDataAConTimestamp(dataInput, dataInputName, azzeraOraMinutiTimestamp, true);
	}
	
	public static Date getDataAConTimestamp(String dataInput, String dataInputName, boolean azzeraOraMinutiTimestamp, boolean throwException) throws ValidationException{
		Date dataOutput = null;
		
		try {
			dataOutput = DateUtils.parseDate(dataInput, SimpleDateFormatUtils.onlyDatePatternsRest.toArray(new String[0]));
			Calendar c = Calendar.getInstance();
			c.setTime(dataOutput);
			c.set(Calendar.HOUR_OF_DAY, 23); 
			c.set(Calendar.MINUTE, 59);
			c.set(Calendar.SECOND, 59);
			c.set(Calendar.MILLISECOND, 999);
			dataOutput = c.getTime();
		}catch(ParseException e) {
			try {
				dataOutput = DateUtils.parseDate(dataInput, SimpleDateFormatUtils.datePatternsRest.toArray(new String[0]));
				if(azzeraOraMinutiTimestamp) {
					Calendar c = Calendar.getInstance();
					c.setTime(dataOutput);
					c.set(Calendar.HOUR_OF_DAY, 23); 
					c.set(Calendar.MINUTE, 59);
					c.set(Calendar.SECOND, 59);
					c.set(Calendar.MILLISECOND, 999);
					dataOutput = c.getTime();
				}
			} catch (ParseException e1) {
				if(throwException)
					throw new ValidationException("Il formato della data indicata ["+dataInput+"] per il parametro ["+dataInputName+"] non e' valido.");
			}
		}
		return dataOutput;
	}
	
	public Date getDataAvvisatura(String dataInput, String dataInputName) throws ValidationException {
		try {
			Date d = getDataAConTimestamp(dataInput, dataInputName);
			return d;
		} catch (Exception e) {
			throw new ValidationException(e);
		} finally {
			
		}
	}
	
	public static LocalDate toLocalDate(Date dateToConvert) {
		if(dateToConvert == null)
			return null;
		
		Calendar c = Calendar.getInstance();
		c.setTime(dateToConvert);
		return LocalDate.of(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
	}
	
	public static LocalDateTime toLocalDatetime(Date dateToConvert) {
		if(dateToConvert == null)
			return null;
		
		Calendar c = Calendar.getInstance();
		c.setTime(dateToConvert);
		LocalDate date = LocalDate.of(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
        LocalTime time = LocalTime.of(c.get(Calendar.HOUR), c.get(Calendar.MINUTE), c.get(Calendar.SECOND), c.get(Calendar.MILLISECOND));
		return LocalDateTime.of(date, time);
	}
}
