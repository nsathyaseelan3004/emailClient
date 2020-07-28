<!DOCTYPE HTML>
<html xmlns:th="https://www.thymeleaf.org">
<head>
    <title>Getting Started: Handling Form Submission</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
	<h1>Form</h1>
    <form action="#" th:action="@{/emailDetails}" th:object="${greeting}" method="post">
    	<p>Username: <input type="text" th:field="*{userName}" /></p>
        <p>Password: <input type="password" th:field="*{password}" /></p>
        <p>IMAP Server: <input type="text" th:field="*{IMAPServer}" /></p>
        <p>port: <input type="text" th:field="*{port}" /></p>
        <p><input type="submit" value="Submit" /> <input type="reset" value="Reset" /></p>
    </form>
</body>
</html>