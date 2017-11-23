package com.ssslinppp.yamlConvert;

import com.google.common.base.Strings;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 转化yaml配置文件的String为Date；
 * yaml中： birthDay: 1995-10-11 12:45:15
 * 转化为POJO中的：private Date birthDay
 */
@Component
@ConfigurationPropertiesBinding
public class StringDateConverter implements Converter<String, Date> {
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public Date convert(String source) {
        if (Strings.isNullOrEmpty(source)) {
            return null;
        }

        try {
            return sdf.parse(source);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}

