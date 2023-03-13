package parcel.delivery.app.auth.mapper;


import java.util.List;

public interface EntityDtoMapper<E, D> {
    E toEntity(D dto);

    List<E> toEntity(List<D> dtoList);

    D toDto(E entity);

    List<D> toDto(Iterable<E> entityList);
}
