package org.example.tracker.datamodel;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
public class Suggestion implements Comparable<Suggestion> {
    @Id
    private UUID id;
    private String title;
    private String description;
    private List<Stage> stages;
    private Vote vote;
    private Integer currentStage;
    private Instant createdDate;
    private Instant lastUpdatedDate;
    private List<Comment> comments;

    @Override
    public int compareTo(@NotNull Suggestion o) {
        return this.createdDate.compareTo(o.createdDate);
    }
}
