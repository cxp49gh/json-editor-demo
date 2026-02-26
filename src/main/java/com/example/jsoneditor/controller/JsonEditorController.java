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
    
    static class ValidationResult {
        private boolean valid;
        private String message;
        private String formattedJson;
        
        public ValidationResult(boolean valid, String message, String formattedJson) {
            this.valid = valid;
            this.message = message;
            this.formattedJson = formattedJson;
        }
        
        // Getters and Setters
        public boolean isValid() { return valid; }
        public void setValid(boolean valid) { this.valid = valid; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public String getFormattedJson() { return formattedJson; }
        public void setFormattedJson(String formattedJson) { this.formattedJson = formattedJson; }
    }
}