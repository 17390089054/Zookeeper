package org.wrf.curator;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 简单日志输出设置
 * @ClassName: LogSimpleTest
 * @Description:
 * @author: knight
 * @date 2020年6月24日 上午10:23:53
 */
public class LogSimpleTest {
	private static final Logger logger=LoggerFactory.getLogger(LogSimpleTest.class);
	/** log4j简单用法 
	  	log4j.rootLogger=DEBUG, stdout, R
		log4j.appender.stdout=org.apache.log4j.ConsoleAppender
		log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
		
		# Pattern to output the caller's file name and line number.
		log4j.appender.stdout.layout.ConversionPattern=%5p [%t] (%F:%L) - %m%n
		log4j.appender.R=org.apache.log4j.RollingFileAppender
		log4j.appender.R.File=example.log
		log4j.appender.R.MaxFileSize=100KB
		# Keep one backup file
		log4j.appender.R.MaxBackupIndex=1
		log4j.appender.R.layout=org.apache.log4j.PatternLayout
		log4j.appender.R.layout.ConversionPattern=%p %t %c - %m%n
	 */
	@Test
	public void log() {
		logger.debug("log4j debug");
		logger.info("log4j info");
	}
	
	
}
