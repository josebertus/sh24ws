package com.sh24;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringEscapeUtils;

import com.sun.org.apache.xml.internal.security.utils.Base64;
//import java.util.Base64;


public class LLamadaServicio {


	public String request ( String pUrl, String pUsuario, String pPwd, String pXml)   {
		
		//System.out.println("victor >>>>>>>>>>>>>>>>>>>>>> Vamos a enviar."+ pXml);
		
		String resultado = "";
		
        HttpClient httpClient = new HttpClient();
        //System.out.println("Vamos a enviar2.");
        BufferedReader br = null;
    	String authorization = "";		
        PostMethod methodPost = new PostMethod(pUrl);
        //System.out.println("Vamos a enviar.3");
        //System.out.println("1.Entramos a llamada Servicio: xml:"+ pXml);
        try {
			methodPost.setRequestEntity(new StringRequestEntity(pXml, "text/xml", "Windows-1252"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			System.out.println("Error al requesty entity");
			e1.printStackTrace();
		}
        //System.out.println("Vamos a enviar.4");
        methodPost.setRequestHeader("Content-Type", "text/xml");
        
        

        //System.out.println("if usuario XML:"+pXml);
		if (pUsuario!= null && pPwd!= null) {
			 
			 System.out.println("pusuario:"+pUsuario+" pPwd"+ pPwd );
             authorization = pUsuario+ ":" + pPwd;
   	         String encodedAuth="Basic "+Base64.encode(authorization.getBytes());
   	         methodPost.setRequestHeader("Authorization", encodedAuth);
			 
			/* System.out.println("pusuario:"+pUsuario+" pPwd"+ pPwd );
             authorization = pUsuario+ ":" + pPwd;
   	         //String encodedAuth="Basic "+Base64.Encoder(authorization.getBytes());
   	         String basicAuth = "Basic " + new String(Base64.getEncoder().encode(authorization.getBytes()));
   	         methodPost.setRequestHeader("Authorization", basicAuth);*/
        }
		
        try {
            int returnCode = httpClient.executeMethod(methodPost);
            if (returnCode == HttpStatus.SC_NOT_IMPLEMENTED) {
                //System.out.println("The Post method is not implemented by this URI");
                methodPost.getResponseBodyAsString();
            } else {
            	//System.out.println("Recuperamos los datos5");
                br = new BufferedReader(new InputStreamReader(methodPost.getResponseBodyAsStream()));
                String readLine;
                while (((readLine = br.readLine()) != null)) {
                    resultado += readLine;
                    //System.out.println("Readline:"+readLine);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        } finally {
            methodPost.releaseConnection();
            if (br != null)             try {
                    br.close();
                } catch (Exception fe) {
                    fe.printStackTrace();
                }
        }
        
 
        
        
        //resultado = "<![CDATA["+ resultado + "]]>";
        //System.out.println("Resultado:"+ resultado);
        
        
        return StringEscapeUtils.unescapeXml(resultado);
        
		
        
	}
		
	    		
}
