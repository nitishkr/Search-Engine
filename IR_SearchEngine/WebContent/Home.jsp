<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
     <%@page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>MyFirstSearchEngine</title>
</head>
<body>


<br/>
<br/>

<form  action="Search" method="post"  align="center"  >
<input type="text" id="searchstring"  name="searchstr" placeholder="So, what are you wishing for today ?" style="border-radius:5px;height:40px;font-size:14pt;width: 600px">
<button type="submit"  style="border-width:1px;border-radius:5px;height:40px;font-size:14pt;width: 150px;cursor:pointer;background-color:#f1c40f;">Search</button>
</form>


<br>
<form action="cart" method="post">
<% 
try
{
 // retrieve your list from the request, with casting 
 String error = (String) request.getAttribute("query_error");

 if (error != null)
	 out.println("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Did you mean <i>"+error+"</i> ?<br><br><hr><br>");
 else
 {
ArrayList<String> list = (ArrayList<String>) request.getAttribute("list");

// print the information about every category of the list
for(String category : list) {
    out.println("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"+category+"</b><br><br><hr><br>");


    
}
out.println("<i>  Search time is :</i> "+ (String) request.getAttribute("exect")+" s");  
 }
 }catch(Exception e)
{
	
}



%>
           
<tbody>
</tbody>
</table>
</form>



</body>
</html>