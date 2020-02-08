package zplum.tools._fc_timeobject;

import java.util.HashSet;

import zplum.tools.Abacus;
import zplum.tools._fc_timeobject.Watch.kit_plus.core;

public class WatchTicker extends core
{
	protected final long time_point_base;
	protected       long time_point_timeup;
	protected final Watch.auxi.type timetype_timeup;
	protected boolean isBegin = false;
	protected boolean isNotFinish = true;
	protected boolean isNotTimeup = true;
	public WatchTicker(long time_initial_millis)
	{
		this(Watch.current(), true, Watch.auxi.type.value, time_initial_millis, false);
	}
	public WatchTicker(Watch.auxi.type time_type, long time_initial_millis)
	{
		this(Watch.current(), true, time_type, time_initial_millis, false);

	}
	public WatchTicker(Watch.auxi.type time_type, long time_initial_millis, boolean isPause_initial)
	{
		this(Watch.current(), true, time_type, time_initial_millis, isPause_initial);
	}
	protected WatchTicker(Long time_current_millis, boolean isStart_initial, Watch.auxi.type time_type, long time_initial_millis, boolean isPause_initial)
	{
		this.timetype_timeup = time_type;
		this.time_point_base = (time_current_millis==null)?Watch.current():time_current_millis;
		switch(this.timetype_timeup)
		{
			case point:
				this.time_point_timeup = time_initial_millis;
				break;
			default:
			case value:
				this.time_point_timeup = this.time_point_base + time_initial_millis;
				break;
		}
		WatchTicker thisone = this;
		this.thread_clock = new Thread(){
			public void run()
			{
				thisone.countdown_internal();
			}
		};

		if(isPause_initial)
			this.pause();
		if(isStart_initial)
			this.thread_clock.start();
	}

	protected final Thread thread_clock;
	protected final HashSet<Thread> thread_clock_keepers = new HashSet<Thread>();

	protected void countdown_internal()
	{
		if(this.isBegin)
			return;
		this.isBegin = true;
		for(;this.isNotFinish;)
			if(this.countdown_internal_funcSleep(this.time_point_timeup))
				this.isNotFinish = this.isNotTimeup = false;
	}
	protected boolean countdown_internal_funcSleep(long tmp)
	{
		if(this.isNotPause)
		{
			tmp = tmp-Watch.sleep(tmp-Watch.current())-this.time_point_timeup;
		} else {
			Watch.sleepAllTheTime();
			tmp = Watch.current()-this.time_point_timeup;
		}
		return tmp >= -Watch.config.time_error;
	}

	protected void countdown_external() throws InterruptedException
	{
		thread_clock_keepers.add(Thread.currentThread());
		try {
			this.thread_clock.join();
		} catch (InterruptedException e) {
			if(this.isNotFinish && !thread_clock_keepers.contains(Thread.currentThread()))
				throw new InterruptedException(Watch.define.TickerExceptionMsg_reNotFinishButKeeperMethodInterrupte + Abacus.getCodeSiteHere());
		} finally {
			thread_clock_keepers.remove(Thread.currentThread());
		}
		if(!this.isNotFinish && this.isNotTimeup)
			throw new InterruptedException(Watch.define.TickerExceptionMsg_reFinishButNotTimeup + Abacus.getCodeSiteHere());

	}
	public void countdown() throws InterruptedException
	{
		this.countdown_external();
	}
	public void countdown(Runnable runner_timeup) throws InterruptedException
	{
		this.countdown_external();
		runner_timeup.run();
	}

	public void timeup_value_add(long time_value_millis)
	{
		this.timeup_value_add(time_value_millis, false);
	}
	public void timeup_point_set(long time_point_millis)
	{
		this.timeup_point_set(time_point_millis, false);
	}
	public void timeup_value_add(long time_value_millis, boolean isInclude_timetype_point)
	{
		if(!this.isNotFinish)
			return;
		if(this.timetype_timeup.isPoint() && !isInclude_timetype_point)
			return;
		this.time_point_timeup += time_value_millis;
		if(time_value_millis < 0)
			this.thread_clock.interrupt();
	}
	public void timeup_point_set(long time_point_millis, boolean isOnly_timetype_point)
	{
		if(!this.isNotFinish)
			return;
		if(!this.timetype_timeup.isPoint() && isOnly_timetype_point)
			return;
		boolean isInterrupt =  this.time_point_timeup < time_point_millis;
		this.time_point_timeup = time_point_millis;
		if(isInterrupt)
			this.thread_clock.interrupt();
	}
	public void finish()
	{
		this.isNotFinish = false;
		this.thread_clock.interrupt();
	}
	public void finish(Thread thread)
	{
		if(thread_clock_keepers.remove(thread))
			thread.interrupt();
	}

