/*
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 *    WekaPackageManager.java
 *    Copyright (C) 2009-2012 University of Waikato, Hamilton, New Zealand
 */

package com.pythia.kernelestimator.weka.core;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


/**
 * Class providing package management and manipulation routines. Also provides a
 * command line interface for package management.
 * 
 * @author Mark Hall (mhall{[at]}pentaho{[dot]}com)
 * @version $Revision: 9875 $
 */
public class WekaPackageManager {

  private static String WEKAFILES_DIR_NAME = "wekafiles";
  public static File WEKA_HOME = new File(System.getProperty("user.home")
      + File.separator + WEKAFILES_DIR_NAME);
  public static File PACKAGES_DIR = new File(System.getProperty("user.home")
      + File.separator + WEKAFILES_DIR_NAME + File.separator + "packages");

  private static String PROPERTIES_DIR_NAME = "props";
  public static File PROPERTIES_DIR = new File(WEKA_HOME.toString()
      + File.separator + PROPERTIES_DIR_NAME);

  private static URL REP_URL; // current repository URL to use
  private static URL CACHE_URL;
  private static boolean INITIAL_CACHE_BUILD_NEEDED = false;
  private static String PACKAGE_LIST_FILENAME = "packageListWithVersion.txt";

  private static String PRIMARY_REPOSITORY = "http://weka.sourceforge.net/packageMetaData";
  private static String REP_MIRROR;
  private static boolean USER_SET_REPO = false;

  private static String PACKAGE_MANAGER_PROPS_FILE_NAME = "PackageManager.props";
  private static Properties m_generalProps;

  public static boolean m_offline;
  private static boolean m_loadPackages = true;

  protected static boolean m_wekaHomeEstablished;
  protected static boolean m_packagesLoaded;
  public static boolean m_initialPackageLoadingInProcess = false;
  public static boolean m_noPackageMetaDataAvailable;

 
  }
