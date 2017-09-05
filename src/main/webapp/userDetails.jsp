<html>
<head>
<link rel="stylesheet" href="stylesheets/template.css" type="text/css">
<title>OAuth2.0 Authorization code flow get User Info Endpoint</title>
<h1>OAuth2.0 Authorization code grant test client, get User Info from the userinfo endpoint</h1>
<script type="text/javascript">
function callServlet(arg){
	document.location.href = "getUserInfo?caller="+arg;
}
</script>
</head>
<body>
<p>This web application will redirect you to the endpoint against whom you wish to test the OAuth2.0 flow.
Make sure you have put in correct values in the <strong>Oauth2Client.config</strong> configuration file.  </p>
<table>

<tr><td>Get User Details</td><td><input type="button" id="userInfo" name="userInfo" value="Get User Details" onclick="callServlet('getUserInfo')"></td></tr>

</table>

</body>
</html>