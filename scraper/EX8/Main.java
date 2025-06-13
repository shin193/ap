package scraper.EX8;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        String domainAddress = Conf.DOMAIN_ADDRESS;
        DomainHtmlScraper domainHtmlScraper = new DomainHtmlScraper(domainAddress);
        domainHtmlScraper.start();
        HtmlAnalyzer.printTopCountUrls(10);
    }
}