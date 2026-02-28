package com.example.jsoneditor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Controller
public class JsonEditorController {
    
    private ObjectMapper objectMapper = new ObjectMapper();
    
    @GetMapping("/")
    public String index(Model model) {
        // 提供一个默认的JSON示例
        String defaultJson = "{\n  \"name\": \"示例\",\n  \"version\": \"1.0\",\n  \"features\": [\n    \"JSON编辑\",\n    \"格式化\",\n    \"验证\"\n  ]\n}";
        model.addAttribute("defaultJson", defaultJson);
        return "index";
    }
    
    @PostMapping("/validate")
    @ResponseBody
    public Object validateJson(@RequestBody String jsonInput) {
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonInput);
            return new ValidationResult(true, "JSON格式有效", 
                objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode));
        } catch (Exception e) {
            return new ValidationResult(false, "JSON格式无效: " + e.getMessage(), null);
        }
    }
    
    @PostMapping("/compare")
    @ResponseBody
    public Object compareJson(@RequestBody CompareRequest request) {
        try {
            JsonNode json1 = objectMapper.readTree(request.getJson1());
            JsonNode json2 = objectMapper.readTree(request.getJson2());
            
            java.util.List<DiffResult> differences = compareNodes("", json1, json2);
            
            if (differences.isEmpty()) {
                return new CompareResult(true, "两个JSON完全相同", null);
            } else {
                return new CompareResult(false, "发现 " + differences.size() + " 处差异", differences);
            }
        } catch (Exception e) {
            return new CompareResult(false, "JSON解析错误: " + e.getMessage(), null);
        }
    }
    
    private java.util.List<DiffResult> compareNodes(String path, JsonNode node1, JsonNode node2) {
        java.util.List<DiffResult> differences = new java.util.ArrayList<>();
        
        if (node1.equals(node2)) {
            return differences;
        }
        
        if (node1.isObject() && node2.isObject()) {
            java.util.Iterator<String> fields1 = node1.fieldNames();
            while (fields1.hasNext()) {
                String field = fields1.next();
                String newPath = path.isEmpty() ? field : path + "." + field;
                
                if (!node2.has(field)) {
                    differences.add(new DiffResult("removed", newPath, node1.get(field).toString(), null));
                } else {
                    differences.addAll(compareNodes(newPath, node1.get(field), node2.get(field)));
                }
            }
            
            java.util.Iterator<String> fields2 = node2.fieldNames();
            while (fields2.hasNext()) {
                String field = fields2.next();
                if (!node1.has(field)) {
                    String newPath = path.isEmpty() ? field : path + "." + field;
                    differences.add(new DiffResult("added", newPath, null, node2.get(field).toString()));
                }
            }
        } else if (node1.isArray() && node2.isArray()) {
            int maxLen = Math.max(node1.size(), node2.size());
            for (int i = 0; i < maxLen; i++) {
                String newPath = path + "[" + i + "]";
                if (i >= node1.size()) {
                    differences.add(new DiffResult("added", newPath, null, node2.get(i).toString()));
                } else if (i >= node2.size()) {
                    differences.add(new DiffResult("removed", newPath, node1.get(i).toString(), null));
                } else {
                    differences.addAll(compareNodes(newPath, node1.get(i), node2.get(i)));
                }
            }
        } else {
            differences.add(new DiffResult("changed", path, node1.toString(), node2.toString()));
        }
        
        return differences;
    }
    
    static class ValidationResult {
        private boolean valid;
        private String message;
        private String formattedJson;
        
        public ValidationResult(boolean valid, String message, String formattedJson) {
            this.valid = valid;
            this.message = message;
            this.formattedJson = formattedJson;
        }
        
        public boolean isValid() { return valid; }
        public void setValid(boolean valid) { this.valid = valid; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public String getFormattedJson() { return formattedJson; }
        public void setFormattedJson(String formattedJson) { this.formattedJson = formattedJson; }
    }
    
    static class CompareRequest {
        private String json1;
        private String json2;
        
        public String getJson1() { return json1; }
        public void setJson1(String json1) { this.json1 = json1; }
        public String getJson2() { return json2; }
        public void setJson2(String json2) { this.json2 = json2; }
    }
    
    static class CompareResult {
        private boolean identical;
        private String message;
        private java.util.List<DiffResult> differences;
        
        public CompareResult(boolean identical, String message, java.util.List<DiffResult> differences) {
            this.identical = identical;
            this.message = message;
            this.differences = differences;
        }
        
        public boolean isIdentical() { return identical; }
        public void setIdentical(boolean identical) { this.identical = identical; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public java.util.List<DiffResult> getDifferences() { return differences; }
        public void setDifferences(java.util.List<DiffResult> differences) { this.differences = differences; }
    }
    
    static class DiffResult {
        private String type;
        private String path;
        private String value1;
        private String value2;
        
        public DiffResult(String type, String path, String value1, String value2) {
            this.type = type;
            this.path = path;
            this.value1 = value1;
            this.value2 = value2;
        }
        
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getPath() { return path; }
        public void setPath(String path) { this.path = path; }
        public String getValue1() { return value1; }
        public void setValue1(String value1) { this.value1 = value1; }
        public String getValue2() { return value2; }
        public void setValue2(String value2) { this.value2 = value2; }
    }
}