package ExternalLib;

import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;

public final class Delphi {


	public interface lib extends StdCallLibrary {
		public lib INSTANCE = (lib)Native.loadLibrary("dellib", lib.class);

		public int MB_ICONHAND = 0x00000010;	
		public int MB_ICONQUESTION = 0x00000020;
		public int MB_ICONEXCLAMATION = 0x00000030;
		public int MB_ICONASTERISK = 0x00000040;
		public int MB_USERICON = 0x00000080;
		
		void MessageBox(String lpText, int utype);
		void CopyFile(String lpExistingFileName, String lpNewFileName);
		void DeleteFile(String lpFileName);
		void ShellExecute(String URL);
		String ShowUploadForm(String sContentIndex);
		String idHTTPGet(String URL);
		boolean ExitsFiles(String lpFileName);
		
		
	}
	
	
	
}
