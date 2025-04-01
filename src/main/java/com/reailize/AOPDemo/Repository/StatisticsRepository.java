package com.reailize.AOPDemo.Repository;

import com.reailize.AOPDemo.Model.Statistic;
import org.springframework.data.repository.CrudRepository;

public interface StatisticsRepository extends CrudRepository<Statistic, Long> {
}
