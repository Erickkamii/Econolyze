package dev.econolyze.interfaces.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/api/report")
public class ReportResource {
    @GET
    public String getReport() {
        return "report";
    }
}
