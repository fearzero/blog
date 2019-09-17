package com.ndsc.blog.mapper;

import com.ndsc.blog.entity.Relation;
import com.ndsc.blog.entity.Userinfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface RelationMapper {
    int insert(Relation record);

    int insertSelective(Relation record);

    //***********************
    int insertRelation(Relation relation);

    List<Userinfo> selectRelationUser(Integer fansId);

    int deleteRelation(Relation relation);

    List<Userinfo> selectFans(Integer blogerId);
    //**********************
    //**********************
    int selectFansCount(Integer blogerId);

    int selectBlogerCount(Integer fansId);
}