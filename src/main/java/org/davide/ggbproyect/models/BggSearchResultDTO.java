package org.davide.ggbproyect.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BggSearchResultDTO {
    private Integer bggId;
    private String name;
    private Integer yearPublished;
}
