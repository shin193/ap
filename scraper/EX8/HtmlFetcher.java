package scraper.EX8;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class HtmlFetcher {
    public static List<String> fetchHtml(String urlAddress) throws IOException {
        System.out.println("Fetching: " + urlAddress);
        URL pageLocation = new URL(urlAddress);
        Scanner in = new Scanner(pageLocation.openStream());

        List<String> htmlLines = new ArrayList<>();
        while (in.hasNext()) {
            htmlLines.add(in.next());
        }
        in.close();
        return htmlLines;
    }
}
