
package com.chengxin.common;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import org.hibernate.Query;

public class DBModelUtil {

    private static Map<String, List<ModelField>> MODEL_STRING_FIELD_TABLE = new Hashtable<String, List<ModelField>>();
    private static Map<String, List<ModelField>> MODEL_REPLICATE_FIELD_TABLE = new Hashtable<String, List<ModelField>>();
    private static Map<String, List<ModelField>> MODEL_FIELD_TABLE = new Hashtable<String, List<ModelField>>();
    private static Map<String, List<ModelField>> MODEL_ENCRYPTED_FIELD_TABLE = new Hashtable<String, List<ModelField>>();

    public static List<ModelField> getModelFieldList(String className, Object object) {

        if(MODEL_FIELD_TABLE.containsKey(className)) {
            return MODEL_FIELD_TABLE.get(className);
        }

        List<ModelField> modelFieldList = new ArrayList<ModelField>();

        List<Field> fieldArray = new ArrayList<Field>();
        List<Method> methodArray = new ArrayList<Method>();

        if(object.getClass().getSuperclass() != null) {

            if(object.getClass().getSuperclass().getName().indexOf("BaseModel") == -1) {
                for(Field field : object.getClass().getSuperclass().getDeclaredFields()) {
                    fieldArray.add(field);
                }

                for(Method method : object.getClass().getSuperclass().getDeclaredMethods()) {
                    methodArray.add(method);
                }
            }
        }

        for(Field field : object.getClass().getDeclaredFields()) {
            fieldArray.add(field);
        }

        for(Method method : object.getClass().getDeclaredMethods()) {
            methodArray.add(method);
        }

        for(Field field : fieldArray) {
            String fieldName = field.getName();

            String getterMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            String setterMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

            Method getterMethod = null;
            Method setterMethod = null;

            for(Method method : methodArray) {
                String methodName = method.getName();

                if(methodName.equals(getterMethodName)) {
                    getterMethod = method;
                } else if(methodName.equals(setterMethodName)) {
                    setterMethod = method;
                } else if (getterMethod != null && setterMethod != null) {
                    break;
                }
            }

            ModelField modelField = new ModelField();

            modelField.setFieldName(fieldName);
            modelField.setFieldType(field.getType().getName());
            modelField.setSetterMethod(setterMethod);
            modelField.setGetterMethod(getterMethod);

            modelFieldList.add(modelField);
        }

        MODEL_FIELD_TABLE.put(className, modelFieldList);

        return modelFieldList;
    }

    public static List<ModelField> getModelStringFieldList(String className, Object object) {

        if(MODEL_STRING_FIELD_TABLE.containsKey(className)) {
            return MODEL_STRING_FIELD_TABLE.get(className);
        }

        List<ModelField> modelFieldList = new ArrayList<ModelField>();

        Field[] fieldArray = object.getClass().getDeclaredFields();
        Method[] methodArray = object.getClass().getDeclaredMethods();

        for(Field field : fieldArray) {
            String fieldName = field.getName();

            String getterMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            String setterMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

            Method getterMethod = null;
            Method setterMethod = null;

            for(Method method : methodArray) {
                String methodName = method.getName();

                if(methodName.equals(getterMethodName)) {
                    getterMethod = method;
                } else if(methodName.equals(setterMethodName)) {
                    setterMethod = method;
                } else if (getterMethod != null && setterMethod != null) {
                    break;
                }
            }

            if(field.getType().getName().equals("java.lang.String")) {
                ModelField modelField = new ModelField();

                modelField.setFieldName(fieldName);
                modelField.setFieldType(field.getType().getName());
                modelField.setSetterMethod(setterMethod);
                modelField.setGetterMethod(getterMethod);

                modelFieldList.add(modelField);
            } else {
            }
        }

        MODEL_STRING_FIELD_TABLE.put(className, modelFieldList);

        return modelFieldList;
    }

