unit UploadWeb;

interface

uses
  Winapi.Windows, Winapi.Messages, System.SysUtils, System.Variants, System.Classes, Vcl.Graphics,
  Vcl.Controls, Vcl.Forms, Vcl.Dialogs, Vcl.OleCtrls, SHDocVw, MSHTML;

type
  TUpload = class(TForm)
    WebBrowser1: TWebBrowser;
    procedure FormCreate(Sender: TObject);
    procedure FormClose(Sender: TObject; var Action: TCloseAction);
    procedure WebBrowser1DocumentComplete(ASender: TObject;
      const pDisp: IDispatch; const URL: OleVariant);
  private
    { Private declarations }
  public
    { Public declarations }
  end;

var
  Upload: TUpload;
  sWebContentIndex: AnsiString = '';
  sWebContentFileName: PAnsiChar;
implementation

{$R *.dfm}

function Split(Source: String; Split: String; Num: Integer): String;
const
  MAXLENGTH = 255;
var
  Res: array[0..MaxLength] of String;
  i: Integer;
begin
  i:= 1;
  while AnsiPos(Split, Source) <> 0 do
  begin
    Res[i]:= Copy(Source, 1, AnsiPos(Split, Source) - 1);
    Delete(Source, 1, AnsiPos(Split, Source) + Length(Split) - 1);
    Inc(i);
  end;
  Res[i]:= Copy(Source, 1, Length(Source));
  Result:= Res[Num + 1];
end;

procedure TUpload.FormClose(Sender: TObject; var Action: TCloseAction);
begin
  if Length(sWebContentFileName) <= 0 then
    sWebContentFileName:= 'nil';
  Upload:= nil;
end;

procedure TUpload.FormCreate(Sender: TObject);
begin
  WebBrowser1.Navigate('http://unknowncode.org/temp/form.html');
  SetWindowPos(Handle, HWND_TOPMOST, Left, Top, Width, Height, 0);
end;

procedure TUpload.WebBrowser1DocumentComplete(ASender: TObject;
  const pDisp: IDispatch; const URL: OleVariant);
var
  Doc: IHTMLDocument2;
  HTMLWindow: IHTMLWindow2;
  sFunction: string;
  buf: String;
begin
  WebBrowser1.OleObject.Document.Body.Style.OverflowY := 'hidden';
  WebBrowser1.OleObject.Document.Body.Style.OverflowX := 'hidden';
  if VarToStr(URL) = 'http://unknowncode.org/temp/upload.php' then
  begin
    buf:= WebBrowser1.OleObject.Document.documentElement.outerHTML;
    buf:= Split(Split(buf, 'ÆÄÀÏ¸í: ', 1), '<', 0);
    buf:= Copy(buf, 0, length(buf) - 3);
    sWebContentFileName:= PAnsiChar(AnsiString(buf));
    Upload.Close;
  end;

  if VarToStr(URL) <> 'http://unknowncode.org/temp/form.html' then
    Exit;

  Doc:= WebBrowser1.Document as IHTMLDocument2;
  HTMLWindow:= Doc.parentWindow;
  try
    sFunction := Format('setText("%s")', [sWebContentIndex]);
    HTMLWindow.execScript(sFunction, 'JavaScript');
  except
  end;
end;

end.
