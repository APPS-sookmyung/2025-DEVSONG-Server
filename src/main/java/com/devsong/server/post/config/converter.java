package com.devsong.server.post.config;

import com.devsong.server.post.entity.Category;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class converter implements Converter<String, Category> {

    @Override
    public Category convert(String source) {
        if (source == null) {
            return null;
        }
        return Category.from(source);
    }
}
