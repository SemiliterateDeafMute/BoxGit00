package zplum.tools._ancestor;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;

import zplum.kit.test0;
import zplum.plus.RfArrayList;

public final class Face
{
	private final class config
	{
		private static final boolean enable_TotalAbleMonitorByFlag = !false;
	}

	public static abstract class Base
	{
		private static final Class<?> faces[];
		static {
			ArrayList<Class<?>> _classes = new ArrayList<Class<?>>();
			for(Class<?> _class: Face.class.getClasses())
				if(_class.isInterface())
					_classes.add(_class);
			faces = _classes.toArray(new Class<?>[_classes.size()]);
		}
		public Base()
		{
			for(Class<?> face: faces)
			{
				if(face.isInstance(this))
				{
					try {
						face.getMethod("init", face).invoke(null, this);
					} catch (NoSuchMethodException e) {

					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	@SuppressWarnings("unused")
	private static interface TestAble
	{
		public static void init(TestAble _this)
		{
			test0.echoHere(_this);
		}
		public static void func()
		{
			test0.echoHere("can you see me ?");
		};
	}
	public static interface TotalAble
	{
		public static abstract class template extends Base implements TotalAble {};
		public static RfArrayList<TotalAble> total_list = new RfArrayList<TotalAble>();
		public static void init(TotalAble _this)
		{
			total_list.add(_this);
		}
	}
	public static interface TotalAble_MonitorByFlag
	{
		public static abstract class template extends Base implements TotalAble_MonitorByFlag {};
		public static RfArrayList<TotalAble_MonitorByFlag> total_list = new RfArrayList<TotalAble_MonitorByFlag>();
		public static HashSet<Class<?>> total_isMonitorNewInstance = new HashSet<Class<?>>();
		public static void init(TotalAble_MonitorByFlag _this)
		{
			if(config.enable_TotalAbleMonitorByFlag)
				if(TotalAble_MonitorByFlag.isMonitor(_this))
					TotalAble_MonitorByFlag.include(_this);
		}
		public static boolean isEnable()
		{
			return config.enable_TotalAbleMonitorByFlag;
		}
		public static void monitor(Class<?> _this_class, boolean isMonitorNewInstance)
		{
			if(isMonitorNewInstance)
				total_isMonitorNewInstance.add(_this_class);
			else
				total_isMonitorNewInstance.remove(_this_class);
		}
		public static boolean isMonitor(Class<?> _this_class)
		{
			for(Class<?> _class_tmp: total_isMonitorNewInstance)
				if(_class_tmp.isAssignableFrom(_this_class))
					return true;
			return false;
		}
		public static boolean isMonitor(TotalAble_MonitorByFlag _this)
		{
			for(Class<?> _class_tmp: total_isMonitorNewInstance)
				if(_class_tmp.isInstance(_this))
					return true;
			return false;
		}
		public static void include(TotalAble_MonitorByFlag _this)
		{
			total_list.add(_this);
		}
		public static void exclude(TotalAble_MonitorByFlag _this)
		{
			total_list.remove(_this);
		}
	}
}

