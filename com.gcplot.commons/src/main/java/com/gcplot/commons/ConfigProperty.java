package com.gcplot.commons;

import java.util.HashMap;
import java.util.Map;

public enum ConfigProperty {
    SERVING_DISABLED("serving.disabled", false),
    POLL_INTERVAL("config.poll.interval", 15000),

    PUBLIC_HOST("public.host.domain", "gcplot.com"),
    API_HOST("api.host.domain", "api.gcplot.com"),
    USE_PUBLIC_SSL("use.public.ssl", false),

    EMAIL_CONFIRM_TEMPLATE("email.confirm.template", ""),
    EMAIL_NEW_PASS_TEMPLATE("email.new.pass.template", ""),
    EMAIL_HOST_NAME("email.host.name", "smtp.yandex.ru"),
    EMAIL_SMTP_PORT("email.smtp.port", 465),
    EMAIL_USE_SSL("email.use.ssl", true),
    EMAIL_AUTH("email.auth", true),
    EMAIL_CONNECTION_TIMEOUT("email.connection.timeout", 10000),
    EMAIL_SEND_TIMEOUT("email.send.timeout", 5000),
    EMAIL_DEFAULT_USERNAME("email.confirm.username", "do-not-reply@gcplot.com"),
    EMAIL_DEFAULT_FROM("email.confirm.from", "do-not-reply@gcplot.com"),
    EMAIL_DEFAULT_PASSWORD("email.confirm.password", "%*KmX_bh/Z:e7)u\""),

    TEST1_CONFIG("test.config.1", "test"),

    USER_ANALYSE_COUNT_CACHE_SIZE("user.analyse.cache.size", 5_000L),
    USER_ANALYSE_COUNT_CACHE_SECONDS("user.analyse.cache.min", 120L),
    GC_EVENTS_MAX_INTERVAL_DAYS("user.gc.events.max.interval.days", 90),
    SURVIVOR_AGES_AVG_THRESHOLD("survivor.ages.avg.threshold", 100),
    TENURED_ACCUMULATE_SECONDS("tenured.accumulate.seconds", 60),
    FORBID_OTHER_GENERATION("forbid.other.generation", true),

    PASSWORD_MIN_LENGTH("password.min.length", 1),

    CONFIRMATION_IS_RESTRICTED("confirmation.is.restricted", false),

    PARSE_LOG_MAX_FILE_SIZE("parse.log.max.file.size", 512L * 1024 * 1024);

    private static Map<String, ConfigProperty> types = new HashMap<>();

    static {
        for (ConfigProperty cp : ConfigProperty.values()) {
            types.put(cp.getKey(), cp);
        }
    }

    public static ConfigProperty get(String key) {
        return types.get(key);
    }

    private String key;
    public String getKey() {
        return key;
    }

    private Object defaultValue;
    public Object getDefaultValue() {
        return defaultValue;
    }

    ConfigProperty(String key, Object defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

}
