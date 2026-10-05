package com.transport.driver.mapper;

import com.transport.driver.dto.DriverRequest;
import com.transport.driver.dto.DriverResponse;
import com.transport.driver.entity.Driver;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DriverMapper {
    DriverResponse toResponse(Driver driver);

    List<DriverResponse> toResponseList(List<Driver> drivers);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", constant = "true")
    Driver toEntity(DriverRequest request);
}
