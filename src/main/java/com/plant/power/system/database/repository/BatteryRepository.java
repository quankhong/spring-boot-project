package com.plant.power.system.database.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.plant.power.system.database.entity.Battery;

@Repository
public interface BatteryRepository extends JpaRepository<Battery, Integer>{
	public List<Battery> findAllByPostcodeGreaterThanEqualAndPostcodeLessThanEqualOrderByNameAsc(String postcodeStart, String postcodeEnd);
	public List<Battery> findAllByPostcodeGreaterThanEqualOrderByNameAsc(String postcodeStart);
	public List<Battery> findAllByPostcodeLessThanEqualOrderByNameAsc(String postcodeEnd);
	public Optional<Battery> findByName(String name);
}
