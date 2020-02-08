package zplum.tools._fc_timeobject;

import java.util.ArrayList;
import java.util.HashMap;

import zplum.tools.Abacus;
import zplum.tools._ancestor.Face;
import zplum.tools._ancestor.UtilPdkTime;

public class Watch
{
	static final class config
	{
		static final long time_error = 0;
	}
	static final class define
	{
		static final String ClassNameTicker = "Ticker";
		static final String ClassNameBelfry = "Belfry";
		static final String ExceptionMsg_reFinishButNotTimeup = "%s is finish but not timeup. ";
		static final String ExceptionMsg_reNotFinishButKeeperMethodInterrupte = "%s is not finish, But %s's KeeperMethod is interrupted. ";
		static final String TickerExceptionMsg_reFinishButNotTimeup = String.format(ExceptionMsg_reFinishButNotTimeup, ClassNameTicker);
		static final String TickerExceptionMsg_reNotFinishButKeeperMethodInterrupte = String.format(ExceptionMsg_reNotFinishButKeeperMethodInterrupte, ClassNameTicker, ClassNameTicker);
		static final String BelfryExceptionMsg_reFinishButNotTimeup = String.format(ExceptionMsg_reFinishButNotTimeup, ClassNameBelfry);
		static final String BelfryExceptionMsg_reNotFinishButKeeperMethodInterrupte = String.format(ExceptionMsg_reNotFinishButKeeperMethodInterrupte, ClassNameBelfry, ClassNameBelfry);
	}

