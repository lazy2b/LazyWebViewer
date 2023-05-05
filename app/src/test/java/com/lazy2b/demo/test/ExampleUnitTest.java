package com.lazy2b.demo.test;

import static org.junit.Assert.assertEquals;

import com.lazy2b.demo.test.xcode.Project;
import com.lazy2b.demo.test.xcode.XResult;
import com.lazy2b.demo.test.xcode.XTools;
import com.lazylibs.webviewer.IGlobal;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    void verifyResult(XResult xResult) {
        Assert.assertEquals(xResult.verifyResult(), xResult.localConfigString);
    }
    @Test
    public void xCode() {
        List<Project> projects = XTools.xCode();

        for (Project project : projects) {
            IGlobal.logP(String.format("++++++++++++++++%s-%s++++++++++++++++++", project.code, project.name));
            IGlobal.logP("++++++++++++++++++++++++++++++++++++++++++++++++++");
            IGlobal.logP("-------------------develop-------------------");
            verifyResult(project.develop.xResult);
            IGlobal.logP("-------------------preview-------------------");
            verifyResult(project.preview.xResult);
            IGlobal.logP("-------------------online--------------------");
            verifyResult(project.online.xResult);
            IGlobal.logP("++++++++++++++++++++++++++++++++++++++++++++++++++");
            IGlobal.logP(String.format("++++++++++++++++%s-%s-结束！++++++++++++++++++", project.code, project.name));
            IGlobal.logP(String.format("———————————————————————————————————————————————————"));
        }
    }

    @Test
    public void dLocalCode(){
        new XResult(IGlobal.pk).rePrintLocalCode("e4tBSykJ0cE3EGucGPlGMiaKEUQM5nsz0TOKpfOONyeuTsrLzZ9lit1YNnZKyCt/NWTNRua+QdU7\nFtZWVOogP8IQr1f21ILcMRAbCi2uI9l/AfCwKsBADcKZwWAaw3pcIzfTKGGIttSzi+Pww7LQ2wZ0\ncpeF1CtI6dwU6gi4K1ZqGN/MQvorPt3030wdK9zxcaEyIyq73m68yLWZLCD/JEUeKG1EMjpfmvnn\nJps9caBQA+5H8fqSFOyHaAjvVRIiJJBCyDdvS/5fsdD5M34rP06ujaROL9AFRHBK5pQ7BcTNMlyp\nM2Q/OCkGLr6ZeK+wGZX+vzkZ162ZUG1PbCXgSK8ajA4pt1YeyK0kv6S016Kp3tODeBQSh4ZZn0Dh\niftIF7B2FCR3MMjQtLfkHj8Gb+J4Tt1pQviFyYyz1nk14Y+PPzHBlPJWK0kX1Y7M2n+7XXt+bq/2\nd0TtfunjJLwtZzIcZsfjQlOkI3KHvvgKWQYpB8TP72rn0njfYYYPF1ZgGUoUoFqqDNaLngEaoVmQ\nH1GolMeUkV8BC/FDnaZqHCg=\n");
        assertEquals("sl2A2String", "sl2A2String");
    }

    @Test
    public void l2String(){
        String raw = "啊,b,c";
        List<String> stringList = new ArrayList<>(Arrays.asList(raw.split(",")));
//        stringList.remove(0);
        String sl2String = stringList.toString();
        String sl2A2String = Arrays.toString(stringList.toArray()).trim().replace("[","").replace("]","").replace(" ","");
        assertEquals(raw, sl2A2String);
    }
}