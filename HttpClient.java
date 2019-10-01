package com.computerNetwork;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

class RequestDetails{
	String requestType;
	String host;
	Integer port;
	String urlFull;
	Boolean verbose;
	String inlineData;
	
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getUrlFull() {
		return urlFull;
	}
	public void setUrlFull(String urlFull) {
		this.urlFull = urlFull;
	}
	public Boolean getVerbose() {
		return verbose;
	}
	public void setVerbose(Boolean verbose) {
		this.verbose = verbose;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getInlineData() {
		return inlineData;
	}
	public void setInlineData(String inlineData) {
		this.inlineData = inlineData;
	}
	
}

public class HttpClient {
    public static void main(String[] args) throws Exception{
    	RequestDetails requestDetails = new RequestDetails();
    	requestDetails.setVerbose(false);
    	requestDetails.setHost("www.httpbin.org");
    	requestDetails.setRequestType("POST");
    	requestDetails.setPort(80);
    	requestDetails.setUrlFull("http://httpbin.org/post");
    	requestDetails.setInlineData("{\"Assignment\": 1}");
    	HttpClient hc = new HttpClient();
    	hc.printResult(requestDetails, hc.getResponseFromHttpCall(requestDetails));

        
    }
    
    public InputStream getResponseFromHttpCall(RequestDetails requestDetails) {
    	String defaultContentType = "Content-Type:application/json";
        
    	try {
    		InetAddress inetAddress = InetAddress.getByName(requestDetails.getHost());
            //SocketAddress socketAddress = new InetSocketAddress(inetAddress, requestDetails.getPort());
            Socket socket = new Socket(inetAddress, requestDetails.getPort());
            
            //sending request
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedWriter.write(requestDetails.getRequestType() + " " + requestDetails.getUrlFull()+ " HTTP/1.0\r\n");
            bufferedWriter.write("Host:" + requestDetails.getHost() + "\r\n");
            //temporary
            if("GET".equalsIgnoreCase(requestDetails.getRequestType())){
            	bufferedWriter.write(defaultContentType);
            }
            if ("POST".equalsIgnoreCase(requestDetails.getRequestType()) && requestDetails.getInlineData() != null) {
            	bufferedWriter.write("Content-Length:" + requestDetails.getInlineData().length() + "\r\n");
            	
            	//temporary
            	
            	bufferedWriter.write(defaultContentType+"\r\n");
            	//bufferedWriter.write(requestDetails.getInlineData()+"\r\n");
            	bufferedWriter.write("User-Agent:Concordia-HTTP/1.0\r\n");
            	//bufferedWriter.write("User-Agent:" + "Concordia-HTTP/1.0" + "\r\n");
            }
            bufferedWriter.write("\r\n");
            bufferedWriter.write(requestDetails.getInlineData());
            //bufferedWriter.write("");
            bufferedWriter.flush();
            
			//bufferedWriter.close();
            //socket.close();
            return socket.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }
    
    public void printResult(RequestDetails requestDetails, InputStream result) {
    	BufferedReader br = new BufferedReader(new InputStreamReader(result));
    	String resultLine;
    	try {
			while((resultLine=br.readLine())!=null){
				if(resultLine.contains("{")) {
					requestDetails.setVerbose(true);
				}
			    if(requestDetails.getVerbose()) {
			    	System.out.println(resultLine);
			    }
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.print("Error in parsing result");
		}
    	
    }
}