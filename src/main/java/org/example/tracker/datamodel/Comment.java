package org.example.tracker.datamodel;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class Comment {
    @Id
    private UUID id;
    private UUID suggestionId;
    private String username;
    private String comment;
    private ZonedDateTime createdDate;
    private ZonedDateTime updatedDate;
}
