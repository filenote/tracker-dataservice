package org.example.tracker.datamodel;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class Comment {
    @Id
    private UUID id;
    private String username;
    private String text;
    private Instant createdDate;
    private Instant updatedDate;
}
