library dellib;


uses
  Winhttp_Tlb,
  ShellAPI,
  WinAPI.ActiveX,
  System.SysUtils,
  Windows,
  dialogs,
  System.Classes,
  UploadWeb in 'UploadWeb.pas' {Upload};

{$R *.res}


function MessageBoxTimeOut(hWnd: HWND; lpText: PChar; lpCaption: PChar; uType: UINT; wLanguageId: WORD; dwMilliseconds: DWORD): Integer; stdcall; external user32 name 'MessageBoxTimeoutA';

procedure MessageBox(const lpText: PAnsiChar; uType: DWORD); stdcall;
begin
  Windows.MessageBox(0, PWideChar(UTF8ToAnsi(lpText)), '', uType or MB_TOPMOST);
end;

procedure CopyFile(lpExistingFileName, lpNewFileName: PAnsiChar); stdcall;
begin
  if not DirectoryExists('C:\JTemp') then
    ForceDirectories('C:\JTemp');
  Windows.CopyFileA(lpExistingFileName, PAnsiChar(AnsiString('C:\JTemp\') + lpNewFileName), False);
end;

function ExitsFiles(lpFileName: PAnsiChar): Boolean; stdcall;
begin
  Result:= FileExists('C:\JTemp\' + lpFileName);
end;

procedure DeleteFile(const lpFileName: PAnsiChar); stdcall;
begin
  Windows.DeleteFileA(lpFileName);
end;

function ShowUploadForm(const sContentIndex: PAnsiChar): PAnsiChar; stdcall;
begin
  Result:= nil;
  if Upload = nil then
  begin
    MessageBoxTimeout(0, 'loading', '', 0, 0, 1) ;
    Upload:= TUpload.Create(nil);
    sWebContentIndex:= sContentIndex;
    Upload.ShowModal;
    Upload.Free;

    const len = Length(sWebContentFileName) + 1;
    Result:= CoTaskMemAlloc(SizeOf(Ansichar) * len);
    CopyMemory(Result, sWebContentFileName, len);
  end;
end;

function idHTTPGet(const URL: PAnsiChar): PAnsiChar; stdcall;
var
  WinHttp :IWinHttpRequest;
  s: PAnsiChar;
begin
  CoInitialize(nil);
  WinHttp:= coWinHttpRequest.Create;
  WinHttp.Open('GET', String(URL), False);
  WinHttp.Send('');
  WinHttp.WaitForResponse('');
  s:= PAnsiChar(AnsiString(WinHttp.ResponseText));
  CoUninitialize;

  const len = Length(s) + 1;
  Result:= CoTaskMemAlloc(SizeOf(Ansichar) * len);
  CopyMemory(Result, s, len);
end;

procedure ShellExecute(const URL: PAnsiChar); stdcall;
begin
  ShellExecuteA(0, 'open', URL, nil, nil, SW_SHOWNORMAL);
end;

exports MessageBox;

exports ExitsFiles;

exports CopyFile;

exports DeleteFile;

exports ShowUploadForm;

exports idHTTPGet;

exports ShellExecute;


begin
end.
