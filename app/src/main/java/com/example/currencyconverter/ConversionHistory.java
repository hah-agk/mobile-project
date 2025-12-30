package com.example.currencyconverter;

public class ConversionHistory {

        public String from;
        public String to;
        public double amount;
        public double result;
        public String date;

        public ConversionHistory(String from, String to, double amount, double result, String date) {
            this.from = from;
            this.to = to;
            this.amount = amount;
            this.result = result;
            this.date = date;
        }
}
