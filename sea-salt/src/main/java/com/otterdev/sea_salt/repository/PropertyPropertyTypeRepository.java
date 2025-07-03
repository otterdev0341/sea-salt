package com.otterdev.sea_salt.repository;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.otterdev.sea_salt.entity.join.PropertyPropertyType;

public interface PropertyPropertyTypeRepository extends ReactiveCrudRepository<PropertyPropertyType, UUID> {


}
