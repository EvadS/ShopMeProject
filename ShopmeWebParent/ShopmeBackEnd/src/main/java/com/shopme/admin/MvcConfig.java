package com.shopme.admin;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public  void addResourceHandlers(ResourceHandlerRegistry registry) {
        //to test
        String dirName = "user-photos";
        //TODO: should be const
        Path userPhotosDir = Paths.get(dirName);

        String userPhotosPath = userPhotosDir.toFile().getAbsolutePath();
       //WINDOWS
        registry.addResourceHandler("/" + dirName + "/**")
                .addResourceLocations("file:/"+ userPhotosPath + "/");
        //LINUX
//        registry.addResourceHandler("/" + dirName + "/**")
//                .addResourceLocations("file:"+ userPhotosPath + "/");
    }
}
