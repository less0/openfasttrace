package org.itsallcode.openfasttrace.report.html.view.html;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.itsallcode.openfasttrace.api.core.LinkedSpecificationItem;
import org.itsallcode.openfasttrace.api.core.Trace;
import org.itsallcode.openfasttrace.api.exporter.ExporterException;
import org.itsallcode.openfasttrace.report.html.view.AbstractViewFactory;
import org.itsallcode.openfasttrace.report.html.view.Viewable;
import org.itsallcode.openfasttrace.report.html.view.ViewableContainer;

/**
 * Factory that creates an HTML OFT view (e.g. for reports) and provides an
 * output stream.
 */
public class HtmlViewFactory extends AbstractViewFactory
{
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final URL cssUrl;

    private HtmlViewFactory(final PrintStream stream, final URL cssUrl)
    {
        super(stream);
        this.cssUrl = cssUrl;
    }

    /**
     * Create a new instance.
     * 
     * @param stream
     *            the output stream.
     * @param cssURL
     *            the URL of the CSS file to include in the HTML report.
     * @return a new {@link HtmlViewFactory}.
     */
    public static HtmlViewFactory create(final OutputStream stream, final URL cssURL)
    {
        if (stream instanceof PrintStream)
        {
            return new HtmlViewFactory((PrintStream) stream, cssURL);
        }
        else
        {
            return new HtmlViewFactory(createPrintStream(stream, DEFAULT_CHARSET), cssURL);
        }
    }

    private static PrintStream createPrintStream(final OutputStream stream, final Charset charset)
    {
        try
        {
            return new PrintStream(stream, false, charset.name());
        }
        catch (final UnsupportedEncodingException e)
        {
            throw new ExporterException("Error creating print stream for charset " + charset, e);
        }
    }

    @Override
    public ViewableContainer createView(final String id, final String title)
    {
        return new HtmlView(this.outputStream, id, title, this.cssUrl);
    }

    @Override
    public ViewableContainer createReportDetails()
    {
        return new HtmlReportDetails(this.outputStream);
    }

    @Override
    public ViewableContainer createSection(final String id, final String title)
    {
        return new HtmlSection(this.outputStream, id, title);
    }

    @Override
    public Viewable createSpecificationItem(final LinkedSpecificationItem item)
    {
        return new HtmlSpecificationItem(this.outputStream, item);
    }

    @Override
    public Viewable createTraceSummary(final Trace trace)
    {
        return new HtmlTraceSummary(this.outputStream, trace);
    }

    @Override
    public ViewableContainer createReportSummary()
    {
        return new HtmlReportSummary(this.outputStream);
    }

    @Override
    public Viewable createTableOfContents(final ViewableContainer from)
    {
        return new HtmlTableOfContents(this.outputStream, from);
    }
}