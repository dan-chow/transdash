//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.03.01 at 02:54:23 PM CST 
//


package chow.dan.mpd;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the chow.dan.mpd package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: chow.dan.mpd
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link MPD }
     * 
     */
    public MPD createMPD() {
        return new MPD();
    }

    /**
     * Create an instance of {@link MPD.Period }
     * 
     */
    public MPD.Period createMPDPeriod() {
        return new MPD.Period();
    }

    /**
     * Create an instance of {@link MPD.Period.AdaptationSet }
     * 
     */
    public MPD.Period.AdaptationSet createMPDPeriodAdaptationSet() {
        return new MPD.Period.AdaptationSet();
    }

    /**
     * Create an instance of {@link MPD.Period.AdaptationSet.Representation }
     * 
     */
    public MPD.Period.AdaptationSet.Representation createMPDPeriodAdaptationSetRepresentation() {
        return new MPD.Period.AdaptationSet.Representation();
    }

    /**
     * Create an instance of {@link MPD.Period.AdaptationSet.Representation.SegmentList }
     * 
     */
    public MPD.Period.AdaptationSet.Representation.SegmentList createMPDPeriodAdaptationSetRepresentationSegmentList() {
        return new MPD.Period.AdaptationSet.Representation.SegmentList();
    }

    /**
     * Create an instance of {@link MPD.Period.AdaptationSet.SegmentList }
     * 
     */
    public MPD.Period.AdaptationSet.SegmentList createMPDPeriodAdaptationSetSegmentList() {
        return new MPD.Period.AdaptationSet.SegmentList();
    }

    /**
     * Create an instance of {@link MPD.ProgramInformation }
     * 
     */
    public MPD.ProgramInformation createMPDProgramInformation() {
        return new MPD.ProgramInformation();
    }

    /**
     * Create an instance of {@link MPD.Period.AdaptationSet.Representation.SegmentList.SegmentURL }
     * 
     */
    public MPD.Period.AdaptationSet.Representation.SegmentList.SegmentURL createMPDPeriodAdaptationSetRepresentationSegmentListSegmentURL() {
        return new MPD.Period.AdaptationSet.Representation.SegmentList.SegmentURL();
    }

    /**
     * Create an instance of {@link MPD.Period.AdaptationSet.SegmentList.Initialization }
     * 
     */
    public MPD.Period.AdaptationSet.SegmentList.Initialization createMPDPeriodAdaptationSetSegmentListInitialization() {
        return new MPD.Period.AdaptationSet.SegmentList.Initialization();
    }

}
