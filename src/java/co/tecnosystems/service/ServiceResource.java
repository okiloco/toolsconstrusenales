/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.tecnosystems.service;

import co.tecnosystems.facade.Facade;
import co.tecnosystems.util.DateUtil;
import java.io.IOException;
import java.sql.SQLException;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import co.tecnosystems.util.GenericJSONResponse;
import co.tecnosystems.util.GenericUtils;
import co.tecnosystems.util.HttpResponse;
import co.tecnosystems.util.JSONResponse;
import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;
import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.json.simple.parser.ParseException;
import org.apache.commons.lang3.StringUtils;

import java.net.*;
import java.io.*;

/**
 * REST Web Service
 *
 *
 */  

@Stateless
@Path("Servicios")
public class ServiceResource {

    @Context
    private HttpServletRequest request;

    @Context
    private HttpServletResponse response;

    @EJB
    private Facade dao;

    /**
     * Creates a new instance of ReporteResource
     */
    public ServiceResource() {
    }

    /**
     * Retrieves representation of an instance of
     * co.tecnosystems.service.ReporteResource
     *
     * @return an instance of java.lang.String
     */
    /**
     * PUT method for updating or creating an instance of ReporteResource
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("tipoDocumentoP")
    public String listarTipoDoc() throws SQLException, IOException, ParseException {
        try {

            HashMap<String, Object> hm = dao.listar();
            List list = (List) hm.get("lista");

            return new JSONSerializer()
                    .include(HttpResponse.DATA)
                    .serialize(HttpResponse.message("OK", list));
        } catch (NumberFormatException e) {

        }

        return null;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("consultaComparendos")
    public String consultaComparendos(
            @FormParam("organismo") String organismo,
            @FormParam("orden") String orden,
            @FormParam("tdocumento") String tdocumento,
            @FormParam("documento") String documento,
            @FormParam("placa") String placa) throws SQLException, IOException, ParseException {
        try {
            String tidoc;
            String doc;

            if (!StringUtils.isBlank(tdocumento)) {
                tidoc = tdocumento;
            } else {
                tidoc = "0";
            }

            if (!StringUtils.isBlank(documento)) {
                doc = documento;
            } else {
                doc = "0";
            }

            HashMap<String, Object> hm = dao.ConsultaComparendos(organismo, orden, tidoc, doc, placa);

            List list = (List) hm.get("lista");

            return new JSONSerializer()
                    .include(HttpResponse.DATA)
                    .serialize(HttpResponse.message("OK", list, list.size()));
        } catch (NumberFormatException e) {

        }
        return null;
    }

    @POST
    @Produces("application/vnd.ms-excel")
    @Path("exportarExcel")
    public Response exportarExcel(
            @FormParam("organismo") String organismo,
            @FormParam("orden") String orden,
            @FormParam("tdocumento") String tdocumento,
            @FormParam("documento") String documento,
            @FormParam("placa") String placa
    ) throws SQLException, IOException, ParseException {
        File file = null;
        try {
            String tidoc;
            String doc;

            if (!StringUtils.isBlank(tdocumento)) {
                tidoc = tdocumento;
            } else {
                tidoc = "0";
            }

            if (!StringUtils.isBlank(documento)) {
                doc = documento;
            } else {
                doc = "0";
            }
            HashMap<String, Object> hm = dao.ConsultaComparendos(organismo, orden, tidoc, doc, placa);
            List list = (List) hm.get("lista");

            file = dao.generarExcel(list);
            Response.ResponseBuilder response = Response.ok((Object) file);
            response.header("Content-Disposition", "attachment; filename=excel-" + "excel" + ".xlsx");
            return response.build();
        } catch (Exception e) {

            Response.ResponseBuilder response = Response.serverError();
            return response.build();
        }
    }

    @GET
    @Produces("application/vnd.ms-excel")
    @Path("/exportarExcelg/")
    public Response exportarExcelg(
            @QueryParam("organismo") String organismo,
            @QueryParam("orden") String orden,
            @QueryParam("tdocumento") String tdocumento,
            @QueryParam("documento") String documento,
            @QueryParam("placa") String placa
    ) throws ParseException {

        File file;
        try {
            String tidoc;
            String doc;

            if (!StringUtils.isBlank(tdocumento)) {
                tidoc = tdocumento;
            } else {
                tidoc = "0";
            }

            if (!StringUtils.isBlank(documento)) {
                doc = documento;
            } else {
                doc = "0";
            }
            HashMap<String, Object> hm = dao.ConsultaComparendos(organismo, orden, tidoc, doc, placa);
            List list = (List) hm.get("lista");

            file = dao.generarExcel(list);

        } catch (IOException ex) {
            return null;

        } catch (SQLException ex) {
            return null;
        }
        Response.ResponseBuilder response = Response.ok((Object) file);
        response.header("Content-Disposition",
                "attachment; filename=excel.xlsx");
        return response.build();

    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("consultaRecaudo")
    public String consultaRecaudo(
            @DefaultValue("")
            @FormParam("fechaIni") String fechaIni
    ) throws SQLException, IOException, ParseException {
        try {
            if (fechaIni.isEmpty()) {
                return new GenericJSONResponse(false, GenericJSONResponse.CONFLICT_STATUS, "Fecha inicial y fecha final requeridas").toString();
            }

            String f1 = fechaIni;

            Date fechaInicial = GenericUtils.getDateFromString("yyyy-MM-dd", f1);

            HashMap<String, Object> hm = dao.consultarecaudoDiario(fechaInicial, fechaInicial);

            List list = (List) hm.get("lista");

            return new JSONSerializer()
                    .include(HttpResponse.DATA)
                    .serialize(HttpResponse.message("OK", list));
        } catch (NumberFormatException e) {

        }
        return null;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("pagar")
    public String pagar(
            @FormParam("comparendo") String Comparendo
    ) throws SQLException, IOException, ParseException {
        int usuario = -1;
        String msg = dao.liquidarYPago(Comparendo, usuario);

        return msg;

    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("buscar")
    public String buscar(
            @FormParam("comparendo") String comparendo,
            @FormParam("placa") String placa,
            @FormParam("cedula") String cedula) throws SQLException, IOException, ParseException {

        if (StringUtils.isBlank(cedula)) {
            cedula = "0";
        }

        HashMap<String, Object> hm = dao.buscarInfoComparendo(comparendo, cedula, placa);
        List list = (List) hm.get("lista");

        return new JSONResponse(list)
                .include(JSONResponse.DATA)
                .transform(new DateTransformer(DateUtil.FORMAT_SQL), "data.fecha_comparendo")
                .transform(new DateTransformer(DateUtil.FORMAT_DATE_TIME), "data.fecha_entrega_comparendo")
                .serialize(JSONResponse.message("ok", list));

    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("registroLlamada")
    public String registro(
            @FormParam("id_organismo_transito") int organismo,
            @FormParam("id_comparendo") Integer idComparendo,
            @FormParam("telefono") String telefono,
            @FormParam("observacion") String observacion,
            @FormParam("nombre_propietario") String nombre,
            @FormParam("tipoLlamada") Integer tipoLlamada,
            @FormParam("codificacion") Integer codificacion,
            @FormParam("idusario") Integer idusuario,
            @FormParam("direccion") String direccion,
            @FormParam("email") String correo,
            @FormParam("ciudad") int ciudad,
            @FormParam("cedula") String cedula
    ) {

        try {
            if (idComparendo == null) {
                return new GenericJSONResponse(false, GenericJSONResponse.CONFLICT_STATUS, "Numero de comparendo obligatorio").toString();
            }

            if (idusuario == null) {
                return new GenericJSONResponse(false, GenericJSONResponse.CONFLICT_STATUS, "El usuario es obligatorio").toString();
            }

            if (tipoLlamada == null) {
                return new GenericJSONResponse(false, GenericJSONResponse.CONFLICT_STATUS, "El tipo de llamada es obligatorio").toString();
            }

            HashMap<String, Object> hm = dao.guardaLogLLmada(organismo, idComparendo, telefono, observacion, nombre, tipoLlamada, codificacion, idusuario, direccion, correo, ciudad, cedula);

            List list = (List) hm.get("lista");

            if (list.toString().equals("[{estado=OK, descripcion=Termino correctamente.}]")) {

            }

            return new GenericJSONResponse(true, GenericJSONResponse.SUCCESFUL_PROCESSED_STATUS, "Datos guardados correctamente").toString();

        } catch (Exception e) {
            return new GenericJSONResponse(false, GenericJSONResponse.GENERAL_BUSINESS_LOGIC_ERROR, "error " + e.getMessage()).toString();
        }

    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("buscarLlamada")
    public String buscar(
            @FormParam("organismo") int organismo,
            @FormParam("idcomparendo") int idComparendo
    ) {
        try {
            HashMap<String, Object> hm = dao.consultarRegistroLlamada(organismo, idComparendo);
            List list = (List) hm.get("lista");

            return new JSONResponse(list)
                    .include(JSONResponse.DATA)
                    .transform(new DateTransformer(DateUtil.FORMAT_DATE_TIME), "data.CALLFERE")
                    .serialize(JSONResponse.message("ok", list));
        } catch (Exception e) {
            return new GenericJSONResponse(false, GenericJSONResponse.GENERAL_BUSINESS_LOGIC_ERROR, "error " + e.getMessage()).toString();
        }

    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("buscartipoLlamada")
    public String buscartipoLlamada(
            @FormParam("organismo") int organismo
    ) {
        try {
            HashMap<String, Object> hm = dao.consultarTipoLlamada(organismo);
            List list = (List) hm.get("lista");

            return new JSONResponse(list)
                    .include(JSONResponse.DATA)
                    .serialize(JSONResponse.message("ok", list));
        } catch (Exception e) {
            return new GenericJSONResponse(false, GenericJSONResponse.GENERAL_BUSINESS_LOGIC_ERROR, "error " + e.getMessage()).toString();
        }

    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("buscarCodificacionLlamada")
    public String buscarCodificacionLlamada(
            @FormParam("organismo") int organismo,
            @FormParam("TIPOID") int tipo
    ) {
        try {
            HashMap<String, Object> hm = dao.consultarCodificacionLlamada(organismo, tipo);
            List list = (List) hm.get("lista");

            return new JSONResponse(list)
                    .include(JSONResponse.DATA)
                    .serialize(JSONResponse.message("ok", list));
        } catch (Exception e) {
            return new GenericJSONResponse(false, GenericJSONResponse.GENERAL_BUSINESS_LOGIC_ERROR, "error " + e.getMessage()).toString();
        }

    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("grabacion")
    public String grabacion(
            @FormParam("llamdada") String llamdada,
            @FormParam("fecha") String fecha) {
        String msg = "repoducir";

        try {
            dao.buscarGrabacion(llamdada, fecha);
            return msg;

        } catch (Exception e) {
        }
        return msg;

    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("actualizarLlamada")
    public String actualizarLlamada(
            @FormParam("id_organismo_transito") int organismo,
            @FormParam("observacion") String observacion,
            @FormParam("id") int idllamada,
            @FormParam("tipoLlamada") int tipoLlamada,
            @FormParam("codificacion") int codificacion,
            @FormParam("idusario") int idusuario,
            @FormParam("nombre_propietario") String nombre,
            @FormParam("telefono") String telefono,
            @FormParam("direccion") String direccion,
            @FormParam("email") String correo,
            @FormParam("ciudad") int ciudad,
            @FormParam("cedula") String cedula
    ) {

        try {

            if (idusuario == 0) {
                return new GenericJSONResponse(false, GenericJSONResponse.CONFLICT_STATUS, "El usuario es obligatorio").toString();
            }

            if (tipoLlamada == 0) {
                return new GenericJSONResponse(false, GenericJSONResponse.CONFLICT_STATUS, "El tipo de llamada es obligatorio").toString();
            }
            GenericJSONResponse mensaje = dao.editarLogLLmada(organismo, idllamada, idusuario, tipoLlamada, codificacion, observacion, nombre, telefono, direccion, correo, ciudad, cedula);

            return mensaje.toString();

        } catch (Exception e) {
            return new GenericJSONResponse(false, GenericJSONResponse.GENERAL_BUSINESS_LOGIC_ERROR, "error " + e.getMessage()).toString();
        }

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("mac")
    public String mac() throws SQLException, IOException, ParseException {
        try {
            InetAddress address = InetAddress.getLocalHost();
            String IPs;
            IPs = java.net.InetAddress.getLocalHost().getHostAddress();

            String ip = request.getRemoteAddr();

            System.out.println("ip " + ip);
            System.out.println("IP Local :" + address.getHostAddress());
            System.out.println("NOMBRE :" + address.getCanonicalHostName());

            // Aqui obtenemos la ip de la web del programador
            String domain = "www.lawebdelprogramador.com";
            InetAddress address2 = InetAddress.getByName(domain);
            byte IP[] = address2.getAddress();
            System.out.print("IP del dominio " + domain + " :");
            for (int index = 0; index < IP.length; index++) {
                if (index > 0) {
                    System.out.print(".");
                }
                System.out.print(((int) IP[index]) & 0xff);
            }
            return new GenericJSONResponse(true, GenericJSONResponse.SUCCESFUL_PROCESSED_STATUS, "exito").toString();

        } catch (Exception e) {
            return new GenericJSONResponse(false, GenericJSONResponse.GENERAL_BUSINESS_LOGIC_ERROR, "error " + e.getMessage()).toString();
        }

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("mac2")
    public String mac2() throws SQLException, IOException, ParseException {
        try {

            InetAddress address = InetAddress.getLocalHost();

            String ipP = request.getRemoteAddr();
            String host = request.getRemoteHost();
            String user = request.getRemoteUser();
            int port = request.getRemotePort();
            String nombre = address.getCanonicalHostName();
            System.out.println("IP Local :" + address.getHostAddress());
            System.out.println("IP Local :" + address.getHostAddress());
            System.out.println("NOMBRE :" + address.getCanonicalHostName());

            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));

            String ip = in.readLine();
            System.out.println("My Public ip is = " + ip);
            in.close();
            return new GenericJSONResponse(true, GenericJSONResponse.SUCCESFUL_PROCESSED_STATUS, "ip: " + ipP + " " + "puerto: " + port + " " + "usuario: " + user + " " + "IpPublica: " + ip).toString();

        } catch (Exception e) {
            return new GenericJSONResponse(false, GenericJSONResponse.GENERAL_BUSINESS_LOGIC_ERROR, "error " + e.getMessage()).toString();
        }

    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("eliminarObservacion")
    public String eliminarObservacion(
            @FormParam("id_organismo_transito") int organismo,
            @FormParam("id") int idllamada,
            @FormParam("idusario") int idusuario
    ) {

        try {

            GenericJSONResponse mensaje = dao.eliminarObservacionLLmada(organismo, idllamada, idusuario);

            return mensaje.toString();

        } catch (Exception e) {
            return new GenericJSONResponse(false, GenericJSONResponse.GENERAL_BUSINESS_LOGIC_ERROR, "error " + e.getMessage()).toString();
        }

    }

    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Path("csvinformeOrganismos")
    public Response csvInforme() {
        try {

            byte[] csv = dao.generarCsv();
            Response.ResponseBuilder response = Response.ok(csv);
            response.header("Content-Disposition", "attachment; filename=\"firme.csv\"");
            return response.build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Path("informeLlamadascsv")
    public Response informeLlamadas(
            @QueryParam("id_organismo") int organismo,
            @QueryParam("id_usuario") String idusuario,
            @QueryParam("fecha_inicio") String fechaInicio,
            @QueryParam("fecha_fin") String fechaFin
           
    ){
        try {
            byte[] csv = dao.generarInformLlamadasCsv(organismo,idusuario,fechaInicio,fechaFin);
            Response.ResponseBuilder response = Response.ok(csv);
            response.header("Content-Disposition", "attachment; filename=\"informeLlamada.csv\"");
            return response.build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}