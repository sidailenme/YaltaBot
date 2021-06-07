package com.yalta.telegram.repository;

import com.yalta.telegram.entity.Taxi;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaxiRepository extends JpaRepository<Taxi, Long> {

}
