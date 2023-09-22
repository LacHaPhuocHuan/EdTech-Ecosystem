package com.thanhha.edtechcosystem.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserPage {
    private List<UserDto> userDtoList;
    private int page;
    private int limit;
    private int startNumber;
    private int endNumber;
    private int totalPage;

    public UserPage(List<UserDto> allUser, int page, int limit) {
        this.userDtoList=new ArrayList<>();
        this.page = page;
        this.limit = limit;
        this.startNumber=page*limit-limit;
        this.totalPage=allUser.size()/limit +1;
        if( !(startNumber>allUser.size()) ) {
            this.endNumber = (page) * limit;
            if (this.endNumber > allUser.size())
                this.endNumber = allUser.size();
            fillPage(allUser, startNumber);
        }else{
            this.startNumber=0;
            this.endNumber=0;
        }

    }

    private void fillPage(List<UserDto> allUser, int n) {
        if(n==endNumber || n==allUser.size())
            return;
        if(n>=startNumber)
            this.userDtoList.add(allUser.get(n));
        fillPage(allUser,n+1);
    }
}
