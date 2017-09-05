package com.okta.oauth;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.okta.oauth.bean.Tokens;
import com.okta.oauth.bean.User;

public class OAuthUtils {

	private static Log logger = LogFactory.getLog(OAuthUtils.class);
	public static OAuth2Details createOAuthDetails(Properties config) {
		OAuth2Details oauthDetails = new OAuth2Details();
		oauthDetails.setAccessToken((String) config
				.get(OAuthConstants.ACCESS_TOKEN));
		oauthDetails.setRefreshToken((String) config
				.get(OAuthConstants.REFRESH_TOKEN));
		oauthDetails.setGrantType((String) config
				.get(OAuthConstants.GRANT_TYPE));
		oauthDetails.setClientId((String) config.get(OAuthConstants.CLIENT_ID));
		oauthDetails.setClientSecret((String) config
				.get(OAuthConstants.CLIENT_SECRET));
		oauthDetails.setScope((String) config.get(OAuthConstants.SCOPE));
		oauthDetails.setAuthenticationServerUrl((String) config
				.get(OAuthConstants.AUTHENTICATION_SERVER_URL));
		oauthDetails.setUserInfoUrl((String) config
				.get(OAuthConstants.USERINFO_ENDPOINT_URL));
		oauthDetails.setUsername((String) config.get(OAuthConstants.USERNAME));
		oauthDetails.setPassword((String) config.get(OAuthConstants.PASSWORD));
		oauthDetails.setResourceServerUrl((String) config
				.get(OAuthConstants.RESOURCE_SERVER_URL));
		oauthDetails.setTokenEndpointUrl((String) config
				.get(OAuthConstants.TOKEN_ENDPOINT_URL));
		oauthDetails.setRedirectURI((String) config
				.get(OAuthConstants.REDIRECT_URI));
		oauthDetails.setState((String) config.get(OAuthConstants.STATE));
		oauthDetails.setApprovalPromptKey((String) config
				.get(OAuthConstants.APPROVAL_PROMPT_KEY));
		oauthDetails.setApprovalPromptValue((String) config
				.get(OAuthConstants.APPROVAL_PROMPT_VALUE));
		oauthDetails.setAccessTypeKey((String) config
				.get(OAuthConstants.ACCESS_TYPE_KEY));
		oauthDetails.setAccessTypeValue((String) config
				.get(OAuthConstants.ACCESS_TYPE_VALUE));
		oauthDetails.setResponseType((String) config
				.get(OAuthConstants.RESPONSE_TYPE));
		oauthDetails.setNonce((String) config
				.get(OAuthConstants.NONCE));

		return oauthDetails;
	}

	public static Properties getClientConfigProps(String path) {
		InputStream is = OAuthUtils.class.getClassLoader().getResourceAsStream(
				path);
		Properties config = new Properties();
		try {
			config.load(is);
		} catch (IOException e) {
			System.out.println("Could not load properties from " + path);
			e.printStackTrace();
			return null;
		}
		return config;
	}

	/**
	 * Request OAuth server for authorization code
	 * 
	 * @param oauthDetails
	 * @return
	 * @throws URISyntaxException 
	 */
	public static String getAuthorizationCode(OAuth2Details oauthDetails) throws URISyntaxException {

		String location = null;
		URIBuilder builder = new URIBuilder(oauthDetails.getAuthenticationServerUrl()); //$NON-NLS-1$
		builder.addParameter("idp", "0oa8u9fz18qjxkFBb0h7");
		
		builder.addParameter("response_type"
				, "code");
		builder.addParameter("client_id", oauthDetails.getClientId());
		builder.addParameter("redirect_uri", oauthDetails.getRedirectURI());
		builder.addParameter("scope", oauthDetails.getScope());
		builder.addParameter("state", oauthDetails.getState());
		builder.addParameter("nonce", oauthDetails.getNonce());
		URI uri = builder.build();
		logger.info("uri ="+uri.toString());

		HttpGet hGet = new HttpGet(uri);
		DefaultHttpClient client = new DefaultHttpClient();
		HttpClient instance = HttpClientBuilder.create().disableRedirectHandling().build();	
		HttpResponse response = null;
		String accessToken = null;
		try {
			response = instance.execute(hGet);
			Header[] headers = response.getAllHeaders();
			for (Header header : headers) {
				System.out.println("Key : " + header.getName()
				      + " ,Value : " + header.getValue());
			}
			int code = response.getStatusLine().getStatusCode();
			logger.info("Code back from the call ="+code);
			Map<String, String> map = handleURLEncodedResponse(response);

			if (OAuthConstants.HTTP_SEND_REDIRECT == code) {
				location = getHeader(response.getAllHeaders(),
						OAuthConstants.LOCATION_HEADER);
				if (location == null) {
					System.out
							.println("The endpoint did not pass in valid location header for redirect");
					throw new RuntimeException(
							"The endpoint did not pass in valid location header for redirect");
				}
			} else {
				System.out
						.println("Was expecting code 302 from endpoint to indicate redirect. Recieved httpCode "
								+ code);
				throw new RuntimeException(
						"Was expecting code 302 from endpoint to indicate redirect. Recieved httpCode "
								+ code);
			}

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}

		return location;
	}

