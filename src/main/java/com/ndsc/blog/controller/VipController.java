package com.ndsc.blog.controller;

import com.alipay.api.*;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.*;
import com.ndsc.blog.config.AlipayConfig;
import com.ndsc.blog.entity.Order;
import com.ndsc.blog.entity.Vip;
import com.ndsc.blog.mapper.UsersafeMapper;
import com.ndsc.blog.mapper.VipMapper;
import com.ndsc.blog.service.LoginService;
import com.ndsc.blog.service.OrderService;
import com.ndsc.blog.service.VipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Author 扶明方
 * @Date 2019/9/11 15:20
 * @Version 1.0
 */
@RestController
public class VipController {

    @Autowired
    OrderService orderService;
    @Autowired
    LoginService loginService;
    @Autowired
    VipService vipService;
    @Autowired
    UsersafeMapper usersafeMapper;

    Order order = new Order();

    @RequestMapping("isVip")
    public int isVip(HttpServletRequest request) {
        //获取id
        HttpSession session = request.getSession();
        String userName = (String) session.getAttribute("userName");
        int id = usersafeMapper.selectUserId(userName);
        return vipService.isVip(id);
    }

    @RequestMapping("/pay")
    public String pay(Integer vipId, String orderNo, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, AlipayApiException {
        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);

        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(AlipayConfig.return_url);
        alipayRequest.setNotifyUrl(AlipayConfig.notify_url);
        Vip vip = vipService.selectById(vipId);
        System.out.println(vip);
        //生成订单号

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = new String(orderNo.getBytes(), "UTF-8");
        //付款金额，必填
        String total_amount = new String(String.valueOf(vip.getVipPrice()).getBytes(), "UTF-8");
        //订单名称，必填
        String subject = new String(vip.getVipName().getBytes(), "UTF-8");
        //商品描述，可空
        String body = new String(vip.getVipDescription().getBytes(), "UTF-8");

        order.setProductName(subject);
        order.setOrderTotal(Integer.parseInt(total_amount));
        //获取id
        HttpSession session = request.getSession();
        String userName = (String) session.getAttribute("userName");
        int id = usersafeMapper.selectUserId(userName);
        order.setUserId(id);

        alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\","
                + "\"total_amount\":\"" + total_amount + "\","
                + "\"subject\":\"" + subject + "\","
                + "\"body\":\"" + body + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        //若想给BizContent增加其他可选请求参数，以增加自定义超时时间参数timeout_express来举例说明
        //alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
        //		+ "\"total_amount\":\""+ total_amount +"\","
        //		+ "\"subject\":\""+ subject +"\","
        //		+ "\"body\":\""+ body +"\","
        //		+ "\"timeout_express\":\"10m\","
        //		+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        //请求参数可查阅【电脑网站支付的API文档-alipay.trade.page.pay-请求参数】章节

        //请求
        String result = alipayClient.pageExecute(alipayRequest).getBody();
        System.out.println(result);
        return result;
    }

    @RequestMapping("/return_url")
    public String returnUrl(HttpServletRequest request) throws AlipayApiException, UnsupportedEncodingException {
        //获取支付宝GET过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名

        //——请在这里编写您的程序（以下代码仅作参考）123456——
        if (signVerified) {
            //商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");

            //支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");

            //付款金额
            String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");

            System.out.println("trade_no:" + trade_no + "<br/>out_trade_no:" + out_trade_no + "<br/>total_amount:" + total_amount);

            //插入订单表
            orderService.insertOrder(order);
            //修改用户角色
            //获取id
            HttpSession session = request.getSession();
            String userName = (String) session.getAttribute("userName");
            int id = usersafeMapper.selectUserId(userName);
            loginService.becomeVip(id);
        } else {
            System.out.println("验签失败");
        }

        //支付完成后跳转到购买页
        return "<a id=\"ak\" href='paySuccess.html'>跳转</a>\n" +
                "<script>\n" +
                "    document.getElementById(\"ak\").click();\n" +
                "</script>";
        //——请在这里编写您的程序（以上代码仅作参考）——
    }

    @RequestMapping("/getAllVip")
    public List<Vip> selectAll() {
        return vipService.selectAll();
    }

}