	public static long current()
	{
		return System.currentTimeMillis();
	}
	public static long sleep(long millis)
	{
		if(millis < 0)
			return millis;
		long time_sleep_point = System.currentTimeMillis();
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			return - System.currentTimeMillis() + time_sleep_point + millis;
		}
		return 0;
	}
	@Deprecated
	@SuppressWarnings("unused")

	private static void ORIG_sleep(long millis)
	{
		if(millis > 0)
		{
			try {
				Thread.sleep(millis);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public static void sleepAllTheTime()
	{
		Object lock = new Object();
		synchronized (lock)
		{
			try {
				lock.wait();
			} catch (InterruptedException e) {
			}
		}
	}

	public static long getTimeValue(String pattern, String time_point_text_head, String time_point_text_tail)
	{
		return Abacus.time.valueOfTimePoint(pattern, time_point_text_tail) - Abacus.time.valueOfTimePoint(pattern, time_point_text_head);
	}
	public static long getTimePoint(String pattern, String time_point_text)
	{
		return Abacus.time.valueOfTimePoint(pattern, time_point_text);
	}
	public static long getTimePoint(String pattern, String time_point_text, Long[] time_value_unituion)
	{
		return Abacus.time.valueOfTimePoint(pattern, time_point_text) + Abacus.time.valueOfTimeValue(time_value_unituion);
	}
	public static long getTimePoint(String pattern, String time_point_text, Abacus.time.valueUnit unit, double time_value_unit)
	{
		return Abacus.time.valueOfTimePoint(pattern, time_point_text) + (long)Abacus.time.valueOfTimeValue(unit, time_value_unit);
	}

	public static final class auxi implements UtilPdkTime
	{
		@Deprecated
		public static enum increment_tgt
		{
			exist,every;
		}
	}

	public static final class kit_base
	{
		public static class Cuckoo
		{
			public String title;
			public long time_value_offset = 0;
			public long record()
			{
				return Watch.current() + time_value_offset;
			}
			public void regulate(int regulate_millis)
			{
				this.time_value_offset += regulate_millis;
			}
		}

		public static class Chromo
		{
			public String title;
			public long time_point_base = Watch.current();
			private Long time_point_pause = null;
			public void reset()
			{
				time_point_base = Watch.current();
			}
			public void reset(long time_point_base_millis)
			{
				time_point_base = time_point_base_millis;
			}
			public long record()
			{
				return ((time_point_pause==null)?Watch.current():this.time_point_pause) - time_point_base;
			}
			public void regulate(int regulate_millis)
			{
				this.time_point_base += -regulate_millis;
			}
			public boolean isPause()
			{
				return time_point_pause != null;
			}
			public void pause_exchange()
			{
				if(time_point_pause == null)
					{time_point_pause = Watch.current(); }
				else
					{time_point_base += Watch.current()-time_point_pause; time_point_pause=null; }
			}
		}

		public static class Ticker
		{
			public String title;
			public final Thread thread_clock;
			public final long time_point_base;
			public final long time_value_duration;
			public Ticker(long duration_millis)
			{
				Ticker thisone = this;
				this.time_point_base = Watch.current();
				this.time_value_duration = duration_millis;
				this.thread_clock = new Thread() {
					public void run()
					{
						thisone.time_value_duration_remaining = Watch.sleep(-Watch.current() + thisone.time_point_base + thisone.time_value_duration);
						thisone.isFinish = true;
					}
				};
				thread_clock.start();
			}
			private boolean isFinish = false;
			private Long time_value_duration_remaining = null;
			public  boolean isFinish()
			{
				return isFinish;
			}
			public  boolean isTimeup()
			{
				return isFinish && this.time_value_duration_remaining == 0;
			}
			public long time_remaining() throws InterruptedException
			{
				if(this.isFinish)
					return this.time_value_duration_remaining;
				else
					return -Watch.current() + this.time_point_base + this.time_value_duration;
			}
			public void countdown() throws InterruptedException
			{
				this.thread_clock.join();
			}
		}
	}
	@Deprecated
	       static final class kit_base_BKUP
	{
		public static class Cuckoo
		{
			public String title;
			public final ArrayList<Long> records = new ArrayList<Long>();
			public final HashMap<Long, String> records_titles = new HashMap<Long, String>();
			public long record()
			{
				Long time = Watch.current();
				records.add(time);
				return time;
			}
			public long record(String record_title)
			{
				long time = this.record();
				records_titles.put(time, record_title);
				return time;
			}
		}

		public static class Chromo extends Cuckoo
		{
			public String title;
			public long time_point_base = Watch.current();
			private Long time_point_pause = null;
			public boolean isPause()
			{
				return time_point_pause != null;
			}
			public void pause_exchange()
			{
				if(time_point_pause == null)
					{time_point_pause = Watch.current(); }
				else
					{time_point_base += Watch.current()-time_point_pause; time_point_pause=null; }
			}
			public long record()
			{
				long time = ((time_point_pause==null)?Watch.current():this.time_point_pause) - time_point_base;
				records.add(time);
				return time;
			}
			public void regulate(int regulate_millis)
			{
				this.time_point_base += -regulate_millis;
			}
		}

		public static class Ticker
		{
			public String title;
			public final long duration;
			public final Long time_point_timeup;
			public final Long time_point_interrupt;
			public Ticker(long duration_millis)
			{
				this.duration = duration_millis;
				this.time_point_timeup = (duration<0) ?null:Watch.current()+this.duration;

				this.time_point_interrupt = (Watch.sleep(this.duration)==0)?null:Watch.current();
				this.isFinish = true;
			}
			private boolean isFinish = false;
			public  boolean isFinish()
			{
				return isFinish;
			}
			public  boolean isTimeup()
			{
				return isFinish && time_point_interrupt==null;
			}
		}
	}

	public static final class kit_plus
	{
		static abstract class core extends Face.TotalAble_MonitorByFlag.template
		{
			public String title = null;
			protected boolean isNotPause = true;
			protected long time_value_pause = 0;
			protected long time_point_pause = 0;
			public void pause()
			{
				if(this.isNotPause)
				{
					this.isNotPause = false;
					this.time_point_pause = Watch.current();
				}
			}
			public void pause_break()
			{
				if(!this.isNotPause)
				{
					this.isNotPause = true;
					this.time_value_pause += (Watch.current()-this.time_point_pause);
					this.time_point_pause = 0;
				}
			}
			public void pause_break(long time_point_timetoPauseBreak)
			{
				core thisone = this;
				new Thread() {
					public void run()
					{
						Watch.sleep(time_point_timetoPauseBreak);
						thisone.pause_break();
					}
				}.start();
			}
			public boolean isPause()
			{
				return !this.isNotPause;
			}
			public void pause_exchange()
			{
				if(this.isNotPause)
					this.pause();
				else
					this.pause_break();
			}
		}
		static abstract class Cuckoo_core extends core
		{
			public       long time_regulate = 0;
			public final ArrayList<Long> records = new ArrayList<Long>();
			public final HashMap<Long, Long> records_regulates = new HashMap<Long, Long>();
			public final HashMap<Long, String> records_titles = new HashMap<Long, String>();
			public abstract long record();
			public          long record(String record_title)
			{
				long time = this.record();
				records_titles.put(time, record_title);
				return time;
			}
			public          void regulate(long regulate_millis)
			{
				this.time_regulate += regulate_millis;
			}
		}

		public static class Cuckoo extends Cuckoo_core
		{
			public final HashMap<Long, Long> records_timeValuesPause = new HashMap<Long, Long>();
			public Cuckoo() {};
			public Cuckoo(long time_initial_millis)
			{
				this(Watch.current()-time_initial_millis, false);
			}
			public Cuckoo(long time_initial_millis, boolean isPause_initial)
			{
				this.regulate(Watch.current() - time_initial_millis);
				if(isPause_initial)
					this.pause();
			}
			public long record()
			{
				long time = Watch.current();
				this.records.add(time);
				if(this.time_regulate != 0)
					this.records_regulates.put(time, this.time_regulate);
				if(this.time_value_pause != 0)
					this.records_timeValuesPause.put(time, this.time_value_pause+(this.isNotPause?0:time-this.time_point_pause));
				return ((this.isNotPause)?time:this.time_point_pause) + this.time_regulate  - this.time_value_pause;
			}
		}

		public static class Chromo extends Cuckoo_core
		{
			public final long time_point_base;
			public Chromo()
			{
				this(Watch.current(), Watch.auxi.type.value, 0, false);
			}
			public Chromo(boolean isPause_initial)
			{
				this(Watch.current(), Watch.auxi.type.value, 0, isPause_initial);
			}
			public Chromo(long time_initial_millis)
			{
				this(Watch.current(), Watch.auxi.type.value, time_initial_millis, false);
			}
			public Chromo(Watch.auxi.type time_type, long time_initial_millis)
			{
				this(Watch.current(), time_type, time_initial_millis, false);

			}
			public Chromo(long time_current_millis, Watch.auxi.type time_type, long time_initial_millis, boolean isPause_initial)
			{
				switch(time_type)
				{
					case point:
						this.time_point_base = time_current_millis + time_initial_millis;
						break;
					default:
					case value:
						this.time_point_base = time_current_millis + time_initial_millis;
						if(isPause_initial)
							this.pause();
						break;
				}
			}
			public long record()
			{
				long time = ((this.isNotPause)?Watch.current():this.time_point_pause) - this.time_point_base - this.time_value_pause;
				this.records.add(time);
				if(this.time_regulate != 0)
					this.records_regulates.put(time, this.time_regulate);
				return time + this.time_regulate;
			}
			public long recordWithPauseBreak()
			{
				this.pause_break();
				return record();
			}
		}
		public static class ChromoORIG_20191003 extends Cuckoo_core
		{
			public final long time_point_base;
			public ChromoORIG_20191003()
			{
				this(Watch.auxi.type.point, Watch.current());
			}
			public ChromoORIG_20191003(long time_initial_millis)
			{
				this(Watch.auxi.type.point, Watch.current()+time_initial_millis);
			}
			public ChromoORIG_20191003(Watch.auxi.type time_type, long time_initial_millis)
			{
				this(Watch.auxi.type.point, ((Watch.auxi.type.point==time_type)?time_initial_millis:Watch.current()+time_initial_millis), false);

			}
			public ChromoORIG_20191003(Watch.auxi.type time_type, long time_initial_millis, boolean isPause_initial)
			{
				switch(time_type)
				{
					case point:
						this.time_point_base = time_initial_millis;
						break;
					default:
					case value:
						this.time_point_base = Watch.current() + time_initial_millis;
						if(isPause_initial)
							this.pause();
						break;
				}
			}
			public long record()
			{
				long time = ((this.isNotPause)?Watch.current():this.time_point_pause) - this.time_point_base - this.time_value_pause;
				this.records.add(time);
				if(this.time_regulate != 0)
					this.records_regulates.put(time, this.time_regulate);
				return time + this.time_regulate;
			}
		}

		public static class Ticker extends WatchTicker
		{
			public Ticker(long time_initial_millis)
			{
				super(Watch.current(), true, Watch.auxi.type.value, time_initial_millis, false);
			}
			public Ticker(Watch.auxi.type time_type, long time_initial_millis)
			{
				super(Watch.current(), true, time_type, time_initial_millis, false);
			}
			public Ticker(Watch.auxi.type time_type, long time_initial_millis, boolean isPause_initial)
			{
				super(Watch.current(), true, time_type, time_initial_millis, isPause_initial);
			}
		}

		public static class Belfry extends WatchBelfry
		{
			public Belfry(long timeLoop_initial_millis)
			{
				super(Watch.current(), Watch.auxi.type.value, timeLoop_initial_millis, -1);

			}
			public Belfry(long timeLoop_initial_millis, int times)
			{
				super(Watch.current(), Watch.auxi.type.value, timeLoop_initial_millis, times);

			}
			public Belfry(long time_current_millis, long timeLoop_initial_millis, int times)
			{
				super(time_current_millis, Watch.auxi.type.value, timeLoop_initial_millis, times);

			}
		}
		public static class BelfryPlus extends WatchBelfryPlus
		{
			public BelfryPlus(Runnable runner, long timeLoop_initial_millis)
			{
				super(runner, Watch.auxi.type.value, Watch.current(), timeLoop_initial_millis, -1);
			}
			public BelfryPlus(Runnable runner, long timeLoop_initial_millis, int times)
			{
				super(runner, Watch.auxi.type.value, Watch.current(), timeLoop_initial_millis, times);
			}
			public BelfryPlus(Runnable runner, long time_current_millis, long timeLoop_initial_millis, int times)
			{
				super(runner, Watch.auxi.type.value, time_current_millis, timeLoop_initial_millis, times);
			}
		}
	}

}
