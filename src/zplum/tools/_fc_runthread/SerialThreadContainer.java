package zplum.tools._fc_runthread;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;

public class SerialThreadContainer<E> implements Callable<E>
{
	private static LinkedBlockingDeque<Object> finishNode = new LinkedBlockingDeque<Object>();

	private int SerialThreadCount;
	private SerialThreadOrder<E> order;
	private ExecutorService executor;
	private CompletionService<E> completioner;

	private LinkedBlockingDeque<Object> startOrderly;
	private LinkedBlockingDeque<Object> finishOrderly_All;
	private EasyMapOO<LinkedBlockingDeque<Object>,Future<E>> finishOrderly_Each;
	private Future<E> tmpFuture;

	public SerialThreadContainer(SerialThread<E>[] threads)
	{
		this.executor = Executors.newCachedThreadPool();
		this.completioner = new ExecutorCompletionService<E>(executor);
		this.order = new SerialThreadOrder<E>(threads);
		this.SerialThreadCount = order.getRegister().length;

		this.finishOrderly_Each = new EasyMapOO<LinkedBlockingDeque<Object>, Future<E>>();
		this.finishOrderly_Each.put(new LinkedBlockingDeque<Object>(), null);
		this.tmpFuture = null;

		this.startOrderly = new LinkedBlockingDeque<Object>();
		this.finishOrderly_All = new LinkedBlockingDeque<Object>();

		this.executor.submit(this);
	}

	public void finalize() throws Throwable
	{
		this.shutdownNow();
		super.finalize();
	}

	public E call() throws Exception
	{
		SerialThread<E>[] register = this.order.getRegister();
		LinkedBlockingDeque<SerialThreadOrder<E>> orderly = new LinkedBlockingDeque<SerialThreadOrder<E>>();
		for(SerialThread<E> tmp:register)
		{
			if(tmp == null)
				continue;
			tmp.initParams(orderly);
			completioner.submit(tmp);
		}
		executor.shutdown();

		Object objOrder;
		objOrder = this.startOrderly.take();
		this.startOrderly.add(objOrder);

		orderly.clear();
		orderly.add(this.order);

		LinkedBlockingDeque<Object> last;
		LinkedBlockingDeque<Object> tmp;
		Future<E> future;
		int times = 0;
		while(this.finishOrderly_All.isEmpty())
		{
			last = this.finishOrderly_Each.lastKey();
			tmp = new LinkedBlockingDeque<Object>();
			future = this.completioner.take();
			times++;

			this.finishOrderly_Each.put(tmp, future);
			last.add(tmp);

			if(times >= this.SerialThreadCount)
			{
				this.finishOrderly_Each.put(finishNode, null);
				tmp.add(finishNode);
				break;
			}
		}

		this.finishOrderly_All.add(objOrder);

		return null;
	}

	public void start()
	{
		if(this.startOrderly.isEmpty())
		{
			this.startOrderly.add(new Object());
		}
	}

	public void shutdownNow()
	{
		this.finishOrderly_All.add(new Object());
		this.executor.shutdownNow();
	}

	public void waitFor() throws InterruptedException
	{
		this.finishOrderly_All.take();
	}

	public Future<E> takeFutrue() throws InterruptedException, ExecutionException, Exception
	{
		return this.tmpFuture = this.takeFutrue(this.tmpFuture);
	}

	@SuppressWarnings("unchecked")
	public Future<E> takeFutrue(Future<E> tmpFuture) throws InterruptedException, ExecutionException
	{
		LinkedBlockingDeque<Object> tmp;
		LinkedBlockingDeque<Object> tmp_next;

		tmp = this.finishOrderly_Each.get_Invert(tmpFuture);
		if(tmp == null)
		{
			tmp = this.finishOrderly_Each.firstKey();
		}

		tmp_next = (LinkedBlockingDeque<Object>) tmp.take();
		tmp.add(tmp_next);

		return this.finishOrderly_Each.get(tmp_next);
	}

	public static void main0(String[] args) throws Exception
	{
		@SuppressWarnings("unchecked")
		SerialThread<String>[] threads = new SerialThread[10];

		threads[1] = new SerialThread<String>()
		{
			protected String run() throws Exception
			{
				Thread.sleep(1 * 1000);
				return "Step1";
			}
		};

		threads[2] = new SerialThread<String>()
		{
			protected String run() throws Exception
			{
				Thread.sleep(1 * 1000);
				return "Step2";
			}
		};

		threads[5] = new SerialThread<String>()
		{
			protected String run() throws Exception
			{
				Thread.sleep(1 * 1000);
				return "Step3";
			}
		};

		SerialThreadContainer<String> container = new SerialThreadContainer<String>(threads);
		container.start();

		Future<String> tmp = null;
		while(true)
		{
			tmp = container.takeFutrue(tmp);

			if(tmp==null)
			{
				Thread.sleep(1 * 1000);
				System.out.println(tmp);
				break;
			}
			System.out.println(tmp.get());
		}

		System.out.println("finish");
	}
}
