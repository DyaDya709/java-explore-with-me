package explorewithme.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HitDto {
    private Long id;
    @NotBlank(message = "app must not be blank")
    private String app;
    @NotBlank(message = "uri must not be blank")
    private String uri;
    @NotBlank(message = "ip must not be blank")
    private String ip;
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}", message = "Invalid date format")
    private String timestamp;
}
