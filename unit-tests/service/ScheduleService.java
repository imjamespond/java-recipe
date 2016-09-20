package service;

import java.util.Date;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService {
	private static final Logger logger = LoggerFactory.getLogger(ScheduleService.class);
	public static boolean statable = true;

	private ScheduledThreadPoolExecutor[] schedulers;
	private TaskScheduler taskScheduler;
	private static final int SCHEDULER_COUNT = 5;
	private static final int SCHEDULER_THREAD_COUNT = 2;

	public ScheduleService() {
		this.schedulers = new Scheduler[SCHEDULER_COUNT];
		for (int i = 0; i < SCHEDULER_COUNT; i++) {
			this.schedulers[i] = new Scheduler();
		}
		this.taskScheduler = new SpringTaskScheduler();
	}

	@PostConstruct
	public void init() {
	}

	public ScheduledThreadPoolExecutor[] getSchedulers() {
		return this.schedulers;
	}

	private ScheduledThreadPoolExecutor getScheduler() {
		return this.schedulers[RandomUtils.nextInt(SCHEDULER_COUNT)];
	}

	public ScheduledFuture<?> schedule(Runnable command, String cron) {
		if (statable)
			command = new StatEnableTask(command);
		return this.taskScheduler.schedule(command, new CronTrigger(cron));
	}

	public ScheduledFuture<?> schedule(Runnable command, Date date) {
		if (statable)
			command = new StatEnableTask(command);
		return this.taskScheduler.schedule(command, date);
	}

	public ScheduledFuture<?> schedule(Runnable command, long delayms) {
		if (statable)
			command = new StatEnableTask(command);
		return getScheduler().schedule(command, delayms, TimeUnit.MILLISECONDS);
	}

	public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long delayms, long periodms) {
		if (statable)
			command = new StatEnableTask(command);
		return getScheduler().scheduleAtFixedRate(command, delayms, periodms, TimeUnit.MILLISECONDS);
	}

	public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, Date startTime, long period) {
		if (statable)
			command = new StatEnableTask(command);
		return this.taskScheduler.scheduleAtFixedRate(command, startTime, period);
	}

	private class SpringTaskScheduler extends ThreadPoolTaskScheduler {
		private static final long serialVersionUID = -6845869115437347960L;

		private SpringTaskScheduler() {
		}

		protected ScheduledExecutorService createExecutor(int poolSize, ThreadFactory threadFactory,
				RejectedExecutionHandler rejectedExecutionHandler) {
			return ScheduleService.this.schedulers[0];
		}

		public ScheduledExecutorService getScheduledExecutor() throws IllegalStateException {
			return ScheduleService.this.getScheduler();
		}
	}

	private static class Scheduler extends ScheduledThreadPoolExecutor {
		public Scheduler() {
			super(SCHEDULER_THREAD_COUNT);
		}

		protected void afterExecute(Runnable r, Throwable t) {
			super.afterExecute(r, t);
			if (t != null)
				t.printStackTrace();
		}

		protected void beforeExecute(Thread t, Runnable r) {
			super.beforeExecute(t, r);
		}
	}

	private class StatEnableTask implements Runnable {
		private final Runnable task;

		public StatEnableTask(Runnable task) {
			this.task = task;
		}

		public void run() {
			long beginTime = System.currentTimeMillis();
			if (this.task == null)
				return;
			try {
				this.task.run();
			} catch (Throwable t) {
				ScheduleService.logger.error(this.task.getClass().getSimpleName());
				ScheduleService.logger.error(t.getLocalizedMessage(), t);
			} finally {
				ScheduleService.logger.trace(this.task.getClass().getSimpleName() + "|"
						+ (System.currentTimeMillis() - beginTime));
			}
		}
	}
}