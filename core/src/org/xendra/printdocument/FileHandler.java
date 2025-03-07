package org.xendra.printdocument;

import java.io.IOException;

import org.compiere.util.CLogger;
import org.simoes.util.FileUtil;

/**
 * Handles PrintJobs by writing them to File.
 * 
 * @author Chris Simoes
 */
public class FileHandler implements HandlerInterface {
	static CLogger log = CLogger.getCLogger(FileHandler.class);

	public FileHandler() {
		super();
	}
	
	/**
	 * Writes the printJob to disk using the jobName and jobId.
	 * @param printJob the PrintJob we are processing
	 * @return the result of our work, true for success or false for non-success
	 */
	//public boolean process(PrintJob printJob, String Format) {
	public boolean process(PrintJob printJob) {
		final String METHOD_NAME = "process(): ";
		boolean result = false;
//		if(null != printJob 
//			&& null != printJob.getControlFile()
//			&& null != printJob.getDataFile()) 
//		{
//			// create file name, pjb == print job
//			String fileName = printJob.getName() + printJob.getControlFile().getJobNumber() + ".pjb";
//			try { 
//				FileUtil.writeFile(printJob.getDataFile().getContents(), fileName);
//				result = true;
//			} catch(IOException e) {
//				log.error(METHOD_NAME + e.getMessage());
//			}
//		} else {
//			log.error(METHOD_NAME + "The printJob or printJob.getControlFile() or printJob.getDataFile() were empty");
//		}
		return result;
	}

	@Override
	public String getError() {
		// TODO Auto-generated method stub
		return null;
	}

}
