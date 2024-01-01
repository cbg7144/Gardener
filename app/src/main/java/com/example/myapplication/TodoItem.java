package com.example.myapplication;

public class TodoItem {
    private String todoName;

    public TodoItem(String todoName){
        this.todoName = todoName;
    }

    public String getTodoName() {
        return todoName;
    }

    public void setTodoName(String todoName) {
        this.todoName = todoName;
    }
}
