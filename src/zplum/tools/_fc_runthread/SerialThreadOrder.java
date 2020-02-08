package zplum.tools._fc_runthread;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@SuppressWarnings("serial")
public class SerialThreadOrder<E> extends ArrayList<SerialThread<E>>
{
	private Date lastTime;
	private SerialThread<E>[] register;

	@SuppressWarnings("unchecked")
	public SerialThreadOrder(SerialThread<E>[] threads)
	{
		for(SerialThread<E> temp:threads)
		{
			if(temp == null)
				continue;
			super.add(temp);
		}
		this.lastTime = new Date();
		this.register = (SerialThread<E>[])new SerialThread<?>[this.size()];
		this.register = this.toArray(this.register);
	}

	public void resetTime()
	{
		this.lastTime = new Date();
	}

	public boolean checkTimeOut(long timeout)
	{
		return (new Date().getTime() - this.lastTime.getTime()) > timeout;
	}

	public SerialThread<E> getFirst()
	{
		return this.get(0);
	}

	public void removeFirst()
	{
		this.remove(0);
	}

	public SerialThread<E>[] getRegister()
	{
		return this.register;
	}

	public SerialThread<E> set(int index, SerialThread<E> element) { return element; }
	public boolean add(SerialThread<E> e) { return false; }
	public void add(int index, SerialThread<E> element) { return; }
	public boolean addAll(Collection<? extends SerialThread<E>> c) { return false; }
	public boolean addAll(int index, Collection<? extends SerialThread<E>> c) { return false; }

	public static void main0(String[] args)
	{
	}
}

