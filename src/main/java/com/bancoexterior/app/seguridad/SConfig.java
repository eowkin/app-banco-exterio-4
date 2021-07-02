package com.bancoexterior.app.seguridad;


public class SConfig {
	
	private SConfig() {
	    super();
	  }
	
	public static final String ALGORITMO = "AES/GCM/NoPadding";
	public static final String CSET = "AES";
	protected static final byte[] IV = new byte[16];
	public static final int TAG_LENGTH_BIT = 128;
	public static final String SHA256 = "SHA-256";
	public static final String SHA512 = "SHA-512";
	
	private static final String DKEY  = "X#!g00aN430=?$|:.dAs";
	private static final String DKEY2 = "A&a032HVWnd||_jk?oBN";

	private static final String PKEY  = "OA#$%&.=?(Ta00fgb$sd";
	private static final String PKEY2 = "FA;:La00%$TopPbn;d|@";
	public static final String BLANK                              = "";
	public static final String DESARROLLO = "des";
	public static final String CALIDAD    = "qa";
	public static final String PRODUCCION = "pro";
	
	public static String[] getkEY(String ambiente) {
		
		ambiente = ambiente == null ? BLANK:ambiente;
		
		switch (ambiente.toLowerCase()) {
		case DESARROLLO: case CALIDAD:
			return new String[] {DKEY,DKEY2};		
		case PRODUCCION : default:
			return new String[] {PKEY,PKEY2};


	}
				
		
	}
	

}
