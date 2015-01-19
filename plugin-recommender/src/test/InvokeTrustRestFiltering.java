import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Set;

import com.google.common.collect.Sets;
import com.google.common.io.CharStreams;
import com.inn.trusthings.integration.TrustFilterByExclusion;
import com.inn.trusthings.integration.TrustFilterByThreshold;
import com.inn.trusthings.integration.TrustScorer;
import com.inn.trusthings.integration.util.RequestBody;


public class InvokeTrustRestFiltering {
	
	public static void main(String[] args) {
		InputStream is = RequestBody.class.getResourceAsStream("/parameters.json");
		String	criteria = null;
		try {
			Set<URI> set = Sets.newHashSet();
			set.add(new URI("http://abiell.pc.ac.upc.edu:9081/iserve/id/services/d905ea82-ae6d-4eb1-9719-0b801e5c758b/google-maps"));
			set.add(new URI("http://abiell.pc.ac.upc.edu:9081/iserve/id/services/bfef4357-0da5-47d7-9beb-54dd95919179/microsoft-bing-maps"));
			criteria = CharStreams.toString(new InputStreamReader(is));
			is.close();
			if (new TrustFilterByExclusion().apply(set, criteria).isEmpty() == false){
				System.out.println("TrustFilterByExclusion OK");
			}
			if (new TrustFilterByThreshold().apply(set, criteria).isEmpty() == false){
				System.out.println("TrustFilterByThreshold OK");
			}
			if (new TrustScorer().apply(set, criteria).isEmpty() == false){
				System.out.println("TrustScorer OK");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
