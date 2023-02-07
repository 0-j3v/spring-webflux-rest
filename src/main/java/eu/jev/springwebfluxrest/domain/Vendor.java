package eu.jev.springwebfluxrest.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Vendor {

    @Id
    private String id;

    private String firstname;

    private String lastname;
}
