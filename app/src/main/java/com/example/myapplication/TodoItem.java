package com.example.myapplication;

public class TodoItem {
    private String todoName;
    boolean isChecked;
    public TodoItem(String todoName){
        this.todoName = todoName;
        this.isChecked = false;
    }

    public String getTodoName() {
        return todoName;
    }

    public void setTodoName(String todoName) {
        this.todoName = todoName;
    }

    public boolean getIsChecked(){
        return isChecked;
    }

    public void setIsChecked(boolean isChecked){
        this.isChecked = isChecked;
    }
}
