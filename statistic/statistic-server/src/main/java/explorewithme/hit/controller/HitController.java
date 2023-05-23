package explorewithme.hit.controller;

import explorewithme.dto.HitDto;
import explorewithme.hit.service.HitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/hit")
@RequiredArgsConstructor
public class HitController {
    private final HitService hitService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody HitDto hitDto) {
        hitService.create(hitDto);
    }
}
