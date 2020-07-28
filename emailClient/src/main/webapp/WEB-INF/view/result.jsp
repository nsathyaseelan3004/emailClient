<!DOCTYPE HTML>
<html xmlns:th="https://www.thymeleaf.org">
<head>
    <title>Getting Started: Handling Form Submission</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
	<div id="mailDiv" th:each="resp : ${response}">
	    <p th:text="'From: ' + ${resp.from}" />
	    <p th:text="'To: ' + ${resp.to}" />
	    <p th:text="'Subject: ' + ${resp.subject}" />
	    <a th:href="'Subject: '+ ${resp.subject}" th:onclick="loadContent(${resp.message})">Click to View Content</a> 
	</div>
	<div id="messageDiv">
	</div>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
    <script type="text/javascript">
           function loadContent(message) {
        	   alert("inside");
               $("#messageDiv").html("<p th:text=\"'Message: '\"+message />");
           }
	</script>
</body>
</html>
