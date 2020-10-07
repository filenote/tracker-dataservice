package org.example.tracker.datamodel.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.tracker.datamodel.Stage;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCurrentStageRequest {
    private UUID id;
    private Stage stage;
}
