package com.example.Multi_Tenant_Task_Management_System.dto;

public class ResponseWithTime<T> {
    private T data;
    private long executionTime;

    public ResponseWithTime(T data, long executionTime) {
        this.data = data;
        this.executionTime = executionTime;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }
}