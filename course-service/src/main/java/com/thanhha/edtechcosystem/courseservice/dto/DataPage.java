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

    public DataPage(int page, int limit, List<?> dataObjectList) {
        this.page = page;
        this.limit = limit;
        double totalDouble= ((double)dataObjectList.size())/((double)limit);
        if(totalDouble>0)
            total=(int) dataObjectList.size()/limit+1;
        startNumber=limit*page-limit;
        endNumber=startNumber+limit;
        if(endNumber>dataObjectList.size())
            endNumber=dataObjectList.size();
        if(startNumber>=dataObjectList.size())
            this.dataList=new ArrayList<>();
        else
            this.dataList=fillObjects(dataObjectList,startNumber, new ArrayList<>());
    }
    private List<Object> fillObjects(List<?> dataObjectList, int index, List<Object> objectList) {
        if(index>=endNumber || index>dataObjectList.size())
            return objectList;
        if(index<startNumber)
            index=startNumber;
        if(index<endNumber)
            objectList.add(dataObjectList.get(index));
        return fillObjects(dataObjectList,index+1, objectList);
    }
}
