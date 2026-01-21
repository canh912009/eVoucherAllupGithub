package com.castis.pos_api.service.common;

import com.castis.pos_api.exception.ApplicationException;
import com.castis.pos_api.utils.CustomResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public abstract class EntityService<E, I, D> {
    public abstract JpaRepository<E, I> getRepository();

    public abstract String getEntityType();

    public abstract EntityNotFoundException getNotFoundException(I id);

    public E findById(I id) throws EntityNotFoundException {
        log.info("find {} by id: {}", getEntityType(), id);
        return getRepository().findById(id).orElseThrow(() -> getNotFoundException(id));
    }

    public D findDtoById(I id) throws EntityNotFoundException {
        return toDto(findById(id));
    }

    public E save(E entity) {
        try {
            log.info("save {}: {}", getEntityType(), entity);
            return getRepository().save(entity);
        } catch (Exception e) {
            log.error("error when save {}", getEntityType());
            log.error(e.getMessage(), e);
            throw new ApplicationException(CustomResponse.E5001_INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
        }
    }

    public List<E> saveAll(Collection<E> entities) {
        try {
            log.info("save all {}: {}", getEntityType(), entities);
            return getRepository().saveAll(entities);
        } catch (Exception e) {
            log.error("error when save all {}", getEntityType());
            throw new ApplicationException(CustomResponse.E5001_INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
        }

    }

    public boolean existById(I id) {
        try {
            boolean result = getRepository().existsById(id);
            log.info("{} {} exist by id-{}", getEntityType(), result ? "is" : "not", id);
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ApplicationException(CustomResponse.E5001_INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
        }
    }

    public List<E> findAllByIdIn(Collection<I> ids) {
        try {
            log.info("find all {} by id in {}", getEntityType(), ids);
            return getRepository().findAllById(ids);
        } catch (Exception e) {
            log.error("error when find all {} by id in {}", getEntityType(), ids);
            log.error(e.getMessage(), e);
            throw new ApplicationException(CustomResponse.E5001_INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
        }
    }

    public List<D> findAllDtoByIdIn(Collection<I> ids) {
        return findAllByIdIn(ids).stream().map(this::toDto).collect(Collectors.toList());
    }

    public void deleteAllById(Collection<I> ids) {
        log.info("delete all {} by id in: {}", getEntityType(), ids);
        try {
            getRepository().deleteAllById(ids);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ApplicationException(CustomResponse.E5001_INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
        }
    }

    public void deleteAll(Collection<E> entities) {
        log.info("delete all {} : {}", getEntityType(), entities);
        try {
            getRepository().deleteAll(entities);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ApplicationException(CustomResponse.E5001_INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
        }
    }

    public abstract E toEntity(D dto);

    public abstract D toDto(E entity);

}