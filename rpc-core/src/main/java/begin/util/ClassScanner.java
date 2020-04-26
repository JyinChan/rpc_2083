package begin.util;


/**
 * @author C172
 *  消息扫描器
 */
public class ClassScanner {
	public String[] scannerPackage(String namespace, String ext) {
		return new PackageScaner().scanNamespaceFiles(namespace, ext, false, true);
	}
	
}
