package com.reailize.AOPDemo.Repository;

import com.reailize.AOPDemo.Model.Entry;
import org.springframework.data.repository.CrudRepository;

public interface DiaryRepository extends CrudRepository<Entry, Long> {
     Entry getEntryByName(String name);
}
