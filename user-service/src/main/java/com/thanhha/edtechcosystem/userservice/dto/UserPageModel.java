package com.thanhha.edtechcosystem.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.EntityModel;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPageModel {
    private List<EntityModel<?>> userList;
    private int page;
    private int limit;
    private int startNumber;
    private int endNumber;
    private int totalPage;


//    public UserPageModel(UserPage userPage) {
//        this.page=userPage.getPage();
//        this.limit=userPage.getLimit();
//        this.startNumber=userPage.getStartNumber();
//        this.endNumber=userPage.getEndNumber();
//        this.totalPage=userPage.getTotalPage();
//
//    }
}
