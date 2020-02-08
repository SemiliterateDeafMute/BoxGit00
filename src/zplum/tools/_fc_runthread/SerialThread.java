package zplum.tools._fc_runthread;

import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingDeque;

public abstract class SerialThread<E> implements Callable<E>
{
	private static int defaultTimeWait = 10000;
	private static int defaultTimeout_SelfIdling = 50;
	private static int defaultTimeout_OrderIdling = 3000000;
	private LinkedBlockingDeque<SerialThreadOrder<E>> orderly;
	private SerialThreadOrder<E> order;

	private boolean isFinish;
	private Integer finishStatus;

	protected abstract E run() throws Exception;

	void initParams(LinkedBlockingDeque<SerialThreadOrder<E>> orderly)
	{
		this.orderly = orderly;
		this.isFinish = false;
		this.finishStatus = null;
	}

	private int checkOrder()
	{
		SerialThread<E> first = this.order.getFirst();
		SerialThread<E>[] register = this.order.getRegister();

		if(first == null)
		{
			return 2;
		}

		for(SerialThread<E> tmp:register)
		{
			if( tmp.equals(first) )
			{
				continue;
			}
			return -1;
		}

		if(first.isFinish == false)
		{
			return 1;
		}

		return 0;
	}

	private boolean checkSelf()
	{
		SerialThread<E>[] register = this.order.getRegister();

		for(SerialThread<E> tmp:register)
		{
			if( tmp.equals(this) )
			{
				continue;
			}
			return false;
		}
		return true;
	}

	private boolean takeOrder() throws InterruptedException
	{
		int takeTimes = 0;
		this.finishStatus = null;

		while(true)
		{
			this.order = this.orderly.takeFirst();

			if(this.order.getFirst().equals(this))
			{
				this.order.removeFirst();
				this.finishStatus = 0;
				return true;
			}

			if(takeTimes++ > defaultTimeout_SelfIdling)
			{
				if(this.checkSelf())
				{
					takeTimes = 0;
				} else {
					this.finishStatus = 1;
					return false;
				}
			}

			if(this.order.checkTimeOut(defaultTimeout_OrderIdling))
			{
				int i = this.checkOrder();
				if(i<0)
				{
					this.finishStatus = i;
					return false;
				} else switch(i) {
					case 1:
					case 2:this.order.removeFirst();break;
					default:break;
				}
				this.order.resetTime();
			}

			this.orderly.putFirst(order);
			this.order = null;
			Thread.sleep( (long)(defaultTimeWait * Math.random()) );
		}
	}

	public E call() throws Exception
	{
		boolean isRun = false;
		E status = null;

		try
		{
			if (orderly == null)
			{
				this.finishStatus = -2;
				throw new Exception();
			}

			isRun = this.takeOrder();

			if(isRun)
			{
				status = this.run();
			}
		} catch (Exception e) {
			if(this.finishStatus == null)
				this.finishStatus = -3;
			status = null;
		}

		this.isFinish = false;
		this.order.resetTime();

		this.orderly.putFirst(this.order);
		this.order=null;

		return status;
	}

	public Integer getFinishStatus()
	{
		return this.finishStatus;
	}
}
