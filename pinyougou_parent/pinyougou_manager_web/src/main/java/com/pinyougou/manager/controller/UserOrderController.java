package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pinyougou.order.service.OrderService;
import com.pinyougou.pojo.TbOrder;
import com.pinyougou.pojo.TbOrderItem;
import com.pinyougou.pojogroup.Order;
import com.pinyougou.utils.FileUtils;
import entity.Result;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/userOrder")
public class UserOrderController {
    @Reference
    private OrderService orderService;
    @Autowired
    private HttpServletResponse response;
    @Autowired
    private HttpServletRequest request;

    @RequestMapping("/findOrderAndOrderItem")
    public Result findOrderAndOrderItem() {
        String OrderStr = request.getParameter("Order");

        Order border = JSON.parseObject(OrderStr, Order.class);


        List<TbOrder> orderList = orderService.findOrderAndOrderItem(border);
        String sheetName = "订单和订单详情";
        int columnNumber = 35;//列数
        int[] columnWidth = {20, 10, 10, 10, 10, 30, 30, 10, 10, 10,
                10, 10, 10, 10, 10, 10, 10, 10, 10, 10,
                10, 10, 10, 10, 10, 10, 10, 10, 10, 10,
                10, 10, 10, 10, 10}; //列宽
        String[] columnName = {"订单id", "实付金额", "支付类型", "邮费", "状态",
                "订单创建时间", "订单更新时间", "付款时间", "发货时间", "交易完成时间",
                "交易关闭时间", "物流名称", "物流单号", "用户id", "买家留言",
                "买家昵称", "买家是否已经评价", "收货人地区名称街道", "收货人手机", "收货人邮编",
                "收货人", "过期时间", "发票类型", "订单来源", "商家ID",
                "id", "商品id", "SPU_ID", "订单id", "商品标题",
                "商品单价", "商品购买数量", "商品总金额", "商品图片地址", "商家名称"};
        try {
        // 生成Excel文件
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        // 创建Sheet
        XSSFSheet sheet = xssfWorkbook.createSheet(sheetName);
        // 表头
        XSSFRow headRow = sheet.createRow(0);

        //创建一个样式对象 开始设置excel的样式
        XSSFCellStyle cellStyle = xssfWorkbook.createCellStyle();
        //标题设置成为粗体
        XSSFFont font = xssfWorkbook.createFont();
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        //标题设置字体颜色为红色
//		short color = HSSFColor.RED.index;
        short color = IndexedColors.RED.getIndex();
        //设置字体颜色为红色
        font.setColor(color);
        //将font对象赋给样式
        cellStyle.setFont(font);
        //设置列的宽度
//        sheet.setColumnWidth(0, 1000);
//        sheet.setColumnWidth(1, 3000);
//        sheet.setColumnWidth(2, 3000);

        for (int i = 0; i < columnNumber; i++) {
            for (int j = 0; j <= i; j++) {
                if (i == j) {
                    sheet.setColumnWidth(i, columnWidth[j] * 256); // 单独设置每列的宽
                }
            }
        }


        // 第四.一步，创建表头的列
        for (int i = 0; i < columnNumber; i++) {
            XSSFCell cell = headRow.createCell(i);
            cell.setCellValue(columnName[i]);
            cell.setCellStyle(cellStyle);
        }



            // 表格数据
            for (TbOrder order : orderList) {

                List<TbOrderItem> orderItemList = order.getOrderItemList();
                for (TbOrderItem orderItem : orderItemList) {
                    // 设置Sheet中最后一行的行号+1，或者for循环的时候用索引for(int i=0;i<wayBills.size();i++)，用i的形式创建行号
                    XSSFRow dataRow = sheet.createRow(sheet.getLastRowNum() + 1);

                    //反射方法
                    Class orderClass = order.getClass();
                    Field[] fields = orderClass.getDeclaredFields();
                    int i = 0;
                    for (Field field : fields) {
                        Method m = (Method) orderClass.getMethod("get" + getMethodName(field.getName()));
                        if (m.invoke(order)!=null) {
                            if (!"getOrderItemList".equals(m.getName())) {
                                dataRow.createCell(i).setCellValue(m.invoke(order).toString());
                            }
                        }else {
                            dataRow.createCell(i).setCellValue("null");
                        }
                        i++;
                    }

                    int j = -1;

                    Class orderItemClass = orderItem.getClass();
                    Field[] Fields = orderItemClass.getDeclaredFields();

                    for (Field field : Fields) {
                        Method m = (Method) orderItemClass.getMethod("get" + getMethodName(field.getName()));

                        if (m.invoke(orderItem)!=null) {
                            dataRow.createCell(i+j).setCellValue(m.invoke(orderItem).toString());
                        }else {
                            dataRow.createCell(i+j).setCellValue("null");
                        }
                        j++;
                    }

                }
            }


        // 下载导出 上传下载的两头一流1.Content-Type 2.Content-Disposition 3.输出流
        // 设置头信息 xls的MIME:application/vnd.ms-excel,将所有的Xssf改成Hssf

        response.setContentType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        String filename = "品牌数据.xlsx";
        String agent = request.getHeader("user-agent");

        filename = FileUtils.encodeDownloadFilename(filename, agent);
        response.setHeader("Content-Disposition",
                "attachment;filename=" + filename);

        // 将Excel文档写到输出流中
        ServletOutputStream outputStream = response.getOutputStream();
        xssfWorkbook.write(outputStream);

        // 关闭
        xssfWorkbook.close();


        //return orderList;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"导出失败");
        }
        return new Result(true,"导出成功");
    }

    // 把一个字符串的第一个字母大写、效率是最高的、
    private static String getMethodName(String fildeName) throws Exception{
        byte[] items = fildeName.getBytes();
        items[0] = (byte) ((char) items[0] - 'a' + 'A');
        return new String(items);
    }
}