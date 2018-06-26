package com.zitopay.gateway.channel;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
/**
 * For third-party interface complex parameters to generate operating entities
 * Simplify the complex low-level workload
 * Desktop new dto.txt file, run the method
 * Details of the parameters can be found in the comments
 * Author: shaomingshuai
 * Time: 2016.11.23
 * Version: V1.0
 */
public class DtoDealWith {
	
	/**变量中最小的长度*/
	private static final int 	len =3;
	/**通用变量中表达式*/
	private static final String exception1 = "[a-z0-9_A-Z]*";
	/**首个字符验证表达式*/
	private static final String exception2 = "[_a-zA-Z]";
	/**要剔除的变量*/
	private static final String del = "";
	/**实体变量名*/
	private static final String ObjNmae="in";
	
	/**
	 * 按行读取txt文件
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		final InputStream input = new FileInputStream("D:\\dto.txt");// 将D:/test.txt文件读取到输入流中
		/**--------------------------------------------------------------------------------**/
		String javaString = File(input);
		System.out.println("创建实体\r" + javaString);
		
		if (!ObjNmae.equals("")) {
			System.out.println("创建Set");
			String set = set(javaString,ObjNmae);
			System.out.println(set);
		}
		if (!ObjNmae.equals("")) {
			System.out.println("创建Get");
			String set = get(javaString,ObjNmae);
			System.out.println(set);
		}
		if (!ObjNmae.equals("")) {
			System.out.println("创建Map");
			String set = putMap(javaString,ObjNmae);
			System.out.println(set);
		}
		if (!ObjNmae.equals("")) {
			System.out.println("创建from");
			String set = fromInput(javaString);
			System.out.println(set);
		}
	}
	/**
	 * 获取文件解析参数
	 * @param exception1 整体验证变量名正则
	 * @param exception2 首字母验证验证变量名正则
	 * @param len 最短字段的一个长度
	 * @param nameString  类名称
	 * @param input 文件流
	 * @return
	 */
	private static String File(InputStream input){
		String javaString = "";
		try {
			// 创建BufferedReader，以UTF-8的编码方式读取文件
			BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
			String line = null;
			// 按行读取文本，直到末尾（一般都这么写）
			while ((line = reader.readLine()) != null) {
				String value = si(line);
				javaString = javaString + value;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return javaString;
	}
	/**
	 * 对每一行的字段数据做解析 生成字段和注释  整行内容将作为注释插入
	 * @param line  行数据字符串
	 * @param exception1 整体验证变量名正则
	 * @param exception2 首字母验证验证变量名正则
	 * @param len 最短字段的一个长度
	 * @return
	 */
	private static String si(String line){
		line = line.replaceAll("\t", " ");//去掉 制表符
		line = line.replaceAll("   ", "  ");//去掉多余的空格
		int stan = 0;//起始位置
		String value = "";//存放临时的字段名--不一定是最终的
		String javaString = "";//全部字段拼接数据存放
		boolean boo = true;//判断删除状态
		/**
		 *从该字符串第一个字符开始足个判断是否满足 exceptiond所限定的条件 
		 *满足则存入value中
		 *当读取到一个不满足的字符时 则判断value的长度是否满足 len最小长度条件
		 *满足则开始生成字段
		 *否则跳过继续
		 *直到 找到合适的变量或者没有变量后返回
		 */
		for (int i = 1; i < line.length()+1; i++) {
			boo = true;
			String str2 = line.substring(stan,i);//这里截取的信息就是e，倒数第二个字符
			boolean isWord=str2.matches(exception1);
			if (isWord) {
				if (((String) str2.subSequence(0,1)).matches(exception2)) {//
					value = str2;
				}
			}else {
				if (value.length() >= len) {
					if (!value.equals(del)) {
						javaString=javaString +"\tprivate String "+ value+";//";
						javaString = javaString + line + "\n";
					}else {
						System.out.println("该行为被删除字段行");
						System.out.println(line);
						boo = false;
					}
					i = line.length()+1;
				}else {
					javaString = "";
				}
				stan++;
			}
		}
		if (("".equals(javaString) || javaString ==null) && boo) {
			System.out.println("此行没有找到合适的变量：\n"+line);
			javaString = javaString + "\t//此行备用\n";
		}
		return javaString;
	}
	/**
	 * 输入类
	 * @param javaString
	 * @param ObjName
	 * @return
	 */
	private static String set(String javaString,String ObjName){
		String[] vlueStrings = javaString.split("\n");
		String value = "";
		for (int i = 0; i < vlueStrings.length; i++) {
			try {
				char aA = vlueStrings[i].charAt(16);
				StringBuffer sb = new StringBuffer();
				StringBuffer va = new StringBuffer();
				va.append(vlueStrings[i]).setCharAt(16, sb.append(Character.toUpperCase(aA)).toString().toCharArray()[0]);
				value = value + (va.toString().replace("private String ", ObjName + ".set") + ";\n").replace(";//", "(\"\");//");
			} catch (Exception e) {
				value = value + "抛出不能识别语句：" + vlueStrings[i] + "\n";
			}
		}
		return value;
	}
	/**
	 * 输出类
	 * @param javaString
	 * @param ObjName
	 * @return
	 */
	private static String get(String javaString,String ObjName){
		String[] vlueStrings = javaString.split("\n");
		String value = "";
		for (int i = 0; i < vlueStrings.length; i++) {
			try {
				String name = getNameVlaue(vlueStrings[i],true);
				String sb = (vlueStrings[i].toString().replace(";", " = " + ObjNmae + ".get" + name + "();")) + "\n";
				value = value + sb;
			} catch (Exception e) {
				e.printStackTrace();
				value = value + "抛出不能识别语句：" + vlueStrings[i] + "\n";
			}
		}
		return value;
	}
	/**
	 * 计算get方法数据 头名称
	 * @param line 数据
	 * @param type true 转换首字母大写 否则默认文档大小写格式
	 * @return
	 */
	private static String getNameVlaue(String line,boolean type){
		/**
		 * 从该字符串第一个字符开始足个判断是否满足 exceptiond所限定的条件 满足则存入value中 当读取到一个不满足的字符时
		 * 则判断value的长度是否满足 len最小长度条件 满足则开始生成字段 否则跳过继续 直到 找到合适的变量或者没有变量后返回
		 */
		line = line.replace("private String ", "");
		int stan = 0;// 起始位置
		String value = "";// 存放临时的字段名--不一定是最终的
		String javaString = "";// 全部字段拼接数据存放
		boolean boo = true;// 判断删除状态
		for (int i = 1; i < line.length() + 1; i++) {
			boo = true;
			String str2 = line.substring(stan, i);// 这里截取的信息就是e，倒数第二个字符
			boolean isWord = str2.matches(exception1);
			if (isWord) {
				if (((String) str2.subSequence(0, 1)).matches(exception2)) {
					value = str2;
				}
			} else {
				if (value.length() >= len) {
					if (!value.equals(del)) {
						char aA = value.charAt(0);
						StringBuffer sb = new StringBuffer();
						StringBuffer va = new StringBuffer();
						if (type) {
							va.append(value).setCharAt(0, sb.append(Character.toUpperCase(aA)).toString().toCharArray()[0]);
						}else {
							va.append(value);
						}
						return va.toString();
					} else {
						System.out.println("改行为被删除字段行");
						System.out.println(line);
						boo = false;
					}
					i = line.length() + 1;
				} else {
					javaString = "";
				}
				stan++;
			}
		}
		if (("".equals(javaString) || javaString == null) && boo) {
			return "empty|empty|empty|empty";
		}
		return "error|error|error|error";
	}
	
	/**
	 * 获取map输入类
	 * @param javaString
	 * @param ObjName
	 * @return
	 */
	private static String putMap(String javaString,String ObjName){
		String[] vlueStrings = javaString.split("\n");
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < vlueStrings.length; i++) {
			try {
				stringBuffer.append("\t" + ObjName + ".put(\"" + getNameVlaue(vlueStrings[i],false) + "\" , \"\");//" + vlueStrings[i].toString().split(";//")[1] +"\r");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return stringBuffer.toString();
	}
	
	
	
	
	private static String fromInput(String javaString){
		StringBuffer input = new StringBuffer();
		input.append("<!DOCTYPE html>\r<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><html><body>\r    <form action=\"\" method=\"GET\">\r");
		String[] vlueStrings = javaString.split("\n");
		for (int i = 0; i < vlueStrings.length; i++) {
			input.append("        <input type=\"text\" value=\"\" id=\""+ getNameVlaue(vlueStrings[i],false) + "\" name=\""+ getNameVlaue(vlueStrings[i],false) + "\">" + vlueStrings[i].split("//")[1]+"<br/> \r");
		}
		input.append("       <input type=\"submit\" value=\"提交\">\r    </form>\r</body>\r</html>");
		return input.toString();
	}
}
