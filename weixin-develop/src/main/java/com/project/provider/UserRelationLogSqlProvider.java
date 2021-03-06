package com.project.provider;

import com.project.common.util.LogUtil;
import com.project.model.sql.UserRelation;
import com.project.model.vo.Page;
import org.apache.ibatis.jdbc.SQL;
import org.slf4j.Logger;

/**
 * Created by goforit on 2017/12/3.
 */
public class UserRelationLogSqlProvider {

    private Logger logger = LogUtil.getLogger(UserSqlProvider.class);

    /**
     * 插入语句
     * @param userRelation
     * @return
     */
    public String insert(final UserRelation userRelation){
        return new SQL() {{
            INSERT_INTO("User_Relation_Log");
            if (userRelation.getIntroducer() != null) {
                VALUES("introducer", "#{introducer}");
                VALUES("introducername", "#{introducername}");
            }
            if (userRelation.getNewmember() !=null){
                VALUES("newmember", "#{newmember}");
                VALUES("newmembername", "#{newmembername}");
            }
            VALUES("releation", "#{releation}");
            VALUES("lmodify", "#{lmodify}");
        }}.toString();

    }


    /**
     *根据添加选择
     * @param page
     * @return
     */
    public String selectList(Page page,UserRelation userRelation){
        return new SQL(){{
            SELECT("newmember, newmembername, releation, introducer, introducername, lmodify");
            FROM("User_Relation");
            if(userRelation.getNewmember() !=null){
                WHERE("newmember = #{newmember}");
            }
            if(userRelation.getNewmembername() !=null){
                WHERE("newmembername = #{newmembername}");
            }
            if(userRelation.getIntroducer() !=null){
                WHERE("introducer = #{introducer}");
            }
            if(userRelation.getIntroducername() !=null){
                WHERE("introducername = #{introducername}");
            }
            ORDER_BY("lmodify desc");
        }}.toString();
    }

}
