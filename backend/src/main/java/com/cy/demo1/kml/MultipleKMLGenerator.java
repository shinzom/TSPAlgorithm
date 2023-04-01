package com.cy.demo1.kml;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class MultipleKMLGenerator {
    public static String generateKML(double[] x, double[] y, int[] path) {
        StringBuilder kml = new StringBuilder();
        kml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        kml.append("<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n");
        kml.append("<Document>\n");

        // 添加所有的点
        for (int i = 0; i < x.length; i++) {
            kml.append("<Placemark>\n");
            kml.append("<name>Point " + (i+1) + "</name>\n");
            kml.append("<Point>\n");
            kml.append("<coordinates>" + y[i] + "," + x[i] + "</coordinates>\n");
            kml.append("</Point>\n");
            kml.append("</Placemark>\n");
        }

        // 添加所有的路径
        for (int i = 0; i < path.length - 1; i++) {
            kml.append("<Placemark>\n");
            kml.append("<name>Path " + (i+1) + "</name>\n");
            kml.append("<LineString>\n");
            kml.append("<coordinates>\n");
            kml.append(y[path[i]] + "," + x[path[i]] + "\n");
            kml.append(y[path[i+1]] + "," + x[path[i+1]] + "\n");
            kml.append("</coordinates>\n");
            kml.append("</LineString>\n");
            kml.append("</Placemark>\n");
        }

        kml.append("</Document>\n");
        kml.append("</kml>\n");
        return kml.toString();
    }

    public static void writeKMLFile(String kml, String filename) throws IOException {
        File file = new File(filename);
        FileWriter writer = new FileWriter(file);
        writer.write(kml);
        writer.close();
    }

    public static void generateMultipleKML(double[] x, double[] y, int[][] path, String zipFileName) throws IOException {
        // 创建一个zip输出流
        try (FileOutputStream fos = new FileOutputStream(zipFileName);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            // 循环遍历每一条路径
            for (int i = 0; i < path.length; i++) {
                // 生成KML字符串
                String kml = generateKML(x, y, path[i]);
                // 创建一个KML文件
                String kmlFileName = "path_" + i + ".kml";
                writeKMLFile(kml, kmlFileName);
                // 将KML文件添加到zip输出流中
                try (FileInputStream fis = new FileInputStream(kmlFileName)) {
                    ZipEntry zipEntry = new ZipEntry(kmlFileName);
                    zos.putNextEntry(zipEntry);
                    byte[] bytes = new byte[1024];
                    int length;
                    while ((length = fis.read(bytes)) >= 0) {
                        zos.write(bytes, 0, length);
                    }
                    zos.closeEntry();
                }
                // 删除临时的KML文件
                File kmlFile = new File(kmlFileName);
                kmlFile.delete();
            }

            zos.finish(); // 添加这一行
        }
    }


}
