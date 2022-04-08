package services;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;

public class WebClientService {
    private WebClient webClient;

    public WebClientService() {
        this.webClient = new WebClient(BrowserVersion.CHROME);

        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setPrintContentOnFailingStatusCode(false);
    }

    public WebClient getWebClient() {
        return this.webClient;
    }
}
