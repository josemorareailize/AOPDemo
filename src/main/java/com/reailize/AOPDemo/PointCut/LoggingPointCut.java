package com.reailize.AOPDemo.PointCut;

import org.aspectj.lang.annotation.Pointcut;

public class LoggingPointCut {

    //Get all method from DiaryService
    @Pointcut("within(com.reailize.AOPDemo.Service.DiaryService)")
    public void diaryServiceMethodExecution() {}

    //Get create, update and delete methods from DiaryService
    @Pointcut("within(com.reailize.AOPDemo.Service.DiaryService) && !execution(* com.reailize.AOPDemo.Service.DiaryService.get*(..))")
    public void diaryServiceModifyExecution() {}

    //Get all read methods (getEntries, getEntry) from DiaryService
    @Pointcut("diaryServiceMethodExecution() && execution(* com.reailize.AOPDemo.Service.DiaryService.get*(..))")
    public void diaryServiceReadMethodExecution() {}

    //Get handleForm endpoint from DiaryController
    @Pointcut("execution(* com.reailize.AOPDemo.Controller.DiaryController.handleForm(..))")
    public void diaryControllerMethod() {}

    //Get createEntry from DiaryService
    @Pointcut("diaryServiceMethodExecution() && " +
            "@annotation(com.reailize.AOPDemo.Annotation.CreateEntry)")
    public void diaryServiceCreateMethod() {}

    //Get updateEntry from DiaryService
    @Pointcut("diaryServiceMethodExecution() && " +
            "@annotation(com.reailize.AOPDemo.Annotation.UpdateEntry)")
    public void diaryServiceUpdateMethod() {}

    //Get DeleteEntry from  DiaryService
    @Pointcut("diaryServiceModifyExecution() && args(Long)")
    public void diaryServiceWithParameterLong(){}
}
