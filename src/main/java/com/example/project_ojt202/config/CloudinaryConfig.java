package com.example.project_ojt202.config;
import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

@org.springframework.context.annotation.Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dl2wnxmal");
        config.put("api_key", "779764287823659");
        config.put("api_secret", "KdUyiUesNbwmZu564GMts6XDVsQ");

        return new Cloudinary(config);
    }
}

