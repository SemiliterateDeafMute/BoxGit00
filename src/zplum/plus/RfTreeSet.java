package zplum.plus;

import java.util.TreeSet;

public class RfTreeSet<E> extends TreeSet<E>
{
	private static final long serialVersionUID = -5480571210434918789L;

	public E first()
	{
		if(this.isEmpty())
			return null;
		return super.first();
	}
	public E last()
	{
		if(this.isEmpty())
			return null;
		return super.last();
	}
}
