package explorewithme.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatDto {
    private String app; //main-service
    private String uri; // /events/1
    private Long hits; // кол-во
}
