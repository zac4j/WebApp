# webapp
This app demonstrates 4 common webapp scenarios:

![home](art/web-app.png)

+ FileChooser: open system gallery and pick a photo show in WebView
  ![file](art/file-chooser.png)
+ Prompt/Alert/Console: override `WebChromeClient` methods to hook js prompt, alert, console functions
  ![prompt](art/js-prompt.png)
+ JavaScriptInterface: use `@JavascriptInterface` to make js could call Java method
  ![jsi](art/js-interface.png)
+ Pdf Viewer: use [pdf.js][pdfjs] to render pdf content in Android WebView
  ![pdf](art/pdf-viewer.png)
  
[pdfjs]:https://mozilla.github.io/pdf.js/