    public static List<ModelField> getModelEncryptedFieldList(String className, Object object) {

        if(MODEL_ENCRYPTED_FIELD_TABLE.containsKey(className)) {
            return MODEL_ENCRYPTED_FIELD_TABLE.get(className);
        }

        List<ModelField> modelFieldList = new ArrayList<ModelField>();

        Field[] fieldArray = object.getClass().getDeclaredFields();
        Method[] methodArray = object.getClass().getDeclaredMethods();

        for(Field field : fieldArray) {
            String fieldName = field.getName();

            String getterMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            String setterMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

            Method getterMethod = null;
            Method setterMethod = null;

            for(Method method : methodArray) {
                String methodName = method.getName();

                if(methodName.equals(getterMethodName)) {
                    getterMethod = method;
                } else if(methodName.equals(setterMethodName)) {
                    setterMethod = method;
                } else if (getterMethod != null && setterMethod != null) {
                    break;
                }
            }

            if(field.getType().getName().equals("java.lang.String")) {
                Annotation[] annotationArray = field.getDeclaredAnnotations();
                
                
                if(annotationArray != null) {
                    
                    for(Annotation annotation  : annotationArray) {
                        
                        if(annotation.annotationType().equals(IEncryptedField.class)) {
                            
                            ModelField modelField = new ModelField();

                            modelField.setFieldName(fieldName);
                            modelField.setFieldType(field.getType().getName());
                            modelField.setSetterMethod(setterMethod);
                            modelField.setGetterMethod(getterMethod);

                            modelFieldList.add(modelField);
                        }
                    }
                }
            } else {
            }
        }

        MODEL_ENCRYPTED_FIELD_TABLE.put(className, modelFieldList);

        return modelFieldList;
    }

    public static List<ModelField> getModelReplicateFieldList(String className, Object sourceObject, Object targetObject) {

        if(MODEL_REPLICATE_FIELD_TABLE.containsKey(className)) {
            return MODEL_REPLICATE_FIELD_TABLE.get(className);
        }

        List<ModelField> modelFieldList = new ArrayList<ModelField>();

        List<Field> fieldArray = new ArrayList<Field>();
        List<Method> methodArray = new ArrayList<Method>();

        List<Field> _fieldArray = new ArrayList<Field>();
        List<Method> _methodArray = new ArrayList<Method>();

        for(Field field : sourceObject.getClass().getDeclaredFields()) {
            fieldArray.add(field);
        }

        for(Method method : sourceObject.getClass().getDeclaredMethods()) {
            methodArray.add(method);
        }

        for(Field field : targetObject.getClass().getDeclaredFields()) {
            _fieldArray.add(field);
        }

        for(Method method : targetObject.getClass().getDeclaredMethods()) {
            _methodArray.add(method);
        }

        for(Field field : fieldArray) {
            String fieldName = field.getName();

            String getterMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            String setterMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

            Method getterMethod = null;
            Method setterMethod = null;

            for(Method method : methodArray) {
                String methodName = method.getName();

                if(methodName.equals(getterMethodName)) {
                    getterMethod = method;
                } else if (getterMethod != null) {
                    break;
                }
            }

            for(Method method : _methodArray) {
                String methodName = method.getName();

                if(methodName.equals(setterMethodName)) {
                    setterMethod = method;
                } else if (setterMethod != null) {
                    break;
                }
            }

            ModelField modelField = new ModelField();

            modelField.setFieldName(fieldName);
            modelField.setFieldType(field.getType().getName());
            modelField.setSetterMethod(setterMethod);
            modelField.setGetterMethod(getterMethod);

            modelFieldList.add(modelField);
        }

        MODEL_REPLICATE_FIELD_TABLE.put(className, modelFieldList);

        return modelFieldList;
    }

    public static final String C_SECURE_TYPE_ENCRYPT = "C_SECURE_ENCRYPT";
    public static final String C_SECURE_TYPE_DECRYPT = "C_SECURE_DECRYPT";
    
    public static String processSecure(String value, String secureType) {
        try {
            if(secureType.equals(C_SECURE_TYPE_ENCRYPT)) {
                return SecureUtil.encrypt(com.chengxin.bfip.Constants.C_DB_SECURE_KEY, value);
            } else {
                return SecureUtil.decrypt(com.chengxin.bfip.Constants.C_DB_SECURE_KEY, value);
            }
        } catch(Exception e) {
        }
        
        return value;
    }
    
