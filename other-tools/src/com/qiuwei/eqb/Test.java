package com.qiuwei.eqb;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class Test {
    public static void main(String[] args) {
        new Test().testDoGetForOpenApi();
        new Test().cjyhzs();
    }

    /**
     * 创建内部用户
      */

    public void testDoGetForOpenApi() {
        String url = "http://47.96.112.93:8888/V1/accounts/innerAccounts/create";
        Map params = new HashMap();
        ArrayList accountList = new ArrayList<String>();
        params.put("accountInfos",accountList);
        Map jsonObject = new HashMap();
        accountList.add(jsonObject);
        jsonObject.put("contactsEmail", "123456@163.com");
        jsonObject.put("contactsMobile", "15757000000");
        jsonObject.put("licenseNumber", "330102201603075032");
        jsonObject.put("licenseType", "IDCard");
        jsonObject.put("loginEmail", "123456@163.com");
        jsonObject.put("loginMobile", "15757000000");
        jsonObject.put("name", "cscsname");
        jsonObject.put("uniqueId", "cscsid");
        jsonObject.put("password", "cscsid");
        String result2 = postMessage( url, params);
        System.out.println(result2);
        //{"errCode":0,"msg":"success","errShow":true,"data":[{"uniqueId":"cscsid","accountId":"0213a3d2-8bfa-4d7d-829a-5ab7847e0491","status":0,"msg":"成功"}]}
    }

    /**
     * 创建内部用户证书
     * @return
     */
    private void cjyhzs(){
        String url = "http://47.96.112.93:8888/V1/certs/innerCerts/create";
        Map params = new HashMap();
        params.put("uniqueId","cscsid");
        params.put("accountId","0213a3d2-8bfa-4d7d-829a-5ab7847e0491");
        String result2 = postMessage( url, params);
        System.out.println(result2);
        //{"errCode":0,"msg":"success","errShow":true,"data":{"certId":"3e7d31d8-d7ed-40a9-9498-cf633940d8ef"}}
    }
    private String postMessage(String url, Map params) {
        String projectkey = "1001002";
        String secret = "a13259d1f84bcd9fc37b8c32033e1e3d";
        String signature = HmacSHA256Utils.hmacSha256(JSONUtil.toJsonStr(params), secret);
        return HttpRequest.post(url)
                .header("x-timevale-project-id", projectkey)
                .header("x-timevale-signature", signature)
                .body(JSONUtil.toJsonStr(params))
                .timeout(20000)
                .execute().body();
    }
//{"errCode":0,"msg":"success","errShow":true,"data":[{"uniqueId":"cscsid","accountId":"0213a3d2-8bfa-4d7d-829a-5ab7847e0491","status":0,"msg":"成功"}]}

    /**
     * 示例演示的是文件上传的过程，先通过post请求获取文件直传地址，然后通过文件直传地址上传文件
     * @throws IOException
     *//*
    @Test
    public void testDoPutForUploadFile() throws IOException {
        //读文档
        File file = new File("D:\\测试.pdf");
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[fis.available()];
        fis.read(data);
        fis.close();

        //获取上传文档直传地址
        String md5 = MD5Util.md5(data);
        int length = data.length;
        Map<String,String> heads = new HashMap<>();
        heads.put("x-timevale-project-id","1000003");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("contentLength",length);
        jsonObject.put("fileMD5",md5);
        jsonObject.put("docType","Pdf");

        String signature = HmacSHA256Utils.hmacSha256(jsonObject.toJSONString(), "2ffb638e64e364103edf927411f087e4");
        heads.put("x-timevale-signature",signature);
        String result = HttpUtil.doPost("http://127.0.0.1:8035/esignpro/rest/filemanage/getUploadUrl", heads, jsonObject.toJSONString());

        //文件直传
        JSONObject resultObject = JSON.parseObject(result);
        JSONObject fileData = resultObject.getJSONObject("data");
        //文件filekey
        String fileKey = fileData.getString("fileKey");
        String url = fileData.getString("url");
        Map<String,String> putHeads = new HashMap<>();
        putHeads.put("Content-Type","application/octet-stream");
        putHeads.put("Content-MD5",md5);
        String s = HttpUtil.doPut(url, putHeads, null, data);
        System.out.println(s);
    }

    *//**
     * 该示例演示的是直接进行文件上传，不通过文件直传地址进行上传文件，文件上传接口加了白名单，无需计算秘钥
     * @throws IOException
     *//*
    @Test
    public void testDoPutForUploadFile2() throws IOException {
        //读文档
        File file = new File("D:\\测试.pdf");
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[fis.available()];
        fis.read(data);
        fis.close();

        Map<String,String> heads = new HashMap<>();
        heads.put("x-timevale-project-id","1000003");

        String result = HttpUtil.doPostFile("http://127.0.0.1:8035/V1/files/upload", data, "file", "劳动合同.pdf", heads, null);
        System.out.println(result);
    }*/
}