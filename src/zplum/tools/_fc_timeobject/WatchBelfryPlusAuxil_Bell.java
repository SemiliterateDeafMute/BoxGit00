package zplum.tools._fc_timeobject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import zplum.plus.ScMath;
import zplum.plus.ScSetSortedLong;

public class WatchBelfryPlusAuxil_Bell
{
	@SuppressWarnings("unused")
	private static final class config
	{
		private static final long timepoint_reserve_sidemax = Long.MAX_VALUE - 0;
		private static final long timepoint_reserve_close   = Long.MAX_VALUE - 1;
		private static final long timepoint_reserve_empty   = Long.MAX_VALUE - 2;
		private static final long timepoint_reserve_pause   = Long.MAX_VALUE - 3;
		private static final long timepoint_reserve_sidemin = Long.MAX_VALUE - 4;
	}
	private static final Runnable quasimodo = new Thread() {
		public void run() {}
	};

	private boolean isFinish = false;
	private boolean isFInishWhenEnpty = false;
	public final Runnable runner;
	public final Watch.auxi.type time_type;

	private long time_increment = 0;
	private Long time_point_break = null;
	protected final ScSetSortedLong.WithFactroyExtd01 time_point_timeupNext = new ScSetSortedLong.WithFactroyExtd01(true);
	protected WatchBelfryPlusAuxil_Bell(WatchBelfryPlus belfry, Watch.auxi.type time_type, Runnable runner)
	{
		this.time_type = time_type;
		this.runner = (runner==null)?quasimodo:runner;
		this.thread_belfry = (belfry==null)?null:belfry.thread_clock;
		if(belfry == null || time_type == null)
			this.isFinish = true;
	}
	public void timeloop_initial(long timeloop_initial_millis, long timeloop_increment_millis, Integer timeloop_times)
	{
		synchronized (lock01_instant) {}
		synchronized (lock02_modify)
		{
			if(this.isFinish)
				return;
			this.time_point_timeupNext.ctrl.setValue(timeloop_initial_millis, timeloop_increment_millis, timeloop_times, null, null);
			this.thread_belfry.interrupt();
		}
	}

	public Long ring()
	{
		if(this.isFinish)
			return null;
		Map.Entry<Long, Integer> entry = this.time_point_timeupNext.pollMin();
		if(entry == null)
			return null;
		for(int i=0,j=entry.getValue(); i<j; i++)
			this.runner.run();
		this.ring_funcDoSomething(entry.getValue());
		return entry.getKey();
	}
	public Long ring(long timeup_point_orig)
	{
		if(this.isFinish)
			return null;
		int times = 0;
		Long time_point_timeupNext_min = null;
		Map.Entry<Long, Integer> entry = null;
		synchronized (lock01_instant)
		{
			for(;;)
			{
				time_point_timeupNext_min = this.time_point_timeupNext.peekMin();
				if(time_point_timeupNext_min == null || ((this.time_point_break==null)?false:this.time_point_break<time_point_timeupNext_min))
					break;
				if(timeup_point_orig < time_point_timeupNext_min)
					break;
				entry = this.time_point_timeupNext.pollMin();
				times += entry.getValue();
				this.ring_funcDoSomething(entry.getValue());
			}
		}

		for(int i=0,j=times; i<j; i++)
			this.runner.run();
		if(this.isFInishWhenEnpty)
			this.finish();
		return (entry==null) ?null :entry.getKey()+this.time_increment;
	}
	public void ring_funcDoSomething(long time_point)
	{
		ArrayList<Thread> threads = this.thread_keepers.get(time_point);
		if(threads!=null)
			for(Thread thread: threads)
				thread.interrupt();
		this.thread_keepers.remove(time_point);
		ArrayList<Runnable> runners = this.timeup_runners.get(time_point);
		if(runners!=null)
			for(Runnable runner: runners)
				runner.run();
		this.timeup_runners.remove(time_point);
	}

	private Long time_point_timeup_mark = null;
	private Long time_point_timeup_markWithIncrement = null;
	public void makeMark_timeup_min()
	{
		synchronized (lock01_instant)
		{
			this.time_point_timeup_mark = this.time_point_timeupNext.peekMin();
			if(this.isFinish)
				this.time_point_timeup_markWithIncrement = config.timepoint_reserve_close;
			else if(this.isPause)
				this.time_point_timeup_markWithIncrement = config.timepoint_reserve_pause;
			else
				this.time_point_timeup_markWithIncrement = (this.time_point_timeup_mark==null) ? config.timepoint_reserve_empty :this.time_point_timeup_mark+this.time_increment;
		}
	}
	public Long peekMark_timeupOrig()
	{
		return this.time_point_timeup_mark;
	}
	public Long peekMark_timeupWithIncrement()
	{
		return this.time_point_timeup_markWithIncrement;
	}

