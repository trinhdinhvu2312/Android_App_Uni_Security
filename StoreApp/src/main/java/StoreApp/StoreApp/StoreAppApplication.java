package StoreApp.StoreApp;

import java.util.HashMap;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.cloudinary.Cloudinary;

@SpringBootApplication
public class StoreAppApplication {

	@Value("${cloudinary.cloud_name}")
	private String cloudName;

	@Value("${cloudinary.api_key}")
	private String apiKey;

	@Value("${cloudinary.api_secret}")
	private String apiSecret;
	
	public static void main(String[] args) {
		SpringApplication.run(StoreAppApplication.class, args);
		System.out.println("-----------------------------------------------------------");
        System.out.println("🚀 Server ready at https://localhost:8080");
        System.out.println("🚀 Api doc ready at https://localhost:8080/swagger-ui.html ");
	}
	@Bean
	public ModelMapper modelMapper() {
	    return new ModelMapper();
	}
	
	@Bean
	public Cloudinary cloudinaryConfig() {
		Cloudinary cloudinary = null;
		Map<String, String> config = new HashMap<String, String>();
		config.put("cloud_name", cloudName);
		config.put("api_key", apiKey);
		config.put("api_secret", apiSecret);
		cloudinary = new Cloudinary(config);
		return cloudinary;
	}
}
