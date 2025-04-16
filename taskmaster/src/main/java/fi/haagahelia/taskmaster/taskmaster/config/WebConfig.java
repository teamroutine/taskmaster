// package fi.haagahelia.taskmaster.taskmaster.config;

// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.servlet.config.annotation.CorsRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// @Configuration
// public class WebConfig implements WebMvcConfigurer {

// @SuppressWarnings("null")
// @Override
// public void addCorsMappings(CorsRegistry registry) {
// registry.addMapping("/**")
// .allowedOrigins(
// "http://localhost:5173", // Allow local frontend(endpoint: localhost:5173) to
// make API-requests
// "https://taskmaster-8ien.onrender.com",
// "https://taskmaster-git-ohjelmistoprojekti-2-taskmaster.2.rahtiapp.fi") //
// Allow this website
// // for API requests
// .allowedMethods("GET", "POST", "PUT", "DELETE")
// .allowedHeaders("*")
// .allowCredentials(true);
// }

// }
