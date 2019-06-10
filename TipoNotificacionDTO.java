package com.hiveag.geepy.dto;

public class TipoNotificacionDTO implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	private String  tipoNotificacion;
	   private Body body;
   
   

   public TipoNotificacionDTO() {
		super();
	}

   
   

	public TipoNotificacionDTO(String tipoNotificacion,Body body) {
		super();
		this.tipoNotificacion = tipoNotificacion;
		
		this.body = body;
	}

	public String getTipoNotificacion() {
		return tipoNotificacion;
	}

	public void setTipoNotificacion(String tipoNotificacion) {
		this.tipoNotificacion = tipoNotificacion;
	}

	
	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}
	
	
	
	
	

}
