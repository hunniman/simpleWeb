package dao.cache;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PoJoCache {
	private static Map<String ,List<Field>>pojoMethodNameMap=new HashMap<String,List<Field>>();

	public static Map<String, List<Field>> getPojoMethodNameMap() {
		return pojoMethodNameMap;
	}
	
}
