package com.xy7.shortlink.project.dao.mapper;

import cn.hutool.db.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xy7.shortlink.project.dao.entity.ShortLinkDO;
import com.xy7.shortlink.project.dto.req.ShortLinkPageReqDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 短链接持久层
 */
public interface ShortLinkMapper extends BaseMapper<ShortLinkDO> {

    /**
     * 短链接访问统计自增
     */
    @Update("update t_link set total_pv = total_pv + #{totalPv}, total_uv = total_uv + #{totalUv}, total_uip = total_uip + #{totalUip} where gid = #{gid} and full_short_url = #{fullShortUrl}")
    void incrementStats(
            @Param("gid") String gid,
            @Param("fullShortUrl") String fullShortUrl,
            @Param("totalPv") Integer totalPv,
            @Param("totalUv") Integer totalUv,
            @Param("totalUip") Integer totalUip
    );

    /**
     * 分页查询短链接当日的监控汇总信息并按照指定字段进行排序
     */
    @Select("<script>" +
            "SELECT t.*, " +
            "COALESCE(s.today_pv, 0) AS todayPv, " +
            "COALESCE(s.today_uv, 0) AS todayUv, " +
            "COALESCE(s.today_uip, 0) AS todayUip " +
            "FROM t_link t " +
            "LEFT JOIN t_link_stats_today s ON t.full_short_url = s.full_short_url " +
            "AND s.date = CURDATE() " +
            "WHERE t.gid = #{param.gid} " +
            "AND t.enable_status = 0 " +
            "AND t.del_flag = 0 " +
            "<choose>" +
            "<when test=\"#{param.orderTag} == 'todayPv'\">" +
            "ORDER BY todayPv DESC " +
            "</when>" +
            "<when test=\"#{param.orderTag} == 'todayUv'\">" +
            "ORDER BY todayUv DESC " +
            "</when>" +
            "<when test=\"#{param.orderTag} == 'todayUip'\">" +
            "ORDER BY todayUip DESC " +
            "</when>" +
            "<when test=\"#{param.orderTag} == 'totalPv'\">" +
            "ORDER BY t.total_uv DESC " +
            "</when>" +
            "<when test=\"#{param.orderTag} == 'totalUv'\">" +
            "ORDER BY t.total_pv DESC " +
            "</when>" +
            "<when test=\"#{param.orderTag} == 'totalUip'\">" +
            "ORDER BY t.total_uip DESC " +
            "</when>" +
            "<otherwise>" +
            "ORDER BY t.create_time DESC " +
            "</otherwise>" +
            "</choose>" +
            "</script>")
    IPage<ShortLinkDO> pageLink(@Param("param") ShortLinkPageReqDTO requestParam);


}