	protected final Thread thread_belfry;
	protected final HashMap<Long, ArrayList<Thread>>   thread_keepers = new HashMap<Long, ArrayList<Thread>>();
	protected final HashMap<Long, ArrayList<Runnable>> timeup_runners = new HashMap<Long, ArrayList<Runnable>>();
	public void countdown(Long time_point_millis) throws InterruptedException
	{
		this.countdown(time_point_millis, 0);
	}
	public void countdown(Long timeup_point_millis, long timeout_millis) throws InterruptedException
	{
		if(this.isFinish)
			return;
		Thread _this_thread = Thread.currentThread();
		ArrayList<Thread> threads;
		timeup_point_millis -= this.time_increment;
		threads = this.thread_keepers.get(timeup_point_millis);
		if(threads == null)
		{
			threads = new ArrayList<Thread>();
			this.thread_keepers.put(timeup_point_millis, threads);
		}
		try {
			threads.add(_this_thread);
			this.timeuplist_add(timeup_point_millis, false);
			this.thread_belfry.join(timeout_millis);
		} finally {
			threads = this.thread_keepers.get(timeup_point_millis);
			threads.remove(_this_thread);
			if(threads.isEmpty())
				this.thread_keepers.remove(timeup_point_millis);
		}
	}
	public void timeuplist_add(long timeup_point_millis, Runnable runner)
	{
		ArrayList<Runnable> runners;
		timeup_point_millis -= this.time_increment;
		runners = this.timeup_runners.get(timeup_point_millis);
		if(runners == null)
		{
			runners = new ArrayList<Runnable>();
			this.timeup_runners.put(timeup_point_millis, runners);
		}
		runners.add(runner);
		this.timeuplist_add(timeup_point_millis, false);
	}

	private final Object lock01_instant = new Object();
	private final Object lock02_modify  = new Object();

	public void timeup_value_add(long time_value_millis, boolean isInclude_timetype_point)
	{
		synchronized (lock01_instant) {}
		synchronized (lock02_modify)
		{
			if(this.isFinish)
				return;
			if(time_value_millis == 0)
				return;
			if(this.time_type.isPoint() && !isInclude_timetype_point)
				return;
			this.time_increment += time_value_millis;
			this.thread_belfry.interrupt();
		}
	}
	public void timeup_point_set(long time_point_millis, boolean isOnly_timetype_point)
	{
		synchronized (lock01_instant) {}
		synchronized (lock02_modify)
		{
			if(this.isFinish)
				return;
			if(!this.time_type.isPoint() && isOnly_timetype_point)
				return;
			this.time_point_break = time_point_millis;

			if(ScMath.min(this.time_point_break, this.time_point_timeupNext.peekMin()) == this.time_point_break)
				this.thread_belfry.interrupt();
		}
	}
	public void timeloop_times_add(int times)
	{
		synchronized (lock01_instant) {}
		synchronized (lock02_modify)
		{
			if(this.isFinish)
				return;
			if(times == 0)
				return;
			Integer times_break = this.time_point_timeupNext.ctrl.shadowReMade0TimesNow1TimesBreak()[1];
			if(times_break == null)
				return;
			this.time_point_timeupNext.ctrl.setValue(null, null, times_break + times, null, null);
			if(times < 0)
				this.thread_belfry.interrupt();
		}
	}
	public void timeloop_times_set(int times)
	{
		synchronized (lock01_instant) {}
		synchronized (lock02_modify)
		{
			if(this.isFinish)
				return;
			if(times < 0)
				this.time_point_timeupNext.ctrl.setNull(true, false, false);
			else
				this.time_point_timeupNext.ctrl.setValue(null, null, times, null, null);
			Integer shadow0TimesNow1TimesBreak[] = this.time_point_timeupNext.ctrl.shadowReMade0TimesNow1TimesBreak();
			if(shadow0TimesNow1TimesBreak[1] != null)
				if(shadow0TimesNow1TimesBreak[1] <= shadow0TimesNow1TimesBreak[0])
					this.thread_belfry.interrupt();
		}
	}
	public void timeloop_increment_add(long timeloop_increment_millis)
	{
		synchronized (lock01_instant) {}
		synchronized (lock02_modify)
		{
			if(this.isFinish)
				return;
			this.time_point_timeupNext.ctrl().addValueReMade_increment(timeloop_increment_millis);
			this.thread_belfry.interrupt();
		}
	}
	public void timeloop_increment_set(long timeloop_increment_millis)
	{
		synchronized (lock01_instant) {}
		synchronized (lock02_modify)
		{
			if(this.isFinish)
				return;
			this.time_point_timeupNext.ctrl().setValueReMade_increment(timeloop_increment_millis);
			this.thread_belfry.interrupt();
		}
	}
	public void timeuplist_add(long timeup_point_millis)
	{
		this.timeuplist_add(timeup_point_millis, true);
	}
	private void timeuplist_add(long timeup_point_millis, boolean isConsiderIncrement)
	{
		synchronized (lock01_instant) {}
		synchronized (lock02_modify)
		{
			if(this.isFinish)
				return;
			timeup_point_millis -= (isConsiderIncrement)?this.time_increment:0;
			this.time_point_timeupNext.add(timeup_point_millis);
		}
	}
	public void finish()
	{
		this.isFinish = true;
		if(this.thread_belfry != null)
			this.thread_belfry.interrupt();
		for(ArrayList<Thread> threads: this.thread_keepers.values())
		{
			for(Thread thread: threads)
				thread.interrupt();
			threads.clear();
		}
		this.thread_keepers.clear();
		this.timeup_runners.clear();
	}
	public void finish(Thread thread_keeper)
	{
		for(Map.Entry<Long, ArrayList<Thread>> threads: this.thread_keepers.entrySet())
		{
			if(threads.getValue().contains(thread_keeper))
			{
				thread_keeper.interrupt();
				threads.getValue().remove(thread_keeper);
				if(threads.getValue().isEmpty())
					this.thread_keepers.remove(threads.getKey());
			}
		}
	}

