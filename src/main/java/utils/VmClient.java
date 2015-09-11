package utils;

import java.io.IOException;
import java.util.HashMap;

public class VmClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			String test= RestHttp.get("http://localhost:8080/greeting", new HashMap<String, String>());
			System.out.println(test);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
