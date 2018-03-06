package chow.dan.controller;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import chow.dan.bll.Downloader;
import chow.dan.common.Content;

@WebServlet("/proxy")
public class HomeController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String url = request.getParameter("url");
		System.out.println(url);

		Content content = Downloader.download(url);

		System.out.println(content.getData().length);

		response.setHeader("Content-Disposition", "filename=" + url);
		response.setContentLength(content.getData().length);
		ServletOutputStream os = response.getOutputStream();
		os.write(content.getData());
		os.close();
	}
}
