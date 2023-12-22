package io.bootify.practica_spring_batch.batch;

import io.bootify.practica_spring_batch.domain.ControlLote;
import io.bootify.practica_spring_batch.domain.Cuenta;
import io.bootify.practica_spring_batch.domain.Transaccion;
import io.bootify.practica_spring_batch.model.TransaccionDTO;
import io.bootify.practica_spring_batch.repos.ControlLoteRepository;
import io.bootify.practica_spring_batch.repos.CuentaRepository;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Autowired
    public BatchConfiguration(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public ItemReader<Transaccion> reader() {
        FlatFileItemReader<Transaccion> reader = new FlatFileItemReader<>();

        // Establece la ruta del archivo CSV
        reader.setResource(new ClassPathResource("transacciones.csv"));

        // Configura cómo dividir los datos en campos
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setNames(new String[]{"id","cuentaID","monto","saldoPostTransaccion","cuenta","controlLote"}); // Nombres de los campos en CSV

        // Configura cómo mapear líneas a objetos Transaccion
        BeanWrapperFieldSetMapper<Transaccion> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Transaccion.class);

        // Configura el mapeador de líneas
        DefaultLineMapper<Transaccion> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        // Establece el mapeador de líneas en el lector
        reader.setLineMapper(lineMapper);

        return reader;
    }

    @Bean
    public ItemProcessor<TransaccionDTO, Transaccion> processor(
            CuentaRepository cuentaRepository,
            ControlLoteRepository controlLoteRepository) {
        return new ItemProcessor<TransaccionDTO, Transaccion>() {
            @Override
            public Transaccion process(TransaccionDTO transaccionDTO) throws Exception {
                // Crear una nueva instancia de Transaccion
                Transaccion transaccion = new Transaccion();

                // Mapeo directo de atributos
                transaccion.setCuentaID(transaccionDTO.getCuentaID());
                transaccion.setMonto(transaccionDTO.getMonto());
                transaccion.setSaldoPostTransaccion(transaccionDTO.getSaldoPostTransaccion());

                // Mapeo de la relación con Cuenta
                // Suponiendo que cuenta es un ID referenciado en TransaccionDTO
                Cuenta cuenta = cuentaRepository.findById(transaccionDTO.getCuenta()).orElse(null);
                transaccion.setCuenta(cuenta);

                // Mapeo de la relación con ControlLote
                // Suponiendo que controlLote es un ID referenciado en TransaccionDTO
                ControlLote controlLote = controlLoteRepository.findById(transaccionDTO.getControlLote()).orElse(null);
                transaccion.setControlLote(controlLote);

                // Los campos dateCreated y lastUpdated son manejados automáticamente por JPA

                // Nota: El ID de Transaccion no se mapea aquí. En la mayoría de los casos,
                // no querrás sobrescribir un ID generado por la base de datos en una nueva inserción.
                // Si estás actualizando un registro existente, entonces sí mapearías este campo.

                return transaccion;
            }
        };
    }

    @Bean
    public ItemWriter<Transaccion> writer(EntityManagerFactory entityManagerFactory) {
        JpaItemWriter<Transaccion> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

    @Bean
    public Job miJob(Step miStep, JobExecutionListener listener) {
        return new JobBuilder("miJob", jobRepository)
                .start(miStep)
                .listener(listener)
                .build();
    }

    @Bean
    public Step miStep(ItemReader<TransaccionDTO> reader,
                       ItemProcessor<TransaccionDTO, Transaccion> processor,
                       ItemWriter<Transaccion> writer) {
        StepBuilder stepBuilder = new StepBuilder("miStep", jobRepository);

        return stepBuilder
                .<TransaccionDTO, Transaccion>chunk(10, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}



