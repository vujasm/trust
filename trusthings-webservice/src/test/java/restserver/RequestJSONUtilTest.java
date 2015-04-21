package restserver;

import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.common.io.CharStreams;
import com.inn.trusthings.rest.util.RequestJSONUtil;

public class RequestJSONUtilTest {
	
	public static void main(String[] args) {
		
		String request = "";
		InputStream is = RequestJSONUtilTest.class.getResourceAsStream("/requestComposite.json");
		try {
			request = CharStreams.toString(new InputStreamReader(is));
			is.close();
			System.out.println(RequestJSONUtil.getCompositeServiceWrapperList(request));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
