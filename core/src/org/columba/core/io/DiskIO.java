//The contents of this file are subject to the Mozilla Public License Version 1.1
//(the "License"); you may not use this file except in compliance with the
//License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
//
//Software distributed under the License is distributed on an "AS IS" basis,
//WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
//for the specific language governing rights and
//limitations under the License.
//
//The Original Code is "The Columba Project"
//
//The Initial Developers of the Original Code are Frederik Dietz and Timo Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003.
//
//All Rights Reserved.

package org.columba.core.io;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.logging.Logger;

import org.compiere.model.MConfig;
import org.compiere.util.CLogger;

/**
 * Utility methods for handling files and directories.
 */
public final class DiskIO {

	private static final CLogger LOG = CLogger.getCLogger("org.columba.core.io");

	private static String resourceFolder = "";

	/**
	 * Private constructor for utility class.
	 */
	private DiskIO() {
		// don't instantiate this
	}

	/**
	 * Ensures the existence of the directory specified by the parameter. If the
	 * directory does not exist, an attempt is performed to create it including
	 * all necessary parent directories that may be implied by the
	 * specification. ### HELPME : against what should a relative pathname be
	 * made absolute? ### ### We need to set the installation directory
	 * somewhere! ####
	 * 
	 * @param dir
	 *            File specifying the intended directory name; if the specified
	 *            name is a relative path, it is always made absolute against
	 *            the program's <b>installationDirectory </b>.
	 * @return <b>true </b> if and only if the specified file exists and is a
	 *         directory
	 */
	public static boolean ensureDirectory(File dir) {
		if (dir == null) {
			throw new IllegalArgumentException("dir = null");
		}
		boolean success = true;
		if (!dir.isDirectory()) {
			success = !dir.isFile() && dir.mkdirs();

			if (success) {
				LOG.info("Created directory: " + dir.toString());
			} else {
				LOG.severe("failed while trying to create directory: "
						+ dir.toString());
			}
		}

		return success;
	}

	// ensureDirectory
	public static boolean ensureDirectory(String path) {
		return ensureDirectory(new File(path));
	}

