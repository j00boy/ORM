package orm.orm_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import orm.orm_backend.dto.request.MountainSearchRequestDto;
import orm.orm_backend.dto.response.MountainResponseDto;
import orm.orm_backend.entity.Mountain;
import orm.orm_backend.entity.Trail;
import orm.orm_backend.repository.MountainRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MountainService {

    private final MountainRepository mountainRepository;

    // id로 산 조회
    public MountainResponseDto getMountainById(Integer id) {
        Mountain findMountain = mountainRepository.findById(id).orElseThrow();
        List<Trail> trails = new ArrayList<>();
        MountainResponseDto dto = new MountainResponseDto(findMountain, trails);
        return dto;
    }

    // name으로 산 조회 (
    public List<MountainResponseDto> getAllMountains(MountainSearchRequestDto mountainSearchRequestDto) {
        Pageable pageable = PageRequest.of(mountainSearchRequestDto.getPgno(), mountainSearchRequestDto.getRecordSize());
        Page<Mountain> mountains = mountainRepository.findByMountainNameContaining(pageable, mountainSearchRequestDto.getKeyword());

        // TODO: 마운틴 객체 + Trail를 DTO에 담아주기
        // TODO: TrailService에서 m_id로 List<Trail> 가져오기
        List<Trail> trails = new ArrayList<>();

        List<MountainResponseDto> mountainResponseDtos = mountains.stream()
                .map(mountain -> {
                    return new MountainResponseDto(mountain, trails);
                })
                .collect(Collectors.toList());

        return mountainResponseDtos;
    }

}
