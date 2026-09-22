package com.spring.springbootapplication.config;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    Path uploadDir = Paths.get("uploads")
      .toAbsolutePath()
      .normalize();

    System.out.println("=== WebConfig確認 ===");
    System.out.println("WebConfig uploadDir: " + uploadDir);
    System.out.println(
      "ResourceLocation: file:" + uploadDir + "/"
    );

    registry
      .addResourceHandler("/uploads/**")
      .addResourceLocations("file:" + uploadDir + "/");
  }
}