package io.bootify.practica_spring_batch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class PracticaSpringBatchApplication {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;
    public static void main(final String[] args) {
        SpringApplication.run(PracticaSpringBatchApplication.class, args);
    }

    // Da error al ejecutarlo
    /*
    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            jobLauncher.run(job, new JobParameters());
        };
    }
    */

}
