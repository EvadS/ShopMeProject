package com.shopme.admin;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //TODO: should be const
        String dirName = "user-photos";
        Path userPhotosDir = Paths.get(dirName);
        String userPhotosPath = userPhotosDir.toFile().getAbsolutePath();

        String categoryImageDirName = "category-images";
        Path categoryImageDir = Paths.get(categoryImageDirName);
        String categoryImagePath  = categoryImageDir.toFile().getAbsolutePath();

        // Brand
        String brandLogosDirName = "brand-logos";
        Path brandLogosDir = Paths.get(brandLogosDirName);
        String brandLogosPath = brandLogosDir.toFile().getAbsolutePath();


        if (OsUtils.isWindows()) {
            registry.addResourceHandler("/" + dirName + "/**")
                    .addResourceLocations("file:/" + userPhotosPath + "/");

            registry.addResourceHandler("/category-images/**")
                    .addResourceLocations("file:/" + categoryImagePath + "/");

            registry.addResourceHandler("/brand-logos/**")
                    .addResourceLocations("file:/" + brandLogosPath + "/");
        }
        //else if (OsUtils.isUnix()) {
//            //LINUX
//            registry.addResourceHandler("/" + dirName + "/**")
//                    .addResourceLocations("file:" + userPhotosPath + "/");
//
//            registry.addResourceHandler("/" + categoryImageDirName + "/**")
//                    .addResourceLocations("file:/" + categoryImagePath + "/");
       // }
    }
}