	/**
	 * Request OAuth server for user info
	 * 
	 * @param oauthDetails
	 * @return
	 * @throws URISyntaxException 
	 */
	public static Map<String, String> getUserInfo(OAuth2Details oauthDetails, String accessToken) throws URISyntaxException {

		String location = null;
		URIBuilder builder = new URIBuilder(oauthDetails.getUserInfoUrl()); //$NON-NLS-1$
		URI uri = builder.build();
		HttpGet hGet = new HttpGet(uri);
		hGet.addHeader(OAuthConstants.AUTHORIZATION,
				getAuthorizationHeaderForAccessToken(accessToken));
		HttpClient instance = HttpClientBuilder.create().disableRedirectHandling().build();	
		HttpResponse response = null;
		Map<String, String> map = new HashMap<String, String>();
		try {
			response = instance.execute(hGet);
			int code = response.getStatusLine().getStatusCode();
			logger.info("code ="+code);
			map = handleResponse(response);

			if (OAuthConstants.HTTP_OK == code) {
				location = getHeader(response.getAllHeaders(),
						OAuthConstants.LOCATION_HEADER);
				logger.info("location ="+location);
/*				if (location == null) {
					System.out
							.println("The endpoint did not pass in valid location header for redirect");
					throw new RuntimeException(
							"The endpoint did not pass in valid location header for redirect");
				}*/
			} else {
				System.out
						.println("Was expecting code 302 from endpoint to indicate redirect. Recieved httpCode "
								+ code);
				throw new RuntimeException(
						"Was expecting code 302 from endpoint to indicate redirect. Recieved httpCode "
								+ code);
			}

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}

		return map;
	}	
	
	/**
	 * Use the Access token or refresh token to access protected resource
	 * 
	 * @param oauthDetails
	 * @return
	 */
	public static Map<String, String> getProtectedResource(
			OAuth2Details oauthDetails) {
		String resourceURL = oauthDetails.getResourceServerUrl();

		Map<String, String> map = new HashMap<String, String>();

		HttpGet get = new HttpGet(resourceURL);
		get.addHeader(OAuthConstants.AUTHORIZATION,
				getAuthorizationHeaderForAccessToken(oauthDetails
						.getAccessToken()));
		DefaultHttpClient client = new DefaultHttpClient();
		HttpResponse response = null;
		String accessToken = null;
		int code = -1;
		try {
			response = client.execute(get);
			code = response.getStatusLine().getStatusCode();
			if (code == OAuthConstants.HTTP_UNAUTHORIZED
					|| code == OAuthConstants.HTTP_FORBIDDEN) {
				// Access token is invalid or expired.Regenerate the access
				// token
				System.out
						.println("Access token is invalid or expired. Refreshing access token....");
				map = refreshAccessToken(oauthDetails);
				accessToken = map.get(OAuthConstants.ACCESS_TOKEN);

				if (isValid(accessToken)) {
					// update the access token
					System.out.println("New access token: " + accessToken);
					oauthDetails.setAccessToken(accessToken);
					get.removeHeaders(OAuthConstants.AUTHORIZATION);
					get.addHeader(OAuthConstants.AUTHORIZATION,
							getAuthorizationHeaderForAccessToken(oauthDetails
									.getAccessToken()));
					get.releaseConnection();
					response = client.execute(get);
					code = response.getStatusLine().getStatusCode();
					if (code >= 400) {
						throw new RuntimeException(
								"Could not access protected resource. Server returned http code: "
										+ code);

					}

				} else {
					throw new RuntimeException("Could not refresh access token");
				}

			}

			map = handleResponse(response);

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			get.releaseConnection();
		}

		return map;
	}