	public boolean isFinish()
	{
		return !this.isNotFinish;
	}
	public boolean isTimeup()
	{
		return !this.isNotTimeup;
	}
	public long time_elapsed()
	{
		return (isNotPause?Watch.current():this.time_point_pause) - this.time_point_base - this.time_value_pause;
	}
	public long time_elapsed_includePause()
	{
		return Watch.current() - this.time_point_base;
	}
	public long time_remaining() throws InterruptedException
	{
		return this.time_point_timeup - (isNotPause?Watch.current():this.time_point_pause);
	}
	public Watch.auxi.type time_type()
	{
		return this.timetype_timeup;
	}

	public void pause()
	{
		this.pause(false);
	}
	public void pause_break()
	{
		if(!isNotPause)
		{
			this.isNotPause = true;
			this.time_value_pause += Watch.current() - this.time_point_pause;
			this.time_point_pause  = 0;
			this.time_point_timeup += this.time_value_pause;
			this.thread_clock.interrupt();
		}
	}
	public void pause(boolean isInclude_timetype_point)
	{
		if(this.timetype_timeup.isPoint() && !isInclude_timetype_point)
			return;
		if(isNotPause)
		{
			this.isNotPause = false;
			this.time_point_pause = Watch.current();
			this.thread_clock.interrupt();
		}
	}
	public void pause_exchange(boolean isInclude_timetype_point)
	{
		if(this.isNotPause)
			this.pause(isInclude_timetype_point);
		else
			this.pause_break();
	}

	@SuppressWarnings("unused")
	private static class BKUP
	{
		public static class WatchTicker_BUKP extends core
		{
			protected final long time_point_base;
			protected       long time_point_timeup;
			protected final Watch.auxi.type timetype_timeup;
			protected boolean isBegin = false;
			protected boolean isNotFinish = true;
			protected boolean isNotTimeup = true;
			public WatchTicker_BUKP(long time_initial_millis)
			{
				this(Watch.current(), true, Watch.auxi.type.value, time_initial_millis, false);
			}
			public WatchTicker_BUKP(Watch.auxi.type time_type, long time_initial_millis)
			{
				this(Watch.current(), true, time_type, time_initial_millis, false);

			}
			public WatchTicker_BUKP(Watch.auxi.type time_type, long time_initial_millis, boolean isPause_initial)
			{
				this(Watch.current(), true, time_type, time_initial_millis, isPause_initial);
			}
			protected WatchTicker_BUKP(Long time_current_millis, boolean isStart_initial, Watch.auxi.type time_type, long time_initial_millis, boolean isPause_initial)
			{
				this.timetype_timeup = time_type;
				this.time_point_base = (time_current_millis==null)?Watch.current():time_current_millis;
				switch(this.timetype_timeup)
				{
					case point:
						this.time_point_timeup = time_initial_millis;
						break;
					default:
					case value:
						this.time_point_timeup = this.time_point_base + time_initial_millis;
						break;
				}
				WatchTicker_BUKP thisone = this;
				this.thread_clock = new Thread(){
					public void run()
					{
						thisone.countdown_internal();
					}
				};
				if(isPause_initial)
					this.pause();
				if(isStart_initial)
					this.thread_clock.start();
			}

			protected final Thread thread_clock;
			protected final HashSet<Thread> thread_clock_keepers = new HashSet<Thread>();

