package com.example.demo.sqlGenerated;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author nelson.li
 * @date 2021/11/26
 **/
public class SqlGreated {

    private static final String insert_sql = "REPLACE INTO staff_history_norm(id, created_at, updated_at, company_id, creator_id, modifier_id, advanced_setting_opened, challenge_value, complete_report_type_code, complete_value_importer_id, complete_value_importer_name, condition_importer_id, condition_importer_name, condition_input_type, condition_report_type_code, data_source, generation_method, guaranteed_value, norm_attribute, norm_code, norm_desc, norm_low_score, norm_name, norm_nature, norm_owner_id, norm_range, norm_standard, norm_top_score, norm_type_code, norm_type_name, quantization, score_input_method, score_option_list, score_unlimit, target_desc, target_value, use_challenge_value, use_complete_value, use_guaranteed_value, use_target_value, weight) \n";
    private static final String value_sql = "    VALUE ('uuid', now(), now(), '0bef1a12ea8a426689f759aa6aab6781', 'creator_id', null, false, null, null, null, null, null, null, null, null, null, null, null, null, null, 'norm_desc', 0, 'norm_name', 'normNature_02', 'norm_owner_id', null, 'norm_standard', norm_top_score, 'norm_type_code', 'norm_type_name', null, 'DIRECT', null, null, null, null, false, false, false, false, null);\n\r";

    private static final Map<String, String> normTypeCodeMap = new HashMap<>();

    static {
        normTypeCodeMap.put("能力考核", "normCategory_02");
        normTypeCodeMap.put("业绩考核", "normCategory_01");
    }

    public static void main(String[] args) throws IOException {
        List<String> uuids = UuidClass.uuids;
        Map<String,String> staffInfoMap = StaffInfoGenerated.staffInfoMap;
        readExcel(uuids, staffInfoMap);
    }

    private static void readExcel(List<String> uuids, Map<String,String> staffInfoMap) throws IOException {
        ExcelReader excelReader = ExcelUtil.getReader("C:\\Users\\3278\\Desktop\\指标库导入模板1(1).xls");
        File sqlFile = new File("C:\\Users\\3278\\Desktop\\自建指标导入.sql");
        FileOutputStream fileOutputStream = new FileOutputStream(sqlFile);

        List<Map<String, Object>> readAll = excelReader.readAll();
        for (int i = 0; i < readAll.size(); i++) {
            System.out.println("+++++++++++++++++++++++" + i);
            String uuid = uuids.get(i);
            Map<String, Object> excelRowData = readAll.get(i);
            String mobileNo = String.valueOf(excelRowData.get("手机号"));
            String normName = (String) excelRowData.get("指标名称");
            String normTypeName = (String) excelRowData.get("指标分类");
            String topScore = String.valueOf(excelRowData.get("最大分值"));
            String normDesc = (String) excelRowData.get("指标说明") == null ? "" : (String) excelRowData.get("指标说明");
            String normStandard = (String) excelRowData.get("评分标准") == null ? "" : (String) excelRowData.get("评分标准");

            StringBuffer sql = new StringBuffer();
            sql.append(insert_sql);
            String valueSql = value_sql.replace("uuid", uuid)
                    .replace("creator_id", staffInfoMap.get(mobileNo))
                    .replace("norm_desc", normDesc)
                    .replace("norm_name", normName)
                    .replace("norm_owner_id", staffInfoMap.get(mobileNo))
                    .replace("norm_type_code", normTypeCodeMap.get(normTypeName))
                    .replace("norm_type_name", normTypeName)
                    .replace("norm_standard", normStandard)
                    .replace("norm_top_score", topScore);

            sql.append(valueSql);

            fileOutputStream.write(StrUtil.bytes(sql));
        }

        fileOutputStream.close();

    }
}
