package zplum.plus;

import java.util.ArrayList;

public class RfListArraySet<E> extends ArrayList<E>
{
	private static final long serialVersionUID = 3784353788665550908L;

	@Override
	public boolean add(E e)
	{
		if(this.contains(e))
			return false;
		return super.add(e);
	}
}
