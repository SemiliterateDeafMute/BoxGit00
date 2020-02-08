package zplum.tools._fc_timeobject;

import zplum.tools.Abacus;

public class WatchBelfry extends WatchTicker
{
	protected Long time_point_timeup_break = null;
	protected long timeloop_lastpoint;
	protected long timeloop_increment;
	protected int     timeloop_times_timeup = 0;
	protected Integer timeloop_times_timeup_break = null;

	public WatchBelfry(Watch.auxi.type time_type, long timeloop_increment_millis)
	{
		this(Watch.current(), time_type, timeloop_increment_millis, -1);
	}
	public WatchBelfry(Watch.auxi.type time_type, long timeloop_increment_millis, Integer timeloop_times)
	{
		this(Watch.current(), time_type, timeloop_increment_millis, timeloop_times);
	}
	protected WatchBelfry(Long time_current_millis, Watch.auxi.type time_type, long timeloop_increment_millis, Integer timeloop_times)
	{
		super(time_current_millis, false, time_type, 0, false);
		this.timeloop_increment = timeloop_increment_millis;
		this.timeloop_lastpoint = this.time_point_timeup;
		if(timeloop_times > 0)
			this.timeloop_times_timeup_break = timeloop_times;
		this.thread_clock.start();
	}

	public Runnable quasimodo = null;

	protected void countdown_internal()
	{
		if(this.isBegin)
			return;
		this.isBegin = true;
		mark_loop_sleep:
		for(long time_point_now = Watch.current();this.isNotFinish;)
		{
			Long time_point_timeup_tmp;
			synchronized (lock01_instant)
			{
				for(;;)
				{
					time_point_timeup_tmp = countdown_internal_funcGetValue(this.timeloop_lastpoint, this.timeloop_increment, this.timeloop_times_timeup_break);
					if(time_point_timeup_tmp == null)
						break mark_loop_sleep;
					else if(time_point_timeup_tmp > time_point_now)
						break;
					this.countdown_internal_funcDoSomethingWhenSubTimeup(this.quasimodo);
				}
			}
			if(this.countdown_internal_funcSleep(this.time_point_timeup=time_point_timeup_tmp))
				this.countdown_internal_funcDoSomethingWhenSubTimeup(this.quasimodo);
		}
		this.isNotFinish = this.isNotTimeup = false;
	}
	protected Long countdown_internal_funcGetValue(long timeloop_lastpoint_temp, long timeloop_increment_tmp, Integer timeloop_times_timeup_break_tmp)
	{
		if(timeloop_increment_tmp <= 0)
			return null;
		if(timeloop_times_timeup_break_tmp !=null && timeloop_times_timeup_break_tmp <= this.timeloop_times_timeup)
			return null;
		long time_point_timeupNext = timeloop_lastpoint_temp + timeloop_increment_tmp;
		if((this.time_point_timeup_break==null) ? false : time_point_timeupNext >= this.time_point_timeup_break )
			return null;
		return time_point_timeupNext;
	}
	protected void countdown_internal_funcDoSomethingWhenSubTimeup(Runnable runner)
	{
		this.timeloop_times_timeup++;
		if(runner != null)
			runner.run();
		this.timeloop_lastpoint = this.time_point_timeup;
		for(Thread keeper: thread_clock_keepers)
			keeper.interrupt();
	}
	protected void countdown_external() throws InterruptedException
	{
		Thread _this_thread = Thread.currentThread();
		thread_clock_keepers.add(Thread.currentThread());
		for(;this.isNotFinish;)
			countdown_external_fucnDoSometingInLoopBody(_this_thread);
		if(!this.isNotFinish && this.isNotTimeup)
			throw new InterruptedException(Watch.define.BelfryExceptionMsg_reFinishButNotTimeup + Abacus.getCodeSiteHere());
	}
	private void countdown_external_fucnDoSometingInLoopBody(Thread _this_thread) throws InterruptedException
	{
		try {
			this.thread_clock.join();
		} catch (InterruptedException e) {

			if(thread_clock_keepers.contains(_this_thread))
				return;
			else if(this.isNotFinish)
				throw new InterruptedException(Watch.define.BelfryExceptionMsg_reNotFinishButKeeperMethodInterrupte + Abacus.getCodeSiteHere());
		}
		return;
	}
	public void countdown(Runnable runner_timeup) throws InterruptedException
	{
		int times_this = 0;
		int times_last = 0;
		Thread _this_thread = Thread.currentThread();
		thread_clock_keepers.add(Thread.currentThread());
		for(;this.isNotFinish;)
		{
			try {
				countdown_external_fucnDoSometingInLoopBody(_this_thread);
			} finally {
				times_last = times_this;
				times_this = this.timeloop_times_timeup;
				for(int i=0,j=times_this-times_last; i<j; i++)
					runner_timeup.run();
			}
		}
		if(!this.isNotFinish && this.isNotTimeup)
			throw new InterruptedException(Watch.define.BelfryExceptionMsg_reFinishButNotTimeup + Abacus.getCodeSiteHere());
	}

