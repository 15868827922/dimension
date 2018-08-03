package com.shenjing.dimension.dimension.base.util.enums;

public enum Strategy {
        ROLLBACK(" ROLLBACK "),
        ABORT(" ABORT "),
        FAIL(" FAIL "),
        IGNORE(" IGNORE "),
        REPLACE(" REPLACE ");

        Strategy(String sql) {
            this.sql = sql;
        }

        public String sql;

        public String getSql() {
            return sql;
        }
    }