package com.leyou.goods.service;

import com.leyou.common.util.ThreadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

@Service
public class GoodsHtmlService {
    @Autowired
    private TemplateEngine engine;

    @Autowired
    private GoodsService goodsService;

    public void createHtml(Long spuId) {
        Context context = new Context();
        context.setVariables(this.goodsService.loadData(spuId));
        PrintWriter printWriter = null;
        try {
            //把静态文件生成到服务器本地
            File file = new File("D:\\a_install\\idea\\workspace2\\tools\\nginx-1.14.0\\html\\item\\" + spuId + ".html");

            printWriter = new PrintWriter(file);

            this.engine.process("item", context, printWriter);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }
    }

    public void asyncExecute(Long spuId){
        ThreadUtils.execute(() -> createHtml(spuId));
        /*ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                createHtml(spuId);
            }
        });*/
    }

    //也可以像上面一样使用线程池防止阻塞
    public void deleteHtml(Long id) {
        File file = new File("D:\\a_install\\idea\\workspace2\\tools\\nginx-1.14.0\\html\\item\\" + id + ".html");
        file.deleteOnExit();
    }
}
