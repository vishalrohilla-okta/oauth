<!--/**
 * @author Vishal Rohilla
 * date Jan 10, 2016
 */ -->

<%@page import="com.okta.oauth.bean.User"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
    <%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
    <%@page import="java.io.InputStream" %>
<%@page import="java.util.Properties" %>
<%@ page import="java.net.URLDecoder"%>
<%!Properties oktaconf = new Properties(); %>

<%!public void jspInit(){
	
 // Thread.currentThread().getContextClassLoader().getResourceAsStream("Oktaconf.properties");
  ClassLoader loader = Thread.currentThread().getContextClassLoader(); 
  InputStream stream = loader.getResourceAsStream("Oktaconf.properties");
	  
 try {
		oktaconf.load(stream);
		
		
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Application</title>



</head>
<script>
function validateForm()
{
var x=document.forms["changepsw"]["Username"].value;
var atpos=x.indexOf("@");
var dotpos=x.lastIndexOf(".");
if (atpos<1 || dotpos<atpos+2 || dotpos+2>=x.length)
  {
  alert("Username should be in e-mail address format");
  return false;
  }
}
function submitForm()
{
	var form = document.getElementById("form14");
	form.action = "/portal/MyProfile";
	form.submit();
}
</script>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Application</title>

  <!--[if lt IE 9]>
      <script src="//cdnjs.cloudflare.com/ajax/libs/html5shiv/3.7.2/html5shiv.min.js"></script>
  <![endif]-->

  <!-- Core Okta login widget and css -->
  <link href="https://jankyco.oktapreview.com/js/sdk/okta-sign-in-1.0.0.min.css" type="text/css" rel="stylesheet">
  <script src="https://jankyco.oktapreview.com/js/sdk/okta-sign-in-1.0.0.min.js" type="text/javascript"></script>

  <!--
    Customizable css theme options for the login widget.
    Link your own customized copy of okta-theme.css
    or override styles in-line.
  -->
  <link href="https://jankyco.oktapreview.com/js/sdk/okta-theme-1.0.0.css" type="text/css" rel="stylesheet">
  <link href="https://fonts.googleapis.com/css?family=Open+Sans:400" type="text/css" rel="stylesheet">
  <style>
    body {
      background-image: url('https://s28.postimg.org/9ivrjwqot/MRI_bg.jpg');
      background-repeat: no-repeat;
      background-position: center center fixed;
      -webkit-background-size: cover;
      -moz-background-size: cover;
      -o-background-size: cover;
      background-size: cover;
    }

    #okta-sign-in.main-container {
      font-family: "Open Sans", Arial, Helvetica, sans-serif;
    }

    #container #okta-sign-in .auth-header {
      padding: 45px 0px 60px;
      height: 120px;
    }

    #container #okta-sign-in .okta-sign-in-title {
      font-size: 18px;
      font-weight: 600;
    }

    #container #okta-sign-in a.link {
      color: #798187;
    }

    #container #okta-sign-in .button {
      color: #ffffff;
      background-color: #3a3f44;
      border-color: #3a3f44;
      background-image: -webkit-linear-gradient(#484e55, #3a3f44 60%, #313539);
      background-image: -o-linear-gradient(#484e55, #3a3f44 60%, #313539);
      background-image: -webkit-gradient(linear, left top, left bottom, from(#484e55), color-stop(60%, #3a3f44), to(#313539));
      background-image: linear-gradient(#484e55, #3a3f44 60%, #313539);
      background-repeat: no-repeat;
      filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#ff484e55', endColorstr='#ff313539', GradientType=0);
      -webkit-filter: none;
      filter: none;
    }

    #container #okta-sign-in .button:hover {
      border-color: rgba(0, 0, 0, 0.6);
      text-shadow: 1px 1px 1px rgba(0, 0, 0, 0.3);
      background-image: -webkit-linear-gradient(#020202, #101112 40%, #191b1d);
      background-image: -o-linear-gradient(#020202, #101112 40%, #191b1d);
      background-image: -webkit-gradient(linear, left top, left bottom, from(#020202), color-stop(40%, #101112), to(#191b1d));
      background-image: linear-gradient(#020202, #101112 40%, #191b1d);
      background-repeat: no-repeat;
      filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#ff020202', endColorstr='#ff191b1d', GradientType=0);
      -webkit-filter: none;
      filter: none;
    }


    #container #okta-sign-in .button-primary:hover {
      background-image: -webkit-linear-gradient(#e72510, #d9230f 6%, #cb210e);
      background-image: -o-linear-gradient(#e72510, #d9230f 6%, #cb210e);
      background-image: -webkit-gradient(linear, left top, left bottom, from(#e72510), color-stop(6%, #d9230f), to(#cb210e));
      background-image: linear-gradient(#e72510, #d9230f 6%, #cb210e);
      background-repeat: no-repeat;
      filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#ffe72510', endColorstr='#ffcb210e', GradientType=0);
      -webkit-filter: none;
      filter: none;
    }

    #container #okta-sign-in .button-primary {
      color: #ffffff;
      background-color: #7a8288;
      border-color: #7a8288;
      background-image: -webkit-linear-gradient(#8a9196, #7a8288 60%, #70787d);
      background-image: -o-linear-gradient(#8a9196, #7a8288 60%, #70787d);
      background-image: -webkit-gradient(linear, left top, left bottom, from(#8a9196), color-stop(60%, #7a8288), to(#70787d));
      background-image: linear-gradient(#8a9196, #7a8288 60%, #70787d);
      background-repeat: no-repeat;
      filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#ff8a9196', endColorstr='#ff70787d', GradientType=0);
      -webkit-filter: none;
      filter: none;
    }

    #container #okta-sign-in .button-primary:hover {
      background-image: -webkit-linear-gradient(#404448, #4e5458 40%, #585e62);
      background-image: -o-linear-gradient(#404448, #4e5458 40%, #585e62);
      background-image: -webkit-gradient(linear, left top, left bottom, from(#404448), color-stop(40%, #4e5458), to(#585e62));
      background-image: linear-gradient(#404448, #4e5458 40%, #585e62);
      background-repeat: no-repeat;
      filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#ff404448', endColorstr='#ff585e62', GradientType=0);
      -webkit-filter: none;
      filter: none;
    }
  </style>

</head>


<body>
<form class="o-form o-form-edit-mode" id="form14" name=""changepsw" data-se="o-form" action="/portal/Logout" method="POST"> 
<div id="okta-sign-in" class="auth-container main-container no-beacon">      
<div class="okta-sign-in-header auth-header">                
<!--  <img src="https://upload.wikimedia.org/wikipedia/commons/a/af/Cisco_upd_03.jpg" class="auth-org-logo">  -->
<% String img1 = oktaconf.getProperty("img"); %>
  <img src=<%=img1%> class="auth-org-logo">              
<div data-type="beacon-container" class="beacon-container"></div>      
</div>      
<div class="auth-content">
<div class="auth-content-inner">
<div class="primary-auth">
     
<div data-se="o-form-content" class="o-form-content o-form-theme clearfix"> 
<%  User userInfo = new User(); %>
<% if(session.getAttribute("user")!=null) {
	  userInfo = (User)session.getAttribute("user");
	 System.out.println("User in the jsp is not null ");
	 %>
  <div class="o-form-fieldset-container" data-se="o-form-fieldset-container">
  <% if(userInfo.getGiven_name()!=null){ %>
  <div class="o-form-fieldset o-form-label-top" data-se="o-form-fieldset">
  <div class="o-form-input" data-se="o-form-input-container">
  <span class="okta-sign-in-input-field input-fix o-form-control" data-se="o-form-input-username">                        
  <span title="" oldtitle="Enter your @ partner.com ID" data-hasqtip="6" class="input-tooltip icon form-help-16"></span>                         
   <span class="icon input-icon person-16-gray"></span>                    
   <input placeholder="FirstName" name="FirstName" id="input21" value=<%=userInfo.getGiven_name()%> type="text" disabled>    </span></div></div>
  <%} if(userInfo.getFamily_name()!=null){ %> 
   <div class="o-form-fieldset o-form-label-top" data-se="o-form-fieldset">
   <div class="o-form-input" data-se="o-form-input-container">
   <span class="okta-sign-in-input-field input-fix o-form-control" data-se="o-form-input-password">                       
    <span title="" oldtitle="Super secret password" data-hasqtip="7" class="input-tooltip icon form-help-16"></span>                          
    <span class="icon input-icon remote-lock-16"></span>                    
    <input placeholder="LastName" name="LastName" id="input28" value=<%=userInfo.getFamily_name()%> type="text" disabled>    </span></div></div>
    
 <%} if (userInfo.getEmail()!=null){ %>   
 
   <div class="o-form-fieldset o-form-label-top" data-se="o-form-fieldset">
      <div class="o-form-input" data-se="o-form-input-container">
  <span class="okta-sign-in-input-field input-fix o-form-control" data-se="o-form-input-username">                        
  <span title="" oldtitle="Enter your @ partner.com ID" data-hasqtip="6" class="input-tooltip icon form-help-16"></span>                         
   <span class="icon input-icon person-16-gray"></span>                    
   <input placeholder="Mobile Number" name="pnumber" id="input21" value=<%=userInfo.getEmail()%> type="text" disabled>    </span></div></div>
<% } if(userInfo.getAddress()!=null){ %>
   <div class="o-form-fieldset o-form-label-top" data-se="o-form-fieldset">
      <div class="o-form-input" data-se="o-form-input-container">
  <span class="okta-sign-in-input-field input-fix o-form-control" data-se="o-form-input-username">                        
  <span title="" oldtitle="Enter your @ partner.com ID" data-hasqtip="6" class="input-tooltip icon form-help-16"></span>                         
   <span class="icon input-icon person-16-gray"></span>                    
   <input placeholder="Street Address" name="pnumber" id="input21" value=<%=userInfo.getAddress().getStreet_address()%> type="text" disabled>    </span></div></div>

   <div class="o-form-fieldset o-form-label-top" data-se="o-form-fieldset">
      <div class="o-form-input" data-se="o-form-input-container">
  <span class="okta-sign-in-input-field input-fix o-form-control" data-se="o-form-input-username">                        
  <span title="" oldtitle="Enter your @ partner.com ID" data-hasqtip="6" class="input-tooltip icon form-help-16"></span>                         
   <span class="icon input-icon person-16-gray"></span>                    
   <input placeholder="City" name="pnumber" id="input21" value=<%=userInfo.getAddress().getLocality()%> type="text" disabled>    </span></div></div>
   
   
      <div class="o-form-fieldset o-form-label-top" data-se="o-form-fieldset">
      <div class="o-form-input" data-se="o-form-input-container">
  <span class="okta-sign-in-input-field input-fix o-form-control" data-se="o-form-input-username">                        
  <span title="" oldtitle="Enter your @ partner.com ID" data-hasqtip="6" class="input-tooltip icon form-help-16"></span>                         
   <span class="icon input-icon person-16-gray"></span>                    
   <input placeholder="State" name="pnumber" id="input21" value=<%=userInfo.getAddress().getRegion()%> type="text" disabled>    </span></div></div>
   
 
         <div class="o-form-fieldset o-form-label-top" data-se="o-form-fieldset">
      <div class="o-form-input" data-se="o-form-input-container">
  <span class="okta-sign-in-input-field input-fix o-form-control" data-se="o-form-input-username">                        
  <span title="" oldtitle="Enter your @ partner.com ID" data-hasqtip="6" class="input-tooltip icon form-help-16"></span>                         
   <span class="icon input-icon person-16-gray"></span>                    
   <input placeholder="Country" name="pnumber" id="input21" value=<%=userInfo.getAddress().getCountry()%> type="text" disabled>    </span></div></div>
 
        <div class="o-form-fieldset o-form-label-top" data-se="o-form-fieldset">
      <div class="o-form-input" data-se="o-form-input-container">
  <span class="okta-sign-in-input-field input-fix o-form-control" data-se="o-form-input-username">                        
  <span title="" oldtitle="Enter your @ partner.com ID" data-hasqtip="6" class="input-tooltip icon form-help-16"></span>                         
   <span class="icon input-icon person-16-gray"></span>                    
   <input placeholder="Zip" name="pnumber" id="input21" value=<%=userInfo.getAddress().getPostal_code()%> type="text" disabled>    </span></div></div> 
  <%} %>
 <!--     
    <div class="o-form-fieldset o-form-label-top" data-se="o-form-fieldset">
      <div class="o-form-input" data-se="o-form-input-container">
  <span class="okta-sign-in-input-field input-fix o-form-control" data-se="o-form-input-username">                        
  <span title="" oldtitle="Enter your @ partner.com ID" data-hasqtip="6" class="input-tooltip icon form-help-16"></span>                         
   <span class="icon input-icon person-16-gray"></span>                    
    
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Group : <select name="DropList" size=1>
 	<option value="Community">Community</option>
    <option value="Support">Support</option>   
    
   
    
</select>
</span></div></div>-->
    </div>  
<!--<h2 data-se="o-form-head" class="okta-sign-in-title o-form-head"><a href="https://socialidm.oktapreview.com/home/salesforce/0oa5c6uz753l2n4Un0h7/24" target="_blank">Community</a></h2>-->
<!--<h2 data-se="o-form-head" class="okta-sign-in-title o-form-head"><a href="https://socialidm.oktapreview.com/home/salesforce/0oa5c6uz753l2n4Un0h7/24" target="_blank">Global Support Login</a></h2>
<h2 data-se="o-form-head" class="okta-sign-in-title o-form-head">Mindtouch Login</h2>
<h2 data-se="o-form-head" class="okta-sign-in-title o-form-head">RbmConnect Login</h2>
<h2 data-se="o-form-head" class="okta-sign-in-title o-form-head">Litmus Login</h2>-->
 <!--   <h2 data-se="o-form-head" class="okta-sign-in-title o-form-head"><a href="#" onclick="document.forms[0].submit();return false;">Logout</a></h2>-->
<%} %>           
 
 <% if(request.getAttribute("message")!=null) {
	 String message = (String)request.getAttribute("message");
	 System.out.println("message in the jsp = "+message);
	 %>
 <div class="o-form-error-container" data-se="o-form-error-container"><%=message%>
 </div>     
 <%} else{ %>
 <div class="o-form-error-container" data-se="o-form-error-container">
 </div>  
 <% }%>
  <div class="o-form-fieldset-container" data-se="o-form-fieldset-container">
 
    
    <div class="auth-footer">      
         
    <ul style="display: block;" class="help-links js-help-links">        
    <!--<li><a href="Register.jsp" class="link js-forgot-password">Register here</a></li>-->
    <li><a href="#" class="link js-forgot-password" onclick="document.forms[0].submit();return false;">Logout</a></li>
    <li><a href="#" class="link js-forgot-password" onclick="submitForm();">My profile</a></li>
  <!-- <li><a href="#" class="link js-unlock">Unlock account?</a></li>
    <li><a href="http://acme.com/custom/link1" class="link js-custom">custom link text 1</a></li>
    <li><a href="http://acme.com/custom/link2" class="link js-custom">custom link text 2</a>                </li>
    <li><a href="http://acme.com/custom/help/page" class="link js-help-link">Help</a>      </li>--></ul>    </div></div></div></div>    </div>
   

</div>
</form>
</body>
</html>
