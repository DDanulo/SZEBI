package com.example.server.Alerts.entities;

public enum Operator {


    GREATER_THAN{
        @Override
        public boolean check(double value, double ruleValue){
            return value > ruleValue;
        }
    },
    LESS_THAN{
        @Override
        public boolean check(double value, double ruleValue){
            return value < ruleValue;
        }
    },
    EQUALS{
        @Override
        public boolean check(double value, double ruleValue){
            return value == ruleValue;
        }
    };


    public abstract boolean check (double value, double ruleValue);
}
