/**
 * This provides an exporter for the SpecObject XML format.
 */
module org.itsallcode.openfasttrace.exporter.specobject
{
    exports org.itsallcode.openfasttrace.exporter.specobject;

    requires java.logging;
    requires java.xml;
    requires org.itsallcode.openfasttrace.api;
    requires org.itsallcode.openfasttrace.exporter.common;
}
