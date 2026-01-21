package com.castis.publishservice.service;

import com.castis.publishservice.exception.defineException.CustomCodeException;
import com.castis.publishservice.exception.defineException.NotFoundException;
import com.castis.publishservice.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public abstract class EntityService<E, I, D> {
    public abstract JpaRepository<E, I> getRepository();

    public abstract String getEntityType();

    public abstract NotFoundException getNotFoundException(I id);

    public E findById(I id) throws NotFoundException {
        log.info("Find {} by id={}", getEntityType(), id);
        return getRepository().findById(id).orElseThrow(() -> getNotFoundException(id));
    }

    public D findDtoById(I id) throws NotFoundException {
        return toDto(findById(id));
    }

    public E save(E entity) throws CustomCodeException {
        try {
            log.info("Save {}={}", getEntityType(), Utils.toJson(entity));
            return getRepository().save(entity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CustomCodeException(
                    "error when saving " + getEntityType(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    public D saveDto(D dto) throws CustomCodeException {
        E entity = toEntity(dto);
        entity = save(entity);

        return toDto(entity);
    }

    public List<E> saveAll(Collection<E> entities) throws CustomCodeException {
        try {
            log.info("Save all {}={}", getEntityType(), Utils.toJson(entities));
            return getRepository().saveAll(entities);
        } catch (Exception e) {
            log.error("error when save all {}", getEntityType());
            throw new CustomCodeException(
                    "error when saving " + getEntityType(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

    }

    public boolean existById(I id) throws CustomCodeException {
        try {
            boolean result = getRepository().existsById(id);
            log.info("{} {} exist by id={}", getEntityType(), result ? "is" : "not", id);
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CustomCodeException(
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    public List<E> findAllByIdIn(Collection<I> ids) throws CustomCodeException {
        try {
            log.info("Find all {} by id in {}", getEntityType(), ids);
            return getRepository().findAllById(ids);
        } catch (Exception e) {
            log.error("Error when find all {} by id in {}", getEntityType(), ids);
            log.error(e.getMessage(), e);
            throw new CustomCodeException(
                    "Error when find all " + getEntityType() + ". Exception: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    public List<D> findAllDtoByIdIn(Collection<I> ids) throws CustomCodeException {
        return findAllByIdIn(ids).stream().map(this::toDto).collect(Collectors.toList());
    }

    public void deleteAllById(Collection<I> ids) throws CustomCodeException {
        log.info("Delete all {} by id in {}", getEntityType(), ids);
        try {
            getRepository().deleteAllById(ids);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CustomCodeException(
                    "Error when delete all " + getRepository() + " by id in : " + ids,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    public void deleteAll(Collection<E> entities) {
        log.info("Delete all {}={}", getEntityType(), entities);
        try {
            getRepository().deleteAll(entities);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CustomCodeException(
                    "Error when delete all " + getRepository(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    public abstract E toEntity(D dto);

    public abstract D toDto(E entity);

}