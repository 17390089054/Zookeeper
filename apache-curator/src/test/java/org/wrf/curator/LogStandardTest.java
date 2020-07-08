package org.wrf.curator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 标准日志测试
 * @ClassName: LogStandardTest
 * @Description:
 * @author: knight
 * @date 2020年6月24日 上午10:23:38
 */
public class LogStandardTest {
	private static final Logger logger=LoggerFactory.getLogger(LogStandardTest.class);
	
	public static void main(String[] args) {
		logger.debug("log debug");	
		logger.info("log info");
		logger.warn("log warn {}","knight");
		logger.error("log error");
	}
}
/**
log4j.rootLogger=CONSOLE,FILE
log4j.addivity.org.apache=true

# 应用于控制台
log4j.appender.CONSOLE=org.apache.log4j.ConsoleAppender
log4j.appender.CONSOLE.Threshold=INFO
log4j.appender.CONSOLE.Target=System.out
log4j.appender.CONSOLE.Encoding=UTF-8
log4j.appender.CONSOLE.layout=org.apache.log4j.PatternLayout
log4j.appender.CONSOLE.layout.ConversionPattern=[framework] %d - %c -%-4r [%t] %-5p %c %x - %m%n

# 每天新建日志
log4j.appender.A1=org.apache.log4j.DailyRollingFileAppender
log4j.appender.A1.File=G:/log4j/log
log4j.appender.A1.Encoding=UTF-8
log4j.appender.A1.Threshold=DEBUG
log4j.appender.A1.DatePattern='.'yyyy-MM-dd
log4j.appender.A1.layout=org.apache.log4j.PatternLayout
log4j.appender.A1.layout.ConversionPattern=%d{ABSOLUTE} %5p %c{1}:%L : %m%n

#应用于文件
log4j.appender.FILE=org.apache.log4j.FileAppender
log4j.appender.FILE.File=G:/log4j/file.log
log4j.appender.FILE.Append=false
log4j.appender.FILE.Encoding=UTF-8
log4j.appender.FILE.layout=org.apache.log4j.PatternLayout
log4j.appender.FILE.layout.ConversionPattern=[framework] %d - %c -%-4r [%t] %-5p %c %x - %m%n
*/