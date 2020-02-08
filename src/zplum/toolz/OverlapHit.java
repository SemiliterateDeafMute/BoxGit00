package zplum.toolz;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OverlapHit
{
public abstract static class Sequence
{
	public static PrintStream out = System.out;
	public static PrintStream test = System.out;
	protected static int myType = 0;

	protected static final int typeCreteSequence = 0;
	protected static final int typeDisCreteSequence = 1;
	protected abstract long[][] getContent();
	protected abstract int getTypeId();
}

public static class CreteSequence extends Sequence implements Comparable<CreteSequence>
{
	private long abs;
	private int loog;

	public CreteSequence() throws Exception
	{
	}

	public CreteSequence(long abs, int loog) throws Exception
	{
		if (loog <= 0)
			throw new Exception("CreteSequenceException: loop is zero or minus");
		this.abs = abs;
		this.loog = loog;
	}

	public static Integer isOverlap_plus(CreteSequence seque1,CreteSequence seque2)
	{
		long tempLoog1 = seque2.abs - seque1.abs;
		if (tempLoog1 == 0)
		{
			return Math.min(seque1.loog, seque2.loog);
		}

		int tempLoog2;
		if (tempLoog1 > 0 && (tempLoog2 = (int) (seque1.loog - tempLoog1)) >= 0)
			return tempLoog2;
		if (tempLoog1 < 0 && (tempLoog2 = (int) (-seque2.loog - tempLoog1)) <= 0)
			return tempLoog2;
		return null;
	}

	public static CreteSequence getIntersection(CreteSequence seque1,CreteSequence seque2) throws Exception
	{
		Integer tempLoog = CreteSequence.isOverlap_plus(seque1, seque2);
		if (tempLoog == null || tempLoog == 0)
			return null;
		if (tempLoog > 0)
		{
			if (tempLoog >= seque2.loog)
				return new CreteSequence(seque2.abs, seque2.loog);
			else
				return new CreteSequence(seque2.abs, tempLoog);
		} else {
			if (-tempLoog >= seque1.loog)
				return new CreteSequence(seque1.abs, seque1.loog);
			else
				return new CreteSequence(seque1.abs, -tempLoog);
		}
	}
	public static CreteSequence getIntersection(CreteSequence seque1, CreteSequence seque2, Integer tempLoog) throws Exception
	{
		if (tempLoog == null || tempLoog == 0)
			return null;
		if (tempLoog > 0)
		{
			if (tempLoog >= seque2.loog)
				return new CreteSequence(seque2.abs, seque2.loog);
			else
				return new CreteSequence(seque2.abs, tempLoog);
		} else {
			if (-tempLoog >= seque1.loog)
				return new CreteSequence(seque1.abs, seque1.loog);
			else
				return new CreteSequence(seque1.abs, -tempLoog);
		}
	}

	public static Sequence getUnion(CreteSequence seque1,CreteSequence seque2) throws Exception
	{
		Integer tempLoog = CreteSequence.isOverlap_plus(seque1, seque2);
		if (tempLoog == null)
		{
			DisCreteSequence temp = new DisCreteSequence();
			temp.addElement(seque1);
			temp.addElement(seque2);
			return temp;
		}
		if (tempLoog == 0)
			return new CreteSequence(Math.min(seque1.abs, seque2.abs),seque1.loog + seque2.loog);
		if (tempLoog > 0)
		{
			if (tempLoog >= seque2.loog)
				return new CreteSequence(seque1.abs, seque1.loog);
			else
				return new CreteSequence(seque1.abs, seque1.loog + seque2.loog - tempLoog);
		} else {
			if (-tempLoog >= seque1.loog)
				return new CreteSequence(seque2.abs, seque2.loog);
			else
				return new CreteSequence(seque2.abs, seque1.loog + seque2.loog + tempLoog );
		}
	}
	public static Sequence getUnion(CreteSequence seque1, CreteSequence seque2, Integer tempLoog) throws Exception
	{
		if (tempLoog == null)
		{
			DisCreteSequence temp = new DisCreteSequence();
			temp.addElement(seque1);
			temp.addElement(seque2);
			return temp;
		}
		if (tempLoog == 0)
			return new CreteSequence(Math.min(seque1.abs, seque2.abs),seque1.loog + seque2.loog);
		if (tempLoog > 0)
		{
			if (tempLoog >= seque2.loog)
				return new CreteSequence(seque1.abs, seque1.loog);
			else
				return new CreteSequence(seque1.abs, seque1.loog + seque2.loog - tempLoog);
		} else {
			if (-tempLoog >= seque1.loog)
				return new CreteSequence(seque2.abs, seque2.loog);
			else
				return new CreteSequence(seque2.abs, seque1.loog + seque2.loog + tempLoog );
		}
	}

	@Override
	protected long[][] getContent() {
		long temp[][] = {{this.abs, this.loog}};
		return temp;
	}

	@Override
	protected int getTypeId() {
		return CreteSequence.typeCreteSequence;
	}

	public int compareTo(CreteSequence other)
	{
		return (int) (this.abs - other.abs);
	}

	public boolean equals(CreteSequence other)
	{
		return (this.loog == other.loog) && (this.abs == other.abs);
	}
}

public static class DisCreteSequence extends Sequence
{
	private List<CreteSequence> elements
									= new ArrayList<CreteSequence>();
	@SuppressWarnings("unused")
	private boolean isOrder = false;

	public DisCreteSequence()
	{
		this.isOrder = false;
	}

	public DisCreteSequence(CreteSequence seque1,CreteSequence seque2)
	{
		this.addElement(seque1);
		this.addElement(seque2);
		this.isOrder = false;
	}

	public void fuse() throws Exception
	{
		if (this.elements.size() <= 1)
		{
			this.isOrder = true;
			return ;
		}

		Collections.sort(this.elements);
		Integer temp;
		int len = this.elements.size();

		for(int i=len-1; i>=1; i--)
		{
			temp = CreteSequence.isOverlap_plus(this.elements.get(i), this.elements.get(i-1));
			if(temp != null)
			{
				this.elements.set(i-1, (CreteSequence)CreteSequence.getUnion(this.elements.get(i), this.elements.get(i-1), temp));
				this.elements.remove(i);
			}
		}
		this.isOrder = true;
		return;
	}

	public static DisCreteSequence getIntersection(DisCreteSequence seque1,DisCreteSequence seque2) throws Exception
	{
		seque1.fuse();
		seque2.fuse();

		DisCreteSequence tgtSequence = new DisCreteSequence();
		int pointer1 = 0;
		int pointer2 = 0;
		CreteSequence temp1 =  null;
		CreteSequence temp2 =  null;
		int size1 = seque1.elements.size();
		int size2 = seque2.elements.size();

		int runFlag = 0;
		Integer tempLoog = 0;

		while (true)
		{
			if ( (pointer1 >= size1 && runFlag >= 0) || (pointer2 >= size2 && runFlag <= 0) )
				break;
			if (runFlag >= 0)
				temp1 = seque1.elements.get(pointer1++);
			if (runFlag <= 0)
				temp2 = seque2.elements.get(pointer2++);

			tempLoog = CreteSequence.isOverlap_plus(temp1, temp2);

			if (tempLoog == null || tempLoog == 0)
			{
				runFlag = (int) (temp2.abs - temp1.abs);
				continue;
			}

			runFlag = (int)Math.signum(tempLoog);

			if ( (tempLoog > 0 && tempLoog >= temp2.loog) || (tempLoog < 0 && -tempLoog >= temp1.loog) )
					runFlag = -runFlag;

			tgtSequence.addElement(CreteSequence.getIntersection(temp1, temp2, tempLoog));
		}

		if (tgtSequence.elements.size() == 0)
			tgtSequence = null;
		return tgtSequence;
	}

	public static DisCreteSequence getUnion(DisCreteSequence seque1,DisCreteSequence seque2) throws Exception
	{
		seque1.fuse();
		seque2.fuse();

		DisCreteSequence tgtSequence = new DisCreteSequence();
		int pointer1 = 0;
		int pointer2 = 0;
		CreteSequence temp1 =  null;
		CreteSequence temp2 =  null;
		int size1 = seque1.elements.size();
		int size2 = seque2.elements.size();

		int runFlag = 0;
		Integer tempLoog = 0;

		while (true)
		{
			if ( (pointer1 >= size1 && runFlag >= 0) || (pointer2 >= size2 && runFlag <= 0) )
				break;
			if (runFlag >= 0)
				temp1 = seque1.elements.get(pointer1++);
			if (runFlag <= 0)
				temp2 = seque2.elements.get(pointer2++);

			tempLoog = CreteSequence.isOverlap_plus(temp1, temp2);

			if (tempLoog == null)
			{
				runFlag = (int) (temp2.abs - temp1.abs);
				continue;
			}

			runFlag = (int)Math.signum(tempLoog);

			if ( (tempLoog > 0 && tempLoog >= temp2.loog) || (tempLoog < 0 && -tempLoog >= temp1.loog) )
					runFlag = -runFlag;

			tgtSequence.addElement(CreteSequence.getUnion(temp1, temp2, tempLoog));
		}

		while(pointer1 < size1)
			tgtSequence.addElement(seque1.elements.get(pointer1++));
		while(pointer2 < size2)
			tgtSequence.addElement(seque2.elements.get(pointer2++));

		tgtSequence.fuse();
		if (tgtSequence.elements.size() == 0)
			tgtSequence = null;
		return tgtSequence;
	}

	public int size()
	{
		return this.elements.size();
	}

	public CreteSequence get(int index)
	{
		return this.elements.get(index);
	}

	public void addElement(Sequence newone)
	{
		this.isOrder = false;
		if (newone.getTypeId() == CreteSequence.typeCreteSequence)
			this.elements.add((CreteSequence)newone);
		if (newone.getTypeId() == DisCreteSequence.typeDisCreteSequence)
		{
			int i = 0;
			int size = ((DisCreteSequence)newone).elements.size();
			for(;i >= size ;i++)
				this.elements.add( ((DisCreteSequence)newone).elements.get(i) );
		}
	}

	public void removeElement(int index)
	{
		this.isOrder = false;
		this.elements.remove(index);
	}

	public void clear()
	{
		this.elements.clear();
		this.isOrder = false;
	}

	@Override
	public long[][] getContent()
	{
		int p = 0;
		int size = this.elements.size();
		long[][] content = new long[size][2];
		for (;p < size; p++)
			content[p] = this.elements.get(p).getContent()[0];

		if (size == 0)
			return null;
		return content;
	}

	@Override
	protected int getTypeId() {
		return DisCreteSequence.typeDisCreteSequence;
	}
}

	public static void main0(String[] args) throws Exception
	{
		int i;
		int[][] ias_temp;
		long[][] las_temp;
		DisCreteSequence seque_temp;

		int[][] ias1 = {
				{50,100},
				{50,400},
				{1,49},
				{2,49},
				{3,49},
				{50,50},
				{500,100},
		};
		int[][] ias2 = {
				{1,100},
				{2,100},
				{3,100},
				{50,50},
				{50,10},
				{50,500},
				{500,100},
		};

		DisCreteSequence seque1 = new DisCreteSequence();
		DisCreteSequence seque2 = new DisCreteSequence();
		for (i=0, ias_temp=ias1, seque_temp=seque1; i < ias_temp.length; i++)
			seque_temp.addElement(new CreteSequence(ias_temp[i][0], ias_temp[i][1]));
		for (i=0, ias_temp=ias2, seque_temp=seque2; i < ias_temp.length; i++)
			seque_temp.addElement(new CreteSequence(ias_temp[i][0], ias_temp[i][1]));

		DisCreteSequence sequeI = DisCreteSequence.getIntersection(seque1, seque2);
		DisCreteSequence sequeU = DisCreteSequence.getUnion(seque1, seque2);
		long[][] lasI = sequeI.getContent();
		long[][] lasU = sequeU.getContent();

		Sequence.out.println("Intersection:");
		for (i=0, las_temp=lasI; i < las_temp.length; i++)
			Sequence.out.println(las_temp[i][0] + "\t" + las_temp[i][1]);
		Sequence.out.println();
		Sequence.out.println();
		Sequence.out.println("Union:");
		for (i=0, las_temp=lasU; i < las_temp.length; i++)
			Sequence.out.println(las_temp[i][0] + "\t" + las_temp[i][1]);

	}

}
