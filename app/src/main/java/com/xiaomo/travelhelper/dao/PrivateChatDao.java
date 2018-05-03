package com.xiaomo.travelhelper.dao;

import android.database.Cursor;

import com.google.common.collect.Lists;
import com.xiaomo.travelhelper.model.chat.PrivateChatModel;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * 聊天内容DAO
 */

public class PrivateChatDao {

    public boolean save(PrivateChatModel model){
        if(model != null){
            return model.save();
        }
        return false;
    }

    public boolean update(PrivateChatModel model){

        if(model != null && model.getId() > 0){
            return model.save();
        }
        return false;
    }

    public List<PrivateChatModel> listReadByFromAndTo(String from,String to){

        // flag == 1 已读
        return DataSupport.where("((fromAccount = ? and toAccount = ?) or (fromAccount = ? and toAccount = ?)) and flag = ? "
                ,from,to,to,from,"1")
                .order("time").find(PrivateChatModel.class);

       /* return DataSupport
                .order("time").find(PrivateChatModel.class);*/


    }

    public List<PrivateChatModel> listUnreadByFromAndTo(String from,String to){

        // flag == 2 未读
        return DataSupport.where("((fromAccount = ? and toAccount = ? ) or (fromAccount = ? and toAccount = ?)) and flag = ? "
                ,from,to,to,from,"2")
                .order("time").find(PrivateChatModel.class);

    }

    public void setRead(List<PrivateChatModel> models){

        if(models != null && !models.isEmpty()){

            for(PrivateChatModel model : models){
                model.setFlag(1);
                update(model);
            }
        }
    }

    public int countUnread(String to){

        return DataSupport.where("toAccount = ? and flag =? ",to,"2").count(PrivateChatModel.class);
    }

    public List<PrivateChatModel> findFirstGroup(String to){

        List<PrivateChatModel> resultList = Lists.newArrayList();
        Cursor cursor = DataSupport.findBySQL("select fromAccount from privateChatModel group by fromAccount " +
               "having toAccount = ? and flag = ? ",to,"2");
       if(cursor != null){

          while (cursor.moveToNext()){

              String from = cursor.getString(cursor.getColumnIndex("fromAccount") +1);
              PrivateChatModel model = DataSupport.where("fromAccount = ? ",from).findFirst(PrivateChatModel.class);
              if(model != null){
                  resultList.add(model);
              }
          }
      }

      return resultList;

    }


}
