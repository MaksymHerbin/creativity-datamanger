package com.creativity.datamanager;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.shell.jline.PromptProvider;

import static org.jline.utils.AttributedStyle.DEFAULT;

@SpringBootApplication
public class DatamanagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatamanagerApplication.class, args);
    }

    @Bean
    public PromptProvider myPromptProvider() {
        return () -> new AttributedString("creativity:>", DEFAULT.foreground(AttributedStyle.YELLOW));
    }
}

