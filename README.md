# Demo Spring AOP

This is simple App to record diary activities. An Activity is composes by:

1. **Name**
2. **Description**
3. **Date Time**

The app runs in port 8080 [App](http://localhost:8080/). It allows to create new diary entries.

![new_entry](Images/new_entry.png)

List all the entries so far and the chance to edit or delete an activity.

![all_entries](Images/all_entries.png)

## Stack

The app was build using the following stack:

1. Spring boot 3.4.4
2. Spring JPA
3. Spring Web
4. Spring thymeleaf
5. Spring validation
6. Spring AOP
7. h2 _(for in memory Database)_

## AOP implementation

Every operation in the app is logged to two different places:

1. **History:** Record all the CRUD operations done over the diary entries.

    ![history_table](Images/history_table.png)

2. **Statistics:** Count the modification operations performed,

    ![statistic_table](Images/statistic_table.png)

These history and statistics operation can be seem in the [historyAndStatistics](historyAndStatistics) option.

![history_statistic](Images/history_statistic.png)

## Spring AOP Concepts

On this app the _AOP_ concepts applied were:
  
1. **Aspect:** We have an _aspect_ class called [Logging](https://github.com/jiujitsuboy/SpringAOP/blob/master/AOPDemo/src/main/java/com/reailize/AOPDemo/Aspect/Logging.java) which have all the logging rules to record the _history_ and _statictis_ data.
2. **PointCuts:** We have a set of _point cuts in the class [LoggingPointCut](https://github.com/jiujitsuboy/SpringAOP/blob/master/AOPDemo/src/main/java/com/reailize/AOPDemo/PointCut/LoggingPointCut.java) where all the filters to applied the _aspect_ are defined.
3. **Advice:** In class [Logging](https://github.com/jiujitsuboy/SpringAOP/blob/master/AOPDemo/src/main/java/com/reailize/AOPDemo/Aspect/Logging.java) we combine the _pointcuts_ with the _advice_ to applied the _aspect_.

### PointCuts

These are the list of _pointcuts_ defined:

1. **diaryServiceMethodExecution:** Using _within_ we match every method on the [DiaryService](https://github.com/jiujitsuboy/SpringAOP/blob/master/AOPDemo/src/main/java/com/reailize/AOPDemo/Service/DiaryService.java) service.
2. **diaryServiceModifyExecution:** Using _within_ in combination with _!execution_ we filter the Create, Update and Delete operations from [DiaryService](https://github.com/jiujitsuboy/SpringAOP/blob/master/AOPDemo/src/main/java/com/reailize/AOPDemo/Service/DiaryService.java) service
3. **diaryServiceReadMethodExecution:** Using _within_ in combination with _execution_ we filter the Get operations from [DiaryService](https://github.com/jiujitsuboy/SpringAOP/blob/master/AOPDemo/src/main/java/com/reailize/AOPDemo/Service/DiaryService.java) service
4. **diaryControllerMethod:** Using _execution_ we get the _handleForm_ action from the [Diary Controller](https://github.com/jiujitsuboy/SpringAOP/blob/master/AOPDemo/src/main/java/com/reailize/AOPDemo/Controller/DiaryController.java) controller.
5. **diaryServiceCreateMethod:** We reuse the pointcut diaryServiceMethodExecution() plus the _@annotation_ definition to get all the actions from [DiaryService](https://github.com/jiujitsuboy/SpringAOP/blob/master/AOPDemo/src/main/java/com/reailize/AOPDemo/Service/DiaryService.java) service that use the annotation [CreateEntry](https://github.com/jiujitsuboy/SpringAOP/blob/master/AOPDemo/src/main/java/com/reailize/AOPDemo/Annotation/CreateEntry.java)
6. **diaryServiceUpdateMethod:** We reuse the pointcut diaryServiceMethodExecution() plus the _@annotation_ definition to get all the actions from [DiaryService](https://github.com/jiujitsuboy/SpringAOP/blob/master/AOPDemo/src/main/java/com/reailize/AOPDemo/Service/DiaryService.java) service that use the annotation [UpdateEntry](https://github.com/jiujitsuboy/SpringAOP/blob/master/AOPDemo/src/main/java/com/reailize/AOPDemo/Annotation/UpdateEntry.java)
7. **diaryServiceWithParameterLong:** We reuse the pointcut diaryServiceMethodExecution() plus the _args_ definition to get all the actions from [DiaryService](https://github.com/jiujitsuboy/SpringAOP/blob/master/AOPDemo/src/main/java/com/reailize/AOPDemo/Service/DiaryService.java) service that receive as arguments a _Long_ type.


### Advices

Using above _pointcuts_ to filter where to apply the advice. We have the following _advice_ used.

1. **@Around("com.reailize.AOPDemo.PointCut.LoggingPointCut.diaryServiceReadMethodExecution()"):** Record execution time for Read Operations on [DiaryService](https://github.com/jiujitsuboy/SpringAOP/blob/master/AOPDemo/src/main/java/com/reailize/AOPDemo/Service/DiaryService.java) service.
2. **@Around("com.reailize.AOPDemo.PointCut.LoggingPointCut.diaryServiceCreateMethod()"):** Record execution time and Entries values for Create Operation on [DiaryService](https://github.com/jiujitsuboy/SpringAOP/blob/master/AOPDemo/src/main/java/com/reailize/AOPDemo/Service/DiaryService.java) service.
3. **@Around("com.reailize.AOPDemo.PointCut.LoggingPointCut.diaryServiceUpdateMethod()"):** Record execution time and Entries values for Update Operation on [DiaryService](https://github.com/jiujitsuboy/SpringAOP/blob/master/AOPDemo/src/main/java/com/reailize/AOPDemo/Service/DiaryService.java) service.
4. **@Around("com.reailize.AOPDemo.PointCut.LoggingPointCut.diaryServiceWithParameterLong()"):** Record execution time and Entries values for Delete Operation on [DiaryService](https://github.com/jiujitsuboy/SpringAOP/blob/master/AOPDemo/src/main/java/com/reailize/AOPDemo/Service/DiaryService.java) service.
5. **@Around("com.reailize.AOPDemo.PointCut.LoggingPointCut.diaryServiceWithParameterLong()"):** Get if the handleForm was called for Create or Update.
6. **@AfterThrowing(pointcut = "com.reailize.AOPDemo.PointCut.LoggingPointCut.diaryServiceReadMethodExecution()", throwing = "ex"):** Capture any exception thrown during Read operations on [DiaryService](https://github.com/jiujitsuboy/SpringAOP/blob/master/AOPDemo/src/main/java/com/reailize/AOPDemo/Service/DiaryService.java) service.
7. **@AfterReturning("com.reailize.AOPDemo.PointCut.LoggingPointCut.diaryServiceWithParameterLong()"):** Increase the statistic counter for delete operation on [DiaryService](https://github.com/jiujitsuboy/SpringAOP/blob/master/AOPDemo/src/main/java/com/reailize/AOPDemo/Service/DiaryService.java) service.
8. **@After("com.reailize.AOPDemo.PointCut.LoggingPointCut.diaryServiceCreateMethod() || com.reailize.AOPDemo.PointCut.LoggingPointCut.diaryServiceUpdateMethod()"):** Increase the statistic counter for create or update operation on [DiaryService](https://github.com/jiujitsuboy/SpringAOP/blob/master/AOPDemo/src/main/java/com/reailize/AOPDemo/Service/DiaryService.java) service.