			protected void countdown_internal()
			{
				if(this.isBegin)
					return;
				this.isBegin = true;
				for(;this.isNotFinish;)
					if(this.countdown_internal_funcSleep(this.time_point_timeup))
						this.isNotFinish = this.isNotTimeup = false;
			}
			protected boolean countdown_internal_funcSleep(long tmp)
			{
				if(this.isNotPause)
				{
					tmp = tmp-Watch.sleep(tmp-Watch.current())-this.time_point_timeup;
				} else {
					Watch.sleepAllTheTime();
					tmp = Watch.current()-this.time_point_timeup;
				}
				return tmp >= -Watch.config.time_error;
			}

			protected void countdown_external() throws InterruptedException
			{
				thread_clock_keepers.add(Thread.currentThread());
				try {
					this.thread_clock.join();
				} catch (InterruptedException e) {
					if(this.isNotFinish && !thread_clock_keepers.contains(Thread.currentThread()))
						throw new InterruptedException(Watch.define.TickerExceptionMsg_reNotFinishButKeeperMethodInterrupte + Abacus.getCodeSiteHere());
				}
				if(!this.isNotFinish && this.isNotTimeup)
					throw new InterruptedException(Watch.define.TickerExceptionMsg_reFinishButNotTimeup + Abacus.getCodeSiteHere());

			}
			public void countdown() throws InterruptedException
			{
				this.countdown_external();
			}
			public void countdown(Runnable runner_timeup) throws InterruptedException
			{
				this.countdown_external();
				runner_timeup.run();
			}

			public void timeup_value_add(long time_value_millis)
			{
				this.timeup_value_add(time_value_millis, false);
			}
			public void timeup_point_set(long time_point_millis)
			{
				this.timeup_point_set(time_point_millis, false);
			}
			public void timeup_value_add(long time_value_millis, boolean isInclude_timetype_point)
			{
				if(!this.isNotFinish)
					return;
				if(this.timetype_timeup.isPoint() && !isInclude_timetype_point)
					return;
				this.time_point_timeup += time_value_millis;
				if(time_value_millis < 0)
					this.thread_clock.interrupt();
			}
			public void timeup_point_set(long time_point_millis, boolean isOnly_timetype_point)
			{
				if(!this.isNotFinish)
					return;
				if(!this.timetype_timeup.isPoint() && isOnly_timetype_point)
					return;
				boolean isInterrupt =  this.time_point_timeup < time_point_millis;
				this.time_point_timeup = time_point_millis;
				if(isInterrupt)
					this.thread_clock.interrupt();
			}
			public void finish()
			{
				this.isNotFinish = false;
				this.thread_clock.interrupt();
			}
			public void finish(Thread thread)
			{
				if(thread_clock_keepers.remove(thread))
					thread.interrupt();
			}

			public boolean isFinish()
			{
				return !this.isNotFinish;
			}
			public boolean isTimeup()
			{
				return !this.isNotTimeup;
			}
			public long time_elapsed()
			{
				return (isNotPause?Watch.current():this.time_point_pause) - this.time_point_base - this.time_value_pause;
			}
			public long time_elapsed_includePause()
			{
				return Watch.current() - this.time_point_base;
			}
			public long time_remaining() throws InterruptedException
			{
				return this.time_point_timeup - (isNotPause?Watch.current():this.time_point_pause);
			}
			public Watch.auxi.type time_type()
			{
				return this.timetype_timeup;
			}

			public void pause()
			{
				this.pause(false);
			}
			public void pause_break()
			{
				if(!isNotPause)
				{
					this.isNotPause = true;
					this.time_value_pause += Watch.current() - this.time_point_pause;
					this.time_point_pause  = 0;
					this.time_point_timeup += this.time_value_pause;
					this.thread_clock.interrupt();
				}
			}
			public void pause(boolean isInclude_timetype_point)
			{
				if(this.timetype_timeup==Watch.auxi.type.point && !isInclude_timetype_point)
					return;
				if(isNotPause)
				{
					this.isNotPause = false;
					this.time_point_pause = Watch.current();
					this.thread_clock.interrupt();
				}
			}
			public void pause_exchange(boolean isInclude_timetype_point)
			{
				if(this.isNotPause)
					this.pause(isInclude_timetype_point);
				else
					this.pause_break();
			}
	}

	}
}
