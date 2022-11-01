package com.example.demo;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@SpringBootTest
class DemoApplicationTests {

    private String schemaNameStr = "ihr360_message_center,schame02_message_center_prod,fy_message_center_prod,by_message_center_prod,template_ihr360_message_center,yy_message_center_prod,aj_message_center_prod,db_message_center_prod,kjdy_message_center_prod,zsx_message_center_prod,tel_message_center_prod,rc_message_center_prod,xdmy_message_center_prod,ap_message_center_prod,skss_message_center_prod,dgg_message_center_prod,cr_message_center_prod,rzjmgf_message_center_prod,message_center_region0000,message_center_region1001,message_center_region1002,message_center_region1003,message_center_region1004,message_center_region1005,message_center_region1006,message_center_region1007,message_center_region1008,message_center_region1009,message_center_region1010,message_center_region1011,message_center_region1012,prod_message_center_region_0000_0,prod_message_center_region_0000_1,prod_message_center_region_0000_2,prod_message_center_region_0000_3,prod_message_center_region_2022_0,prod_message_center_region_2022_1,prod_message_center_region_2022_2,prod_message_center_region_2022_3,prod_message_center_region_2022_4,prod_message_center_region_2022_5,prod_message_center_region_2022_6,prod_message_center_region_2022_7,prod_message_center_region1013,prod_message_center_region1014,prod_message_center_region1015,prod_message_center_region1016,prod_message_center_region1017,prod_message_center_region1018,prod_message_center_region1019,prod_message_center_region1020,prod_message_center_region1021,prod_message_center_region1022,prod_message_center_region1023,prod_message_center_region1024,prod_message_center_region1025,prod_message_center_region1026,prod_message_center_region1027,prod_message_center_region1028,prod_message_center_region1029,prod_message_center_region1030,prod_message_center_region1031,ihr360_message_center,schame02_message_center_amazon,hy_message_center_amazon,zy_message_center_amazon,ys_message_center_amazon,byc_message_center_amazon,ws_message_center_amazon,ihr360_message_center,zaba_message_center_amazon,hbgs_message_center_amazon,ihr360_message_center,bell_message_center_amazon,schame05_message_center_amazon,wfjy_message_center_amazon,ftjy_message_center_amazon,clng_message_center_amazon,hyjt_message_center_amazon,dksw_message_center_amazon,tj_message_center_amazon,ihr360_message_center,kxcps_message_center_amazon,zzpy_message_center_amazon,ihr360_message_center,zzsd_message_center_amazon,mlys_message_center_amazon,rt_message_center_amazon,zf_message_center_amazon,cgx_message_center_amazon,clslyy_message_center_amazon,gh_message_center_amazon,yxhzgd_message_center_amazon,sr_message_center_amazon,czzyc_message_center_amazon,krd_message_center_amazon,fn_message_center_amazon,fdzc_message_center_amazon,wde_message_center_amazon,gdymzcgl_message_center_amazon,shbrxxkj_message_center_amazon,yzjgsy_message_center_amazon,dhkjs_message_center_amazon,ihr360_message_center,dsxtjc_message_center_amazon,zdlhjt_message_center_amazon,zjhbqc_message_center_amazon,zjfdhb_message_center_amazon,rzjmgf_message_center_amazon,bjpep_message_center_amazon,ccgxrc_message_center_amazon,tcfdc_message_center_amazon,shkms_message_center_amazon,ddhgsxt_message_center_amazon,wmkjyx_message_center_amazon,schysc_message_center_amazon,hyxcl_message_center_amazon,zm_message_center_amazon,ahhsxny_message_center_amazon,szyxt_message_center_amazon,bjabt_message_center_amazon,zgmsxy_message_center_amazon,zwlh_message_center_amazon,scyxgf_message_center_amazon,nmghty_message_center_amazon,ihr360_message_center,shytfz_message_center_amazon,anddx_message_center_amazon,prod_message_center_region_2022_8,prod_message_center_region_2022_9,prod_message_center_region_2022_10\n";

    @Autowired
    private Environment environment;

    @Test
    public void propertiesTest() {
        // FileUtil.file()
        Reader reader = FileUtil.getReader("xlsx/export_result—amazon.csv", StandardCharsets.UTF_8);
        CsvReader csvReader = CsvUtil.getReader();
        List<Map<String, String>> csvDataList = csvReader.readMapList(reader);


        for (int i = 0; i < 202211; i++) {
            String mysqlUrl = environment.getProperty("db" + i + ".datasource.url");
            if (StringUtils.isNotBlank(mysqlUrl)) {
                boolean checkResult = true;
                for (Map<String, String> csvDataMap : csvDataList) {
                    String dbStr = csvDataMap.get("\uFEFFhost") + ":" + csvDataMap.get("port") + "/" + csvDataMap.get("schemaName");
                    if (mysqlUrl.contains(dbStr)) {
                        checkResult = false;
                        break;
                    }
                }
                if (checkResult) {
                    System.out.println("数据库连接不存在" + mysqlUrl);
                }
            }
        }
    }
}
