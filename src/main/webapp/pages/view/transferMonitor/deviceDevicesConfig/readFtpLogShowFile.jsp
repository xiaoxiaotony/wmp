<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@  page import="java.io.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>读取ftp日志文件内容</title>
</head>
<body>
	<!-- <div class="search_bar"></div> -->
	<%
		String path = request.getParameter("logUrl"); //目录分隔符必须用双斜杠
		File file = new File(path);
		StringBuffer strB = null;
		if (file.exists()) {
			FileReader fr = new FileReader(file); //字符输入流
			BufferedReader br = new BufferedReader(fr); //使文件可按行读取并具有缓冲功能
			strB = new StringBuffer(); //strB用来存储jsp.txt文件里的内容
			String str = br.readLine();
			while (str != null) {
				strB.append(str).append("<br>"); //将读取的内容放入strB
				str = br.readLine();
			}
			br.close(); //关闭输入流
			fr.close();
		}
	%>
	<textarea style="width: 99%; height: 97%;"><%=strB%></textarea>
</body>
</html>