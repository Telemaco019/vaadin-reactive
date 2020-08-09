# Vaadin Reactive
This repository contains a simple TODO web-app built for exploring a reactive stack composed of [Vaadin](https://vaadin.com/) + [Spring Web Flux](https://docs.spring.io/spring-framework/docs/5.0.0.BUILD-SNAPSHOT/spring-framework-reference/html/web-reactive.html) + [R2DBC](https://r2dbc.io/). 

## Multi-threading
Multithreading is managed using two Reactor operators: 
- *subscribeOn*: it allows specifying which Scheduler a source Flux should use for pushing the emissions all the way to the final 
Subscriber (all the chain is therefore executed on that scheduler, unless publishOn is called). If there are multiple 
subscribeOn calls on the same chain, only the one closest to the source wins (e.g. the top one in the chain) and all 
the subsequent subscribeOn calls have practically no effect.
- *publishOn*: it allows specifying which Scheduler a source Flux should use for emitting emissions from a specific point 
in the chain (the placement of publishOn operator therefore matters).

Two main schedulers are used:
- *elastic*: it is basically the equivalent of Schedulers.io() in RxJava. It is a scheduler that dynamically
creates workers and caches the thread pools, reusing them once the Workers have been shut down. It is therefore suitable 
for I/O tasks, that generally use little CPU time while having long idle times waiting for data to be sent or received.
- *parallel*: it hosts a fixed pool of thread according to the processor availability, making it suitable for heavy 
computational tasks.

Note on multi-threading from [WebFlux Documentation](https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/web-reactive.html#webflux-concurrency-model):
```
In Spring WebFlux, it is assumed that applications do not block the current thread (for example for remote calls), 
threfore non-blocking servers use a small, fixed-size thread pool (event loop workers) to handle requests.
```
Therefore, by adopting WebFlux, the Engine of the application uses fewer thread for serving the requests since the threads
do not block, and a single thread can serve more requests over time.
                                                                           
### Portal
Since the API exposed by the engine are non-blocking, the operator subscribeOn is not used on the web client
when making http calls. 

If the API were blocking, then it would be reasonable to use the operator subscribeOn in all the HTTP calls to the 
engine API, placing it closest possible to the event source (that is the web client used for making the calls).

### Engine
The operator subscribeOn is used within the services that call the repository methods, placed right after each repository
method calls. Since the repository access the data layer, the elastic scheduler is used. 

By using the subscribeOn operator, the thread handling a REST request that needs to call any service method from the 
REST controller does not get blocked (even if the invoked method of the service performs some time-consuming operation),
and once it calls a service method it is immediately ready for serving other possible request received by the controller.

## Testing
### Data layer
Testing is performed using an H2 in-memory DB and R2DBC H2 implementation.

The following dependencies are required:
- *testImplementation 'io.projectreactor:reactor-test'*: it includes the StepVerifier used for testing reactive streams
- *testImplementation 'io.r2dbc:r2dbc-h2:0.8.4.RELEASE'*: an R2DBC driver implemented for H2, the DB used for performing tests.

The configuration is done in a class that provides the beans required for connecting to the DB. This class extends
AbstractR2dbcConfiguration (which also registers a DB Client instance) and it is annotated with
[@TestConfiguration](https://howtodoinjava.com/spring-boot2/testing/springboot-test-configuration/). 
The configuration class contains the following bean definitions:

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
