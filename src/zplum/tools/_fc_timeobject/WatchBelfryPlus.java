package zplum.tools._fc_timeobject;

import zplum.tools.Abacus;

public class WatchBelfryPlus extends WatchTicker
{
	public static class Bell extends WatchBelfryPlusAuxil_Bell
	{
		private Bell(WatchBelfryPlus belfry, zplum.tools._fc_timeobject.Watch.auxi.type time_type, Runnable runner)
		{
			super(belfry, time_type, runner);
		}
	}

	protected final Bell bell;
	protected WatchBelfryPlus(Runnable runner, Watch.auxi.type time_type, Long time_current_millis, long... timelist_point_millis)
	{
		super(time_current_millis, false, time_type, 0, false);
		this.bell = new Bell(this, time_type, runner);
		for(long time_point_millis: timelist_point_millis)
			this.bell.timeuplist_add(time_point_millis);
		this.thread_clock.start();
	}
	protected WatchBelfryPlus(Runnable runner, Watch.auxi.type time_type, Long time_current_millis, long timeloop_increment_millis, Integer timeloop_times)
	{
		super(time_current_millis, false, time_type, 0, false);
		this.bell = new Bell(this, time_type, runner);
		this.bell.timeloop_initial(this.time_point_timeup, timeloop_increment_millis, timeloop_times);
		this.thread_clock.start();
	}
	public void dosomething()
	{
	}

	protected void countdown_internal()
	{
		if(this.isBegin)
			return;
		this.isBegin = true;
		for(;this.isNotFinish;)
		{
			long time_point_timeup_tmp;
			Long time_point_timeup_orig;
			synchronized (lock01_instant)
			{
				this.bell.makeMark_timeup_min();
				time_point_timeup_tmp = this.bell.peekMark_timeupWithIncrement();
				time_point_timeup_orig = this.bell.peekMark_timeupOrig();
			}
			if(time_point_timeup_orig != null)
				if(this.countdown_internal_funcSleep(this.time_point_timeup=time_point_timeup_tmp))
					this.bell.ring(time_point_timeup_orig);
			if(this.bell.isEmpty())
				this.isNotFinish = this.isNotTimeup = false;
		}
		this.bell.finish();
	}
	protected void countdown_external() throws InterruptedException
	{
		thread_clock_keepers.add(Thread.currentThread());
		try {
			this.thread_clock.join();
		} catch (InterruptedException e) {
			if(this.isNotFinish && !thread_clock_keepers.contains(Thread.currentThread()))
				throw new InterruptedException(Watch.define.BelfryExceptionMsg_reNotFinishButKeeperMethodInterrupte + Abacus.getCodeSiteHere());
		} finally {
			thread_clock_keepers.remove(Thread.currentThread());
		}
		if(!this.isNotFinish && this.isNotTimeup)
			throw new InterruptedException(Watch.define.BelfryExceptionMsg_reFinishButNotTimeup + Abacus.getCodeSiteHere());

	}

	private final Object lock01_instant = new Object();
	private final Object lock02_modify  = new Object();
	public void timeup_value_add(long time_value_millis, boolean isInclude_timetype_point)
	{
		synchronized (lock01_instant) {}
		synchronized (lock02_modify)
		{
			if(!this.isNotFinish)
				return;
			if(this.timetype_timeup.isPoint() && !isInclude_timetype_point)
				return;
			this.bell.timeup_value_add(time_value_millis, isInclude_timetype_point);
			this.thread_clock.interrupt();
		}
	}
	public void timeup_point_set(long time_point_millis, boolean isOnly_timetype_point)
	{
		synchronized (lock01_instant) {}
		synchronized (lock02_modify)
		{
			if(!this.isNotFinish)
				return;
			this.bell.timeup_point_set(time_point_millis, isOnly_timetype_point);
		}
	}
	public void timeloop_times_add(int times)
	{
		synchronized (lock01_instant) {}
		synchronized (lock02_modify)
		{
			if(!this.isNotFinish)
				return;
			this.bell.timeloop_times_add(times);
		}
	}
	public void timeloop_times_set(int times)
	{
		synchronized (lock01_instant) {}
		synchronized (lock02_modify)
		{
			if(!this.isNotFinish)
				return;
			this.bell.timeloop_times_set(times);
		}
	}
	public void timeloop_increment_add(long timeloop_increment_millis)
	{
		synchronized (lock01_instant) {}
		synchronized (lock02_modify)
		{
			if(!this.isNotFinish)
				return;
			this.bell.timeloop_increment_add(timeloop_increment_millis);
		}
	}
	public void timeloop_increment_set(long timeloop_increment_millis)
	{
		synchronized (lock01_instant) {}
		synchronized (lock02_modify)
		{
			if(!this.isNotFinish)
				return;
			this.bell.timeloop_increment_set(timeloop_increment_millis);
		}
	}
	public void timeuplist_add(long time_point_millis)
	{
		synchronized (lock01_instant) {}
		synchronized (lock02_modify)
		{
			if(!this.isNotFinish)
				return;
			this.bell.timeuplist_add(time_point_millis);
		}
	}

	public long time_remaining()
	{
		return -1;
	}

	public void pause_break()
	{
		synchronized (lock01_instant) {}
		synchronized (this.lock02_modify)
		{
			this.bell.pause_break();
			this.isNotPause = !this.bell.isPause();
		}
	}
	public void pause(boolean isInclude_timetype_point)
	{
		synchronized (lock01_instant) {}
		synchronized (this.lock02_modify)
		{
			this.bell.pause(isInclude_timetype_point);
			this.isNotPause = !this.bell.isPause();
		}
	}
}

