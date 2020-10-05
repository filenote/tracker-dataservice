package org.example.tracker.datamodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stage {
    private Integer stage;
    private Boolean enabled;
    private String name;
}
