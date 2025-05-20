package com.br.gasto_comum.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@ConfigurationProperties(prefix = "document")
@Getter
@Setter
public class DocumentStorageProperty {

    private String uploadDirectory;

}
