package pt.amane.infrastructure.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration// This anotation inform Spring to configurated beans  and rad method that it want manage by default
@ComponentScan("pt.amane") // This anotation inform spring the default package to run classes  by classes that it will generate an bean.
public class WebServerConfig {

}

