package com.reailize.AOPDemo.Service;

import com.reailize.AOPDemo.Model.History;
import com.reailize.AOPDemo.Repository.HistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryService {
    private final HistoryRepository historyRepository;

    public HistoryService(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    public List<History> getHistories() {
        return (List<History>) historyRepository.findAll();
    }

    public History createHistory(History history) {
        return historyRepository.save(history);
    }
}
