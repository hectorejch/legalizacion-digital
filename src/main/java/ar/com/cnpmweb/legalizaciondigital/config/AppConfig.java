package ar.com.cnpmweb.legalizaciondigital.config;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                try {
                    MessageDigest md = MessageDigest.getInstance("MD5");
                    byte[] messageDigest = md.digest(rawPassword.toString().getBytes());
                    BigInteger no = new BigInteger(1, messageDigest);
                    String hashtext = no.toString(16);
                    while (hashtext.length() < 32) {
                        hashtext = "0" + hashtext;
                    }
                    return hashtext;
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException("Error al codificar la contraseña", e);
                }
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return encode(rawPassword).equals(encodedPassword);
            }
        };
    }

/*    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApiKeyRepository apiKeyRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Verificar si ya existe
            if (apiKeyRepository.findByApiUsuario("api_test").isEmpty()) {
                // Crear nuevo usuario API con contraseña "test123"
                ApiKey apiKey = new ApiKey();
                apiKey.setApiUsuario("api_test");
                apiKey.setApiContrasena(passwordEncoder.encode("test123"));
                apiKey.setActivo(true);
                apiKeyRepository.save(apiKey);
                System.out.println("Usuario API creado: api_test");
            }
        };
    }
*/
}