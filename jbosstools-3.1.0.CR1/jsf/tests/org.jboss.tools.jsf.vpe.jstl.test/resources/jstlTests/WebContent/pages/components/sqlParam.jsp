<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>

<html>
<head>
<title>JSTL</title>
</head>
<body>
	<h1>sql:param</h1>
	<sql:query dataSource="someDataSource" var="queryResults" maxRows="10"
		scope="session" sql="SELECT * FROM customers">
		<sql:param value="paramValue" id="id1"></sql:param>
	</sql:query>
</body>
</html>
