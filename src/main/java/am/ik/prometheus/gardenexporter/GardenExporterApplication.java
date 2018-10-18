package am.ik.prometheus.gardenexporter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GardenExporterApplication {



    public static void main(String[] args) {
        SpringApplication.run(GardenExporterApplication.class, args);
    }
}
