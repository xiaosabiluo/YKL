package com.ycr.choosevideos;

import java.io.Serializable;

/**
 * 一个图片对象
 * 
 * @author Administrator
 * 
 */
public class VideoItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6015122319165844838L;
	public String imageId;
	public String thumbnailPath;
	public String imagePath;
	public boolean isSelected = false;
}
