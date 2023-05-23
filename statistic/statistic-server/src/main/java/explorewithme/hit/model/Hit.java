package explorewithme.hit.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Table(name = "hits", schema = "public")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Hit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    @Column(name = "app")
    private String app; // Идентификатор сервиса для которого записывается информация main-service
    @Column(name = "uri")
    private String uri; // URI для которого был осуществлен запрос  /events/1
    @Column(name = "ip")
    private String ip; //IP-адрес пользователя, осуществившего запрос
    @Column(name = "time_stamp")
    private LocalDateTime timestamp; // Дата и время, когда был совершен запрос к эндпоинту (в формате "yyyy-MM-dd HH:mm:ss")
}