    public static void processSecure(String className, Object object, String secureType) {

        if(object == null) {
            return;
        }

        try {
            List<ModelField> modelFieldList = getModelEncryptedFieldList(className, object);

            for(ModelField modelField : modelFieldList) {

                if(modelField.getGetterMethod() == null) {
                    continue;
                }

                String value = (String)modelField.getGetterMethod().invoke(object, new Object[]{});

                if(value == null) {
                    modelField.getSetterMethod().invoke(object, new Object[]{""});
                } else if(value.equals(CommonType.C_STRING_NULL)) {
                    modelField.getSetterMethod().invoke(object, new Object[]{""});
                } else if(value.trim().equals(CommonType.C_STRING_NULL)) {
                    modelField.getSetterMethod().invoke(object, new Object[]{""});
                } else {
                    if(secureType.equals(C_SECURE_TYPE_ENCRYPT)) {
                        try {

                            modelField.getSetterMethod().invoke(object, new Object[]{SecureUtil.encrypt(com.chengxin.bfip.Constants.C_DB_SECURE_KEY, value)});
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    } else if(secureType.equals(C_SECURE_TYPE_DECRYPT)) {
                        try {

                            modelField.getSetterMethod().invoke(object, new Object[]{SecureUtil.decrypt(com.chengxin.bfip.Constants.C_DB_SECURE_KEY, value)});
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch(Exception e) {
        }
    }

    public static void fillBlank(String className, Object object) {

        if(object == null) {
            return;
        }

        List<ModelField> modelFieldList = getModelStringFieldList(className, object);

        try {
            for(ModelField modelField : modelFieldList) {

                if(modelField.getGetterMethod() == null) {
                    continue;
                }
                
                String value = (String)modelField.getGetterMethod().invoke(object, new Object[]{});

                if(value == null) {
                    modelField.getSetterMethod().invoke(object, new Object[]{""});
                } else if(value.equals(CommonType.C_STRING_NULL)) {
                    modelField.getSetterMethod().invoke(object, new Object[]{""});
                } else if(value.trim().equals(CommonType.C_STRING_NULL)) {
                    modelField.getSetterMethod().invoke(object, new Object[]{""});
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void replaceBlank(String className, Object object) {
        List<ModelField> modelFieldList = getModelStringFieldList(className, object);

        try {
            for(ModelField modelField : modelFieldList) {
                String value = (String)modelField.getGetterMethod().invoke(object, new Object[]{});

                if(value == null || value.equals(" ")) {
                    modelField.getSetterMethod().invoke(object, new Object[]{CommonType.C_STRING_NULL});
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void replaceQuote(String className, Object object) {
        List<ModelField> modelFieldList = getModelStringFieldList(className, object);

        try {
            for(ModelField modelField : modelFieldList) {
                String value = (String)modelField.getGetterMethod().invoke(object, new Object[]{});

                if(value != null) {
                    modelField.getSetterMethod().invoke(object, value.replace("'", "＇"));
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void replicate(String className, Object sourceObject, Object replicateObject) {

        List<ModelField> modelFieldList = getModelReplicateFieldList(className, sourceObject, replicateObject);

        try {
            for(ModelField modelField : modelFieldList) {
                Object value = modelField.getGetterMethod().invoke(sourceObject, new Object[]{});

                modelField.getSetterMethod().invoke(replicateObject, new Object[]{value});
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void fillParameter(SQLWithParameters sql, Query query) {

        List<KeyValueObject> parameterList = sql.getParameterList();
        
        for(KeyValueObject kvo : parameterList) {
            
            if(kvo.getDataType().equals(KeyValueObject.C_DATA_TYPE_STRING)) {
                query.setString(kvo.getKey(), (String)kvo.getValue());
                
                System.out.println(kvo.getDataType() + " : " + kvo.getKey() + " : " + kvo.getValue());
                
            } else if(kvo.getDataType().equals(KeyValueObject.C_DATA_TYPE_ARRAY)) {
                query.setParameterList(kvo.getKey(), (String[])kvo.getValue());
                
                String value = "";
                
                for(String str : (String[])kvo.getValue()) {
                    value += str + ",";
                }
                
                System.out.println(kvo.getDataType() + " : " + kvo.getKey() + " : " + value);
            }
        }
    }
}
