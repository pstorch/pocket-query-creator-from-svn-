package org.pquery.webdriver.parser;

import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

import org.pquery.dao.PQ;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PocketQueryPage {

    private Source parsedHtml;

    public PocketQueryPage(Source parsedHtml) {
        this.parsedHtml = parsedHtml;
    }

    /**
     * <thead><tr>
     * <tr id="ctl00_ContentBody_PQDownloadList_uxDownloadPQList_ctl01_trPQDownloadRow">
     * <tr class="TableFooter">
     *
     * @return
     * @throws ParseException
     */
    public PQ[] getReadyForDownload() throws ParseException {
        Element table = getReadyForDownloadTable();

        List<Element> rows = table.getAllElements(HTMLElementName.TR);

        // Don't think this ever happens
        if (rows.size() <= 2)
            return new PQ[0];

        // Detect if middle row says empty

		/*
		 * If there are no downloadable PQ then single row (between header and footer)
		 * looks like this
		 * 
		 * <tr class="BorderBottom"><td colspan="4">
		 * No Downloads Available
		 * </td></tr>
		 */
        Attribute classattrib = rows.get(1).getAttributes().get("class");
        if (classattrib != null && classattrib.getValue().equalsIgnoreCase("BorderBottom"))
            return new PQ[0];

        ArrayList<PQ> ret = new ArrayList<PQ>();

        // Table has header and footer we can skip
        for (int i = 1; i < rows.size() - 1; i++) {
            ret.add(decodeDownloadRow(rows.get(i)));
        }
        return ret.toArray(new PQ[0]);
    }

    public PQ decodeDownloadRow(Element row) throws ParseException {
        List<Element> column = row.getAllElements(HTMLElementName.TD);

        // Trying to catch error seen here once
        if (column.size() <= 5)
            throw new ParseException("Parse Error. Missing columns in download table");

        PQ down = new PQ();

        // Column 2. Extract title, which is wrapped in a link
        down.name = column.get(2).getTextExtractor().toString();

        // Column 2. Target of hyperlink is URL to download PQ
        down.url = column.get(2).getFirstStartTag(HTMLElementName.A).getElement().getAttributeValue("href");

        // Column 3. String size
        down.size = column.get(3).getTextExtractor().toString();
        // Column 4. Number of caches matching PQ
        down.waypoints = column.get(4).getTextExtractor().toString();
        // Column 5. Time last generated
        down.age = column.get(5).getTextExtractor().toString();

        return down;
    }

    public Element getReadyForDownloadTable() {
        return parsedHtml.getElementById("uxOfflinePQTable");
    }

    public PQ[] getRepeatables() throws ParseException {
        Element table = getRepeatableTable();

        List<Element> rows = table.getAllElements(HTMLElementName.TR);

        // Don't think this ever happens
        if (rows.size() <= 2)
            return new PQ[0];

        // Detect if middle row says empty

		/*
		 * If there are no repeatable PQ then single row (between header and footer)
		 * looks like this
		 *
		 * <tr class="BorderBottom"><td colspan="4">
		 * No Downloads Available
		 * </td></tr>
		 */
        Attribute classattrib = rows.get(1).getAttributes().get("class");
        if (classattrib != null && classattrib.getValue().equalsIgnoreCase("TableFooter"))
            return new PQ[0];

        ArrayList<PQ> ret = new ArrayList<PQ>();

        // Table has header and footer we can skip
        for (int i = 1; i < rows.size() - 1; i++) {
            ret.add(decodeRepeatableRow(rows.get(i)));
        }
        return ret.toArray(new PQ[0]);
    }

    private PQ decodeRepeatableRow(Element row) throws ParseException {
        List<Element> column = row.getAllElements(HTMLElementName.TD);

        // Trying to catch error seen here once
        if (column.size() <= 12)
            throw new ParseException("Parse Error. Missing columns in repeatable table");

        PQ repeatable = new PQ();

        // Column 3. Extract title, which is wrapped in a link
        repeatable.name = column.get(3).getFirstStartTag(HTMLElementName.A).getElement().getTextExtractor().toString();
        // Column 3. Number of caches matching PQ
        String waypoints = column.get(3).getTextExtractor().toString();
        Matcher m = Pattern.compile("\\((\\d+)\\)").matcher(waypoints);
        if (m.find()) {
            waypoints = m.group(1);
        }
        repeatable.waypoints = waypoints;

        // Column 5-11. extract days of week

        return repeatable;
    }

    private Element getRepeatableTable() {
        return parsedHtml.getElementById("pqRepeater");
    }

    //        Matcher m = Pattern.compile("<table id=\"uxOfflinePQTable\"(.+?)<\table>", DOTALL+CASE_INSENSITIVE).matcher(html);
    //        if (m.find()) {
    //            return m.group(1);
    //        }
    //        throw new ParseException("Unable to find 'Ready to download' table");
    //    }


    /**
     * <a href="/pocket/downloadpq.ashx?g=e6fe1c12-a8ca-4f39-ab43-d1f4c6b6978c">
     10-20-12 6.57 PM</a>
     </td>
     <td class="AlignRight">
     174.75 KB
     </td>
     <td class="AlignCenter">
     159
     </td>
     */


}
