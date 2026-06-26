package cn.sxu.enterprise.module.system.service.impl;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.system.entity.SysRole;
import cn.sxu.enterprise.module.system.mapper.SysRoleMapper;
import cn.sxu.enterprise.module.system.service.SysRoleService;
import cn.sxu.enterprise.module.system.vo.SysRolePageQuery;
import cn.sxu.enterprise.module.system.vo.SysRolePageVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Override
    public PageResult<SysRolePageVO> pageRoles(SysRolePageQuery query) {
        long pageNo = query.getPageNo() == null || query.getPageNo() < 1 ? 1 : query.getPageNo();
        long pageSize = query.getPageSize() == null || query.getPageSize() < 1 ? 10 : query.getPageSize();
        pageSize = Math.min(pageSize, 100);

        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(query.getRoleName()), SysRole::getRoleName, query.getRoleName());
        wrapper.like(StringUtils.hasText(query.getRoleKey()), SysRole::getRoleKey, query.getRoleKey());
        wrapper.eq(query.getStatus() != null, SysRole::getStatus, query.getStatus());
        wrapper.orderByAsc(SysRole::getRoleSort);
        wrapper.orderByDesc(SysRole::getCreateTime);

        Page<SysRole> page = this.page(new Page<>(pageNo, pageSize), wrapper);

        List<SysRolePageVO> records = page.getRecords()
                .stream()
                .map(SysRolePageVO::fromEntity)
                .toList();

        return PageResult.of(
                page.getCurrent(),
                page.getSize(),
                page.getTotal(),
                page.getPages(),
                records
        );
    }
}