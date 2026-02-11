package org.davide.ggbproyect.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LayoutDTO {
    private Integer id;
    private Double posX;
    private Double posY;
    private String forma;
    private Integer rotacion;
}
