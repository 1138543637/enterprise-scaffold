package cn.sxu.enterprise.common.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${enterprise.file.local-upload-path}")
    private String localUploadPath;

    @Value("${enterprise.file.access-prefix:/files}")
    private String accessPrefix;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String pathPattern = accessPrefix.endsWith("/") ? accessPrefix + "**" : accessPrefix + "/**";
        String resourceLocation = Paths.get(localUploadPath)
                .toAbsolutePath()
                .normalize()
                .toUri()
                .toString();

        registry.addResourceHandler(pathPattern)
                .addResourceLocations(resourceLocation);
    }
}