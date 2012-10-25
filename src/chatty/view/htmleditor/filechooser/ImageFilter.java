package chatty.view.htmleditor.filechooser;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class ImageFilter extends FileFilter {

  public boolean accept(File f) {
    if (f.isDirectory()) {
      return true;
    }

    String extension = Utilities.getExtension(f);
    if (extension != null) {
      if (extension.equals(Utilities.png) || extension.equals(Utilities.gif)
          || extension.equals(Utilities.jpeg)
          || extension.equals(Utilities.jpg)) {
        return true;
      } else {
        return false;
      }
    }
    return false;
  }

  public String getDescription() {
    return "Images (GIF, JPEG, PNG)";
  }
}
