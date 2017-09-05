package com.okta.oauth;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.okta.oauth.bean.User;

public class OAuth2GetUserInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(OAuth2GetUserInfo.class);

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OAuth2GetUserInfo() {
        super();
        // TODO Auto-generated constructor stub
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		Map<String,String> list = (HashMap)session.getAttribute("tokens");
		String access_token = "";
		for (Map.Entry<String,String> entry : list.entrySet()) {
			String key = entry.getKey();		
						
			Object value = entry.getValue();
		    String stringvalue = value.toString();
		    if(key.equals("access_token")){
				access_token = stringvalue;				
			}
		}
		//Load the properties file
		Properties config = OAuthUtils.getClientConfigProps(OAuthConstants.CONFIG_FILE_PATH);
				
		//Generate the OAuthDetails bean from the config properties file
		OAuth2Details oauthDetails = OAuthUtils.createOAuthDetails(config);
		try{
			logger.info("access_token in OAuth2GetUserInfo ="+access_token);
			Map<String, String> userDetails = OAuthUtils.getUserInfo(oauthDetails, access_token);
//			Gson gson = new Gson();
//			JsonElement jsonElement = gson.toJsonTree(userDetails);
//			User user = gson.fromJson(jsonElement, User.class);
//			logger.info("sub ="+user.getSub());
//			logger.info("lastname ="+user.getFamily_name());
//			logger.info("preffered username ="+user.getPreferred_username());
//			request.getSession().setAttribute("user", user);
//			request.getRequestDispatcher("application.jsp").forward(request, response);
			response.getWriter().println(new Gson().toJson(userDetails));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}	

}
