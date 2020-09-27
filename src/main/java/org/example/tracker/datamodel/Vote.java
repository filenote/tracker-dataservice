package org.example.tracker.datamodel;

import lombok.Data;

@Data
public class Vote {
    private Boolean voted;
    private Long amount;
}
