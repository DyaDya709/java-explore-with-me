package explorewithme.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatDto {
    String app; //main-service
    String uri; // /events/1
    Long hits; // кол-во
}
