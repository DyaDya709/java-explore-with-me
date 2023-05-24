package explorewithme.hit.service;

import explorewithme.dto.HitDto;
import explorewithme.hit.mapper.HitMapper;
import explorewithme.hit.storage.HitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HitServiceImpl implements HitService {
    private final HitRepository hitRepository;

    @Transactional
    @Override
    public void create(HitDto hitDto) {
        hitRepository.save(HitMapper.toEntity(hitDto));
    }
}
