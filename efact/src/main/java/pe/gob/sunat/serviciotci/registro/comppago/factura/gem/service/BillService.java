
package pe.gob.sunat.serviciotci.registro.comppago.factura.gem.service;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "billService", targetNamespace = "http://service.gem.factura.comppago.registro.servicio.sunat.gob.pe/", wsdlLocation = "http://proy.ose.tci.net.pe/ol-ti-itcpe/ws/billService?wsdl")
public class BillService
    extends Service
{

    private final static URL BILLSERVICE_WSDL_LOCATION;
    private final static WebServiceException BILLSERVICE_EXCEPTION;
    private final static QName BILLSERVICE_QNAME = new QName("http://service.gem.factura.comppago.registro.servicio.sunat.gob.pe/", "billService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://proy.ose.tci.net.pe/ol-ti-itcpe/ws/billService?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        BILLSERVICE_WSDL_LOCATION = url;
        BILLSERVICE_EXCEPTION = e;
    }

    public BillService() {
        super(__getWsdlLocation(), BILLSERVICE_QNAME);
    }

    public BillService(WebServiceFeature... features) {
        super(__getWsdlLocation(), BILLSERVICE_QNAME, features);
    }

    public BillService(URL wsdlLocation) {
        super(wsdlLocation, BILLSERVICE_QNAME);
    }

    public BillService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, BILLSERVICE_QNAME, features);
    }

    public BillService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public BillService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns BillServicePort
     */
    @WebEndpoint(name = "BillServicePortSoap11")
    public BillServicePort getBillServicePortSoap11() {
        return super.getPort(new QName("http://service.gem.factura.comppago.registro.servicio.sunat.gob.pe/", "BillServicePortSoap11"), BillServicePort.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns BillServicePort
     */
    @WebEndpoint(name = "BillServicePortSoap11")
    public BillServicePort getBillServicePortSoap11(WebServiceFeature... features) {
        return super.getPort(new QName("http://service.gem.factura.comppago.registro.servicio.sunat.gob.pe/", "BillServicePortSoap11"), BillServicePort.class, features);
    }

    private static URL __getWsdlLocation() {
        if (BILLSERVICE_EXCEPTION!= null) {
            throw BILLSERVICE_EXCEPTION;
        }
        return BILLSERVICE_WSDL_LOCATION;
    }

}
