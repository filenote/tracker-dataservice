package org.example.tracker.datamodel;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.UUID;

@Data
public class Suggestion {
    @Id
    private UUID id;
    private String title;
    private String description;
    private List<Stage> stages;
    private Vote vote;
}