	public static void saveStringInFile(File toFile, String insertString)
			throws IOException {
		BufferedWriter out;

		out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
				toFile), "ISO-8859-1"));
		out.write(insertString);
		out.flush();
		out.close();
	}

	public static void saveStreamInFile(File toFile, InputStream is)
			throws IOException {

		BufferedOutputStream out = new BufferedOutputStream(
				new FileOutputStream(toFile));

		StreamUtils.streamCopy(is, out);

		out.flush();
		out.close();

		is.close();
	}

	// saveStringInFile
	public static String readFileInString(File fromFile) throws IOException {
		StringBuffer strbuf = new StringBuffer((int) fromFile.length());

		BufferedReader in = new BufferedReader(new InputStreamReader(
				new FileInputStream(fromFile), "ISO-8859-1"));
		String str;
		strbuf = new StringBuffer((int) fromFile.length());

		while ((str = in.readLine()) != null) {
			strbuf.append(str + "\n");
		}

		in.close();

		return strbuf.toString();

		/*
		 * int lineNumber = 0; byte[] buffer = new byte[1024]; int read;
		 * StringBuffer out = new StringBuffer((int)fromFile.length());
		 * FileInputStream in = new FileInputStream( fromFile );
		 * 
		 * read = in.read(buffer); while ( read == 1024 ) { out.append(new
		 * String(buffer,"ISO-8859-1")); read = in.read(buffer); }
		 * 
		 * out.append(new String(buffer,0,read,"ISO-8859-1")); in.close();
		 * 
		 * return out.toString();
		 */
	}

	// saveStringInFile

	/**
	 * Deletes the directory specified by the parameter and all of its contents.
	 * This does recurse into subdirectories. Function reports errors. If the
	 * parameter does not denote a directory, <b>false </b> is always returned.
	 * 
	 * @param dir
	 *            a File representing the directory to be delete
	 * @return <b>true </b> if and only if the directory does not exist on
	 *         termination; a return value <b>false </b> does not imply that
	 *         there were no files deleted
	 * @throws IllegalArgumentException
	 *             if the parameter is <b>null </b>
	 */
	public static boolean deleteDirectory(File dir) {
		File[] files;
		File f;
		int i;
		boolean success = true;

		if (dir == null) {
			throw new IllegalArgumentException("dir = null");
		}

		if (!dir.isDirectory()) {
			return false;
		}

		files = dir.listFiles();

		for (i = 0; i < files.length; i++) {
			if ((f = files[i]).isDirectory()) {
				deleteDirectory(f);
			} else if (!f.delete()) {
				LOG.severe("*** failed to delete file " + f.getAbsolutePath());
			}
		}

		success = dir.delete();

		if (!success) {
			LOG.severe("*** failed to delete directory "
					+ dir.getAbsolutePath());
		}

		return success;
	}

	// deleteDirectory

	/**
	 * Deletes the contents of an existing directory. (May be applied to
	 * non-existing files without causing error.)
	 * 
	 * @return <b>true </b> if and only if on termination the directory exists
	 *         and is empty
	 */
	public static boolean emptyDirectory(File dir) {
		boolean success;

		if (dir == null) {
			throw new IllegalArgumentException("dir = null");
		}

		if ((success = dir.exists() && deleteDirectory(dir))) {
			dir.mkdirs();
		}

		return success && dir.exists();
	}

	// emptyDirectory

	/**
	 * General use columba resource InputStream getter.
	 * 
	 * @param path
	 *            the full path and filename of the resource requested. If
	 *            <code>path</code> begins with "#" it is resolved against the
	 *            program's standard resource folder after removing "#"
	 * @return an InputStream to read the resource data, or <b>null </b> if the
	 *         resource could not be obtained
	 * @throws java.io.IOException
	 *             if there was an error opening the input stream
	 */
	public static InputStream getResourceStream(String path)
			throws java.io.IOException {
		URL url;

		if ((url = getResourceURL(path)) == null) {
			return null;
		}
		return url.openStream();
	}

	// getResourceStream

	/**
	 * General use columba resource URL getter.
	 * 
	 * @param path
	 *            the full path and filename of the resource requested. If
	 *            <code>path</code> begins with "#" it is resolved against the
	 *            program's standard resource folder after removing "#"
	 * @return an URL instance, or <b>null </b> if the resource could not be
	 *         obtained
	 * @throws java.io.IOException
	 *             if there was an error opening the input stream
	 */
	public static URL getResourceURL(String path) // throws
	// java.io.IOException
	{
		URL url;

		if (path == null) {
			throw new IllegalArgumentException("path = null");
		}

		if (path.startsWith("#")) {
			path = resourceFolder + path.substring(1);
		}

		// url = ClassLoader.getSystemResource(path);
		url = DiskIO.class.getResource("/" + path);

		if (url == null) {
			LOG.info("*** failed locating resource: " + path);

			return null;
		}

		return url;
	}

	// getResourceURL
	public static void setResourceRoot(String path) {
		if (path == null) {
			resourceFolder = "";
		} else {
			if (!path.endsWith("/")) {
				path += "/";
			}

			resourceFolder = path;
		}
	}

	// setResourceRoot
	public static String getResourceRoot() {
		return resourceFolder;
	}

	/**
	 * Copies the contents of any disk file to the specified output file. The
	 * output file will be overridden if it exist. Function reports errors.
	 * 
	 * @param inputFile
	 *            a File object
	 * @param outputFile
	 *            a File object
	 * @throws java.io.IOException
	 *             if the function could not be completed because of an IO error
	 */
	public static void copyFile(File inputFile, File outputFile)
			throws java.io.IOException {
		FileInputStream in;
		FileOutputStream out;

		byte[] buffer = new byte[512];
		int len;

		try {
			out = new FileOutputStream(outputFile);
			in = new FileInputStream(inputFile);

			while ((len = in.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}

			in.close();
			out.close();
		} catch (IOException e) {
			LOG.info("*** error during file copy "
					+ outputFile.getAbsolutePath() + ": " + e.getMessage());
			throw e;
		}
	}

	// copyFile

	public static boolean copyResource(String resource, MConfig config)
		throws java.io.IOException {
		String line;
		StringBuffer sb = new StringBuffer();
		BufferedReader br = new BufferedReader(new InputStreamReader(DiskIO.getResourceStream(resource)));
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		config.setContent(sb.toString());		
		return config.save();
		
	}
	/**
	 * Copies a system resource to the specified output file. The output file
	 * will be overridden if it exist, so the calling routine has to take care
	 * about unwanted deletions of content. Function reports errors.
	 * 
	 * @param resource
	 *            a full resource path. If the value begins with "#", it is
	 *            resolved against the program's standard resource folder after
	 *            removing "#"
	 * @return <b>true </b> if and only if the operation was successful,
	 *         <b>false </b> if the resource was not found
	 * @throws java.io.IOException
	 *             if there was an IO error
	 */
	public static boolean copyResource(String resource, File outputFile)
			throws java.io.IOException {
		InputStream in;
		FileOutputStream out;
		byte[] buffer = new byte[512];
		int len;

		// attempt
		try {
			if ((in = DiskIO.getResourceStream(resource)) == null) {
				return false;
			}

			out = new FileOutputStream(outputFile);

			while ((len = in.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}

			out.close();
			in.close();

			LOG.fine("created : " + outputFile.getAbsolutePath());
		} catch (IOException e) {
			LOG.severe("*** error during resource file copy "
					+ outputFile.getAbsolutePath() + ": " + e.getMessage());
			throw e;
		}

		return true;
	}

	// copyResource
	public static String readStringFromResource(String resource)
			throws java.io.IOException {
		InputStream in;

		StringBuffer result = new StringBuffer();

		// attempt
		try {
			if ((in = DiskIO.getResourceStream(resource)) == null) {
				return "";
			}

			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));

			String nextLine = reader.readLine();

			while (nextLine != null) {
				result.append(nextLine);
				result.append("\n");
				nextLine = reader.readLine();
			}

			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}

		return result.toString();
	}

	/**
	 * Results equal
	 * <code>copyResource ( String resource, new File (outputFile) )</code>.
	 */
	public static boolean copyResource(String resource, String outputFile)
			throws java.io.IOException {
		return copyResource(resource, new File(outputFile));
	}
}