	private final Object lock01_instant = new Object();
	private final Object lock02_modify = new Object();
	public void timeup_value_add(long time_value_millis, boolean isInclude_timetype_point)
	{
		synchronized (lock01_instant) {}
		synchronized (lock02_modify)
		{
			if(!this.isNotFinish)
				return;
			if(this.timetype_timeup.isPoint() && !isInclude_timetype_point)
				return;
			this.timeloop_lastpoint += time_value_millis;
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
			if(!this.timetype_timeup.isPoint() && isOnly_timetype_point)
				return;
			this.time_point_timeup_break = time_point_millis;
			if(this.time_point_timeup_break < this.time_point_timeup)
				this.thread_clock.interrupt();
		}
	}
	public void timeloop_times_add(int times)
	{
		synchronized (lock01_instant) {}
		synchronized (lock02_modify)
		{
			if(!this.isNotFinish)
				return;
			if(this.timeloop_times_timeup_break == null)
				return;
			this.timeloop_times_timeup_break += times;
		}
	}
	public void timeloop_times_set(int times)
	{
		synchronized (lock01_instant) {}
		synchronized (lock02_modify)
		{
			if(!this.isNotFinish)
				return;
			if(times < 0)
				this.timeloop_times_timeup_break = null;
			this.timeloop_times_timeup_break = times;
			if(this.timeloop_times_timeup_break < this.timeloop_times_timeup)
				this.thread_clock.interrupt();
		}
	}
	public void timeloop_increment_add(long timeloop_increment_millis)
	{
		synchronized (lock01_instant) {}
		synchronized (lock02_modify)
		{
			if(!this.isNotFinish)
				return;
			this.timeloop_increment += timeloop_increment_millis;
			if(this.timeloop_increment <= 0)
				this.thread_clock.interrupt();
		}
	}
	public void timeloop_increment_set(long timeloop_increment_millis)
	{
		synchronized (lock01_instant) {}
		synchronized (lock02_modify)
		{
			if(!this.isNotFinish)
				return;
			boolean isInterrupt = timeloop_increment_millis < this.timeloop_increment;
			this.timeloop_increment = timeloop_increment_millis;
			if(isInterrupt)
				this.thread_clock.interrupt();
		}
	}

	public long time_remaining()
	{
		int times = this.times_remaining();
		long increment = this.timeloop_increment;
		long timepoint = this.time_point_timeup;
		if(times < 0)
			return -1;
		if((times-=1) < 0)
			return 0;
		return (increment * times) + (timepoint - (isNotPause?Watch.current():this.time_point_pause) );
	}
	public int times_elapsed()
	{
		return this.timeloop_times_timeup;
	}
	public int times_remaining()
	{
		Integer times = this.timeloop_times_timeup_break;
		if(times == null)
			return -1;
		if((times-=this.timeloop_times_timeup) < 0)
			return 0;
		return times;
	}

}

