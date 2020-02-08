package zplum.tools._zp_ball;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

import zplum.tools._io_scout.Scout;

@Deprecated
public class ScoutBall extends Scout
{
	public ScoutBall(File file) throws IOException
	{
		super(file);
		if(file instanceof FileBall)
		{
			reader = new BufferedReader( ((FileBall)file).makeReader() );
		} else {

			synchronized(file)
			{}
		}
	}

	BufferedReader reader = null;
	@Override
	public String readLine() throws IOException
	{
		if(reader == null)
		{
			return super.readLine();
		} else {
			return reader.readLine();
		}
	}

}
