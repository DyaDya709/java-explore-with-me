package explorewithme.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HitDto {
    Long id;
    @NotBlank(message = "app must no be blank")
    String app;
    @NotBlank(message = "uri must no be blank")
    String uri;
    @NotBlank(message = "ip must no be blank")
    String ip;
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}", message = "Invalid date format")
    String timestamp;
}
