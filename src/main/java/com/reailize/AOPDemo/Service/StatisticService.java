package com.reailize.AOPDemo.Service;

import com.reailize.AOPDemo.Model.Statistic;
import com.reailize.AOPDemo.Repository.StatisticsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticService {
    private final StatisticsRepository statisticsRepository;

    public StatisticService(StatisticsRepository statisticsRepository) {
        this.statisticsRepository = statisticsRepository;
    }

    public List<Statistic> getStatistics() {
        return (List<Statistic>) statisticsRepository.findAll();
    }

    public Statistic createStatistics(Statistic statistics) {
        return statisticsRepository.save(statistics);
    }
}
