package parcel.delivery.app.common.mapper.annotations;


import org.mapstruct.Mapper;
import parcel.delivery.app.common.mapper.PdaMapperConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
@Mapper(config = PdaMapperConfig.class)
public @interface PdaMapper {
}
