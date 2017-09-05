package com.okta.oauth;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
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

import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;

public class OAuth2Callback extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(OAuth2Callback.class);

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OAuth2Callback() {
        super();
        // TODO Auto-generated constructor stub
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		String code = request.getParameter(OAuthConstants.CODE);
		logger.info("code ="+code);
		//Load the properties file
		Properties config = OAuthUtils.getClientConfigProps(OAuthConstants.CONFIG_FILE_PATH);
				
		//Generate the OAuthDetails bean from the config properties file
		OAuth2Details oauthDetails = OAuthUtils.createOAuthDetails(config);
		try{
			Map<String,String> map = OAuthUtils.getAccessToken(oauthDetails, code);
			Map<String,String> list = new HashMap<String,String>();
//			map = new HashMap<String, String>();
			for (Map.Entry<String,String> entry : map.entrySet()) {
				String key = entry.getKey();
			
				logger.info("key ="+key);
				Object value = entry.getValue();
			    String stringvalue = value.toString();
				if(key.equals("access_token")){
					
					list.put("access_token", stringvalue);
				}
				if(key.equals("id_token")){
					
					list.put("id_token", stringvalue);
				}
			    logger.info("decoding access token JWT ");
			    String result = OAuthUtils.decodeJWT(stringvalue);
			    
			    logger.info("value ="+stringvalue);
			    session.setAttribute("tokens", list);
			}
			response.getWriter().println(new Gson().toJson(map));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		logger.info("returned map =" + (response.getWriter().println(new Gson().toJson(map))));
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}	

}
