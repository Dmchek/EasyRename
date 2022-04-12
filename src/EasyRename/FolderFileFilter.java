package EasyRename;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by DM5 on 01.03.2020.
 */
public class FolderFileFilter implements FilenameFilter {

    public boolean accept(File dir, String name) {
        return name.endsWith(".pdf") || name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".jpeg") || name.endsWith(".bmp") || name.endsWith(".xlsx") || name.endsWith(".xlsb") || name.endsWith(".xltx") || name.endsWith(".xls") || name.endsWith("9");
    }
}
