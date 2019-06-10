package com.hiveag.geepy.util;

public class View {
	
	public interface Summary {}
	public interface Message extends Summary {} // In this way you can have several filters using the same class
	
	
	public interface BikeCatalog {}

}
