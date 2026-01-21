package com.castis.publishservice.service;

import com.castis.publishservice.dto.EndUserDto;
import com.castis.publishservice.entity.EndUser;
import com.castis.publishservice.exception.defineException.NotFoundException;
import com.castis.publishservice.exception.defineException.ServerRuntimeException;
import com.castis.publishservice.mapper.EndUserMapper;
import com.castis.publishservice.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService extends EntityService<EndUser, Long, EndUserDto> {
    @Getter
    private final UserRepository repository;
    private static final EndUserMapper mapper = EndUserMapper.INSTANCE;

    @Override
    public String getEntityType() {
        return "end user";
    }

    @Override
    public NotFoundException getNotFoundException(Long id) {
        return new NotFoundException("can not found user by id: " + id);
    }

    @Override
    public EndUser toEntity(EndUserDto dto) {
        return mapper.dtoToEntity(dto);
    }

    @Override
    public EndUserDto toDto(EndUser entity) {
        return mapper.entityToDto(entity);
    }

    public List<EndUserDto> saveAllDto(List<EndUserDto> users) throws ServerRuntimeException {
        log.debug("save all users: {}", users);
        List<EndUser> entities = users.stream().map(mapper::dtoToEntity).collect(Collectors.toList());
        try {
            entities = this.saveAll(entities);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServerRuntimeException(e.getMessage());
        }
        return entities.stream().map(mapper::entityToDto).collect(Collectors.toList());
    }
}
