package org.md.spider;


import java.net.MalformedURLException;
import java.nio.channels.FileChannel;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.apache.jmeter.protocol.http.parser.HtmlParsingUtils;
import org.apache.jmeter.protocol.http.sampler.HTTPSampleResult;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerBase;
import org.apache.jmeter.protocol.http.util.ConversionUtils;
import org.apache.jmeter.protocol.http.util.HTTPConstants;
import org.apache.log.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class JMeterSpiderUtil {
	private static Pattern pattern = Pattern.compile(";jsessionid[^?]*\\?", Pattern.CASE_INSENSITIVE);
	private static Pattern tstampPattern = Pattern.compile("tstamp[^&]*&", Pattern.CASE_INSENSITIVE);
	private static Pattern pathPattern = Pattern.compile("jmeter", Pattern.CASE_INSENSITIVE);
	private static Pattern extnPattern = Pattern.compile("(\\.jmx)|(\\.pdf)", Pattern.CASE_INSENSITIVE);
	public static void extractAndWriteLinksToFile(String tempDir,String nextLevel,
			HTTPSampleResult result, Logger log, String allowedHost) throws IOException{
		BufferedWriter fw = null;
		try {
			String fetchedUrl = result.getUrlAsString();
			System.out.println("checking " + fetchedUrl);
			if(isExcluded(fetchedUrl) ){
				//dont try to fetch urls
				return;
			}
			String finalDir = getTempDir(tempDir, nextLevel);
			File dir = new File(finalDir);
			dir.mkdirs();
			File f = File.createTempFile("extractedurl", ".csv", new File(finalDir));
			fw = new BufferedWriter(new FileWriter(f));
			String responseText = ""; // $NON-NLS-1$
			responseText = result.getResponseDataAsString();
			Document html;
			int index = responseText.indexOf("<"); // $NON-NLS-1$
			if (index == -1) {
				index = 0;
			}
			html = (Document) HtmlParsingUtils
					.getDOM(responseText.substring(index));
	
			String base = "";
			NodeList baseList = html.getElementsByTagName("base"); // $NON-NLS-1$
			if (baseList.getLength() > 0) {
				base = baseList.item(0).getAttributes().getNamedItem("href")
						.getNodeValue(); // $NON-NLS-1$
			}
			NodeList nodeList = html.getElementsByTagName("a"); // $NON-NLS-1$
	
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node tempNode = nodeList.item(i);
				NamedNodeMap nnm = tempNode.getAttributes();
				Node namedItem = nnm.getNamedItem("href"); // $NON-NLS-1$
				if (namedItem == null) {
					continue;
				}
				String hrefStr = namedItem.getNodeValue();
				if (hrefStr.startsWith("javascript:")) { // $NON-NLS-1$
					continue; // No point trying these
				}
				try {
					HTTPSamplerBase newUrl = HtmlParsingUtils.createUrlFromAnchor(
							hrefStr, ConversionUtils.makeRelativeURL(result
									.getURL(), base));
					newUrl.setMethod(HTTPConstants.GET);
					
					if(allowedHost.equalsIgnoreCase(newUrl.getDomain())) {
						String currUrl = newUrl.getUrl().toString();
						if(matchesPath(currUrl)) {
							//currUrl = stripSessionId(currUrl);
							//currUrl = stripTStamp(currUrl);
							fw.write(currUrl + "\n");
						}
						
					}
				} catch (MalformedURLException e) {
					log.warn("Bad URL " + e);
				}
			}
		} finally {
			if(fw != null) {
				fw.close();
			}
		}
	}
	
	public static void combineAndWriteFile(String tempDir,String nextLevel, String urlDir) throws IOException{
		//just read all urls into a set, and write the set.if memory was a problem we'd probably individually sort each file
		//then deal with multiple files at a time (but only keeping their current record in memory to write new file) 
		// we now have all the candidate urls, but we need to subtract those urls that we have already processed
		try {
			String finalDir = getTempDir(tempDir, nextLevel);		
			Set<String> set = populateSet(finalDir);
			Set<String> processedSet = populateSet(urlDir);
			set.removeAll(processedSet);
			BufferedWriter bw = null;
			try {
				bw = new BufferedWriter(new FileWriter(new File(getUrlFile(urlDir, nextLevel))));
				for(String url : set){
					bw.write(url + "\n");
				}
			} finally {
				if(bw != null){
					bw.close();
				}
			}
		} catch(Throwable t){
			t.printStackTrace();
		}
	}
	
	private static String getUrlFile(String urlDir, String nextLevel){
		return urlDir + "/L_" + nextLevel + ".csv";
	}
	private static String getTempDir(String tempDir,String nextLevel){
		return tempDir + "/L_" + nextLevel;
	}
	private static boolean matchesPath(String currUrl){
		return pathPattern.matcher(currUrl).find();
	}
	
	
	private static Set<String> populateSet(String dir) throws IOException{
		Set<String> set = new TreeSet<String>();

		File f = new File(dir);
		File[] extractedUrlFiles = f.listFiles();
		for(int i =0;i<extractedUrlFiles.length;i++){
			BufferedReader br = null;
			try {
				br = new BufferedReader(new InputStreamReader(new FileInputStream(extractedUrlFiles[i])));
				String url = null;
				while((url = br.readLine()) != null){
					set.add(url);
				}
			} finally {
				if(br != null){
					br.close();
				}
			}
		}
		return set;
	}
	
	private static String stripSessionId(String in){
		return pattern.matcher(in).replaceAll("?");
	}
	private static String stripTStamp(String in){
		return tstampPattern.matcher(in).replaceAll("");
	}
	private static boolean isExcluded(String in){
		return extnPattern.matcher(in).find();
	}
	
	public static void main(String[] args) throws Exception {
		

		System.out.println(stripSessionId("https://idevevn.bio-rad.com/evportal/evolutionPortal.portal"));
		System.out.println(stripSessionId("https://idevevn.bio-rad.com/evportal/evolutionPortal.portal;JSESSIONID_EVPORTAL=38fhK0NhLQFcD5WT2xGzKpF6DgZgkPV2x89h2Vk1qvs0GBGhbccs!548063601?_nfpb=true&_pageLabel=myBioRad_Page"));
		System.out.println(stripSessionId("https://idevevn.bio-rad.com/evportal/evolutionPortal.portal?_nfpb=true&_pageLabel=NewsEventsLandingPage&catID=1135"));
		System.out.println(stripTStamp("https://idevevn.bio-rad.com/idev/en/US/adirect/biorad?ts=1&tstamp=1257542776010&cmd=BRUserPromotionList&catID=1100&vertical=LSR"));
		System.out.println(matchesPath("http://jakarta.apache.org/site/downloads/"));
		System.out.println(matchesPath("http://jakarta.apache.org/jmeter/index.html"));
		System.out.println(isExcluded("http://www.jakart.apache.org/index.html"));
		System.out.println(isExcluded("http://www.jakart.apache.org/index.pdf"));
	}
	
	public static void copyFile(File sourceFile, File destFile) throws IOException {
		try {
		 if(!destFile.exists()) {
		  destFile.createNewFile();
		 }
		 
		 FileChannel source = null;
		 FileChannel destination = null;
		 try {
		  source = new FileInputStream(sourceFile).getChannel();
		  destination = new FileOutputStream(destFile).getChannel();
		  destination.transferFrom(source, 0, source.size());
		 }
		 finally {
		  if(source != null) {
		   source.close();
		  }
		  if(destination != null) {
		   destination.close();
		  }
		}
		} catch(Throwable t) {
			t.printStackTrace();
		}
	}
	
	private static void test(Properties props, String currLevel) throws IOException{
		JMeterSpiderUtil.copyFile(new File(props.getProperty("url.dir") + "/L_" + currLevel), new File(props.getProperty("url.dir") + "/urls.csv"));
	}
	
}
