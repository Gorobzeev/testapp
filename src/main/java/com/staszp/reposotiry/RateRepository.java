package com.staszp.reposotiry;

import com.staszp.model.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RateRepository extends JpaRepository<Rate, Integer> {

    @Query(value = "SELECT (CASE WHEN COUNT(rt) > 0 THEN TRUE ELSE FALSE END) FROM Rate rt WHERE rt.time = ?1")
    boolean existByTime(String time);

}
