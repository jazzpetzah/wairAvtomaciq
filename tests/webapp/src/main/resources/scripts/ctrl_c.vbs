Dim strComputer, objWMIService
Dim supportedBrowsers
supportedBrowsers = Array("opera", "iexplore", "chrome", "firefox")

Set objWMIService = GetObject("winmgmts:{impersonationLevel=impersonate}!\\.\root\cimv2")
Set oShell = CreateObject("Wscript.Shell")

Set colItems = objWMIService.ExecQuery("SELECT * FROM Win32_Process")
For Each objItem in colItems
    For Each suportedBrowser in supportedBrowsers
		If InStr(1, objItem.Commandline, suportedBrowser, 1) > 0 Then
			oShell.AppActivate(objItem.ProcessId)
			WScript.Sleep 500
			oShell.SendKeys "^(c)"
			WScript.Sleep 500
		End If
	Next
Next