	public boolean isFinish()
	{
		return this.isFinish;
	}
	public boolean isEmpty()
	{
		return this.time_point_timeupNext.isEmpty();
	}
	public boolean isTimeLoopEnable()
	{
		return this.time_point_timeupNext.ctrl.isWorking();
	}
	public boolean isTimeLoopEnableIllimited()
	{
		return this.time_point_timeupNext.ctrl.checkElementMadeTimes_remaining() == -1;
	}
	public long time_remaining() throws InterruptedException
	{
		long time_point_now = Watch.current();
		Long time_point_break_tmp = this.time_point_break;
		Integer shadowTimes0Now1Break[] = this.time_point_timeupNext.ctrl.shadowReMade0TimesNow1TimesBreak();
		if(time_point_break_tmp == null)
		{
			if(this.time_point_timeupNext.ctrl.isWorking())
				if(shadowTimes0Now1Break[1] == null)
					return -1;
			return this.time_point_timeupNext.peekMax() - time_point_now;
		} else {
			return -2;

		}
	}
	public Watch.auxi.type time_type()
	{
		return this.time_type;
	}

	protected boolean isPause = false;
	protected long time_value_pause = 0;
	protected long time_point_pause = 0;
	public void pause()
	{
		this.pause(false);
	}
	public void pause_break()
	{
		synchronized (lock01_instant) {}
		synchronized (lock02_modify)
		{
			if(this.isFinish)
				return;
			if(isPause)
			{
				this.isPause = false;
				this.time_value_pause += Watch.current() - this.time_point_pause;
				this.time_point_pause  = 0;
				this.time_increment += this.time_value_pause;
				this.thread_belfry.interrupt();
			}
		}
	}
	public void pause(boolean isInclude_timetype_point)
	{
		if(this.isFinish)
			return;
		if(this.time_type.isPoint() && !isInclude_timetype_point)
			return;
		if(!isPause)
		{
			this.isPause = true;
			this.time_point_pause = Watch.current();
			this.thread_belfry.interrupt();
		}
	}
	public boolean isPause()
	{
		return this.isPause;
	}
	public void pause_exchange(boolean isInclude_timetype_point)
	{
		if(this.isPause)
			this.pause_break();
		else
			this.pause(isInclude_timetype_point);
	}

}
