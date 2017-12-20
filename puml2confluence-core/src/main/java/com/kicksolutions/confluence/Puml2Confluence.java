package com.kicksolutions.confluence;

import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;

import com.kicksolutions.CliArgs;

/**
 * MSANTOSH
 *
 */
public class Puml2Confluence {
	private static final Logger LOGGER = Logger.getLogger(Puml2Confluence.class.getName());
	private static final String USAGE = new StringBuilder()
			.append(" Usage: com.kicksolutions.confluence.Puml2Confluence <options> \n")
			.append(" -i <Path of Puml> ").append(" -a <Parent Page Id> ").append(" -u <User Name> ")
			.append(" -p <Password> ").append(" -l <Conflunce URL> ").append(" -s <Confluenec Space key>")
			.append(" -t <Title of Page>").toString();

	public Puml2Confluence() {
		super();
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Puml2Confluence swagger2Confluence = new Puml2Confluence();
		swagger2Confluence.init(args);
	}

	/**
	 * 
	 * @param args
	 */
	private void init(String args[]) {
		CliArgs cliArgs = new CliArgs(args);
		String specFile = cliArgs.getArgumentValue("-i", "");
		String parentPageID = cliArgs.getArgumentValue("-a", "");
		String userName = cliArgs.getArgumentValue("-u", "");
		String password = cliArgs.getArgumentValue("-p", "");
		String confluenceURL = cliArgs.getArgumentValue("-l", "");
		String confluenceSpaceKey = cliArgs.getArgumentValue("-s", "");
		String title = cliArgs.getArgumentValue("-t", "");

		if (StringUtils.isNotEmpty(specFile) && StringUtils.isNotEmpty(parentPageID) && StringUtils.isNotEmpty(userName)
				&& StringUtils.isNotEmpty(password) && StringUtils.isNotEmpty(confluenceURL)
				&& StringUtils.isNotEmpty(confluenceSpaceKey)) {
			processSwagger2Confluence(specFile, parentPageID, userName, password, confluenceURL, confluenceSpaceKey,
					title);
		} else {
			LOGGER.severe(USAGE);
		}
	}

	/**
	 * 
	 * @param specFile
	 * @param parentPageID
	 * @param userName
	 * @param password
	 * @param confluenceURL
	 * @param spaceKey
	 * @param htmlDocumentationURL
	 * @param clientkitURL
	 */
	private void processSwagger2Confluence(String specFile, String parentPageID, String userName, String password,
			String confluenceURL, String spaceKey, String title) {

		try{
			PumlConfluenceUploader confluenceUploader = new PumlConfluenceUploader();
			confluenceUploader.processPuml2Confluence(specFile, parentPageID, userName, password, confluenceURL, spaceKey,
					title);
		}catch (Exception e){
			throw new RuntimeException(e);
		}
	}
}