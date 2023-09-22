package com.thanhha.edtechcosystem.courseservice.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class DataPage {
    private int startNumber;
    private int endNumber;
    private int page;
    private int total;
    private int limit;
    private List<?> dataList=new ArrayList<>();

    public DataPage(int page, int limit, List<?> dataList) {
        this.page = page;
        this.limit = limit;
        double totalDouble= ((double)dataList.size())/((double)limit);
        if(total>0)
            total=(int) dataList.size()/limit+1;
        startNumber=limit*page-limit;
        endNumber=startNumber+limit;
        if(
                startNumber>=dataList.size()
        )
            dataList=new ArrayList<>();
        else
            dataList=fillObjects(dataList,startNumber, new ArrayList<>());
    }
    private List<Object> fillObjects(List<?> dataList, int index, List<Object> objectList) {
        if(index>=endNumber)
            return objectList;
        if(index<startNumber)
            index=startNumber;
        objectList.add(dataList.get(index));
        return fillObjects(dataList,index+1, objectList);
    }
}
