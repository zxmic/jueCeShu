package com.jueCeShu.controller;

import com.jueCeShu.decide_tree.DE_tree;
import com.jueCeShu.decide_tree.treeNode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/file/")
public class FileController {
    String result;
    @RequestMapping("upload")
    @ResponseBody
    public ModelAndView upload (@RequestParam("filedata") MultipartFile filedata,
                                @RequestParam("filetest") MultipartFile filetest,
                                Map<String, treeNode> all, Map<String,String> map) {
        // 获取上传的文件原始名字
        if(filedata.isEmpty()||filetest.isEmpty()){
            map.put("msg","文件名称有误，请重新上传");
            return new ModelAndView("upload.html");
        }
        String filedataName = filedata.getOriginalFilename();
        String filetestName = filetest.getOriginalFilename();
        // 获取后缀名
        String suffixNamedata = filedataName.substring(filedataName.lastIndexOf("."));
        String suffixNametest = filetestName.substring(filetestName.lastIndexOf("."));
        if(suffixNamedata==null||!suffixNamedata.equals(".txt" )
                ||suffixNametest==null||!suffixNametest.equals(".txt")){
            map.put("msg","文件格式有误，请重新上传");
            return new ModelAndView("upload.html");
        }
        // 文件保存路径
        String filePath = "/home/zx/opt/upload/";
        // 文件重命名，防止重复
        String ppth= filePath + UUID.randomUUID();
        System.out.println(ppth);
        filedataName = ppth + filedataName;
        filetestName = ppth + filetestName;
        //定义输出结果txt
        result=ppth+"result.txt";
        // fileName是保存在服务器的文件的名称
        // 文件对象
        File destdata = new File(filedataName);
        File desttest = new File(filetestName);
        File destresult = new File(result);
        // 判断路径是否存在，如果不存在则创建
        if(!destdata.getParentFile().exists()||!desttest.getParentFile().exists()
                ||!destresult.getParentFile().exists()) {
            destdata.getParentFile().mkdirs();
            desttest.getParentFile().mkdirs();
            destresult.getParentFile().mkdirs();
        }
        try {
            // 保存到服务器中
            filedata.transferTo(destdata);
            filetest.transferTo(desttest);
            String data=filedataName;//训练数据集
            String test=filetestName;//测试数据集
            //String result="/home/zx/DAI/javawjj/my_game_m/src/result.txt";//预测结果集
            //id里面保存所有属性的值 node
            System.out.println("初始化数据前");
            System.out.println("训练数据集名"+data);
            System.out.println("测试数据集名"+test);
            //初始化
            DE_tree deTree=new DE_tree(data,test);
            //构建并输出决策树
            deTree.print(deTree.getDate());
            //预测数据并输出结果
            deTree.testdate(deTree.test,result);
            treeNode node=deTree.creattree(deTree.getDate());
//            node.getsname();
//            node.label.get(1);
//            node.node.get(1);

            System.out.println("初始化数据后");
            //all.put("id",node);
            map.put("msg","二叉树已构建完成");
            ModelAndView model=new ModelAndView("download.html");
            //model.addObject("node",node);
            return model;
        } catch (Exception e) {
            System.out.println("文件try有问题");
            e.printStackTrace();
        }
        map.put("msg","上传失败，请重新上传");
        return new ModelAndView("upload.html");
    }




    @RequestMapping("download")
    public void download(HttpServletResponse response) throws Exception {
        // 文件地址，真实环境是存放在数据库中的
        File file = new File(result);
        // 穿件输入对象
        FileInputStream fis = new FileInputStream(file);
        // 设置相关格式
        response.setContentType("application/force-download");
        // 设置下载后的文件名以及header
        response.addHeader("Content-disposition", "attachment;fileName=" + "ppp.txt");
        // 创建输出对象
        OutputStream os = response.getOutputStream();
        // 常规操作
        byte[] buf = new byte[1024];
        int len = 0;
        while((len = fis.read(buf)) != -1) {
            os.write(buf, 0, len);
        }
        fis.close();
    }
}