	/**
	 * Exchange the authorization code for the access and refresh tokens
	 * 
	 * @param oauthDetails
	 * @param authorizationCode
	 * @return
	 */
	public static Map<String, String> getAccessToken(
			OAuth2Details oauthDetails, String authorizationCode) throws URISyntaxException{
		HttpPost post = new HttpPost(oauthDetails.getTokenEndpointUrl());
		String clientId = oauthDetails.getClientId();
		String clientSecret = oauthDetails.getClientSecret();
		String scope = oauthDetails.getScope();
		Map<String, String> map = new HashMap<String, String>();

		List<BasicNameValuePair> parametersBody = new ArrayList<BasicNameValuePair>();

		parametersBody.add(new BasicNameValuePair(OAuthConstants.GRANT_TYPE,
				oauthDetails.getGrantType()));

		parametersBody.add(new BasicNameValuePair(OAuthConstants.CODE,
				authorizationCode));

		parametersBody.add(new BasicNameValuePair(OAuthConstants.CLIENT_ID,
				clientId));

		if (isValid(clientSecret)) {
			parametersBody.add(new BasicNameValuePair(
					OAuthConstants.CLIENT_SECRET, clientSecret));
		}

		parametersBody.add(new BasicNameValuePair(OAuthConstants.SCOPE,
				scope));
		parametersBody.add(new BasicNameValuePair(OAuthConstants.REDIRECT_URI,
				oauthDetails.getRedirectURI()));

		DefaultHttpClient client = new DefaultHttpClient();
		HttpResponse response = null;
		String accessToken = null;		
		try {
			post.setEntity(new UrlEncodedFormEntity(parametersBody, HTTP.UTF_8));
			response = client.execute(post);
			int code = response.getStatusLine().getStatusCode();

			map = handleResponse(response);
//			Gson gson = new Gson();
//			JsonElement jsonElement = gson.toJsonTree(map);
//			Tokens tokens = gson.fromJson(jsonElement, T.class);
			accessToken = map.get(OAuthConstants.ACCESS_TOKEN);
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}

		return map;
	}

	/**
	 * Refresh an expired access token by using the refresh token
	 * 
	 * @param oauthDetails
	 * @return
	 */
	public static Map<String, String> refreshAccessToken(
			OAuth2Details oauthDetails) {
		HttpPost post = new HttpPost(oauthDetails.getTokenEndpointUrl());
		String clientId = oauthDetails.getClientId();
		String clientSecret = oauthDetails.getClientSecret();
		String scope = oauthDetails.getScope();
		String refreshToken = oauthDetails.getRefreshToken();
		Map<String, String> map = new HashMap<String, String>();

		if (!isValid(refreshToken)) {
			throw new RuntimeException(
					"Please provide valid refresh token in config file");
		}

		List<BasicNameValuePair> parametersBody = new ArrayList<BasicNameValuePair>();

		parametersBody.add(new BasicNameValuePair(OAuthConstants.GRANT_TYPE,
				OAuthConstants.REFRESH_TOKEN));

		parametersBody.add(new BasicNameValuePair(OAuthConstants.REFRESH_TOKEN,
				oauthDetails.getRefreshToken()));

		if (isValid(clientId)) {
			parametersBody.add(new BasicNameValuePair(OAuthConstants.CLIENT_ID,
					clientId));
		}

		if (isValid(clientSecret)) {
			parametersBody.add(new BasicNameValuePair(
					OAuthConstants.CLIENT_SECRET, clientSecret));
		}

		if (isValid(scope)) {
			parametersBody.add(new BasicNameValuePair(OAuthConstants.SCOPE,
					scope));
		}

		DefaultHttpClient client = new DefaultHttpClient();
		HttpResponse response = null;
		try {
			post.setEntity(new UrlEncodedFormEntity(parametersBody, HTTP.UTF_8));

			response = client.execute(post);
			int code = response.getStatusLine().getStatusCode();

			map = handleResponse(response);

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}

		return map;
	}

