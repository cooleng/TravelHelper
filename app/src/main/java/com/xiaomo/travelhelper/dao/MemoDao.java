package com.xiaomo.travelhelper.dao;

import com.xiaomo.travelhelper.model.memo.MemoListItemModel;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * 备忘模块 DAO
 */

public class MemoDao {

    public List<MemoListItemModel> findAll(){

        return DataSupport.findAll(MemoListItemModel.class,true);
    }

    public MemoListItemModel findById(Long id){

        return  DataSupport.find(MemoListItemModel.class,id,true);
    }

    public List<MemoListItemModel> findByLikeTitle(String title){

        return DataSupport.where("title like ?",title).find(MemoListItemModel.class,true);
    }

    public boolean update(MemoListItemModel model){

       if(model != null && model.getId() > 0){
           return model.save();
       }
       return false;
    }

    public boolean save(MemoListItemModel model){

        if(model != null ){
            return model.save();
        }
        return false;
    }








}
