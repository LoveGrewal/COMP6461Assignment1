package com.computerNetwork;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

class RequestDetails {
	String requestType;
	String host;
	Integer port;
	String urlFull;
	Boolean verbose;
	String inlineData;
	List<String> headers;
	String fileName;
	String getQueryParams;
	
	public String getGetQueryParams() {
		return getQueryParams;
	}

	public void setGetQueryParams(String getQueryParams) {
		this.getQueryParams = getQueryParams;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setHeaders(List<String> headers) {
		this.headers = headers;
	}

	public void addHeaders(String newHeaders) {
		if (headers == null) {
			headers = new ArrayList<>();
		}
		this.headers.add(newHeaders);
	}

	public List<String> getHeaders() {
		return headers;
	}

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

public class Httpc {
	public static void main(String[] args) throws Exception {
		RequestDetails requestDetails = new RequestDetails();
		/*
		 * requestDetails.setVerbose(false); requestDetails.setHost("www.httpbin.org");
		 * requestDetails.setRequestType("POST"); requestDetails.setPort(80);
		 * requestDetails.setUrlFull("http://httpbin.org/post");
		 * requestDetails.setInlineData("{\"Assignment\": 1}");
		 */
		Httpc hc = new Httpc();

		if (args.length > 0) {
			String firstArg = args[0].toUpperCase();
			switch (firstArg) {
			case "HELP":
				if (args.length == 1) {
					System.out.println("httpc is a curl-like application but supports HTTP protocol only.");
					System.out.println("Usage:");
					System.out.println("    httpc command [arguments]");
					System.out.println("The commands are:");
					System.out.println("    get executes a HTTP GET request and prints the response.");
					System.out.println("    post executes a HTTP POST request and prints the response.");
					System.out.println("    help prints this screen.");
					System.out.println("Use \"httpc help [command]\" for more information about a command.");
				} else if (args.length > 1 && args[1].equalsIgnoreCase("GET")) {
					System.out.println("httpc help get");
					System.out.println("usage: httpc get [-v] [-h key:value] URL");
					System.out.println("Get executes a HTTP GET request for a given URL.");
					System.out.println(
							"   -v             Prints the detail of the response such as protocol, status, and headers.");
					System.out.println(
							"   -h             key:value Associates headers to HTTP Request with the format 'key:value'.");
				} else if (args.length > 1 && args[1].equalsIgnoreCase("POST")) {
					System.out.println("httpc help post");
					System.out.println("usage: httpc post [-v] [-h key:value] [-d inline-data] [-f file] URL");
					System.out.println(
							"Post executes a HTTP POST request for a given URL with inline data or from file.");
					System.out.println(
							"   -v             Prints the detail of the response such as protocol, status, and headers.");
					System.out.println(
							"   -h             key:value Associates headers to HTTP Request with the format 'key:value'.");
					System.out.println(
							"   -d             string Associates an inline data to the body HTTP POST request.");
					System.out.println(
							"   -f             file Associates the content of a file to the body HTTP POST request.");
					System.out.println("Either [-d] or [-f] can be used but not both.");
				}
				break;
			case "GET":
			case "POST":
				try {
					hc.makeRequestObject(args, requestDetails);
					hc.printResult(requestDetails, hc.getResponseFromHttpCall(requestDetails));
				} catch (Exception e) {
					System.out.println("Invalid Command");
				}
				break;
			default:
				System.out.println("Invalid Command");
			}
		}

	}

	/**
	 * To create request object from parameters passed
	 *
	 * @param parameters parameters passed in arguments
	 */
	public void makeRequestObject(String[] parameters, RequestDetails newRequest) throws Exception {
		newRequest.setVerbose(false);

		for (int i = 0; i < parameters.length; i++) {
			if (parameters[i].equalsIgnoreCase("get") || parameters[i].equalsIgnoreCase("post")) {
				newRequest.setRequestType(parameters[i].toUpperCase());
				continue;
			}
			if (parameters[i].equalsIgnoreCase("-v")) {
				newRequest.setVerbose(true);
			}
			if (parameters[i].equalsIgnoreCase("-h")) {
				newRequest.addHeaders(parameters[i + 1]);
			}
			if (parameters[i].equalsIgnoreCase("-d")) {
				newRequest.setInlineData(parameters[i + 1].replace("'", " ").trim());
			}
			if (parameters[i].equalsIgnoreCase("-f")) {
				newRequest.setInlineData(readFromFile(parameters[i + 1]));
			}
			if (parameters[i].equalsIgnoreCase("-o")) {
				newRequest.setFileName(parameters[i + 1]);
			}
			String host = null;
			if (parameters[i].startsWith("'")) {
				newRequest.setUrlFull(parameters[i].replace("'", " ").trim());
				host = parameters[i].replace("'", " ");
				if (host.contains("https://")) {
					host = host.replace("https://", " ");
				} else if (host.contains("http://")) {
					host = host.replace("http://", " ");
				}
				int temp = host.length();
				if (host.contains("?")) {
					temp = host.indexOf("?");
					newRequest.setGetQueryParams(host.substring(temp, host.length()));
				}
				host = (host.substring(0, temp));
				temp = host.length();
				if (host.contains("/")) {
					temp = host.indexOf("/");
				}
				newRequest.setHost(host.substring(0, temp).trim());
			}

		}

		// default port
		newRequest.setPort(80);

	}

	public String readFromFile(String path) throws Exception {
		// This will reference one line at a time
		String line = null;
		String inlineData = null;
		try {
			// FileReader reads text files in the default encoding.
			FileReader fileReader = new FileReader(path);

			// Always wrap FileReader in BufferedReader.
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				if (inlineData == null)
					inlineData = line;
				else
					inlineData = inlineData + line;
			}
			// Always close files.
			bufferedReader.close();
			return inlineData;
		} catch (FileNotFoundException e) {
			System.out.println("Unable to open file '" + path + "'");
			e.printStackTrace();
			throw new Exception();
		} catch (IOException e) {
			System.out.println("Error reading file '" + path + "'");
			e.printStackTrace();
			throw new Exception();
		}
	}

	public void saveResultInFile(String result, String fileName) throws Exception {
		FileWriter file = new FileWriter(fileName + ".txt", true);
		BufferedWriter bufferedWriter = new BufferedWriter(file);
		bufferedWriter.append(result);
		bufferedWriter.close();

	}

	public InputStream getResponseFromHttpCall(RequestDetails requestDetails) {
		// String defaultContentType = "Content-Type:application/json";

		try {
			InetAddress inetAddress = InetAddress.getByName(requestDetails.getHost().trim());
			// SocketAddress socketAddress = new InetSocketAddress(inetAddress,
			// requestDetails.getPort());
			Socket socket = new Socket(inetAddress, requestDetails.getPort());

			// sending request
			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			bufferedWriter.write(requestDetails.getRequestType().trim() + " " + requestDetails.getUrlFull().trim()
					+ " HTTP/1.0\r\n");
			bufferedWriter.write("Host:" + requestDetails.getHost() + "\r\n");
			// temporary
			if ("GET".equalsIgnoreCase(requestDetails.getRequestType())) {
				// bufferedWriter.write(defaultContentType);
			}
			if ("POST".equalsIgnoreCase(requestDetails.getRequestType()) && requestDetails.getInlineData() != null) {
				bufferedWriter.write("Content-Length:" + requestDetails.getInlineData().length() + "\r\n");

				// temporary

				// bufferedWriter.write(defaultContentType + "\r\n");
				// bufferedWriter.write(requestDetails.getInlineData()+"\r\n");
				// bufferedWriter.write("User-Agent:Concordia-HTTP/1.0\r\n");
				// bufferedWriter.write("User-Agent:" + "Concordia-HTTP/1.0" + "\r\n");
			}
			if (requestDetails.getHeaders() != null && requestDetails.getHeaders().size() > 0) {
				for (int i = 0; i < requestDetails.getHeaders().size(); i++) {
					bufferedWriter.write(requestDetails.getHeaders().get(i).trim() + "\r\n");
				}
			}
			bufferedWriter.write("\r\n");
			if (requestDetails.getInlineData() != null) {
				bufferedWriter.write(requestDetails.getInlineData());
			}
			// bufferedWriter.write("");
			bufferedWriter.flush();

			// bufferedWriter.close();
			// socket.close();
			return socket.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public void redirectWithRequest(RequestDetails requestDetails, BufferedReader br) {
		String resultLine;
		
		try {
			while ((resultLine = br.readLine()) != null) {
				if (resultLine.contains("Location")) {
					resultLine = resultLine.split(":")[1].trim();
					String originalLocation = resultLine;
					int temp;
					//requestDetails.setUrlFull(urlFull); = textFromServer.substring(10);
					if (resultLine.contains("https://")) {
						resultLine = resultLine.replace("https://", " ");
					} else if (resultLine.contains("http://")) {
						resultLine = resultLine.replace("http://", " ");
					}
                    if (resultLine.contains("/")) {
    					temp = resultLine.indexOf("/");
    					resultLine = resultLine.substring(0, temp).trim();
    					if(resultLine != null && !resultLine.trim().isEmpty()) {
    						requestDetails.setHost(resultLine);
    					}
    				}
    				if(originalLocation.contains("?")) {
    					requestDetails.setUrlFull(originalLocation);
    				}else if(requestDetails.getQueryParams != null){
    					requestDetails.setUrlFull(originalLocation+requestDetails.getQueryParams); 
    				}
                    break;
                }
			}
			printResult(requestDetails, getResponseFromHttpCall(requestDetails));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.print("Error in parsing result");
		}
	}

	public void printResult(RequestDetails requestDetails, InputStream result) {
		BufferedReader br = new BufferedReader(new InputStreamReader(result));
		String resultLine;
		StringBuilder resultToDisplay = new StringBuilder("");
		boolean isRedirect = false;
		try {
			while ((resultLine = br.readLine()) != null) {
				if (resultLine.contains("HTTP") && (resultLine.contains("300") || resultLine.contains("301") || resultLine.contains("302")
						|| resultLine.contains("303") || resultLine.contains("304") || resultLine.contains("305")
						|| resultLine.contains("306") || resultLine.contains("307") || resultLine.contains("308"))) {
					redirectWithRequest(requestDetails, br);
					isRedirect = true;
					break;
				}
				if (resultLine.contains("{")) {
					requestDetails.setVerbose(true);
				}
				if (requestDetails.getVerbose()) {
					resultToDisplay.append(resultLine + "\n");
				}
			}
			if (!isRedirect && requestDetails.getFileName() == null) {
				System.out.println(resultToDisplay.toString());
			} else if(!isRedirect) {
				saveResultInFile(resultToDisplay.toString(), requestDetails.getFileName());
				System.out.println("response saved in file");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.print("Error in parsing result");
		}

	}
}