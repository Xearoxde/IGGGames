package de.xearox.creator;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class SendStatistic extends Thread {
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		String json = "{\"version\":\""+LinkCreator.VERSION+"\"}";

		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

	    try {
	        HttpPost request = new HttpPost("http://xearox.de/uploads/linkcreator/postdata.php");
	        StringEntity params = new StringEntity("version=" + json);
	        request.addHeader("content-type", "application/x-www-form-urlencoded");
	        request.setEntity(params);
	        HttpResponse response = httpClient.execute(request);

	        // handle response here...
	        EntityUtils.consume(response.getEntity());
	    } catch (Exception e) {
	    	Utilz.logger(this, e);
	    } finally {
	        try {
				httpClient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}

}
