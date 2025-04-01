package com.reailize.AOPDemo.Repository;

import com.reailize.AOPDemo.Model.Entry;
import com.reailize.AOPDemo.Model.History;
import org.springframework.data.repository.CrudRepository;

public interface HistoryRepository extends CrudRepository<History, Long> {
}
