package chatty.view;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;
import chatty.controller.FileTransferManager;

public class FileTransferHandler extends TransferHandler {
  /**
   * 
   */
  private static final long serialVersionUID = -3303002366889552217L;

  private FileDropSource fileDropSource;
  private DataFlavor uriListFlavor;
  private FileTransferManager fileSender;

  public FileTransferHandler(FileDropSource fileDropSource) {
    this.fileDropSource = fileDropSource;
    try {
      uriListFlavor = new DataFlavor("text/uri-list;class=java.lang.String");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  public void setHandler(FileTransferManager ftm) {
    fileSender = ftm;
  }

  @Override
  public boolean canImport(TransferSupport support) {
    return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)
        || support.isDataFlavorSupported(uriListFlavor);
  }

  @Override
  public boolean importData(TransferSupport support) {
    if (canImport(support)) {
      try {
        File file = null;
        if (support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
          @SuppressWarnings("unchecked")
          List<File> fileList = (List<File>) support.getTransferable()
              .getTransferData(DataFlavor.javaFileListFlavor);
          if (fileList.size() > 0) {
            file = fileList.get(0);
          }
        } else if (support.isDataFlavorSupported(uriListFlavor)) {
          Object data = support.getTransferable()
              .getTransferData(uriListFlavor);
          if (data != null) {
            String[] uriList = data.toString().split("\r\n");
            String fileURI = "";
            for (int i = 0; i < uriList.length; i++) {
              if (uriList[i].startsWith("file:/")) {
                fileURI = uriList[i];
                break;
              }
            }
            try {
              URI uri = new URI(fileURI);
              if (uri != null) {
                file = new File(uri);
              }
            } catch (URISyntaxException e) {
              e.printStackTrace();
            }
          }
        } else {
          System.out.println("Not supported.");
        }
        if (file != null) {
          fileSender.sendFile(fileDropSource.getUserName(), file);
          return true;
        } else {
          System.out.println("No file dropped.");
        }
      } catch (UnsupportedFlavorException e) {
        e.printStackTrace();
      } catch (final IOException e) {
        e.printStackTrace();
      }
    }
    return false;
  }

  @Override
  protected Transferable createTransferable(JComponent c) {
    if (c instanceof JList) {
      String data = ((JList) c).getSelectedValue().toString();
      return new StringSelection(data);
    } else {
      return null;
    }
  }
}
