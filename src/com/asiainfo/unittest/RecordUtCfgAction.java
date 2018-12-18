package com.asiainfo.unittest;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.*;

import java.util.HashMap;
import java.util.Map;

public class RecordUtCfgAction extends AnAction {

    private static final String FILE_TYPE = "JAVA";

    @Override
    public void actionPerformed(AnActionEvent e) {
        PsiMethod selectedMethod = this.getSelectedMethod(e);
        if (selectedMethod != null){
            HashMap<String, Object> methodInfo = this.getMethodInfo(selectedMethod);
            System.out.println(1);
        }
    }

    private PsiMethod getSelectedMethod(AnActionEvent e){
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        Editor editor = e.getData(LangDataKeys.EDITOR);
        SelectionModel selectionModel = editor.getSelectionModel();
        if (!"".equals(selectionModel.getSelectedText())) {
            String selectedText = selectionModel.getSelectedText();
            FileType fileType = psiFile.getFileType();
            if (FILE_TYPE.equals(fileType.getName()) && psiFile instanceof PsiJavaFile) {
                PsiJavaFile javaFile = (PsiJavaFile) psiFile;
                String fileName = javaFile.getName();
                String className = fileName.substring(0, fileName.length() - 5);
                PsiClass[] psiClasses = javaFile.getClasses();
                PsiClass currentClass = null;
                for (PsiClass psiClass : psiClasses) {
                    if (className.equals(psiClass.getName())) {
                        currentClass = psiClass;
                        break;
                    }
                }
                PsiMethod[] allMethods = currentClass.getAllMethods();
                for (PsiMethod method : allMethods) {
                    if (method.getName().equals(selectedText)) {
                        return method;
                    }
                }
            }
        }
        return null;
    }

    private HashMap<String,Object> getMethodInfo(PsiMethod selectedMethod){
        HashMap<String,Object> map = new HashMap<>();
        map.put("methodName",selectedMethod.getName());
        PsiParameterList parameterList = selectedMethod.getParameterList();
        Map<String,String> paramMap = new HashMap<>();
        PsiParameter[] parameters = parameterList.getParameters();
        for (PsiParameter p : parameters){
            paramMap.put(p.getType().getPresentableText(),p.getName());
            System.out.println(1);
        }
        map.put("params",paramMap);
        map.put("returnType",selectedMethod.getReturnType().getPresentableText());
        return map;
    }
}
