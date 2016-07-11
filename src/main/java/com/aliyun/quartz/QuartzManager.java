package com.aliyun.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.CronTriggerImpl;

import java.text.ParseException;
import java.util.Date;
import java.util.Set;

import static org.quartz.JobBuilder.newJob;

/**
 * Created by yangf on 2016/7/7.
 */
public class QuartzManager {
    public static SchedulerFactory schedulerFactory = new StdSchedulerFactory();

    /**
     * @param cls        实现Job类的类Class<? extends Job> cls = ExampleJob.class;
     * @param jobKey     例子：JobKey jobKey = new JobKey("JobName", "myjob-group");
     * @param triggerKey 例子TriggerKey triggerKey = new TriggerKey("myTriggerName", "myTrigger-group");
     * @param cron       网上有相关解释：String cron = "0/1 * * * * ?";
     * @throws SchedulerException
     * @throws ParseException
     * @throws InterruptedException void
     * @Title: addJob
     * @Description: 给SchedulerFactory添加一个定时任务
     * 参数、返回类型及异常的说明
     */
    public static void addJob(Class<? extends Job> cls, JobKey jobKey, TriggerKey triggerKey,
        String cron) throws SchedulerException, ParseException, InterruptedException {
        // 通过工厂创建一个调度者
        Scheduler scheduler = schedulerFactory.getScheduler();
        JobDetail myJob = newJob(cls).withIdentity(jobKey).build();
        // 往job中传入参数
        myJob.getJobDataMap().put("aa", "1234");
        CronTriggerImpl trigger = new CronTriggerImpl();
        trigger.setKey(triggerKey);
        trigger.setCronExpression(cron);
        Date startDate = scheduler.scheduleJob(myJob, trigger);
        System.out.println(myJob.getKey() + " will start at:" + startDate.toString());
        // 开始运行调度程序
        scheduler.start();
    }

    public static void modifyJob(String cron, TriggerKey triggerKey)
        throws SchedulerException, ParseException {
        Scheduler sched = schedulerFactory.getScheduler();
        // 获取一个组内所有的TriggerKey
        GroupMatcher<TriggerKey> gm = GroupMatcher.triggerGroupEquals("myTrigger-group");
        Set<TriggerKey> tiggerkeys = sched.getTriggerKeys(gm);
        Trigger trigger = sched.getTrigger(triggerKey);
        if (trigger != null) {
            CronTriggerImpl cronTriggerImpl = (CronTriggerImpl) trigger;
            cronTriggerImpl.setCronExpression(cron);
            sched.rescheduleJob(triggerKey, cronTriggerImpl);
        }
    }

    public static void removeJob(JobKey jobKey, TriggerKey triggerKey) throws SchedulerException {
        Scheduler sched = schedulerFactory.getScheduler();
        sched.pauseTrigger(triggerKey);
        sched.unscheduleJob(triggerKey);
        sched.deleteJob(jobKey);
    }
}