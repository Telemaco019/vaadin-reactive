# Vaadin Reactive
This repository contains a simple TODO web-app built for exploring a reactive stack composed of [Vaadin](https://vaadin.com/) + [Spring Web Flux](https://docs.spring.io/spring-framework/docs/5.0.0.BUILD-SNAPSHOT/spring-framework-reference/html/web-reactive.html) + [R2DBC](https://r2dbc.io/). 


## Testing
### Data layer
Testing is performed using an H2 in-memory DB and R2DBC H2 implementation.


The following dependencies are required:
- *testImplementation 'io.projectreactor:reactor-test'*: it includes the StepVerifier used for testing reactive streams
- *testImplementation 'io.r2dbc:r2dbc-h2:0.8.4.RELEASE'*: an R2DBC driver implemented for H2, the DB used for performing tests.

The configuration is done in a class that provides the beans required for connecting to the DB. This class extends AbstractR2dbcConfiguration (which also registers a DB Client instance) and it is annotated with [@TestConfiguration](https://howtodoinjava.com/spring-boot2/testing/springboot-test-configuration/). The configuration class contains the following bean definitions:

Connection factory used for connecting to the H2 DB:
```
@Override
public ConnectionFactory connectionFactory() {
    H2ConnectionConfiguration h2ConnectionConfiguration = H2ConnectionConfiguration.builder()
            .url(url)
            .username(username)
            .password(password)
            .build();
    return new H2ConnectionFactory(h2ConnectionConfiguration);
}
```

ConnectionFactoryInitializer in order to initialize the schema the data of the DB: 
```
@Bean
public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {

    ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
    initializer.setConnectionFactory(connectionFactory);

    CompositeDatabasePopulator populator = new CompositeDatabasePopulator();
    populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("data/schema.sql")));
    populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("data/data.sql")));
    initializer.setDatabasePopulator(populator);

    return initializer;
}
```

#### Creating a test
Test classes requires the following annotations: 

- @ActiveProfile("test"): with this annotation Spring loads the properties from application.properties and from application-test.properties, where the properties defined in application-test.properties override the ones defined in application.properties with the same name.
- @SpringBootTest(classes = {VaadinReactiveEngineApplication.class}): annotation required for performing integration tests. It creates a SpringBoot application context, using the classes provided as argument. This allows Spring to create the JPA repositories implementations that have to be tested. For more info, see [here](https://reflectoring.io/spring-boot-test/) and [here](https://howtodoinjava.com/spring-boot2/testing/springboottest-annotation/).
- @RunWith(SpringRunner.class): required for enabling annotations and autowiring when using JUnit 4. No necessary if JUnit 5 is used and the class is already annotated with @SpringBootTest.


### Web layer
