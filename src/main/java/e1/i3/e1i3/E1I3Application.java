package e1.i3.e1i3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class E1I3Application {

    public static void main(String[] args) {
        SpringApplication.run(E1I3Application.class, args);
    }

}
