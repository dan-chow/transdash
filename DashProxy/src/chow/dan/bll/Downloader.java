package chow.dan.bll;

import java.io.IOException;

import org.apache.commons.io.IOUtils;

import chow.dan.common.Content;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Downloader {
	public static Content download(String uri) throws IOException {
		OkHttpClient httpClient = new OkHttpClient();
		Headers.Builder headers = new Headers.Builder();
		Request.Builder request = new Request.Builder().url(uri).headers(headers.build());
		Response response = httpClient.newCall(request.build()).execute();

		return new Content(IOUtils.toByteArray(response.body().byteStream()));
	}

}