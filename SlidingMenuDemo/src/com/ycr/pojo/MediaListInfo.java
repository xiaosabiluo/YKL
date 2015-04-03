package com.ycr.pojo;

import java.io.Serializable;
import java.util.List;

public class MediaListInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2636624994714183405L;
	private List<MediaInfo> medisa;
	public List<MediaInfo> getMedisa() {
		return medisa;
	}
	public void setMedisa(List<MediaInfo> medisa) {
		this.medisa = medisa;
	}
}
