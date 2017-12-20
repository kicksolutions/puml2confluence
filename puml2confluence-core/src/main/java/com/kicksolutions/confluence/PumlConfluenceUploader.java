package com.kicksolutions.confluence;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.kicksolutions.confluence.vo.ConfluenceVo;

/**
 * MSANTOSH
 *
 */
public class PumlConfluenceUploader {
	private static final Logger LOGGER = Logger.getLogger(PumlConfluenceUploader.class.getName());
	
	public PumlConfluenceUploader() {
		super();
	}
		
	/**
	 * Title: 
	 * | 
	 * |----- V1.0 Title
	 * | 
	 * |----- V1.1 Title
	 * 
	 * 
	 * @param specFile
	 * @param parentPageID
	 * @param userName
	 * @param password
	 * @param confluenceURL
	 * @param spaceKey
	 * @param htmlDocumentationURL 
	 * @param clientkitURL 
	 * @throws IOException 
	 */
	public void processPuml2Confluence(String specFile, String parentPageID, String userName, String password,
			String confluenceURL, String spaceKey,String title) throws IOException {
		
		File pumlFile = new File(specFile);
		
		if(pumlFile.isFile()){
			
			String pumlContents = FileUtils.readFileToString(pumlFile, "UTF-8");
			
			if(StringUtils.isNotEmpty(pumlContents) 
					&& pumlContents.contains("@startuml")){
				String swaggerPageContent =  plantUMLMacroContent(pumlContents);
				
				LOGGER.log(Level.INFO, "About to generate Page -->" + title);
				
				ConfluenceVo parentPageVo = createSwaggerPage(new ConfluenceVo(userName, password, confluenceURL, "",
						parentPageID, "", title, "0", swaggerPageContent, spaceKey, false));
				
				LOGGER.log(Level.INFO, "Done.... by generating Pages "+ parentPageVo.getPageID());
			}
			else{
				throw new RuntimeException("Puml File provided in input is not valid");
			}
		}
		else{
			throw new RuntimeException("Puml File provided in input is not valid");
		}
	}
		
	/**
	 * 
	 * @param swaggerLoctaion
	 * @param htmlDocumentationURL 
	 * @param clientkitURL 
	 * @return
	 */
	private String plantUMLMacroContent(String pumlContents) {
		
		StringBuilder macroString = new StringBuilder();
		macroString.append("<ac:structured-macro ac:name=\"plantuml\" ac:schema-version=\"1\" ac:macro-id=\"29f50cc-3338-43cc-9f67-15a851eb3417\">")
		.append("<ac:parameter ac:name=\"atlassian-macro-output-type\">INLINE</ac:parameter>")
		.append("<ac:plain-text-body><![CDATA[")
		.append(addEscapeChars(pumlContents))
		.append("]]></ac:plain-text-body></ac:structured-macro>");
						
		return macroString.toString();
	}
	
	/**
	 * 
	 * @param inputString
	 * @return
	 */
	private String addEscapeChars(String inputString){
		return inputString;
	}
	
	/**
	 * 
	 * @param title
	 * @return
	 */
	private ConfluenceVo createSwaggerPage(ConfluenceVo confluenceVo) {
		ConfluenceUtils confluenceUtils = new ConfluenceUtils();

		if (!confluenceUtils.isPageExists(confluenceVo)) {
			LOGGER.log(Level.INFO, "Page Doesn't Exists, so Creating Page");
			String pageID = confluenceUtils.createPage(confluenceVo);
			confluenceVo.setPageID(pageID);
		} else {
			LOGGER.log(Level.INFO, "Page Exists, so Updating Page");
			String pageID = confluenceUtils.updatePage(confluenceVo);
			confluenceVo.setPageID(pageID);
		}

		return confluenceVo;
	}
}