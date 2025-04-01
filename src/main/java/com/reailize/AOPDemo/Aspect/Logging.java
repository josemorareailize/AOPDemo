package com.reailize.AOPDemo.Aspect;

import com.reailize.AOPDemo.Model.Entry;
import com.reailize.AOPDemo.Model.History;
import com.reailize.AOPDemo.Model.Statistic;
import com.reailize.AOPDemo.Service.DiaryService;
import com.reailize.AOPDemo.Service.HistoryService;
import com.reailize.AOPDemo.Service.StatisticService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import java.util.List;

@Aspect
@Component
public class Logging {

    private final HistoryService historyService;
    private final StatisticService statisticsService;
    private final DiaryService diaryService;

    public Logging(HistoryService historyService, StatisticService statisticsService, DiaryService diaryService) {
        this.historyService = historyService;
        this.statisticsService = statisticsService;
        this.diaryService = diaryService;
    }


    private Statistic getStatistics() {
        List<Statistic> statistics = statisticsService.getStatistics();
        return statistics.isEmpty() ? new Statistic(): statistics.get(0) ;
    }

    private History createHistoryRecord(String operation, long executionTime, String previousValue, String newValue, String message) {

        History history = new History();
        history.setOperation(operation);
        history.setNsTaken(executionTime);
        history.setPreviousValue(previousValue);
        history.setNewValue(newValue);
        history.setMessage(message);

        return history;
    }

    @Around("com.reailize.AOPDemo.PointCut.LoggingPointCut.diaryServiceReadMethodExecution()")
    public Object logExecutionTimeForReadOperations(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.nanoTime();

        Object returnValue = proceedingJoinPoint.proceed();
        long endTime = System.nanoTime(); // End time
        long executionTime = (endTime - startTime) / 1_000_000;

        String operation = proceedingJoinPoint.getSignature().getName();

        History history = createHistoryRecord(operation, executionTime, null, null, null);

        historyService.createHistory(history);

        return returnValue;
    }


    @Around("com.reailize.AOPDemo.PointCut.LoggingPointCut.diaryServiceCreateMethod()")
    public Object logExecutionTimeForCreateOperations(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        long startTime = System.nanoTime();

        Object returnValue = proceedingJoinPoint.proceed();

        long endTime = System.nanoTime(); // End time
        long executionTime = (endTime - startTime) / 1_000_000;

        String operation = proceedingJoinPoint.getSignature().getName();

        History history = createHistoryRecord(operation, executionTime, null, returnValue.toString(), null);

        historyService.createHistory(history);

        return returnValue;
    }


    @Around("com.reailize.AOPDemo.PointCut.LoggingPointCut.diaryServiceUpdateMethod()")
    public Object logExecutionTimeFoUpdateOperations(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String operation = proceedingJoinPoint.getSignature().getName();

        Entry entry = (Entry) proceedingJoinPoint.getArgs()[0];
        String prevEntry = diaryService.getEntry(entry.getId()).toString();

        long startTime = System.nanoTime();

        Object returnValue = proceedingJoinPoint.proceed();

        long endTime = System.nanoTime(); // End time
        long executionTime = (endTime - startTime) / 1_000_000;

        History history = createHistoryRecord(operation, executionTime, prevEntry, returnValue.toString(), null);

        historyService.createHistory(history);

        return returnValue;
    }

    @Around("com.reailize.AOPDemo.PointCut.LoggingPointCut.diaryServiceWithParameterLong()")
    public Object logExecutionTimeForDeleteOperations(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String operation = proceedingJoinPoint.getSignature().getName();

        Long id = (Long) proceedingJoinPoint.getArgs()[0];
        String prevEntry = diaryService.getEntry(id).toString();

        long startTime = System.nanoTime();

        Object returnValue = proceedingJoinPoint.proceed();

        long endTime = System.nanoTime(); // End time
        long executionTime = (endTime - startTime) / 1_000_000;

        History history = createHistoryRecord(operation, executionTime, prevEntry, null, null);

        historyService.createHistory(history);

        return returnValue;
    }


    @Before("com.reailize.AOPDemo.PointCut.LoggingPointCut.diaryControllerMethod()")
    public void verifyTypeOfOperationToPerformOnHandleForm(JoinPoint joinPoint) {

        Entry entry = (Entry) joinPoint.getArgs()[0];

        if (entry.getId() == null) {
            System.out.println("Create entry");
        } else {
            System.out.println("Update entry");
        }
    }

    @AfterThrowing(pointcut = "com.reailize.AOPDemo.PointCut.LoggingPointCut.diaryServiceReadMethodExecution()",
            throwing = "ex")
    public void expectationOnReadOperation(JoinPoint joinPoint, RuntimeException ex) {

        Long id = (Long) joinPoint.getArgs()[0];
        String operation = joinPoint.getSignature().getName();

        String message = String.format("Method %s with parameters %d throw exception %s:\n", operation, id, ex.getMessage());

        History history = createHistoryRecord(operation, 0, null, null, message);

        historyService.createHistory(history);
    }

    @AfterReturning("com.reailize.AOPDemo.PointCut.LoggingPointCut.diaryServiceWithParameterLong()")
    public void verifyTypeOfOperationToPerformOnWithParameterLong(JoinPoint joinPoint) {
        Long id = (Long) joinPoint.getArgs()[0];
        String methodName = joinPoint.getSignature().getName();

        Statistic statistics = getStatistics();

        statistics.setDeleted(statistics.getDeleted() + 1);

        statisticsService.createStatistics(statistics);
    }

    @After("com.reailize.AOPDemo.PointCut.LoggingPointCut.diaryServiceCreateMethod() || com.reailize.AOPDemo.PointCut.LoggingPointCut.diaryServiceUpdateMethod()")
    public void countModificationOperations(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();

        Statistic statistics = getStatistics();

        if (methodName.equals("createEntry")) {

            statistics.setCreated(statistics.getCreated() + 1);
        } else {
            statistics.setUpdated(statistics.getUpdated() + 1);
        }

        statisticsService.createStatistics(statistics);
    }
}
