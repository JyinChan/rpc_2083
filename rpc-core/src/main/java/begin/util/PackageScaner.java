package begin.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.net.www.protocol.file.FileURLConnection;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 包扫描器
 * @author C172
 *
 */
public final class PackageScaner
{
	/**
	 * Log for this class
	 */
	public final Logger logger = LoggerFactory.getLogger(PackageScaner.class);

	public String[] scanNamespaceFiles(String namespace, String fileext,boolean isReturnCanonicalPath){
		return scanNamespaceFiles(namespace, fileext, isReturnCanonicalPath , false);
	}

	public String[] scanNamespaceFiles(String namespace, String fileext,boolean isReturnCanonicalPath, boolean checkSub)
	{
		logger.debug("namespace,{}",namespace);
		String respath = namespace.replace('.', '/');
		respath = respath.replace('.', '/');

		List<String> tmpNameList = new ArrayList<String>();
		try
		{
			URL url;
			logger.info("scan url path " + respath);
			if (!respath.startsWith("/")) {
				url = PackageScaner.class.getResource('/' + respath);
			} else {
				url = PackageScaner.class.getResource(respath);
			}

			URLConnection tmpURLConnection = url.openConnection();
			String tmpItemName;
			if (tmpURLConnection instanceof JarURLConnection)
			{
				JarURLConnection tmpJarURLConnection = (JarURLConnection) tmpURLConnection;
				int tmpPos;
				String tmpPath;
				JarFile jarFile = tmpJarURLConnection.getJarFile();
				Enumeration<JarEntry> entrys = jarFile.entries();				
				while (entrys.hasMoreElements())
				{
					JarEntry tmpJarEntry = entrys.nextElement();
					if (!tmpJarEntry.isDirectory())
					{
						tmpItemName = tmpJarEntry.getName();
						if (tmpItemName.indexOf('$') < 0
								&& (fileext == null || tmpItemName.endsWith(fileext)))
						{
							tmpPos = tmpItemName.lastIndexOf('/');
							if (tmpPos > 0)
							{
								tmpPath = tmpItemName.substring(0, tmpPos);
								if(checkSub){
									if (tmpPath.startsWith(respath))
									{
										
										String r = tmpItemName.substring(respath.length()+1).replaceAll("/", ".");
										tmpNameList.add(r);
									}	
								}else{
									if (respath.equals(tmpPath))
									{
										tmpNameList.add(tmpItemName.substring(tmpPos + 1));
									}
								}
								
							}
						}
					}
				}
				jarFile.close();
			}
			else if (tmpURLConnection instanceof FileURLConnection)
			{
				File file = new File(url.getFile());
				if (file.exists() && file.isDirectory())
				{
					File[] fileArray = file.listFiles();
					for (File f : fileArray)
					{
						if(f.isDirectory() && f.getName().indexOf('.') != -1) {
							continue;
						}
						
						if(isReturnCanonicalPath) {
							tmpItemName = f.getCanonicalPath();
						} else {
							tmpItemName = f.getName();
						}
						if(f.isDirectory()){
							String[] inner = scanNamespaceFiles(namespace + '.' + tmpItemName, fileext, isReturnCanonicalPath);
							if(inner == null){
								continue;
							}
							for(String i : inner){
								if(i!=null){
									tmpNameList.add(tmpItemName + '.' + i);
								}
							}
						}else if(fileext == null || tmpItemName.endsWith(fileext) )
						{
							tmpNameList.add(tmpItemName);
						}else{
							continue;// 明确一下，不符合要求就跳过
						}
					}
				}
				else
				{
					logger.error("scaning stop,invalid package path:" + url.getFile());
				}
			}
		}
		catch (Exception e)
		{
			logger.error("scaning stop,invalid package path error" + e.toString());
		}
		
		
		if (tmpNameList.size() > 0)
		{
			String[] r = new String[tmpNameList.size()];
			tmpNameList.toArray(r);
			tmpNameList.clear();
			return r;
		}
		return null;
	}

	
	public static void main(String[] args) throws IOException {

		PackageScaner scaner = new PackageScaner();
		String fileName[] = scaner.scanNamespaceFiles("begin.service.imp", ".class", false, true);

		for (String f : fileName) {
			System.out.println(f);
		}

		String s = PackageScaner.class.getResource("Log.class").openConnection().toString();

		System.out.println(s);


	}
}

