package dev.guilhermesebastiao.authenticationapi.infrastructure.config;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggerConfig {
    public static final String APPLICATION_LOGGER = "APPLICATION_LOGGER";

    @Bean(APPLICATION_LOGGER)
    public org.slf4j.Logger applicationLogger() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(context);
        encoder.setPattern("%msg%n");
        encoder.start();

        ConsoleAppender<ILoggingEvent> appender = new ConsoleAppender<>();
        appender.setContext(context);
        appender.setName("APPLICATION_CONSOLE");
        appender.setEncoder(encoder);
        appender.start();

        Logger logger = context.getLogger(APPLICATION_LOGGER);

        logger.setAdditive(false);
        logger.addAppender(appender);

        return logger;
    }
}
