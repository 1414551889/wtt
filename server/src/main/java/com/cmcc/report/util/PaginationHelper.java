package com.cmcc.report.util;

import java.text.MessageFormat;

public class PaginationHelper {
    public static String decorateCountSql(String sql) {
        return "SELECT count(*) FROM (" + sql + ")";
    }

    private final static String FIRST_PAGE_SQL = "SELECT * FROM ( {0} ) WHERE rownum <= {1}";
    private final static String PAGING_SQL = "SELECT * FROM ( SELECT row_.*, rownum r FROM ( {0} ) row_ WHERE rownum <= {1} ) WHERE r > {2}";

    public static String decorateToPaginationSql(String sql, int startIndex, int count) {
        if (startIndex < 0 || count < 0)
            throw new IllegalArgumentException("startIndex || which value < 0, is a illegal argument");
        if (startIndex == 0) {
            return MessageFormat.format(FIRST_PAGE_SQL, sql, String.valueOf(count));
        } else {
            return MessageFormat.format(PAGING_SQL, sql, String.valueOf(startIndex + count), String.valueOf(startIndex));
        }
    }
}