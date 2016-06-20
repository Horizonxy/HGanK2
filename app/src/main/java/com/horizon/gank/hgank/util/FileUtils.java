package com.horizon.gank.hgank.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class FileUtils {

	/********************音频*************************/
	public final static String FILE_TYPE_AUDIO = "audio";
	public final static String FILE_TYPE_M4A = "m4a";
	public final static String FILE_TYPE_MP3 = "mp3";
	public final static String FILE_TYPE_MID = "mid";
	public final static String FILE_TYPE_XMF = "xmf";
	public final static String FILE_TYPE_OGG = "ogg";
	public final static String FILE_TYPE_WAV = "wav";
	/********************视频*************************/
	public final static String FILE_TYPE_VIDEO = "video";
	public final static String FILE_TYPE_3GP = "3gp";
	public final static String FILE_TYPE_MP4 = "mp4";
	public final static String FILE_TYPE_AVI = "avi";
	public final static String FILE_TYPE_MKV = "mkv";
	/********************图片*************************/
	public final static String FILE_TYPE_IMAGE = "image";
	public final static String FILE_TYPE_JPG = "jpg";
	public final static String FILE_TYPE_GIF = "gif";
	public final static String FILE_TYPE_PNG = "png";
	public final static String FILE_TYPE_JPEG = "jpeg";
	public final static String FILE_TYPE_BMP = "bmp";
	/********************文件*************************/
	public final static String FILE_TYPE_TEXT = "text";
	public final static String FILE_TYPE_TXT = "txt";
	public final static String FILE_TYPE_UMD = "umd";
	public final static String FILE_TYPE_APK = "apk";
	public final static String FILE_TYPE_CHM = "chm";
	public final static String FILE_TYPE_EPUB = "epub";
	public final static String FILE_TYPE_PDB = "pdb";
	public final static String FILE_TYPE_PDF = "pdf";
	public final static String FILE_TYPE_ZIP = "zip";

	/**
	 * 根据路径和文件名查找文件
	 * @author 蒋先明
	 * @param path
	 * @param imageName
	 * @return
	 */
	public static File findFile(String path, String imageName) {
		File dir = new File(path);
		if (dir.exists()) {
			File filses[] = dir.listFiles();
			for (int i = 0; i < filses.length; i++) {
				String fileName = filses[i].getName();
				if (fileName.equals(imageName)) {
					return filses[i];
				}
			}
		} else {
			dir.mkdirs();
		}
		return null;
	}

	/**
	 * 路径设置
	 * 
	 * @param context
	 * @param isSDCard
	 *            是否使用sd卡
	 * @param folder
	 *            目录名称
	 * @return
	 */
	public static String getEnvPath(Context context, boolean isSDCard, String folder) {
		String filePath;
		boolean hasSDCard = Environment.getExternalStorageState()
				.equals(Environment.MEDIA_MOUNTED);

		if (hasSDCard && isSDCard) {
			filePath = Environment.getExternalStorageDirectory().toString();
		} else {
			filePath = context.getCacheDir().getAbsolutePath().toString();
		}
		return filePath.concat("/").concat(folder);

	}
	
	/**
	 * 检查SDCard是否插入
	 * 
	 * @return
	 */
	public static boolean isHasSdcard() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 获取文件类型
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getMIMEType(String fileName) {
		return getMIMEType(new File(fileName), false);
	}
	
	/**
	 * 判断文件MimeType
	 * 
	 * @param file
	 * @param isOpen
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	public static String getMIMEType(File file, boolean isOpen) {
		String fileType = "";
		String fileName = file.getName();
		// 取得扩展名
		String end = fileName.substring(fileName.lastIndexOf(".") + 1,
				fileName.length()).toLowerCase();
		if (isOpen) {
			// 依附档名的类型决定MimeType
			if (end.equals(FILE_TYPE_M4A)
					|| end.equals(FILE_TYPE_MP3)
					|| end.equals(FILE_TYPE_MID)
					|| end.equals(FILE_TYPE_XMF)
					|| end.equals(FILE_TYPE_OGG)
					|| end.equals(FILE_TYPE_WAV)) {
				fileType = FILE_TYPE_AUDIO;
			} else if (end.equals(FILE_TYPE_3GP)
					|| end.equals(FILE_TYPE_MP4)
					|| end.equals(FILE_TYPE_AVI)
					|| end.equals(FILE_TYPE_MKV)) {
				fileType = FILE_TYPE_VIDEO;
			} else if (end.equals(FILE_TYPE_JPG)
					|| end.equals(FILE_TYPE_GIF)
					|| end.equals(FILE_TYPE_PNG)
					|| end.equals(FILE_TYPE_JPEG)
					|| end.equals(FILE_TYPE_BMP)) {
				fileType = FILE_TYPE_IMAGE;
			} else if (end.equals(FILE_TYPE_TXT)) {
				fileType = FILE_TYPE_TEXT;
			} else {
				/* 如果无法直接打开，就跳出软件列表给用户选择 */
				fileType = "*";
			}
			fileType += "/*";
		} else {
			if (end.equals(FILE_TYPE_M4A)
					|| end.equals(FILE_TYPE_MP3)
					|| end.equals(FILE_TYPE_MID)
					|| end.equals(FILE_TYPE_XMF)
					|| end.equals(FILE_TYPE_OGG)
					|| end.equals(FILE_TYPE_WAV)) {
				fileType = FILE_TYPE_AUDIO;
			} else if (end.equals(FILE_TYPE_3GP)
					|| end.equals(FILE_TYPE_MP4)
					|| end.equals(FILE_TYPE_AVI)
					|| end.equals(FILE_TYPE_MKV)) {
				fileType = FILE_TYPE_VIDEO;
			} else if (end.equals(FILE_TYPE_JPG)
					|| end.equals(FILE_TYPE_GIF)
					|| end.equals(FILE_TYPE_PNG)
					|| end.equals(FILE_TYPE_JPEG)
					|| end.equals(FILE_TYPE_BMP)) {
				fileType = FILE_TYPE_IMAGE;
			} else if (end.equals(FILE_TYPE_TXT)) {
				fileType = FILE_TYPE_TXT;
			} else if (end.equals(FILE_TYPE_APK)) {
				fileType = FILE_TYPE_APK;
			} else if (end.equals(FILE_TYPE_UMD)) {
				fileType = FILE_TYPE_UMD;
			} else {
				fileType = end; // 如果类型都不匹配，就直接写后缀
			}
		}
		return fileType;
	}

	/**
	 * 获取文件时间
	 * 
	 * @param file
	 * @return
	 */
	public static String getFileLastModified(File file) {
		return DateUtils.dateToString(new Date(file.lastModified()),
				DateUtils.TIME_PATTERN_YMDHM);
	}
	
	/**
	 * 缩放图片
	 * 
	 * @param file
	 * @return
	 */
	public static Bitmap fitSizePic(File file) {
		Bitmap resizeBmp = null;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		// 数字越大读出的图片占用的heap越小 不然总是溢出
		if (file.length() < 20480) { // 0-20k
			opts.inSampleSize = 1;
		} else if (file.length() < 51200) { // 20-50k
			opts.inSampleSize = 2;
		} else if (file.length() < 307200) { // 50-300k
			opts.inSampleSize = 4;
		} else if (file.length() < 819200) { // 300-800k
			opts.inSampleSize = 6;
		} else if (file.length() < 1048576) { // 800-1024k
			opts.inSampleSize = 8;
		} else {
			opts.inSampleSize = 10;
		}
		resizeBmp = BitmapFactory.decodeFile(file.getPath(), opts);
		return resizeBmp;
	}

	/**
	 * 文件大小描述
	 * 
	 * @param file
	 * @return
	 */
	public static String fileSizeMsg(File file) {
		int subIndex = 0;
		String retValue = "";
		if (file.isFile()) {
			long length = file.length();
			if (length >= 1073741824) {
				subIndex = (String.valueOf((float) length / 1073741824))
						.indexOf(".");
				retValue = ((float) length / 1073741824 + "000").substring(0,
						subIndex + 3) + "GB";
			} else if (length >= 1048576) {
				subIndex = (String.valueOf((float) length / 1048576))
						.indexOf(".");
				retValue = ((float) length / 1048576 + "000").substring(0,
						subIndex + 3) + "MB";
			} else if (length >= 1024) {
				subIndex = (String.valueOf((float) length / 1024)).indexOf(".");
				retValue = ((float) length / 1024 + "000").substring(0,
						subIndex + 3) + "KB";
			} else if (length < 1024) {
				retValue = String.valueOf(length) + "B";
			}
		}
		return retValue;
	}

	/**
	 * 获取文件列表大小
	 * 
	 * @param fileList
	 * @return
	 */
	public static String fileSizeMsg(List<File> fileList) {
		int subIndex = 0;
		String retValue = "";
		if (fileList == null || fileList.size() == 0)
			return "0MB";
		File file = null;
		long totalLen = 0;
		for (int i = 0; i < fileList.size(); i++) {
			file = fileList.get(i);
			if (file.isFile()) {
				totalLen = totalLen + file.length();
			}
		}
		if (totalLen >= 1073741824) {
			subIndex = (String.valueOf((float) totalLen / 1073741824))
					.indexOf(".");
			retValue = ((float) totalLen / 1073741824 + "000").substring(0,
					subIndex + 3) + "GB";
		} else if (totalLen >= 1048576) {
			subIndex = (String.valueOf((float) totalLen / 1048576))
					.indexOf(".");
			retValue = ((float) totalLen / 1048576 + "000").substring(0,
					subIndex + 3) + "MB";
		} else if (totalLen >= 1024) {
			subIndex = (String.valueOf((float) totalLen / 1024)).indexOf(".");
			retValue = ((float) totalLen / 1024 + "000").substring(0,
					subIndex + 3) + "KB";
		} else if (totalLen < 1024) {
			retValue = String.valueOf(totalLen) + "B";
		}
		return retValue;
	}

	/**
	 * 新建文件
	 * 
	 * @param file
	 * @return
	 */
	public static boolean createFile(File file) {
		try {
			if (!file.exists())
				file.createNewFile();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 写入文件
	 * 
	 * @param filePath
	 *            文件路劲
	 * @param content
	 *            文件内容
	 * @param isAppend
	 *            是否将文件写到文件末尾（false为否，true为是）
	 * @return
	 */
	public static boolean writeFile(String filePath, String content,
			boolean isAppend) {
		OutputStreamWriter out = null;
		BufferedWriter bw = null;
		try {
			out = new OutputStreamWriter(new FileOutputStream(filePath,
					isAppend), "GBK");
			// fw = new FileWriter(filePath, isAppend);//
			// 创建FileWriter对象，用来写入字符流
			bw = new BufferedWriter(out); // 将缓冲对文件的输出
			bw.write(content); // 写入文件
			bw.flush(); // 刷新该流的缓冲
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	/**
	 * 写入文件
	 * 
	 * @param filePath
	 * @param obj
	 * @return
	 */
	public static boolean writeFile(String filePath, Object obj) {
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(filePath));
			oos.writeObject(obj);
			oos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	/**
	 * 读文件
	 * 
	 * @param filePath
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static ArrayList<HashMap> readFileList(String filePath) {
		ObjectInputStream ois = null;
		ArrayList<HashMap> retList = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(filePath));
			retList = (ArrayList<HashMap>) ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return retList;
	}

	/**
	 * 新建文件夹
	 * 
	 * @param path
	 * @return
	 */
	public static boolean createDir(String path) {
		try {
			new File(path).mkdir();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 复制单个文件
	 * 
	 * @param oldPath
	 * @param newPath
	 * @return
	 */
	public static boolean copyFile(String oldPath, String newPath) {
		try {
			int byteRead = 0;
			String newFileStr = "";
			File oldFile = new File(oldPath);
			if (newPath.endsWith(File.separator)) {
				newFileStr = newPath + oldFile.getName();
			} else {
				newFileStr = newPath + File.separator + oldFile.getName();
			}
			new File(newPath).mkdirs(); // 如果文件夹不存在 则建立新文件夹
			new File(newFileStr).createNewFile(); // 如果文件不存在 则建立新文件
			// 文件存在时
			if (oldFile.exists()) {
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				@SuppressWarnings("resource")
				FileOutputStream fs = new FileOutputStream(newFileStr);
				byte[] buffer = new byte[1444];
				while ((byteRead = inStream.read(buffer)) != -1) {
					fs.write(buffer, 0, byteRead);
				}
				inStream.close();
			}
		} catch (Exception e) {
			return false;

		}
		return true;
	}

	/**
	 * 复制文件夹
	 * 
	 * @param oldPath
	 * @param newPath
	 * @return
	 */
	public static boolean copyDir(String oldPath, String newPath) {
		try {
			File oldFile = new File(oldPath);
			String oldFileStr = "";
			String newFileStr = newPath + File.separator + oldFile.getName();
			new File(newFileStr).mkdirs(); // 如果文件夹不存在 则建立新文件夹
			File[] files = oldFile.listFiles();
			for (int i = 0; i < files.length; i++) {
				oldFileStr = oldPath + File.separator + files[i].getName(); // 要复制的文件夹下的文件
				if (files[i].isFile()) {
					FileUtils.copyFile(oldFileStr, newFileStr);
				} else {
					FileUtils.copyDir(oldFileStr, newFileStr);
				}
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 移动文件到指定目录
	 * 
	 * @param oldPath
	 *            ，如：/a.txt
	 * @param newPath
	 *            ，如：/b/a.txt
	 * @return
	 */
	public static boolean moveFile(String oldPath, String newPath) {
		boolean ret = false;
		try {
			if (FileUtils.copyFile(oldPath, newPath)) {
				new File(oldPath).delete();
				ret = true;
			}
		} catch (Exception e) {
			return false;
		}
		return ret;
	}

	/**
	 * 移动文件夹到指定目录
	 * 
	 * @param oldPath
	 *            ,如：/a
	 * @param newPath
	 *            ,如：/b/a
	 * @return
	 */
	public static boolean moveDir(String oldPath, String newPath) {
		boolean ret = false;
		try {
			if (FileUtils.copyDir(oldPath, newPath)) {
				if (FileUtils.delDir(new File(oldPath))) {
					ret = true;
				}
			}
		} catch (Exception e) {
			return false;
		}
		return ret;
	}

	/**
	 * 删除单个文件
	 * 
	 * @param
	 * @return
	 */
	public static boolean delFile(File file) {
		boolean ret = false;
		try {
			if (file.exists()) {
				file.delete();
				ret = true;
			}
		} catch (Exception e) {
			return false;
		}
		return ret;
	}

	/**
	 * 删除文件夹
	 * 
	 * @param
	 * @return
	 */
	public static boolean delDir(File file) {
		boolean ret = false;
		try {
			if (file.exists()) {
				File[] files = file.listFiles();
				// 删除文件夹内容的所有文件
				for (int i = 0; i < files.length; i++) {
					if (files[i].isDirectory()) {
						if (!delDir(files[i])) {
							return false;
						}
					} else {
						files[i].delete();
					}
				}
				file.delete(); // 删除空文件夹
				ret = true;
			}
		} catch (Exception e) {
			return false;
		}
		return ret;
	}

	/**
	 * 校验输入的文件夹名称是否合法
	 * 
	 * @param newName
	 * @return
	 */
	public static boolean checkDirPath(String newName) {
		boolean ret = false;
		if (newName.indexOf("\\") == -1) {
			ret = true;
		}
		return ret;
	}

	/**
	 * 校验输入的文件名称是否合法
	 * 
	 * @param newName
	 * @return
	 */
	public static boolean checkFilePath(String newName) {
		boolean ret = false;
		if (newName.indexOf("\\") == -1) {
			ret = true;
		}
		return ret;
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean checkFileExists(String filePath) {
		File file = new File(filePath);
		if (file.exists())
			return true;
		else
			return false;
	}

	/**
	 * 计算文件或者文件夹的大小 ，单位 MB
	 * 
	 * @param file
	 *            要计算的文件或者文件夹 ， 类型：java.io.File
	 * @return 大小，单位：MB
	 */
	public double getFolderSize(File file) {
		// 判断文件是否存在
		if (file.exists()) {
			// 如果是目录则递归计算其内容的总大小，如果是文件则直接返回其大小
			if (!file.isFile()) {
				// 获取文件大小
				File[] fl = file.listFiles();
				double ss = 0;
				for (File f : fl)
					ss += getFolderSize(f);
				return ss;
			} else {
				double ss = (double) file.length() / 1024 / 1024;
				System.out.println(file.getName() + " : " + ss + "MB");
				return ss;
			}
		} else {
			System.out.println("文件或者文件夹不存在，请检查路径是否正确！");
			return 0.0;
		}
	}

	/**
	 * 将一个InputStream里面的数据写入到SD卡中
	 * 
	 * @param path
	 * @param fileName
	 * @param input
	 * @return
	 */
	public static File write2SDFromInput(String path, String fileName,
			InputStream input) {
		File file = new File(path + fileName);
		OutputStream output = null;
		try {
			createDir(path);
			createFile(file);
			output = new FileOutputStream(file);
			byte[] buffer = new byte[4 * 1024];
			int length;
			while ((length = (input.read(buffer))) > 0) {
				output.write(buffer, 0, length);
			}
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}
	
	/**
	 * File 转换为 byte[] .
	 * @param file
	 * @return
	 */
	public static byte[] getBytesFromFile(File file){  
		if (file == null){  
			return null;  
		}  
		try{  
			FileInputStream stream = new FileInputStream(file);  
			ByteArrayOutputStream out = new ByteArrayOutputStream(1000);  
			byte[] b = new byte[1000];  
			int n;  
			while ((n = stream.read(b)) != -1)  
				out.write(b, 0, n);  
				stream.close();  
				out.close();  
				return out.toByteArray();  
		} catch (IOException e){  
			e.printStackTrace();  
		}  
		return null;  
	}  
}
