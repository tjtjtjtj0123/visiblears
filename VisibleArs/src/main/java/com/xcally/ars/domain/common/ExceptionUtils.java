package com.xcally.ars.domain.common;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtils {
    public static String getPrintStackTrace(Exception e) {
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));
        return errors.toString();
    }
}