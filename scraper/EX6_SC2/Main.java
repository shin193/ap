package scraper.EX6_SC2;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        String domainAddress = Conf.DOMAIN_ADDRESS;
        String savePath = Conf.SAVE_DIRECTORY;

        DomainHtmlScraper domainHtmlScraper = new DomainHtmlScraper(domainAddress, savePath);
        domainHtmlScraper.start();

        HtmlAnalyzer.printTopCountUrls(10);
    }
}



