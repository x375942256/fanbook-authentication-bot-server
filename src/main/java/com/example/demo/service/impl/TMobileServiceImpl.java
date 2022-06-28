package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.dao.TMobileDao;
import com.example.demo.entity.Role;
import com.example.demo.entity.TMobile;
import com.example.demo.entity.TRecord;
import com.example.demo.service.FanBookService;
import com.example.demo.service.TMobileService;
import com.example.demo.service.TMobileTempService;
import com.example.demo.service.TRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * (TMobile)表服务实现类
 *
 * @author makejava
 * @since 2022-04-11 20:43:40
 */
@Service("tMobileService")
@Slf4j
public class TMobileServiceImpl extends ServiceImpl<TMobileDao, TMobile> implements TMobileService {

    @Autowired
    TMobileTempService tMobileTempService;
    @Autowired
    TRecordService recordService;
    @Autowired
    FanBookService fanBookService;


    @Override
    public void operation(Long guild, Integer sheetNo) {
        //获取需要新增的数据
        Long beginTime = System.currentTimeMillis();
        log.info("{}开始处理数据库数据", guild);
        List<TMobile> tMobiles = this.baseMapper.getAddList(guild, sheetNo);

        if (tMobiles.size() > 0) {
            log.debug("服务器:{},添加新增的数据,共{}条", guild, tMobiles.size());
            this.baseMapper.insertBitch(tMobiles);
        }
        //获取需要删除的数据
        List<TMobile> ids = this.baseMapper.getDelList(guild, sheetNo);
        List<Long> deleteId = ids.stream().map(i -> i.getId()).collect(Collectors.toList());
        if (deleteId.size() > 0) {
            log.debug("服务器:{},删除清除的数据,共{}条", guild, deleteId.size());
            this.removeByIds(deleteId);
        }
        ids.forEach(tMobile -> {
            TRecord tRecord = recordService.getOne(Wrappers.<TRecord>lambdaQuery().eq(TRecord::getGuild, guild).eq(TRecord::getMobile, tMobile.getMobile()));
            if (null != tRecord) {
                log.info("删除用户：手机号码：{}||USER_ID:{}", tMobile.getMobile(), tRecord.getFbUser());
                fanBookService.delMemberRoles(guild, tRecord.getFbUser(), tMobile.getRoleIds());
            }
        });
        log.info("{}处理数据库数据耗时{}ms", guild, System.currentTimeMillis() - beginTime);
    }

    @Override
    public List<Role> getRoleGroup() {
        return this.baseMapper.getRoleGroup();
    }

    @Override
    public List<TMobile> getTMobileList(String roleName) {
        return this.baseMapper.getTMobileList(roleName);
    }

    @Override
    public void deleteBySheet(Long guildId, Integer size) {
        this.baseMapper.deleteBySheet(guildId, size);
    }

    @Override
    public void clear() {
        this.clear();
    }


}
