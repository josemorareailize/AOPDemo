package com.reailize.AOPDemo.Controller;

import com.reailize.AOPDemo.Model.Statistic;
import com.reailize.AOPDemo.Service.HistoryService;
import com.reailize.AOPDemo.Service.StatisticService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HistoryAndStaticsController {

    private final HistoryService historyService;
    private final StatisticService statisticsService;

    public HistoryAndStaticsController(HistoryService historyService, StatisticService statisticsService) {
        this.historyService = historyService;
        this.statisticsService = statisticsService;
    }

    @GetMapping("/historyAndStatistics")
    public String GetHistoryAndStatistic(Model model) {

        var histories = historyService.getHistories();
        var statistics =  statisticsService.getStatistics();
        Statistic statistic = new Statistic();
        if(!statistics.isEmpty()){
            statistic = statistics.get(0);
        }

        model.addAttribute("histories", histories);
        model.addAttribute("statistic", statistic);
        return "historyAndStatistics";
    }
}
