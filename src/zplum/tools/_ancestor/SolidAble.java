package zplum.tools._ancestor;

public abstract class SolidAble
{
	private boolean isSolid = false;
	protected void solidify()
	{
		if(this.isSolid)
			return;
		this.isSolid = true;
	}

	protected void checkAllow(boolean isSolidMustSameWith, Exception exceptionWhatThrowWhenCheckAllowFailure)
	{
		if(this.isSolid == isSolidMustSameWith)
			return;
		exceptionWhatThrowWhenCheckAllowFailure.printStackTrace();
		System.exit(1);
	}

}
