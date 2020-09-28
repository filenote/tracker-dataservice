package org.example.tracker.datamodel;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vote {
    private Boolean voted;
    private Long amount;

    public Vote(Vote defaultVote) {
        this.voted = defaultVote.voted;
        this.amount = defaultVote.amount;
    }
}
