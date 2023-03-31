package com.cy.demo1.kml;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class KmlUtils {

    //coordinates(经纬度：经度,纬度,高度)代表一个地理信息的对象集合（自定义）  生成kml的名称
    public static boolean setTravelsKml(String coordinates, String fileName) throws IOException {
        Element root = DocumentHelper.createElement("kml");

        Document document = DocumentHelper.createDocument(root);
        //根节点添加属性
        /*这里用namespace是因为当时xmlns这个属性添加不上去，所以用这个方法可行
         * Namespace namespace = Namespace.get("http://www.opengis.net/kml/2.2");
         *  root.add(namespace);
         * */
        Namespace namespace = Namespace.get("http://www.opengis.net/kml/2.2");
        root.addAttribute("xmlns", "http://www.opengis.net/kml/2.2");
        // .addAttribute("xmlns:gx", "http://www.google.com/kml/ext/2.2")
        //  .addAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance")
        // .addAttribute("xsi:schemaLocation",
        //     "http://www.opengis.net/kml/2.2 http://schemas.opengis.net/kml/2.2.0/ogckml22.xsd http://www.google.com/kml/ext/2.2 http://code.google.com/apis/kml/schema/kml22gx.xsd");
        root.add(namespace);
        Element documentElement = root.addElement("Document");
        // documentElement.addElement("name").addText(fileName);
        // documentElement.addElement("Snippet").addText("");
        // Element folderElement = documentElement.addElement("Document");//添加一个目录
        // folderElement.addAttribute("id", "FeatureLayer0");
        documentElement.addElement("name").addText("轨迹点位"); //名称
        //folderElement.addElement("Snippet").addText(""); //显示在Google Earth之中的对description的简短概要.


        //生成点位图标数据
        int i = 1;
        // for (String travelRecord:coordinates) {
        //  i++;
        Element placeMarkElement = documentElement.addElement("Placemark");//在文件夹中添加一个地标
        //placeMarkElement.addAttribute("id", String.valueOf(i));
        placeMarkElement.addElement("name").addText("点位" + String.valueOf(i));
        placeMarkElement.addElement("styleUrl").addText("#randomColorRegion");
        // placeMarkElement.addElement("Snippet").addText("");
        placeMarkElement.addElement("description").addText("R");//简介
        //placeMarkElement.addElement("styleUrl").addText("#MyStyle");//风格
        Element pointElement = placeMarkElement.addElement("Polygon");
        // Element pointElement = placeMarkElement.addElement("Point");
        Element outerBoundaryIs1 = pointElement.addElement("outerBoundaryIs");
        Element LinearRing1 = outerBoundaryIs1.addElement("LinearRing");
        //分解
        // String[] con = travelRecord.split(",");
        //添加点位的经纬度坐标以及高度(显示时绘制高度m)
        LinearRing1.addElement("coordinates").addText(coordinates);//可以是是任何几何形状的子元素，定义每一个点的经度、纬度和高度(按照严格的顺序). 多个点使用空格隔开，经纬度按照WGS84标准.
        //  }


        //生成轨迹线路径数据
//        Element lineElement = documentElement.addElement("Placemark");//在文件夹外添加一个地标
//        lineElement.addElement("name").addText("轨迹线");
//        lineElement.addElement("description").addText("");
//        lineElement.addElement("styleUrl").addText("#MyStyle");
//        Element pointElement1 = lineElement.addElement("LineString");
//        pointElement1.addElement("altitudeMode").addText("absolute");
//        pointElement1.addElement("extrude").addText("1");
//        pointElement1.addElement("tessellate").addText("1");
        String linedata = "";
        //每个坐标以及高度用换行符或空格分开
        /*for (TravelRecord travelRecord:travelRecords) {
            linedata =linedata+"\n"+ travelRecord.lng+","+travelRecord.lat+",30";
        }*/
        //pointElement.addElement("coordinates").addText("115.86602,25.70925");
        //生成显示风格
        //Element styleElement = documentElement.addElement("Style");//Style节点
        //styleElement.addAttribute("id", "MyStyle");
        // IconStyle 图标风格
        //Element iconStyleElement = styleElement.addElement("IconStyle");
        // Element iconElement = iconStyleElement.addElement("Icon");
//	        iconElement.addElement("href").addText("http://192.168.10.108:8080/images/mark_b.png");//在线图标
        //iconStyleElement.addElement("scale").addText("0.250000");
        // LabelStyle  标签风格
        // Element labelStyleElement = styleElement.addElement("LabelStyle");
        // labelStyleElement.addElement("color").addText("00000000");
        //labelStyleElement.addElement("scale").addText("0.000000");
        // PolyStyle 图形风格
        Element Style = placeMarkElement.addElement("Style");
        Element polyStyleElement = Style.addElement("PolyStyle");
        polyStyleElement.addElement("color").addText("96ffff00");
        //polyStyleElement.addElement("outline").addText("0");

        // LineStyle  路径线风格
     /*  Element lineStyleElement = styleElement.addElement("LineStyle");
        lineStyleElement.addElement("color").addText("7f00ffff");
        lineStyleElement.addElement("width").addText("4");*/

        //创建kml到本地
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("utf-8");
        XMLWriter xmlWriter = new XMLWriter(new FileOutputStream("D:/Desktop/TSPAlgorithm-backend/TSPAlgorithm-backend/kmlExportFile/airportData/" + fileName + "_1.kml"), format);

        xmlWriter.write(document);

        xmlWriter.close();
        //开始对文件进行压缩，一个kml文件其实是一个压缩文件，里面包含一个kml文件和一个png图标
        String[] strs = new String[2];
        strs[0] = "D:/Desktop/TSPAlgorithm-backend/TSPAlgorithm-backend/kmlExportFile/Travels/" + fileName + "_1.kml";
        strs[1] = "D:/Desktop/TSPAlgorithm-backend/TSPAlgorithm-backend/kmlExportFile/Travels/images/img_mark.png";//这里写图片的路径  如果使用在线图标这段代码屏蔽
        writeKml(strs, "D:/Desktop/TSPAlgorithm-backend/TSPAlgorithm-backend/kmlExportFile/Travels/" + fileName);//压缩

        return true;

    }


    public static void writeKml(String[] strs, String kmlName) throws IOException {
        String[] files = strs;
        OutputStream os = new BufferedOutputStream(new FileOutputStream(kmlName + ".kml"));
        ZipOutputStream zos = new ZipOutputStream(os);
        byte[] buf = new byte[8192];
        int len;
        for (int i = 0; i < files.length; i++) {
            File file = new File(files[i]);
            if (!file.isFile())
                continue;
            ZipEntry ze = new ZipEntry(file.getName());
            zos.putNextEntry(ze);
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            while ((len = bis.read(buf)) > 0) {
                zos.write(buf, 0, len);
            }
            zos.closeEntry();
            bis.close();
        }

        zos.closeEntry();
        zos.close();
        os.close();

    }
}