	/**
	 * Handles the Server response. Delegates to appropriate handler
	 * 
	 * @param response
	 * @return
	 */
	public static Map handleResponse(HttpResponse response) {
		String contentType = OAuthConstants.JSON_CONTENT;
		if (response.getEntity().getContentType() != null) {
			contentType = response.getEntity().getContentType().getValue();
		}
		if (contentType.contains(OAuthConstants.JSON_CONTENT)) {
			return handleJsonResponse(response);
		} else if (contentType.contains(OAuthConstants.URL_ENCODED_CONTENT)) {
			return handleURLEncodedResponse(response);
		} else if (contentType.contains(OAuthConstants.XML_CONTENT)) {
			return handleXMLResponse(response);
		} else {
			// Unsupported Content type
			throw new RuntimeException(
					"Cannot handle "
							+ contentType
							+ " content type. Supported content types include JSON, XML and URLEncoded");
		}

	}

	public static Map handleJsonResponse(HttpResponse response) {
		Map<String, String> oauthLoginResponse = null;
		// String contentType =
		// response.getEntity().getContentType().getValue();
		try {
			oauthLoginResponse = (Map<String, String>) new JSONParser()
					.parse(EntityUtils.toString(response.getEntity()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException();
		} catch (Exception e) {
			System.out.println("Could not parse JSON response");
			throw new RuntimeException(e.getMessage());
		}
		System.out.println();
		System.out.println("********** Response Received **********");
		for (Map.Entry<String, String> entry : oauthLoginResponse.entrySet()) {
			System.out.println(String.format("  %s = %s", entry.getKey(),
					entry.getValue()));
		}
		return oauthLoginResponse;
	}

	public static Map handleURLEncodedResponse(HttpResponse response) {
		logger.info("In handleURLEncodedResponse");
		Map<String, Charset> map = Charset.availableCharsets();
		Map<String, String> oauthResponse = new HashMap<String, String>();
		Set<Map.Entry<String, Charset>> set = map.entrySet();
		Charset charset = null;
		HttpEntity entity = response.getEntity();

		System.out.println();
		System.out.println("********** Response Received **********");
		logger.info("********** Response Received **********");

		for (Map.Entry<String, Charset> entry : set) {
//			System.out.println(String.format("  %s = %s", entry.getKey(),
//					entry.getValue()));
			logger.info(String.format("  %s = %s", entry.getKey(),
					entry.getValue()));
			if (entry.getKey().equalsIgnoreCase(HTTP.UTF_8)) {
				charset = entry.getValue();
			}
		}

		try {
			List<NameValuePair> list = URLEncodedUtils.parse(
					EntityUtils.toString(entity), Charset.forName(HTTP.UTF_8));
			for (NameValuePair pair : list) {
				logger.info("building oauthresponse object");
//				System.out.println(String.format("  %s = %s", pair.getName(),
//						pair.getValue()));
				logger.info(String.format("  %s = %s", pair.getName(),
						pair.getValue()));
				oauthResponse.put(pair.getName(), pair.getValue());
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("Could not parse URLEncoded Response");
		}

		return oauthResponse;
	}

	public static Map handleXMLResponse(HttpResponse response) {
		Map<String, String> oauthResponse = new HashMap<String, String>();
		try {

			String xmlString = EntityUtils.toString(response.getEntity());
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder db = factory.newDocumentBuilder();
			InputSource inStream = new InputSource();
			inStream.setCharacterStream(new StringReader(xmlString));
			Document doc = db.parse(inStream);

			System.out.println("********** Response Receieved **********");
			parseXMLDoc(null, doc, oauthResponse);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(
					"Exception occurred while parsing XML response");
		}
		return oauthResponse;
	}

	public static void parseXMLDoc(Element element, Document doc,
			Map<String, String> oauthResponse) {
		NodeList child = null;
		if (element == null) {
			child = doc.getChildNodes();

		} else {
			child = element.getChildNodes();
		}
		for (int j = 0; j < child.getLength(); j++) {
			if (child.item(j).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
				org.w3c.dom.Element childElement = (org.w3c.dom.Element) child
						.item(j);
				if (childElement.hasChildNodes()) {
					System.out.println(childElement.getTagName() + " : "
							+ childElement.getTextContent());
					oauthResponse.put(childElement.getTagName(),
							childElement.getTextContent());
					parseXMLDoc(childElement, null, oauthResponse);
				}

			}
		}
	}

	public static String getAuthorizationHeaderForAccessToken(String accessToken) {
		return OAuthConstants.BEARER + " " + accessToken;
	}

	public static String getBasicAuthorizationHeader(String username,
			String password) {
		return OAuthConstants.BASIC + " "
				+ encodeCredentials(username, password);
	}

	public static String encodeCredentials(String username, String password) {
		String cred = username + ":" + password;
		String encodedValue = null;
		byte[] encodedBytes = Base64.encodeBase64(cred.getBytes());
		encodedValue = new String(encodedBytes);
		System.out.println("encodedBytes " + new String(encodedBytes));

		byte[] decodedBytes = Base64.decodeBase64(encodedBytes);
		System.out.println("decodedBytes " + new String(decodedBytes));

		return encodedValue;

	}

	public static List<String> validateInput(OAuth2Details input) {

		List<String> invalidProps = new ArrayList<String>();

		if (input == null) {
			invalidProps
					.add("The OAuth2Details bean itself is null. Please check the OAuth2Client code");
			return invalidProps;
		}

		String grantType = input.getGrantType();

		if (!isValid(grantType)) {
			System.out.println("Please provide valid value for grant_type");
			invalidProps.add(OAuthConstants.GRANT_TYPE);
		}

		if (!isValid(input.getAuthenticationServerUrl())) {
			System.out
					.println("Please provide valid value for authentication server url");
			invalidProps.add(OAuthConstants.AUTHENTICATION_SERVER_URL);
		}

		if (!isValid(input.getTokenEndpointUrl())) {
			System.out
					.println("Please provide valid value for token endpoint url");
			invalidProps.add(OAuthConstants.TOKEN_ENDPOINT_URL);
		}

		if (!isValid(input.getApprovalPromptValue())) {
			System.out
					.println("Please provide valid value for approval prompt value");
			invalidProps.add(OAuthConstants.APPROVAL_PROMPT_VALUE);
		}

		if (!isValid(input.getApprovalPromptKey())) {
			System.out
					.println("Please provide valid value for approval prompt key");
			invalidProps.add(OAuthConstants.APPROVAL_PROMPT_KEY);
		}

		if (!isValid(input.getRedirectURI())) {
			System.out.println("Please provide valid value for redirect uri");
			invalidProps.add(OAuthConstants.REDIRECT_URI);
		}

		return invalidProps;

	}

	public static String getHeader(Header[] headers, String name) {

		String header = null;
		if (headers != null) {
			for (Header h : headers) {
				if (h.getName().equalsIgnoreCase(name)) {
					header = h.getValue();
					break;
				}
			}
		}

		return header;

	}

	public static boolean isValid(String str) {
		return (str != null && str.trim().length() > 0);
	}
	
	public static boolean isIDTokenValid(String str){
		
		boolean flag = false;
		
		return flag;
		
	}
	
	public static String decodeJWT(String token){
//		String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXUyJ9.eyJpc3MiOiJhdXRoMCJ9.AbIJTDMFc7yUa5MhvcP03nJPyCPzZtQcGEp-zWfOkEE";
		try {
		    JWT jwt = JWT.decode(token);
		   
		    String alg = jwt.getAlgorithm();
		    logger.info("alg = "+alg);
		    String iss = jwt.getIssuer();
		    logger.info("iss = "+iss);
		    
		    String kid = jwt.getKeyId();
		    logger.info("kid = "+kid);
		    List<String> aud = jwt.getAudience();
		    logger.info("aud = "+aud);
		    Claim claims = jwt.getClaim("groups");
		    String a = claims.asString();
		    logger.info("claims = "+a);
		    
		    
		} catch (JWTDecodeException exception){
		    //Invalid token
		}
		return "";
	}

}
